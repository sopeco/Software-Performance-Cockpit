/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * 
 */
package org.sopeco.engine.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.engine.IEngine;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experimentseries.IExperimentSeriesManager;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.engine.util.EngineTools;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.util.session.SessionAwareObject;

/**
 * The default implementation of SoPeCo engine.
 * 
 * @author Roozbeh Farahbod
 * @author Dennis Westermann
 * 
 */
public class EngineImp extends SessionAwareObject implements IEngine {

	private static final Logger LOGGER = LoggerFactory.getLogger(EngineImp.class);

	private IConfiguration configuration = null;

	private IExtensionRegistry registry = null;

	private final IExperimentController experimentController;
	private final IExperimentSeriesManager experimentSeriesManager;
	private final IPersistenceProvider persistenceProvider;

	/**
	 * Constructor.
	 * 
	 * @param sessionId
	 *            Session id to be used by this session aware object
	 * @param experimentController
	 *            experiment controller to be used for executing experiments
	 * @param experimentSeriesManager
	 *            experiment series manager to be used for managing series
	 */
	public EngineImp(String sessionId, IExperimentController experimentController,
			IExperimentSeriesManager experimentSeriesManager) {
		super(sessionId);
		this.experimentController = experimentController;
		this.experimentSeriesManager = experimentSeriesManager;
		this.persistenceProvider = PersistenceProviderFactory.getInstance().getPersistenceProvider(sessionId);
	}

	@Override
	public IConfiguration getConfiguration() {
		if (configuration == null) {
			configuration = Configuration.getSessionSingleton(getSessionId());
		}
		return configuration;
	}

	@Override
	public IExtensionRegistry getExtensionRegistry() {
		if (registry == null) {
			registry = ExtensionRegistry.getSingleton();
		}
		return registry;
	}

	@Override
	public ScenarioInstance run(ScenarioDefinition scenario) {
		ScenarioInstance scenarioInstance;

		try {

			scenarioInstance = persistenceProvider.loadScenarioInstance(scenario.getScenarioName(), getConfiguration()
					.getMeasurementControllerURIAsStr());
			LOGGER.debug("Loaded ScenarioInstance {} from database", scenarioInstance);
			LOGGER.debug("Compare Scenario definition defined in the specification with the one loaded from database");

			scenarioInstance = mergeScenarioDefinitions(scenarioInstance, getConfiguration()
					.getMeasurementControllerURIAsStr(), scenario);
			persistenceProvider.store(scenarioInstance);

		} catch (DataNotFoundException e) {
			scenarioInstance = EntityFactory.createScenarioInstance(scenario, getConfiguration()
					.getMeasurementControllerURIAsStr());
			persistenceProvider.store(scenarioInstance);
			LOGGER.debug("Created new ScenarioInstance {}", scenarioInstance);
		}

		@SuppressWarnings("unchecked")
		Map<String, List<String>> filterObject = (Map<String, List<String>>) Configuration.getSessionSingleton(
				getSessionId()).getProperty(IConfiguration.EXECUTION_EXPERIMENT_FILTER);
		if (filterObject == null) {
			filterObject = new HashMap<String, List<String>>();
		}

		experimentController.acquireMEController();
		try {
			for (MeasurementSpecification measSpec : scenario.getMeasurementSpecifications()) {

				experimentController.initialize(
						EngineTools.getConstantParameterValues(measSpec.getInitializationAssignemts()),
						scenario.getMeasurementEnvironmentDefinition());

				// loop over all the experiment series in the specs
				for (ExperimentSeriesDefinition esd : measSpec.getExperimentSeriesDefinitions()) {

					// skip experiment if the filter contains it
					if (filterObject.containsKey(measSpec.getName()) && filterObject.get(measSpec.getName()) != null
							&& filterObject.get(measSpec.getName()).contains(esd.getName())) {
						continue;
					}

					ExperimentSeries series = scenarioInstance.getExperimentSeries(esd.getName(), esd.getVersion());
					if (series == null) {
						series = EntityFactory.createExperimentSeries(esd);
						scenarioInstance.getExperimentSeriesList().add(series);
						series.setScenarioInstance(scenarioInstance);

						persistenceProvider.store(series);
					}

					experimentSeriesManager.runExperimentSeries(series);
				}
			}
			experimentController.releaseMEController();
		} catch (Exception e) {
			// TODO check whether expcontroller null...
			experimentController.releaseMEController();
			throw new RuntimeException(e);
		}

		try {
			ScenarioInstance loadedScenario = persistenceProvider
					.loadScenarioInstance(scenarioInstance.getPrimaryKey());
			return loadedScenario;
		} catch (DataNotFoundException e) {
			LOGGER.error("Cannot load the scenario from the persistnce provider. Something is seriously gone wrong.");
			throw new RuntimeException("Something went wrong");
		}
	}

	/**
	 * Checks whether the scneario definition contained in the passed scenario
	 * instance contains the passed scenario definition. If the passed scenario
	 * definition contains additional elements, these are merged into the
	 * existing scenario definition
	 * 
	 * @param scenarioInstance
	 *            Existing scenario definition to be extended
	 * @param scenarioDefinition
	 *            new scenario definition to compare with
	 * @throws DataNotFoundException
	 */
	private ScenarioInstance mergeScenarioDefinitions(ScenarioInstance scenarioInstance,
			String measurementEnvironmentUrl, ScenarioDefinition scenarioDefinition) throws DataNotFoundException {

		if (!scenarioInstance.getScenarioDefinition().containsAllElementsOf(scenarioDefinition)) {

			String modelChangeHandlingMode = (String) configuration
					.getProperty(IConfiguration.CONF_MODEL_CHANGE_HANDLING_MODE);
			String detailMessage = "";
			if (modelChangeHandlingMode.equals(IConfiguration.MCH_MODE_FAIL)) {
				throw new RuntimeException(
						"Scenario definition has been changed! The option 'fail' is used for model change handling mode. "
								+ "Use another mode (newVersion or overwrite), "
								+ "rename the new scenario definition id or delete the old scenario definition (with the same id) from the database!");
			} else if (modelChangeHandlingMode.equals(IConfiguration.MCH_MODE_OVERWRITE)) {
				persistenceProvider.remove(scenarioInstance);
				scenarioInstance = EntityFactory.createScenarioInstance(scenarioDefinition, measurementEnvironmentUrl);
				detailMessage = "Model Change Handling Mode: 'overwrite'. Existing scenario instance is overwritten. Old data is lost!";
				return scenarioInstance;
			} else if (modelChangeHandlingMode.equals(IConfiguration.MCH_MODE_OVERWRITE_KEEP_RESULTS)) {
				scenarioInstance.setScenarioDefinition(scenarioDefinition);
				detailMessage = "Model Change Handling Mode: 'overwrite keep results'. Existing scenario definition is overwritten. "
						+ "Old model data is lost, however results are kept!";
				return scenarioInstance;
			} else {
				scenarioInstance.extendScenarioInstance(scenarioDefinition);
				detailMessage = "Model Change Handling Mode: 'newVersion'. Existing scenario instance is extended!";
				return scenarioInstance;
			}

		}
		return scenarioInstance;
	}

	@Override
	public IExperimentController getExperimentController() {
		return experimentController;
	}

	@Override
	public IExperimentSeriesManager getExperimentSeriesManager() {
		return experimentSeriesManager;
	}

	@Override
	public IPersistenceProvider getPersistenceProvider() {
		return persistenceProvider;
	}

}

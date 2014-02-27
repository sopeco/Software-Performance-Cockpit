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

import java.util.ArrayList;
import java.util.Collection;
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

	/** * */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EngineImp.class);

	private IConfiguration configuration = null;

	private IExtensionRegistry registry = null;

	private final IExperimentController experimentController;
	private final IExperimentSeriesManager experimentSeriesManager;
	private final IPersistenceProvider persistenceProvider;

	/**
	 * Constructor.
	 * Opens a connection to the persistence provider with the configuration data fetched via the
	 * given session ID.
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
	public ScenarioInstance run(ScenarioDefinition scenarioDefinition) {
		List<String> experimentSeriesNames = new ArrayList<String>();

		for (MeasurementSpecification ms : scenarioDefinition.getMeasurementSpecifications()) {
			for (ExperimentSeriesDefinition esd : ms.getExperimentSeriesDefinitions()) {
				experimentSeriesNames.add(ms.getName() + "." + esd.getName());
			}
		}

		return run(scenarioDefinition, experimentSeriesNames);

	}

	@Override
	public ScenarioInstance run(ScenarioDefinition scenarioDefinition, Collection<String> experimentSeriesNames) {
		if (experimentSeriesNames == null || experimentSeriesNames.isEmpty()) {
			throw new RuntimeException("List of experiment series names is empty or null!");
		}

		ScenarioInstance scenarioInstance = retrieveScenarioInstance(scenarioDefinition);
		scenarioInstance.getScenarioDefinition().getAllExperimentSeriesDefinitions(); 

		Map<MeasurementSpecification, List<ExperimentSeriesDefinition>> experimentSeries = new HashMap<MeasurementSpecification, List<ExperimentSeriesDefinition>>();

		for (MeasurementSpecification ms : scenarioDefinition.getMeasurementSpecifications()) {
			List<ExperimentSeriesDefinition> currentExpSeriesList = new ArrayList<ExperimentSeriesDefinition>();
			for (ExperimentSeriesDefinition esd : ms.getExperimentSeriesDefinitions()) {

				for (String expSeriesName : experimentSeriesNames) {
					if ((ms.getName() + "." + esd.getName()).equals(expSeriesName)) {
						currentExpSeriesList.add(esd);
					}
				}
			}
			if (!currentExpSeriesList.isEmpty()) {
				experimentSeries.put(ms, currentExpSeriesList);
			}
		}

		runExperimentSeries(scenarioInstance, experimentSeries);

		try {
			ScenarioInstance loadedScenario = persistenceProvider
					.loadScenarioInstance(scenarioInstance.getPrimaryKey());
			return loadedScenario;
		} catch (DataNotFoundException e) {
			LOGGER.error("Cannot load the scenario from the persistnce provider. Something is seriously gone wrong.");
			throw new RuntimeException("Something went wrong");
		}

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

	@Override
	public void abortExperimentRun() {
		Configuration.getSessionSingleton(getSessionId()).setProperty(IConfiguration.EXPERIMENT_RUN_ABORT,
				new Boolean(true));
	}

	/**
	 * Tries to fetch the {@link ScenarioInstance} for the given {@link ScenarioDefinition}
	 * out of the database.<br />
	 * If the <code>ScenarioInstance</code> does not exist yet, it's created from the information
	 * in the <code>ScenarioDefinition</code>. See more <code>createNewScenarioInstance()</code>.
	 * 
	 * @param scenarioDefinition the {@link ScenarioDefinition}
	 * @return the {@link ScenarioInstance} corresponding to the given definiton
	 */
	private ScenarioInstance retrieveScenarioInstance(ScenarioDefinition scenarioDefinition) {
		ScenarioInstance scenarioInstance = null;

		try {

			scenarioInstance = persistenceProvider.loadScenarioInstance(scenarioDefinition.getScenarioName(),
					getConfiguration().getMeasurementControllerURIAsStr());
			LOGGER.debug("Loaded ScenarioInstance {} from database", scenarioInstance);
			LOGGER.debug("Compare Scenario definition defined in the specification with the one loaded from database");
			
		} catch (DataNotFoundException e) {
			LOGGER.debug("No ScenarioInstance for the given ScenarioDefinition found in the database.");
			scenarioInstance = createNewScenarioInstance(scenarioDefinition);
		}
		if (scenarioInstance == null) {
			throw new RuntimeException("Failed to retrieve scenario instance!");
		}
		return scenarioInstance;
	}

	private ScenarioInstance createNewScenarioInstance(ScenarioDefinition scenarioDefinition) {
		ScenarioInstance scenarioInstance = EntityFactory.createScenarioInstance(scenarioDefinition, getConfiguration()
				.getMeasurementControllerURIAsStr());
		persistenceProvider.store(scenarioInstance);
		LOGGER.debug("Created new ScenarioInstance {}", scenarioInstance);
		return scenarioInstance;
	}

	private void runExperimentSeries(ScenarioInstance scenarioInstance,
			Map<MeasurementSpecification, List<ExperimentSeriesDefinition>> experimentSeries) {
		boolean mecAcquired = false;
		try {
			experimentController.acquireMEController();
			mecAcquired = true;

			for (MeasurementSpecification measSpec : experimentSeries.keySet()) {
				experimentController.initialize(
						EngineTools.getConstantParameterValues(measSpec.getInitializationAssignemts()),
						scenarioInstance.getScenarioDefinition().getMeasurementEnvironmentDefinition());

				for (ExperimentSeriesDefinition esd : experimentSeries.get(measSpec)) {
					ExperimentSeries series = scenarioInstance.getExperimentSeries(esd.getName());
					if (series == null) {
						series = EntityFactory.createExperimentSeries(esd);
						scenarioInstance.getExperimentSeriesList().add(series);
						series.setScenarioInstance(scenarioInstance);

						persistenceProvider.store(series);
					}

					experimentSeriesManager.runExperimentSeries(series);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Failed executing experiments!", e);
		} finally {
			if (mecAcquired) {
				experimentController.releaseMEController();
			}
		}
	}

}

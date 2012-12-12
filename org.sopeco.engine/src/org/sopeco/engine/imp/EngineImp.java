/**
 * 
 */
package org.sopeco.engine.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.engine.IEngine;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experimentseries.IExperimentSeriesManager;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.engine.status.EventType;
import org.sopeco.engine.status.StatusBroker;
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

		boolean sendigStatusMessages = Configuration.getSessionSingleton(getSessionId()).getPropertyAsBoolean(
				IConfiguration.SENDING_STATUS_MESSAGES, false);
		String generatedToken = null;
		if (sendigStatusMessages) {
			String controllerUrl = Configuration.getSessionSingleton(getSessionId()).getPropertyAsStr(
					IConfiguration.CONF_MEASUREMENT_CONTROLLER_URI);
			generatedToken = StatusBroker.get().createToken(getSessionId(), controllerUrl);
			Configuration.getSessionSingleton(getSessionId()).setProperty(IConfiguration.STATUS_MESSAGES_TOKEN,
					generatedToken);
		}

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

		boolean sendingStatusMessages = Configuration.getSessionSingleton(getSessionId()).getPropertyAsBoolean(
				IConfiguration.SENDING_STATUS_MESSAGES, false);
		try {
			if (sendingStatusMessages) {
				StatusBroker.startHttpServer();
			}
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
		}

		experimentController.acquireMEController();
		try {
			for (MeasurementSpecification measSpec : scenario.getMeasurementSpecifications()) {

				experimentController.initialize(
						EngineTools.getConstantParameterValues(measSpec.getInitializationAssignemts()),
						scenario.getMeasurementEnvironmentDefinition());

				// loop over all the experiment series in the specs
				for (ExperimentSeriesDefinition esd : measSpec.getExperimentSeriesDefinitions()) {

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
			experimentController.releaseMEController();
			throw new RuntimeException(e);
		}

		if (sendingStatusMessages) {
			StatusBroker.get().getManager(generatedToken).newStatus(EventType.MEASUREMENT_FINISHED);
		}
		try {
			if (sendingStatusMessages) {
				StatusBroker.stopHttpServer();
			}
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
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

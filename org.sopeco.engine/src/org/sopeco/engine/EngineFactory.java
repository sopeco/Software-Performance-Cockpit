package org.sopeco.engine;

import java.net.URI;
import java.rmi.RemoteException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.config.exception.ConfigurationException;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experiment.impl.ExperimentController;
import org.sopeco.engine.experimentseries.IExperimentSeriesManager;
import org.sopeco.engine.experimentseries.impl.ExperimentSeriesManager;
import org.sopeco.engine.imp.EngineImp;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.rmi.RmiMEConnector;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;

/**
 * The factory is responsible for creating a valid instance of the SoPeCo
 * engine.
 * 
 * @author Dennis Westermann
 * @author Roozbeh Farahbod
 * @author Alexander Wert
 */
public final class EngineFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(EngineFactory.class);

	private static EngineFactory instance;

	private static final String DEFAULT_ENGINE_CONFIG_FILE_NAME = "sopeco-engine-defaults.conf";

	/**
	 * Private constructor for singleton.
	 */
	private EngineFactory() {

	}

	/**
	 * Creates an instance for {@link IEngine} using the given sessionId for all
	 * its session-aware objects .
	 * 
	 * @param sessionId
	 *            Session id to be used for the new engine and all its
	 *            session-aware elements.
	 * @return Returns the newly created engine instance.
	 * 
	 * 
	 */
	public IEngine createEngine(String sessionId) {
		final IConfiguration config = Configuration.getSessionSingleton(sessionId);

		loadDefaultConfigValues(sessionId);

		IExperimentController experimentController;
		String meClassName = config.getMeasurementControllerClassName();
		IMeasurementEnvironmentController meController = retrieveMEController(sessionId);
		
		experimentController = createExperimentController(sessionId, meController);

		LOGGER.debug("Experiment controller is created.");

		IExperimentSeriesManager expSeriesManager;
		expSeriesManager = new ExperimentSeriesManager();

		IEngine engine = new EngineImp(sessionId, experimentController, expSeriesManager);
		((ExperimentSeriesManager) expSeriesManager).setEngine(engine);
		return engine;
	}
	
	
	public IMeasurementEnvironmentController retrieveMEController(String sessionId) {
		final IConfiguration config = Configuration.getSessionSingleton(sessionId);

		String meClassName = config.getMeasurementControllerClassName();
		IMeasurementEnvironmentController meController = null;
		// if the measurement environment class is not set
		if (meClassName == null) {
			final URI meURI = config.getMeasurementControllerURI();
			meController = RmiMEConnector.connectToMEController(meURI);
			LOGGER.debug("Connected to the measurement environment controller service.");
		} else {
			// load the given class
			try {
				Class<?> mec = Class.forName(meClassName);
				Object o = mec.newInstance();
				if (o instanceof IMeasurementEnvironmentController) {
					LOGGER.debug("Measurement environment controller is instantiated.");
				} else {
					throw new RuntimeException("The measurement environment class must implement "
							+ IMeasurementEnvironmentController.class.getName() + ".");
				}
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Cannot load the measurement environment class (" + meClassName + ").");
			} catch (InstantiationException e) {
				throw new RuntimeException("Cannot instantiate the measurement environment object. Error: "
						+ e.getMessage());
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Cannot instantiate the measurement environment object. Error: "
						+ e.getMessage());
			}
		}
		return meController;
	}



	/**
	 * Loads the configuration defaults from the specified path into the SoPeCo
	 * configuration.
	 */
	private void loadDefaultConfigValues(String sessionId) {
		try {
			Configuration.getSessionSingleton(sessionId).loadDefaultConfiguration(this.getClass().getClassLoader(),
					DEFAULT_ENGINE_CONFIG_FILE_NAME);
		} catch (ConfigurationException e) {
			LOGGER.error("Unable to read default config.");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates an experiment controller based on the given measurement
	 * environment controller.
	 * 
	 * @param meController
	 * @return an instance of experiment controller
	 */
	protected IExperimentController createExperimentController(String sessionId, IMeasurementEnvironmentController meController) {
		ExperimentController expController = new ExperimentController(sessionId);
		expController.setMeasurementEnvironmentController(meController);
		return expController;
	}

	/**
	 * Returns the singleton instance of the factory.
	 * 
	 * @return Returns the singleton instance of the factory.
	 */
	public static EngineFactory getInstance() {
		if (instance == null) {
			instance = new EngineFactory();
		}
		return instance;
	}

}

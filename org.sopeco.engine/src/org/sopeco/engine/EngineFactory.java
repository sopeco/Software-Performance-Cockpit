package org.sopeco.engine;

import java.io.File;
import java.net.URI;

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

/**
 * The factory is responsible for creating a valid instance of the SoPeCo
 * engine.
 * 
 * @author Dennis Westermann
 * @author Roozbeh Farahbod
 */
public class EngineFactory {

	private static final Logger logger = LoggerFactory.getLogger(EngineFactory.class);

	public static EngineFactory INSTANCE = new EngineFactory();

	protected static IEngine engine = null;

	private static final String DEFAULT_ENGINE_CONFIG_FILE_NAME = "sopeco-engine-defaults.conf";

	/**
	 * Creates an engine.
	 */
	public IEngine createEngine() {
		final IConfiguration config = Configuration.getSingleton();

		loadDefaultConfigValues();

		IPersistenceProvider persistenceProvider = PersistenceProviderFactory.getPersistenceProvider();

		IExperimentController experimentController;
		String meClassName = config.getMeasurementControllerClassName();

		// if the measurement environment class is not set
		if (meClassName == null) {
			final URI meURI = config.getMeasurementControllerURI();
			final IMeasurementEnvironmentController meController = RmiMEConnector.connectToMEController(meURI);

			logger.debug("Connected to the measurement environment controller service.");

			experimentController = createExperimentController(meController, persistenceProvider);

		} else {
			// load the given class
			try {
				Class<?> mec = Class.forName(meClassName);
				Object o = mec.newInstance();
				if (o instanceof IMeasurementEnvironmentController) {
					logger.debug("Measurement environment controller is instantiated.");
					experimentController = createExperimentController((IMeasurementEnvironmentController) o, persistenceProvider);
				} else
					throw new RuntimeException("The measurement environment class must implement " + IMeasurementEnvironmentController.class.getName() + ".");
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Cannot load the measurement environment class (" + meClassName + ").");
			} catch (InstantiationException e) {
				throw new RuntimeException("Cannot instantiate the measurement environment object. Error: " + e.getMessage());
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Cannot instantiate the measurement environment object. Error: " + e.getMessage());
			}
		}

		logger.debug("Experiment controller is created.");

		IExperimentSeriesManager expSeriesManager;
		expSeriesManager = new ExperimentSeriesManager();

		engine = new EngineImp(experimentController, expSeriesManager, persistenceProvider);

		return engine;
	}

	/**
	 * Loads the configuration defaults from the specified path into the SoPeCo configuration.
	 */
	private void loadDefaultConfigValues() {
		try {
			Configuration.getSingleton()
					.loadDefaultConfiguration(this.getClass().getClassLoader(), DEFAULT_ENGINE_CONFIG_FILE_NAME);
		} catch (ConfigurationException e) {
			logger.error("Unable to read default config.");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the last created engine. If the engine is not created before,
	 * returns null.
	 * 
	 * @see #createEngine()
	 */
	public IEngine getEngine() {
		return engine;
	}
	
	public void registerEngine(IEngine newEngine){
		engine = newEngine;
	}

	/**
	 * Creates an experiment controller based on the given measurement
	 * environment controller.
	 * 
	 * @param meController
	 * @return an instance of experiment controller
	 */
	protected IExperimentController createExperimentController(IMeasurementEnvironmentController meController, IPersistenceProvider persistenceProvider) {
		ExperimentController expController = new ExperimentController();
		expController.setMeasurementEnvironmentController(meController);
		expController.setPersistenceProvider(persistenceProvider);
		return expController;
	}

}

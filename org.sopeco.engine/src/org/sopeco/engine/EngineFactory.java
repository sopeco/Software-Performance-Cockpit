package org.sopeco.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experiment.impl.ExperimentController;
import org.sopeco.engine.experiment.impl.MEConnector;
import org.sopeco.engine.experimentseries.IExperimentSeriesManager;
import org.sopeco.engine.experimentseries.impl.ExperimentSeriesManager;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;

public class EngineFactory {

	private static final Logger logger = LoggerFactory.getLogger(EngineFactory.class);
	
	public static EngineFactory INSTANCE = new EngineFactory();

	protected static IEngine engine = null;
	
	/**
	 * Creates an engine. 
	 */
	public IEngine createEngine() {
		final IConfiguration config = Configuration.getSingleton();
	
		IExperimentController experimentController;
		String meClassName = config.getPropertyAsStr(IConfiguration.CONF_MEASUREMENT_CONTROLLER_CLASS_NAME);
		
		// if the measurement environment class is not set
		if (meClassName == null) {
			final String meURI = config.getPropertyAsStr(IConfiguration.CONF_MEASUREMENT_CONTROLLER_URI);
			final IMeasurementEnvironmentController meController = MEConnector.getMeasurementEnvironmentController(meURI);
			
			logger.debug("Connected to the measurement environment controller service.");
			
			experimentController = createExperimentController(meController);
			
		} else {
			// load the given class
			try {
				Class<?> mec = Class.forName(meClassName);
				Object o = mec.newInstance();
				if (o instanceof IMeasurementEnvironmentController) {
					logger.debug("Measurement environment controller is instantiated.");
					experimentController = createExperimentController((IMeasurementEnvironmentController)o);
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
		
		IPersistenceProvider persistenceProvider = PersistenceProviderFactory.getPersistenceProvider();
		
		engine = new EngineImp(experimentController, expSeriesManager, persistenceProvider);

		return engine;
	}
	
	/**
	 * Returns the last created engine. If the engine is not created before, returns null.
	 * 
	 * @see #createEngine()
	 */
	public IEngine getEngine() {
		return engine;
	}

	/**
	 * Creates an experiment controller based on the given measurement environment controller.
	 * 
	 * @param meController
	 * @return an instance of experiment controller
	 */
	protected IExperimentController createExperimentController(IMeasurementEnvironmentController meController) {
		ExperimentController expController = new ExperimentController();
		expController.setMeasurementEnvironment(meController);
		return expController;
	}

}
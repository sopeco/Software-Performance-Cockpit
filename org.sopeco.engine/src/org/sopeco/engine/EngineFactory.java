package org.sopeco.engine;

import java.net.URI;

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

	public static EngineFactory INSTANCE = new EngineFactory();

	protected static IEngine engine = null;
	
	/**
	 * Creates an engine. 
	 */
	public IEngine createEngine() {
		final IConfiguration config = Configuration.getSingleton();
	
		IExperimentController experimentController;
		experimentController = createExperimentController(
				MEConnector.getMeasurementEnvironmentController(
						(URI)config.getProperty(IConfiguration.CONF_MEASUREMENT_CONTROLLER_URI)));
		
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
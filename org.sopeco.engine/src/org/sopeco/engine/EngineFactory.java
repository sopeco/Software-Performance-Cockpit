package org.sopeco.engine;

import java.net.URI;

import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experiment.impl.ExperimentController;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.model.configuration.ScenarioDefinition;

public class EngineFactory {

	public static EngineFactory INSTANCE = new EngineFactory();

	protected static IEngine engine = null;
	
	/**
	 * Creates an engine based on the given scenario definition. 
	 * 
	 * @param scenarioDefinition a scenario definition
	 */
	public IEngine createEngine(ScenarioDefinition scenarioDefinition) {
		
		// TODO properly create an engine
		
		engine = new EngineImp();
		return engine;
	}
	
	/**
	 * Returns the last created engine
	 */
	public IEngine getEngine() {
		return engine;
	}

	public IExperimentController createExperimentController(IMeasurementEnvironmentController meController) {
		ExperimentController expController = new ExperimentController();
		expController.setMeasurementEnvironment(meController);
		return expController;
	}

	public IExperimentController createExperimentController(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}
}

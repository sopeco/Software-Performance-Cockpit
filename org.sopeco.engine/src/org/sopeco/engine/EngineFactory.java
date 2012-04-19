package org.sopeco.engine;

import java.net.URI;

import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experiment.impl.ExperimentController;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;

public class EngineFactory {

	public static EngineFactory INSTANCE = new EngineFactory();

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

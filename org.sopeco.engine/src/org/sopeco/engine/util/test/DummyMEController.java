package org.sopeco.engine.util.test;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.AbstractMEController;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.InputParameter;
import org.sopeco.engine.measurementenvironment.ObservationParameter;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.NumberOfRepetitions;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.util.Tools;

/**
 * Dummy measurement environment controller that logs the input parameter values
 * passed to it and returns random values for the observation parameters. Class
 * can be used for test and debug purposes.
 * 
 * @author Dennis Westermann
 * 
 */
public class DummyMEController extends AbstractMEController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyMEController.class);

	@InputParameter(namespace = "init")
	private int initParameter;
	@InputParameter(namespace = "prepare")
	private int prepareParameter;
	@InputParameter(namespace = "test.input")
	private int inputParameter;
	@InputParameter(namespace = "test.input")
	private int inputParameter2;
	@ObservationParameter(namespace = "test.observation")
	private ParameterValueList<Integer> observationParameterOne;
	@ObservationParameter(namespace = "test.observation")
	private ParameterValueList<String> observationParameterTwo;






	@Override
	public void finalizeExperimentSeries() {
		LOGGER.debug("Finalize experiment series.");
	}



	private Object createRandomValue(ParameterDefinition parameter) {
		Random r = new Random(System.nanoTime());
		switch (Tools.SupportedTypes.get(parameter.getType())) {
		case Integer:
			return r.nextInt();
		case Double:
			return r.nextDouble();
		case Boolean:
			return r.nextBoolean();
		case String:
			return "Random_" + r.nextInt();
		default:
			throw new IllegalArgumentException("ParameterType not supported: " + parameter);
		}
	}



	@Override
	protected void initialize() {
		LOGGER.debug("Initialize measurement environment: initParameter = {}", initParameter);
		
	}

	@Override
	protected void prepareExperimentSeries() {
		LOGGER.debug("Initialize measurement environment: prepareParameter = {}", prepareParameter);
		
	}

	@Override
	protected void runExperiment() {
		
		
		
		LOGGER.debug("Run experiment: ");
		LOGGER.debug("Initialize measurement environment: inputParameter = {}", inputParameter);

		for (int i = 0; i < ((NumberOfRepetitions) getTerminationCondition()).getNumberOfRepetitions(); i++) {
			LOGGER.debug("Running repetition {}.", i + 1);
			observationParameterOne.addValue(createRandomValue(observationParameterOne.getParameter()));
			observationParameterTwo.addValue(createRandomValue(observationParameterTwo.getParameter()));
		}
		LOGGER.debug("Finished experiment.");
		
	
	}



	@Override
	protected void defineResultSet() {
		addParameterObservationsToResult(observationParameterOne);
		addParameterObservationsToResult(observationParameterTwo);
		
	}
}

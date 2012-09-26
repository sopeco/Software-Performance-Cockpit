package org.sopeco.runner;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.NumberOfRepetitions;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
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
public class DummyMEController implements IMeasurementEnvironmentController {

	private static Logger logger = LoggerFactory.getLogger(DummyMEController.class);

	private ParameterCollection<ParameterDefinition> observationParameterList = null;

	@Override
	public void initialize(ParameterCollection<ParameterValue<?>> initializationPVList) {
		logger.debug("Initialize measurement environment:");
		logParameterValueCollection(initializationPVList);
	}

	@Override
	public void prepareExperimentSeries(ParameterCollection<ParameterValue<?>> preparationPVList) {
		logger.debug("Prepare experiment series:");
		logParameterValueCollection(preparationPVList);
	}

	@Override
	public Collection<ParameterValueList<?>> runExperiment(ParameterCollection<ParameterValue<?>> inputPVList,
			ExperimentTerminationCondition terminationCondition) {
		Map<ParameterDefinition, ParameterValueList<?>> map = new HashMap<ParameterDefinition, ParameterValueList<?>>();
		for (ParameterDefinition pd : observationParameterList)
			map.put(pd, new ParameterValueList<Serializable>(pd));

		logger.debug("Run experiment: ");
		logParameterValueCollection(inputPVList);

		for (int i = 0; i < ((NumberOfRepetitions) terminationCondition).getNumberOfRepetitions(); i++) {
			logger.debug("Running repetition {}.", i + 1);
			for (ParameterDefinition parameter : observationParameterList) {
				map.get(parameter).addValue(createRandomValue(parameter));
			}
		}
		logger.debug("Finished experiment.");
		return new ArrayList<ParameterValueList<?>>(map.values());

	}

	@Override
	public void finalizeExperimentSeries() {
		logger.debug("Finalize experiment series.");
	}

	@Override
	public void setObservationParameters(ParameterCollection<ParameterDefinition> observationParameters) {
		logger.debug("Set observation parameters: ");
		logParameterDefinitionCollection(observationParameters);
		this.observationParameterList = observationParameters;
	}

	private void logParameterValueCollection(ParameterCollection<ParameterValue<?>> parameterCollection) {
		for (ParameterValue<?> parameterValue : parameterCollection) {
			logger.debug("{}: {}", parameterValue.getParameter().getFullName(), parameterValue.getValueAsString());
		}
	}

	private void logParameterDefinitionCollection(ParameterCollection<ParameterDefinition> parameterCollection) {
		for (ParameterDefinition parameterDefinition : parameterCollection) {
			logger.debug("{}", parameterDefinition.getFullName());
		}
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
	public MeasurementEnvironmentDefinition getMEDefinition() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
}

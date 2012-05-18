package org.sopeco.engine.helper;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.NumberOfRepetitions;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.util.ParameterCollection;

public class DummyMEController implements IMeasurementEnvironmentController {

	private static Logger logger = LoggerFactory.getLogger(DummyMEController.class);
	
	private ParameterCollection<ParameterValue<?>> initializationPVList;
	private ParameterCollection<ParameterValue<?>> preparationPVList;
	private ParameterCollection<ParameterDefinition> observationParameterList = null; 
	
	@Override
	public void initialize(ParameterCollection<ParameterValue<?>> initializationPVList) {
		logger.debug("Initialize measurement environment.");
		this.initializationPVList = initializationPVList;
	}

	@Override
	public void prepareExperimentSeries(ParameterCollection<ParameterValue<?>> preparationPVList) {
		logger.debug("Prepare experiment series");
		this.preparationPVList = preparationPVList;
	}

	@Override
	public Collection<ParameterValueList<?>> runExperiment(ParameterCollection<ParameterValue<?>> inputPVList, ExperimentTerminationCondition terminationCondition) {
		Map<ParameterDefinition, ParameterValueList<?>> map = new HashMap<ParameterDefinition, ParameterValueList<?>>();
		for (ParameterDefinition pd : observationParameterList)
			map.put(pd, new ParameterValueList<Object>(pd));
		
		logger.debug("Run experiment.");
		for (int i=0; i< ((NumberOfRepetitions)terminationCondition).getNumberOfRepetitions(); i++){
			logger.debug("Running repetition {}.", i+1);
			for(ParameterDefinition parameter : observationParameterList){
				map.get(parameter).addValue(new Integer(0));
			}
		}
		logger.debug("Finished experiment.");
		return map.values();

//		DataSetRowBuilder builder = new DataSetRowBuilder();
//		builder.startRow();
//		for (ParameterValue<?> parameterValue : inputPVList) {
//			builder.addInputParameterValue(parameterValue.getParameter(), parameterValue.getValue());
//		}
//		for (int i=0; i< ((NumberOfRepetitions)terminationCondition).getNumberOfRepetitions(); i++){
//			logger.debug("Running repetition {}.", i+1);
//			for(ParameterDefinition parameter : observationParameterList){
//				builder.addObservationParameterValue(parameter, 0);
//			}
//		}
//		builder.finishRow();
//		logger.debug("Finished experiment.");
//		return builder.createDataSet();

	}

	public ParameterValue<?> getInitializationValue() {
		assertEquals(1, initializationPVList.size());
		return initializationPVList.toArray(new ParameterValue<?>[] {})[0];
	}

	public ParameterValue<?> getPreparationValue() {
		assertEquals(1, preparationPVList.size());
		return preparationPVList.toArray(new ParameterValue<?>[] {})[0];
	}

	@Override
	public void finalizeExperimentSeries() {
		// no need to do something here
		logger.debug("Finalize experiment series.");
	}
	
	@Override
	public void setObservationParameters(ParameterCollection<ParameterDefinition> observationParameters){
		logger.debug("Set observation parameters.");
		this.observationParameterList = observationParameters;
	}

}

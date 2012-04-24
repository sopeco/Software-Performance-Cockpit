package org.sopeco.engine.helper;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.model.configuration.environment.MeasurementEnvironmentDefinition;
import org.sopeco.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.ParameterValue;

public class DummyMEController implements IMeasurementEnvironmentController {

	private List<ParameterValue<?>> initializationPVList;
	private List<ParameterValue<?>> preparationPVList;
	private MeasurementEnvironmentDefinition meDefinition;

	@Override
	public void initialize(List<ParameterValue<?>> initializationPVList) {
		this.initializationPVList = initializationPVList;
	}

	@Override
	public void prepareExperimentSeries(Collection<ParameterValue<?>> preparationPVList) {
		this.preparationPVList = new ArrayList<ParameterValue<?>>(preparationPVList);
	}

	@Override
	public DataSetAggregated runExperiment(List<ParameterValue<?>> inputPVList, ExperimentTerminationCondition terminationCondition) {
		
		// TODO Auto-generated method stub
		return null;
	}

	public ParameterValue<?> getInitializationValue() {
		assertEquals(1, initializationPVList.size());
		return initializationPVList.get(0);
	}

	public ParameterValue<?> getPreparationValue() {
		assertEquals(1, preparationPVList.size());
		return preparationPVList.get(0);
	}

	@Override
	public void setMeasurementEnvironment(MeasurementEnvironmentDefinition meDefinition) {
		this.meDefinition = meDefinition;
	}

}

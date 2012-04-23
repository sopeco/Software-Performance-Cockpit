package org.sopeco.engine.experiment.impl;

import java.util.List;

import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.model.configuration.environment.MeasurementEnvironmentDefinition;
import org.sopeco.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.ExperimentSeriesRun;

public class ExperimentController implements IExperimentController {
	
	IMeasurementEnvironmentController meController = null;

	@Override
	public void initialize(List<ParameterValue<?>> initializationPVList) {
		meController.initialize(initializationPVList);
	}

	@Override
	public void prepareExperimentSeries(ExperimentSeriesRun experimentSeriesRun, List<ParameterValue<?>> preparationPVList) {
		meController.prepareExperimentSeries(preparationPVList);
		// TODO do it right!
	}

	@Override
	public DataSetAggregated runExperiment(List<ParameterValue<?>> inputPVList, ExperimentTerminationCondition terminationCondition) {
		if (terminationCondition == null)
			throw new IllegalArgumentException("TerminationCondition must be set (not null).");
		
		// TODO append the new row to the already-persisted data
		
		return meController.runExperiment(inputPVList, terminationCondition);
	}

	public void setPersistenceProvider(IPersistenceProvider persistenceProvider) {
		// TODO Auto-generated method stub
	}

	public void setMeasurementEnvironment(IMeasurementEnvironmentController meController) {
		this.meController = meController;
	}

	public void setMeasurementEnvironment(MeasurementEnvironmentDefinition meDefinition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finalizeExperimentSeries() {
		// TODO Auto-generated method stub
	}

}

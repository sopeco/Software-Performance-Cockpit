package org.sopeco.engine.experiment;

import java.net.URI;
import java.util.List;

import org.sopeco.core.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.engine.experimentseries.ParameterValue;


/**
 * An ExperimentController controls execution of any single experiment.
 * 
 * @author D053711
 *
 */
public interface IExperimentController {
	
	/**
	 * @param environmentUri
	 */
	public void initialize(URI environmentUri, List<ParameterValue> initializationPVList);
	
	public void prepareExperimentSeries(List<ParameterValue> preparationAssignments);
	
	/**
	 * To be executed you have to set the entry point of the measurement env. first by calling initialize(...) 
	 */
	public DataSetAggregated runExperiment(List<ParameterValue> inputPVList, ExperimentTerminationCondition terminationCondition);

	public void setPersistenceProvider(IPersistenceProvider persistenceProvider);
	
}

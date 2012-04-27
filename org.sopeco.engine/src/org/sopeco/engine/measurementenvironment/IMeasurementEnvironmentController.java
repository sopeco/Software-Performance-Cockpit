package org.sopeco.engine.measurementenvironment;

import java.util.Collection;
import java.util.List;

import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;

/**
 * Interface that has to be implemented by the provider of the measurement environment. 
 * It is the connection between the SoPeCo engine and the target system.
 * 
 * @author Dennis Westermann, Roozbeh Farahbod
 *
 */
public interface IMeasurementEnvironmentController {

	/**
	 * Initializes the measurement environment controller with a list 
	 * of initialization values.
	 * 
	 * @param initializationPVList the list of constant values defined in the scenario as initialization assignments
	 */
	public void initialize(List<ParameterValue<?>> initializationPVList);

	/**
	 * Prepares the measurement environment for a series of experiments with a collection 
	 * of value assignments that remain constant in the series. 
	 *   
	 * @param preparationPVList a collection of constant value assignments
	 */
	public void prepareExperimentSeries(Collection<ParameterValue<?>> preparationPVList);
	
	/**
	 * Runs a single experiment on the measurement environment. As a result, for every 
	 * observation parameter a list of observed values is returned. 
	 * 
	 * @param inputPVList a list of input value assignments
	 * @param terminationCondition a termination condition
	 * 
	 * @return a collection of parameter value lists that includes one parameter value list for every observation parameter
	 */
	public Collection<ParameterValueList<?>> runExperiment(List<ParameterValue<?>> inputPVList, ExperimentTerminationCondition  terminationCondition);
	
	/**
	 * Finalizes the measurement environment.
	 */
	public void finalizeExperimentSeries();

	/**
	 * Sets the list of observation parameters. 
	 * 
	 * @param observationParameters
	 */
	void setObservationParameters(List<ParameterDefinition> observationParameters);
	
}

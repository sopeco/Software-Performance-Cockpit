package org.sopeco.engine.measurementenvironment;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.exceptions.ExperimentFailedException;
import org.sopeco.persistence.util.ParameterCollection;

/**
 * Interface that has to be implemented by the provider of the measurement
 * environment. It is the connection between the SoPeCo engine and the target
 * system.
 * 
 * @author Dennis Westermann, Roozbeh Farahbod, Alexander Wert
 * 
 */
public interface IMeasurementEnvironmentController extends Remote {



	/**
	 * Initializes the measurement environment controller with a list of
	 * initialization values.
	 * 
	 * @param initializationPVs
	 *            the collection of constant values defined in the scenario as
	 *            initialization assignments
	 * 
	 * @throws RemoteException
	 *             throws this exception if remote communication fails
	 */
	void initialize(ParameterCollection<ParameterValue<?>> initializationPVs) throws RemoteException;

	/**
	 * Prepares the measurement environment for a series of experiments with a
	 * collection of value assignments that remain constant in the series.
	 * 
	 * @param preparationPVs
	 *            a collection of constant value assignments
	 * @throws RemoteException
	 *             throws this exception if remote communication fails
	 */
	void prepareExperimentSeries(ParameterCollection<ParameterValue<?>> preparationPVs) throws RemoteException;

	/**
	 * Runs a single experiment on the measurement environment. As a result, for
	 * every observation parameter a list of observed values is returned.
	 * 
	 * @param inputPVs
	 *            a collection of input value assignments
	 * @param terminationCondition
	 *            a termination condition
	 * 
	 * @return a collection of parameter value lists that includes one parameter
	 *         value list for every observation parameter
	 * @throws RemoteException
	 *             throws this exception if remote communication fails
	 * @throws ExperimentFailedException
	 *             throws this exception if experiment execution fails
	 */
	Collection<ParameterValueList<?>> runExperiment(ParameterCollection<ParameterValue<?>> inputPVs,
			ExperimentTerminationCondition terminationCondition) throws RemoteException, ExperimentFailedException;

	/**
	 * Finalizes the measurement environment.
	 * 
	 * @throws RemoteException
	 *             throws this exception if remote communication fails
	 */
	void finalizeExperimentSeries() throws RemoteException;

	/**
	 * Returns the measurement environment definition (
	 * {@link MeasurementEnvironmentDefinition}) known to this MEController.
	 * 
	 * @return Returns an instance of the (
	 *         {@link MeasurementEnvironmentDefinition}) known to this
	 *         MEController. If this MEController is not able to provide a
	 *         measurement environment definition, this method returns null.
	 * @throws RemoteException
	 *             throws this exception if remote communication fails
	 */
	MeasurementEnvironmentDefinition getMEDefinition() throws RemoteException;

}

/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.engine.measurementenvironment;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.sopeco.engine.status.StatusMessage;
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
	 * @param acquirerID
	 *            identifier of the acquirer holding the MEController
	 * @param initializationPVs
	 *            the collection of constant values defined in the scenario as
	 *            initialization assignments
	 * 
	 * @throws RemoteException
	 *             throws this exception if remote communication fails or if
	 *             acquirerID does not match the id of the actual holder of the
	 *             MEController
	 */
	void initialize(String acquirerID, ParameterCollection<ParameterValue<?>> initializationPVs) throws RemoteException;

	/**
	 * Prepares the measurement environment for a series of experiments with a
	 * collection of value assignments that remain constant in the series.
	 * 
	 * @param acquirerID
	 *            identifier of the acquirer holding the MEController
	 * @param preparationPVs
	 *            a collection of constant value assignments
	 * @param terminationConditions
	 *            a collection of termination conditions set by the scenario
	 *            definition
	 * @throws RemoteException
	 *             throws this exception if remote communication fails or if
	 *             acquirerID does not match the id of the actual holder of the
	 *             MEController
	 */
	void prepareExperimentSeries(String acquirerID, ParameterCollection<ParameterValue<?>> preparationPVs,
			Set<ExperimentTerminationCondition> terminationConditions) throws RemoteException;

	/**
	 * Runs a single experiment on the measurement environment. As a result, for
	 * every observation parameter a list of observed values is returned.
	 * 
	 * @param acquirerID
	 *            identifier of the acquirer holding the MEController
	 * @param inputPVs
	 *            a collection of input value assignments
	 * 
	 * @return a collection of parameter value lists that includes one parameter
	 *         value list for every observation parameter
	 * @throws RemoteException
	 *             throws this exception if remote communication fails
	 * @throws ExperimentFailedException
	 *             throws this exception if remote communication fails or if
	 *             acquirerID does not match the id of the actual holder of the
	 *             MEController
	 */
	Collection<ParameterValueList<?>> runExperiment(String acquirerID, ParameterCollection<ParameterValue<?>> inputPVs)
			throws RemoteException, ExperimentFailedException;

	/**
	 * Finalizes the measurement environment.
	 * 
	 * @param acquirerID
	 *            identifier of the acquirer holding the MEController
	 * @throws RemoteException
	 *             throws this exception if remote communication fails or if
	 *             acquirerID does not match the id of the actual holder of the
	 *             MEController
	 */
	void finalizeExperimentSeries(String acquirerID) throws RemoteException;

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

	/**
	 * Acquires the corresponding MEController for execution of measurements for
	 * the acquirer identified by the passed {@link acquirerID} . As soon as the
	 * corresponding MEController is acquired, the MEController cannot be used
	 * for other measurements until the MEController is released explicitly by
	 * the acquirer. If the MEController cannot be assigned within the specified
	 * time frame, the method returns <code>false</code>.
	 * 
	 * 
	 * @param acquirerID
	 *            identifier of the acquirer. This id has to be used for
	 *            releasing the MEController, as well.
	 * @param timeout
	 *            The maximum waiting time in seconds for getting the
	 *            MEController assigned to the acquirer. If timeout is zero or
	 *            negative the method returns <code>false</code> immediately if
	 *            the MEController could not be acquired.
	 * @return <code>true</code> if the MEController has been assigned
	 *         successfully to the acquirer, <code>false</code> if timeout has
	 *         been reached without the MEController beeing assigned to the
	 *         qcauirer
	 * @throws RemoteException
	 *             throws this exception if remote communication fails or a
	 *             remote exception is thrown
	 */
	boolean acquireMEController(String acquirerID, long timeout) throws RemoteException;

	/**
	 * This method releases the MEController for the given acquirerID. As soon
	 * as the MEController is released the it can be used by another acquirer.
	 * 
	 * @param acquirerID
	 *            identifier of the acquirer the MEController is currently
	 *            assigned to
	 * @throws RemoteException
	 *             throws this exception if remote communication fails or a
	 *             remote exception is thrown
	 */
	void releaseMEController(String acquirerID) throws RemoteException;

	/**
	 * @return Returns the current state of the MEController.
	 * @throws RemoteException
	 *             throws this exception if remote communication fails or a
	 *             remote exception is thrown
	 */
	MEControllerState getState() throws RemoteException;

	/**
	 * @return Returns the number of measurement jobs waiting for acquisition of
	 *         the MEController.
	 * @throws RemoteException
	 *             throws this exception if remote communication fails or a
	 *             remote exception is thrown
	 */
	int getQueueLength() throws RemoteException;
	
	
	List<StatusMessage> fetchStatusMessages() throws RemoteException;
}

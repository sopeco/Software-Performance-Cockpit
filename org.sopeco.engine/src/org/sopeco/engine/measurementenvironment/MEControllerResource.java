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

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.sopeco.engine.measurementenvironment.status.StatusProvider;
import org.sopeco.engine.status.ProgressInfo;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.exceptions.ExperimentFailedException;
import org.sopeco.persistence.util.ParameterCollection;

/**
 * This interface encapsulates the passive resource characteristic of an
 * MEController. In particular, an MEController can be used only by one
 * measurement executer at a time. For that purpose, this class implements an
 * acquire/release mechanism
 * 
 * @author Alexander Wert
 * 
 */
public abstract class MEControllerResource implements IMeasurementEnvironmentController {

	private final Semaphore semaphore;
	private MEControllerState currentState;
	private String assignedTo;

	/** The StatusProvider to send information of the experiment execution. */
	private StatusProvider statusProvider;

	/**
	 * Constructor. Initial state is MEControllerState.AVAILABLE.
	 */
	protected MEControllerResource() {
		semaphore = new Semaphore(1, true);
		updateState(MEControllerState.AVAILABLE);
	}

	@Override
	public boolean acquireMEController(String acquirerID, long timeout) throws RemoteException {
		try {
			boolean acquired = semaphore.tryAcquire(timeout, TimeUnit.SECONDS);

			synchronized (this) {
				if (acquired) {
					assignedTo = acquirerID;
				}
				statusProvider = StatusProvider.getProvider(this);
				updateState(MEControllerState.IN_USE);
				return acquired;
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public synchronized void releaseMEController(String acquirerID) throws RemoteException {
		if (acquirerID.equals(assignedTo)) {
			cleanUpMEController();
			semaphore.release();
			assignedTo = null;
			updateState(MEControllerState.AVAILABLE);
		} else {
			throw new RuntimeException(
					"Invalid acquirerID. The MEController can only be released with the same acquirerID as it was acquired with!");
		}
	}

	@Override
	public void initialize(String acquirerID, ParameterCollection<ParameterValue<?>> initializationPVs)
			throws RemoteException {
		checkExecutionPermission(acquirerID);
		updateState(MEControllerState.INITIALIZATION);
		initialize(initializationPVs);
	}

	@Override
	public void prepareExperimentSeries(String acquirerID, ParameterCollection<ParameterValue<?>> preparationPVs,
			Set<ExperimentTerminationCondition> terminationConditions) throws RemoteException {
		checkExecutionPermission(acquirerID);
		updateState(MEControllerState.SERIES_PREPARATION);
		prepareExperimentSeries(preparationPVs, terminationConditions);
	}

	@Override
	public Collection<ParameterValueList<?>> runExperiment(String acquirerID,
			ParameterCollection<ParameterValue<?>> inputPVs) throws RemoteException, ExperimentFailedException {
		checkExecutionPermission(acquirerID);
		updateState(MEControllerState.EXPERIMENT_EXECUTION);
		return runExperiment(inputPVs);
	}

	@Override
	public void finalizeExperimentSeries(String acquirerID) throws RemoteException {
		checkExecutionPermission(acquirerID);
		updateState(MEControllerState.FINALIZING_SERIES);
		finalizeExperimentSeries();
	}

	@Override
	public synchronized MEControllerState getState() throws RemoteException {
		return currentState;
	}

	@Override
	public int getQueueLength() throws RemoteException {
		return semaphore.getQueueLength();
	}

	protected abstract void initialize(ParameterCollection<ParameterValue<?>> initializationPVs);

	protected abstract void prepareExperimentSeries(ParameterCollection<ParameterValue<?>> preparationPVs,
			Set<ExperimentTerminationCondition> terminationConditions);

	protected abstract Collection<ParameterValueList<?>> runExperiment(ParameterCollection<ParameterValue<?>> inputPVs)
			throws ExperimentFailedException;

	protected abstract void finalizeExperimentSeries();

	protected abstract void cleanUpMEController();

	private synchronized void updateState(MEControllerState newState) {
		currentState = newState;
	}

	private void checkExecutionPermission(String acquirerID) {
		if (currentState.equals(MEControllerState.AVAILABLE)) {
			throw new RuntimeException(
					"The MEController has not been acquired, yet. Please properly acquire the MEController before executing any operations on it!");
		}

		if (!acquirerID.equals(assignedTo)) {
			throw new RuntimeException(
					"The MEController is used by another process. Please properly acquire the MEController before executing any operations on it!");
		}
	}

	/**
	 * Sends a message/information to SoPeCo.
	 * 
	 * @param information
	 * 
	 * @deprecated Use instead of this the method
	 *             {@link MEControllerResource#print(String)}
	 */
	@Deprecated
	protected void sendInformation(String information) {
		print(information);
	}

	/**
	 * Sends a message/information to SoPeCo.
	 * 
	 * @param information
	 */
	protected void print(String information) {
		if (statusProvider == null) {
			return;
		}
		statusProvider.sendInformation(information);
	}

	/**
	 * Sends a error message/information to SoPeCo.
	 * 
	 * @param information
	 */
	protected void printErr(String errorText) {
		if (statusProvider == null) {
			return;
		}
		statusProvider.sendError(errorText);
	}

	/**
	 * Sends a ProgressInfo package with a description to SoPeCo.
	 * 
	 * @param description
	 * @param progressInfo
	 */
	protected void sendProgressInfo(String description, ProgressInfo progressInfo) {
		if (statusProvider == null) {
			return;
		}
		statusProvider.sendProgressInfo(description, progressInfo);
	}
}

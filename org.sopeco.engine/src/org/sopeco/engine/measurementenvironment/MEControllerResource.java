package org.sopeco.engine.measurementenvironment;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
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

	/**
	 * Constructor. Initial state is MEControllerState.AVAILABLE.
	 */
	public MEControllerResource() {
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
	public void prepareExperimentSeries(String acquirerID, ParameterCollection<ParameterValue<?>> preparationPVs)
			throws RemoteException {
		checkExecutionPermission(acquirerID);
		updateState(MEControllerState.SERIES_PREPARATION);
		prepareExperimentSeries(preparationPVs);
	}

	@Override
	public Collection<ParameterValueList<?>> runExperiment(String acquirerID,
			ParameterCollection<ParameterValue<?>> inputPVs)
			throws RemoteException, ExperimentFailedException {
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

	protected abstract void prepareExperimentSeries(ParameterCollection<ParameterValue<?>> preparationPVs);

	protected abstract Collection<ParameterValueList<?>> runExperiment(ParameterCollection<ParameterValue<?>> inputPVs) throws ExperimentFailedException;

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

}

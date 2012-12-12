package org.sopeco.engine.measurementenvironment;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.sopeco.engine.status.EventType;
import org.sopeco.engine.status.InitializePackage;
import org.sopeco.engine.status.StatusSender;
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
	private StatusSender statusSender;

	/**
	 * Constructor. Initial state is MEControllerState.AVAILABLE.
	 */
	protected MEControllerResource() {
		semaphore = new Semaphore(1, true);
		updateState(MEControllerState.AVAILABLE);
	}

	@Override
	public boolean acquireMEController(String acquirerID, long timeout) throws RemoteException {
		return acquireMEController(acquirerID, timeout, null);
	}

	@Override
	public boolean acquireMEController(String acquirerID, long timeout, InitializePackage initializePackage)
			throws RemoteException {
		StatusSender nextStatusSender = new StatusSender(initializePackage);
		try {
			nextStatusSender.next(EventType.ACQUIRE_MEC);
			boolean acquired = semaphore.tryAcquire(timeout, TimeUnit.SECONDS);

			synchronized (this) {
				if (acquired) {
					assignedTo = acquirerID;
				}
				updateState(MEControllerState.IN_USE);
				statusSender = nextStatusSender;
				return acquired;
			}
		} catch (InterruptedException e) {
			nextStatusSender.next(EventType.ACQUIRE_MEC_FAILED, e);
			throw new RuntimeException(e);
		}

	}

	@Override
	public synchronized void releaseMEController(String acquirerID) throws RemoteException {
		statusSender.next(EventType.RELEASE_MEC);
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
		statusSender.next(EventType.INIT_MEC);
		checkExecutionPermission(acquirerID);
		updateState(MEControllerState.INITIALIZATION);
		initialize(initializationPVs);
	}

	@Override
	public void prepareExperimentSeries(String acquirerID, ParameterCollection<ParameterValue<?>> preparationPVs,
			Set<ExperimentTerminationCondition> terminationConditions) throws RemoteException {
		statusSender.next(EventType.PREPARE_EXPERIMENTSERIES);
		checkExecutionPermission(acquirerID);
		updateState(MEControllerState.SERIES_PREPARATION);
		prepareExperimentSeries(preparationPVs, terminationConditions);
	}

	@Override
	public Collection<ParameterValueList<?>> runExperiment(String acquirerID,
			ParameterCollection<ParameterValue<?>> inputPVs) throws RemoteException, ExperimentFailedException {
		statusSender.next(EventType.EXECUTE_EXPERIMENTRUN);
		checkExecutionPermission(acquirerID);
		updateState(MEControllerState.EXPERIMENT_EXECUTION);
		return runExperiment(inputPVs);
	}

	@Override
	public void finalizeExperimentSeries(String acquirerID) throws RemoteException {
		statusSender.next(EventType.FINALIZE_EXPERIMENTSERIES);
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

}

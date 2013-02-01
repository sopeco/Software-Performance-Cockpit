package org.sopeco.engine.measurementenvironment.socket;

import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.MEControllerState;
import org.sopeco.engine.status.StatusMessage;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.exceptions.ExperimentFailedException;
import org.sopeco.persistence.util.ParameterCollection;

public class SocketMECWrapper implements IMeasurementEnvironmentController {

	private Socket socket;
	private String name;

	public SocketMECWrapper(Socket pSocket, String pName) {
		socket = pSocket;
		name = pName;
	}

	@Override
	public void initialize(String acquirerID,
			ParameterCollection<ParameterValue<?>> initializationPVs)
			throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void prepareExperimentSeries(String acquirerID,
			ParameterCollection<ParameterValue<?>> preparationPVs,
			Set<ExperimentTerminationCondition> terminationConditions)
			throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<ParameterValueList<?>> runExperiment(String acquirerID,
			ParameterCollection<ParameterValue<?>> inputPVs)
			throws RemoteException, ExperimentFailedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void finalizeExperimentSeries(String acquirerID)
			throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public MeasurementEnvironmentDefinition getMEDefinition()
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean acquireMEController(String acquirerID, long timeout)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void releaseMEController(String acquirerID) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public MEControllerState getState() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getQueueLength() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<StatusMessage> fetchStatusMessages() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}

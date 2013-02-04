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
package org.sopeco.engine.measurementenvironment.socket;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.MEControllerState;
import org.sopeco.engine.status.StatusBroker;
import org.sopeco.engine.status.StatusMessage;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.exceptions.ExperimentFailedException;
import org.sopeco.persistence.util.ParameterCollection;

public class SocketMECWrapper extends ConnectionResource implements IMeasurementEnvironmentController {

	private String controllerName;

	public SocketMECWrapper(ConnectionResource conResource, String pName) {
		controllerName = pName;

		bind(conResource);
	}

	public String getName() {
		return controllerName;
	}

	@Override
	public void initialize(String acquirerID, ParameterCollection<ParameterValue<?>> initializationPVs)
			throws RemoteException {
		call(acquirerID, initializationPVs);
	}

	@Override
	public void prepareExperimentSeries(String acquirerID, ParameterCollection<ParameterValue<?>> preparationPVs,
			Set<ExperimentTerminationCondition> terminationConditions) throws RemoteException {
		call(acquirerID, preparationPVs, terminationConditions);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<ParameterValueList<?>> runExperiment(String acquirerID,
			ParameterCollection<ParameterValue<?>> inputPVs) throws RemoteException, ExperimentFailedException {
		Object result = call(acquirerID, inputPVs);
		while (true) {
			if (result instanceof StatusMessage) {
				StatusBroker.getManagerViaSessionID(acquirerID).newStatus((StatusMessage) result);
			} else {
				break;
			}

			try {
				result = getOiStream().readObject();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return (Collection<ParameterValueList<?>>) result;
	}

	@Override
	public void finalizeExperimentSeries(String acquirerID) throws RemoteException {
		call(acquirerID);
	}

	@Override
	public MeasurementEnvironmentDefinition getMEDefinition() throws RemoteException {
		return call();
	}

	@Override
	public boolean acquireMEController(String acquirerID, long timeout) throws RemoteException {
		return this.<Boolean> call(acquirerID, timeout);
	}

	@Override
	public void releaseMEController(String acquirerID) throws RemoteException {
		call(acquirerID);
	}

	@Override
	public MEControllerState getState() throws RemoteException {
		return call();
	}

	@Override
	public int getQueueLength() throws RemoteException {
		return this.<Integer> call();
	}

	@Override
	public List<StatusMessage> fetchStatusMessages() throws RemoteException {
		return call();
	}

	private <T extends Object> T call(Object... parameter) {
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		return callMethod(controllerName, methodName, parameter);
	}
}

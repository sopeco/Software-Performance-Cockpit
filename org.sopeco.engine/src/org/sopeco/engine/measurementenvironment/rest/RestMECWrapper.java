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
package org.sopeco.engine.measurementenvironment.rest;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.MEControllerState;
import org.sopeco.engine.measurementenvironment.connector.RestMEConnector;
import org.sopeco.engine.measurementenvironment.rest.TransferPackage.State;
import org.sopeco.engine.status.StatusBroker;
import org.sopeco.engine.status.StatusMessage;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.exceptions.ExperimentFailedException;
import org.sopeco.persistence.util.ParameterCollection;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class RestMECWrapper implements IMeasurementEnvironmentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestMEConnector.class);

	private static final int REQUEST_DELAY = 1000;

	private String url;
	private String controllerName;

	public RestMECWrapper(String pUrl, String pControllerName) {
		url = pUrl;
		controllerName = pControllerName;
	}

	private String getFullUrl() {
		return url + controllerName;
	}

	private TransferPackage waitingForController(State expectedState, String acquirerId) throws RemoteException {
		while (true) {

			try {
				Thread.sleep(REQUEST_DELAY);
			} catch (InterruptedException e) {
				LOGGER.info(e.getMessage());
			}

			TransferPackage result = request("waiting", acquirerId);

			if (result.getB() != null) {
				List<StatusMessage> list = result.getB(List.class);
				for (StatusMessage s : list) {
					StatusBroker.getManager(getFullUrl()).newStatus(s);
				}
			}

			if (result.getA(State.class) != State.WAITING) {
				if (result.getA(State.class) != expectedState) {
					throw new IllegalStateException("Returned state is '" + result.getA(State.class) + "' and '"
							+ expectedState + "' was expected.");
				}
				return result;
			}
		}
	}

	private TransferPackage request(TransferPackage putPackage, String... paths) throws RemoteException {

		WebResource resource = Client.create().resource(getFullUrl());
		for (String path : paths) {
			resource = resource.path(path);
		}

		Builder requestBuilder = resource.accept(MediaType.APPLICATION_XML);
		ClientResponse response;
		if (putPackage == null) {
			// Do GET request
			response = requestBuilder.get(ClientResponse.class);
		} else {
			// Do PUT request
			response = requestBuilder.type(MediaType.APPLICATION_XML).put(ClientResponse.class, putPackage);
		}

		if (response.getStatus() == Status.OK.getStatusCode() && response.hasEntity()) {
			return response.getEntity(TransferPackage.class);
		} else if (response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
			return null;
		} else {
			throw new RemoteException("Request was not successful. Server returned: " + response.getStatus());
		}

	}

	private TransferPackage request(String... paths) throws RemoteException {
		return request(null, paths);
	}

	@Override
	public void initialize(String acquirerID, ParameterCollection<ParameterValue<?>> initializationPVs)
			throws RemoteException {

		TransferPackage putPackage = new TransferPackage(initializationPVs);
		boolean success = request(putPackage, "initialize", acquirerID).getA(Boolean.class);

		if (success) {
			LOGGER.debug("Initialize controller '{}' successful.", controllerName);
		} else {
			LOGGER.error("Initialize controller '{}' failed.", controllerName);
		}
	}

	@Override
	public void prepareExperimentSeries(String acquirerID, ParameterCollection<ParameterValue<?>> preparationPVs,
			Set<ExperimentTerminationCondition> terminationConditions) throws RemoteException {

		TransferPackage putPackage = new TransferPackage(preparationPVs, terminationConditions);
		boolean success = request(putPackage, "prepareExperimentSeries", acquirerID).getA(Boolean.class);

		if (success) {
			LOGGER.debug("Prepare ExperimentSeries on controller '{}' successful.", controllerName);
		} else {
			LOGGER.error("Prepare ExperimentSeries on controller '{}' failed.", controllerName);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<ParameterValueList<?>> runExperiment(String acquirerID,
			ParameterCollection<ParameterValue<?>> inputPVs) throws RemoteException, ExperimentFailedException {
		LOGGER.debug("Run experiment on controller '{}'.", controllerName);

		TransferPackage putPackage = new TransferPackage(inputPVs);
		request(putPackage, "runExperiment", acquirerID);

		Object result = waitingForController(State.RUNFINISHED, acquirerID).getC();

		if (result instanceof RemoteException) {
			throw (RemoteException) result;
		} else if (result instanceof ExperimentFailedException) {
			throw new ExperimentFailedException(((Exception) result).getMessage());
		} else if (result instanceof RuntimeException) {
			throw (RuntimeException) result;
		} else if (result instanceof Exception) {
			throw new RuntimeException(((Exception) result).getMessage());
		}

		LOGGER.debug("Experiment run finished on controller '{}'.", controllerName);

		return (Collection<ParameterValueList<?>>) result;
	}

	@Override
	public void finalizeExperimentSeries(String acquirerID) throws RemoteException {
		boolean success = request("finalizeExperimentSeries", acquirerID).getA(Boolean.class);

		if (success) {
			LOGGER.debug("Finalize ExperimentSeries on controller '{}' successful.", controllerName);
		} else {
			LOGGER.error("Finalize ExperimentSeries on controller '{}' failed.", controllerName);
		}
	}

	@Override
	public MeasurementEnvironmentDefinition getMEDefinition() throws RemoteException {
		return request("meDefinition").getA(MeasurementEnvironmentDefinition.class);
	}

	@Override
	public boolean acquireMEController(String acquirerID, long timeout) throws RemoteException {
		LOGGER.debug("Trying to acquire controller '{}'.", controllerName);

		request("aquire", acquirerID, "" + timeout);
		boolean result = waitingForController(State.ACQUIRED, acquirerID).getC(Boolean.class);
		if (result) {
			LOGGER.debug("Controller '{}' acquired.", controllerName);
		} else {
			LOGGER.error("Acquiring failed.");
		}
		return result;
	}

	@Override
	public void releaseMEController(String acquirerID) throws RemoteException {
		request("release", acquirerID);
		boolean result = waitingForController(State.RELEASED, acquirerID).getC(Boolean.class);
		if (result) {
			LOGGER.debug("Controller '{}' released.", controllerName);
		} else {
			LOGGER.error("Releasing failed.");
		}
	}

	@Override
	public MEControllerState getState() throws RemoteException {
		return request("state").getA(MEControllerState.class);
	}

	@Override
	public int getQueueLength() throws RemoteException {
		return request("queueLength").getA(Integer.class);
	}

	@Override
	public List<StatusMessage> fetchStatusMessages() {
		// TODO
		return null;
	}
}

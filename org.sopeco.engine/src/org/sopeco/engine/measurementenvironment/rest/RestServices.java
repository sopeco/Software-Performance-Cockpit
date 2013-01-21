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
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.MEControllerState;
import org.sopeco.engine.measurementenvironment.app.MECApplication;
import org.sopeco.engine.measurementenvironment.rest.TransferPackage.State;
import org.sopeco.engine.measurementenvironment.status.StatusProvider;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.util.ParameterCollection;

/**
 * 
 * @author Marius Oehler
 * 
 */
@Path("")
public class RestServices {
	/** */
	public static final String ALIVE_MESSAGE = "Yep!";

	private static final Logger LOGGER = LoggerFactory.getLogger(RestServices.class);

	@GET
	@Path("alive")
	@Produces(MediaType.TEXT_PLAIN)
	public String alive() {
		return ALIVE_MESSAGE;
	}

	@GET
	@Path("{controllerName}/aquire/{acquirerID}/{timeout}")
	public Response aquireController(@PathParam("controllerName") String controllerName,
			@PathParam("acquirerID") String acquirerID, @PathParam("timeout") String timeout) {
		LOGGER.debug("Acquire controller '{}' with id '{}' and timeout '" + timeout + "'.", controllerName, acquirerID);

		MECExecutor.acquire(controllerName, acquirerID, Long.parseLong(timeout));

		return Response.noContent().build();
	}

	@GET
	@Path("{controllerName}/finalizeExperimentSeries/{acquirerID}")
	@Produces(MediaType.APPLICATION_XML)
	public TransferPackage finalizeExperimentSeries(@PathParam("controllerName") String controllerName,
			@PathParam("acquirerID") String acquirerID) throws RemoteException {
		LOGGER.debug("Finalize ExperimentSeries on controller '{}' with id '{}'.", controllerName, acquirerID);

		try {
			MECApplication.get().getMEController(controllerName).finalizeExperimentSeries(acquirerID);
			return new TransferPackage(true);
		} catch (Exception e) {
			e.printStackTrace();
			return new TransferPackage(false);
		}
	}

	@GET
	@Path("availableController")
	@Produces(MediaType.APPLICATION_XML)
	public TransferPackage getControllerList() {
		return new TransferPackage(MECApplication.get().getControllerList());
	}

	@GET
	@Path("{controllerName}/meDefinition")
	@Produces(MediaType.APPLICATION_XML)
	public TransferPackage getMEDefinition(@PathParam("controllerName") String controllerName) throws RemoteException {
		LOGGER.debug("Get meDefinition of controller '{}'.", controllerName);

		return new TransferPackage(MECApplication.get().getMEController(controllerName).getMEDefinition());
	}

	@GET
	@Path("{controllerName}/queueLength")
	@Produces(MediaType.APPLICATION_XML)
	public TransferPackage getQueueLength(@PathParam("controllerName") String controllerName) throws RemoteException {
		LOGGER.debug("Get queueLength of controller '{}'.", controllerName);

		int length = MECApplication.get().getMEController(controllerName).getQueueLength();
		return new TransferPackage(length);
	}

	@GET
	@Path("{controllerName}/waiting/{acquirerID}")
	@Produces(MediaType.APPLICATION_XML)
	public TransferPackage waiting(@PathParam("controllerName") final String controllerName,
			@PathParam("acquirerID") final String acquirerID) {
		LOGGER.debug("Get result of acquirerID '{}'.", acquirerID);

		TransferPackage result = MECExecutor.getResult(acquirerID);
		if (result == null) {
			throw new RuntimeException();
		}

		if (StatusProvider.getProvider(controllerName).hasNext()) {
			result.setAttachmentB(StatusProvider.getProvider(controllerName).getNotFetched());
		} else {
			result.setAttachmentB(null);
		}

		if (result.getA(State.class) != State.WAITING) {
			MECExecutor.cleanResult(acquirerID);
		}

		return result;
	}

	@GET
	@Path("{controllerName}/state")
	@Produces(MediaType.APPLICATION_XML)
	public TransferPackage getState(@PathParam("controllerName") String controllerName) throws RemoteException {
		LOGGER.debug("Get state of controller '{}'.", controllerName);

		MEControllerState state = MECApplication.get().getMEController(controllerName).getState();
		TransferPackage tPackage = new TransferPackage(state);
		return tPackage;
	}

	@PUT
	@Path("{controllerName}/initialize/{acquirerID}")
	@Consumes({ "application/xml" })
	@Produces(MediaType.APPLICATION_XML)
	public TransferPackage initialize(@PathParam("controllerName") String controllerName,
			@PathParam("acquirerID") String acquirerID, TransferPackage transferPackage) throws RemoteException {
		LOGGER.debug("Initialize controller '{}' with id '{}'.", controllerName, acquirerID);

		try {
			@SuppressWarnings("unchecked")
			ParameterCollection<ParameterValue<?>> paraCollection = transferPackage.getA(ParameterCollection.class);

			MECApplication.get().getMEController(controllerName).initialize(acquirerID, paraCollection);

			return new TransferPackage(true);
		} catch (Exception e) {
			e.printStackTrace();
			return new TransferPackage(false);
		}
	}

	@PUT
	@Path("{controllerName}/prepareExperimentSeries/{acquirerID}")
	@Consumes({ "application/xml" })
	@Produces(MediaType.APPLICATION_XML)
	public TransferPackage prepareExperimentSeries(@PathParam("controllerName") String controllerName,
			@PathParam("acquirerID") String acquirerID, TransferPackage data) throws RemoteException {
		LOGGER.debug("Prepare ExperimentSeries on controller '{}' with id '{}'.", controllerName, acquirerID);

		try {
			@SuppressWarnings("unchecked")
			// ParameterCollection<ParameterValue<?>> paraCollection =
			// data.getAttachment(ParameterCollection.class);
			ParameterCollection<ParameterValue<?>> paraCollection = data.getA(ParameterCollection.class);
			@SuppressWarnings("unchecked")
			// Set<ExperimentTerminationCondition> terminationConditions =
			// data.getSecondAttachment(Set.class);
			Set<ExperimentTerminationCondition> terminationConditions = data.getB(ParameterCollection.class);

			MECApplication.get().getMEController(controllerName)
					.prepareExperimentSeries(acquirerID, paraCollection, terminationConditions);
			return new TransferPackage(true);
		} catch (Exception e) {
			e.printStackTrace();
			return new TransferPackage(false);
		}
	}

	@GET
	@Path("{controllerName}/release/{acquirerID}")
	public Response releaseController(@PathParam("controllerName") String controllerName,
			@PathParam("acquirerID") String acquirerID) {
		LOGGER.debug("Release controller '{}' with id '{}'.", controllerName, acquirerID);

		MECExecutor.release(controllerName, acquirerID);

		return Response.noContent().build();
	}

	@PUT
	@Path("{controllerName}/runExperiment/{acquirerID}")
	@Consumes({ "application/xml" })
	public Response runExperiment(@PathParam("controllerName") String controllerName,
			@PathParam("acquirerID") String acquirerID, TransferPackage data) throws RemoteException {
		LOGGER.debug("Run Experiment on controller '{}' with id '{}'.", controllerName, acquirerID);

		@SuppressWarnings("unchecked")
		ParameterCollection<ParameterValue<?>> paraCollection = data.getA(ParameterCollection.class);

		MECExecutor.runExperiment(controllerName, acquirerID, paraCollection);

		return Response.noContent().build();
	}

	@GET
	@Path("{controllerName}/test")
	@Produces(MediaType.APPLICATION_XML)
	public TransferPackage test(@PathParam("controllerName") String controllerName) throws RemoteException {
		LOGGER.debug("test");

		return new TransferPackage("Test 1", 55, new String[] { "345", "asd" });
	}
}

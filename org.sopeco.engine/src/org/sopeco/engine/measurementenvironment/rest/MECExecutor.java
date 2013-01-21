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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.app.MECApplication;
import org.sopeco.engine.measurementenvironment.rest.TransferPackage.State;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.util.ParameterCollection;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class MECExecutor {

	private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

	private static final Logger LOGGER = LoggerFactory.getLogger(MECExecutor.class);

	private static Map<String, TransferPackage> resultMap = new HashMap<String, TransferPackage>();

	/**
	 * 
	 * @param controllerName
	 * @param acquirerID
	 * @param timeout
	 */
	public static void acquire(final String controllerName, final String acquirerID, final long timeout) {
		EXECUTOR.execute(new Runnable() {
			@Override
			public void run() {

				boolean result;
				resultMap.put(acquirerID, new TransferPackage());
				resultMap.get(acquirerID).setAttachmentA(State.WAITING);

				try {
					result = MECApplication.get().getMEController(controllerName)
							.acquireMEController(acquirerID, timeout);
				} catch (RemoteException e) {
					result = false;
					e.printStackTrace();
				}

				if (result) {
					LOGGER.debug("Acquiring controller '{}' with id '{}' was successful.", controllerName, acquirerID);
				} else {
					LOGGER.debug("Acquiring controller '{}' with id '{}' failed.", controllerName, acquirerID);
				}

				resultMap.get(acquirerID).setAttachmentC(result);
				resultMap.get(acquirerID).setAttachmentA(State.ACQUIRED);
			}
		});
	}

	/**
	 * 
	 * @param key
	 */
	public static void cleanResult(String key) {
		resultMap.remove(key);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static TransferPackage getResult(String key) {
		if (!resultMap.containsKey(key)) {
			return null;
		}
		return resultMap.get(key);
	}

	/**
	 * 
	 * @param controllerName
	 * @param acquirerID
	 */
	public static void release(final String controllerName, final String acquirerID) {
		EXECUTOR.execute(new Runnable() {
			@Override
			public void run() {

				boolean result;
				resultMap.put(acquirerID, new TransferPackage());
				resultMap.get(acquirerID).setAttachmentA(State.WAITING);

				try {

					MECApplication.get().getMEController(controllerName).releaseMEController(acquirerID);
					result = true;
					LOGGER.debug("Releasing controller '{}' with id '{}' was successful.", controllerName, acquirerID);

				} catch (Exception e) {

					result = false;
					LOGGER.debug("Releasing controller '{}' with id '{}' failed.", controllerName, acquirerID);
					e.printStackTrace();

				}

				resultMap.get(acquirerID).setAttachmentC(result);
				resultMap.get(acquirerID).setAttachmentA(State.RELEASED);
			}
		});
	}

	/**
	 * 
	 * @param controllerName
	 * @param acquirerID
	 * @param inputPVs
	 */
	public static void runExperiment(final String controllerName, final String acquirerID,
			final ParameterCollection<ParameterValue<?>> inputPVs) {
		EXECUTOR.execute(new Runnable() {
			@Override
			public void run() {

				Collection<ParameterValueList<?>> result;
				resultMap.put(acquirerID, new TransferPackage());
				resultMap.get(acquirerID).setAttachmentA(State.WAITING);

				try {

					result = MECApplication.get().getMEController(controllerName).runExperiment(acquirerID, inputPVs);

					LOGGER.debug("RunExperiment on controller '{}' with id '{}' was successful.", controllerName,
							acquirerID);

				} catch (Exception e) {

					result = null;
					LOGGER.error("RunExperiment on controller '{}' with id '{}' failed.", controllerName, acquirerID);
					e.printStackTrace();

					resultMap.get(acquirerID).setAttachmentC(e);
				}

				if (result != null) {
					resultMap.get(acquirerID).setAttachmentC(result);
				}
				resultMap.get(acquirerID).setAttachmentA(State.RUNFINISHED);
			}
		});
	}

	/**
	 * Constructor.
	 */
	private MECExecutor() {
	}
}

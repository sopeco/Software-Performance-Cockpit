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
package org.sopeco.engine.measurementenvironment.connector;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.status.ErrorInfo;
import org.sopeco.engine.status.EventType;
import org.sopeco.engine.status.StatusBroker;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class MEConnectorFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(MEConnectorFactory.class);

	private MEConnectorFactory() {
	}

	public static IMeasurementEnvironmentController connectTo(URI uri) {
		if (uri.getScheme().toLowerCase().equals("rmi")) {
			StatusBroker.getManager(uri.toString()).newStatus(EventType.CONNECT_TO_MEC);
			LOGGER.debug("Creating RMI MEConnector.");
			try {
				return new RmiMEConnector().connectToMEController(uri);
			} catch (RuntimeException e) {
				ErrorInfo info = new ErrorInfo();
				info.setThrowable(e);
				StatusBroker.getManager(uri.toString()).newStatus(EventType.ERROR, e);
				throw e;
			}
		} else if (uri.getScheme().toLowerCase().equals("http")) {
			StatusBroker.getManager(uri.toString()).newStatus(EventType.CONNECT_TO_MEC);
			LOGGER.debug("Creating REST MEConnector.");
			try {
				return new RestMEConnector().connectToMEController(uri);
			} catch (RuntimeException e) {
				ErrorInfo info = new ErrorInfo();
				info.setThrowable(e);
				StatusBroker.getManager(uri.toString()).newStatus(EventType.ERROR, e);
				throw e;
			}
		}  else if (uri.getScheme().toLowerCase().equals("socket")) {
			StatusBroker.getManager(uri.toString()).newStatus(EventType.CONNECT_TO_MEC);
			LOGGER.debug("Getting Socket MEConnector.");
			try {
				return new SocketMEConnector().connectToMEController(uri);
			} catch (RuntimeException e) {
				ErrorInfo info = new ErrorInfo();
				info.setThrowable(e);
				StatusBroker.getManager(uri.toString()).newStatus(EventType.ERROR, e);
				throw e;
			}
		} else {
			IllegalStateException exception = new IllegalStateException("Only 'rmi://' and 'http://' are supported.");

			ErrorInfo info = new ErrorInfo();
			info.setThrowable(exception);

			StatusBroker.getManager(uri.toString()).newStatus(EventType.ERROR, exception);
			throw exception;
		}

	}
}

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
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketAppWrapper extends ConnectionResource {
	private static final String GET_IDENTIFIER = "getIdentifier";

	private static final String AVAILABLE_CONTROLLER = "availableController";

	private static final Logger LOGGER = LoggerFactory.getLogger(SocketAppWrapper.class);
	
	private String[] controller;
	private Map<String, SocketMECWrapper> wrapperMap;

	public SocketAppWrapper(Socket pSocket) {
		wrapperMap = new HashMap<String, SocketMECWrapper>();

		try {
			bind(pSocket);

			controller = fetchAvailableMEController();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String[] fetchAvailableMEController() {
		return callMethod(AVAILABLE_CONTROLLER);
	}
	
	public String getIdentifier(){
		return callMethod(GET_IDENTIFIER);
	}

	public String[] getAvailableController() {
		if (controller == null) {
			controller = fetchAvailableMEController();
		}
		return controller;
	}

	public SocketMECWrapper getMECWrapper(String controllerName) {
		if (Arrays.asList(controller).contains(controllerName)) {
			if (!wrapperMap.containsKey(controllerName)) {
				wrapperMap.put(controllerName, new SocketMECWrapper(this, controllerName));
			}
			return wrapperMap.get(controllerName);
		}
		LOGGER.warn("No MEController with name {} available.", controllerName);
		return null;
	}
}

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
package org.sopeco.engine.measurementenvironment.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.app.MECApplication;
import org.sopeco.engine.status.EventType;
import org.sopeco.engine.status.IStatusInfo;
import org.sopeco.engine.status.ProgressInfo;
import org.sopeco.engine.status.StatusMessage;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class StatusProvider {

	private static Map<String, StatusProvider> providerMap;

	public static StatusProvider getProvider(IMeasurementEnvironmentController controller) {
		String controllerName = MECApplication.nameOfMEC(controller);
		if (controllerName == null) {
			return null;
		}
		return getProvider(controllerName);
	}

	public static StatusProvider getProvider(String controllerName) {
		if (providerMap == null) {
			providerMap = new HashMap<String, StatusProvider>();
		}
		if (!providerMap.containsKey(controllerName)) {
			providerMap.put(controllerName, new StatusProvider());
		}
		return providerMap.get(controllerName);
	}

	private List<StatusMessage> statusList;
	private int lastFetchedStatus = -1;

	private StatusProvider() {
		statusList = new ArrayList<StatusMessage>();
	}

	public void sendInformation(String information) {
		nextStatus(EventType.INFORMATION, information, null);
	}

	public void sendProgressInfo(String description, ProgressInfo progressInfo) {
		nextStatus(EventType.INFORMATION, description, progressInfo);
	}

	// public void nextStatus(String description) {
	// nextStatus(description, null);
	// }

	private void nextStatus(EventType eventType, String description, IStatusInfo statusInfo) {
		StatusMessage status = new StatusMessage();
		status.setDescription(description);
		status.setStatusInfo(statusInfo);
		status.setEventType(eventType);

		statusList.add(status);
	}

	public synchronized boolean hasNext() {
		return lastFetchedStatus + 1 < statusList.size();
	}

	public synchronized StatusMessage next() {
		if (lastFetchedStatus + 1 < statusList.size()) {
			lastFetchedStatus++;
			return statusList.get(lastFetchedStatus);
		}
		return null;
	}

	public synchronized List<StatusMessage> getNotFetched() {
		List<StatusMessage> list = new ArrayList<StatusMessage>();
		while (hasNext()) {
			list.add(next());
		}
		return list;
	}

	public void remove() {
		providerMap.remove(this);
	}
}

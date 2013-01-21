package org.sopeco.engine.measurementenvironment.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.app.MECApplication;
import org.sopeco.engine.status.EventType;
import org.sopeco.engine.status.IStatusInfo;
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

	public void nextStatus(String description) {
		nextStatus(description, null);
	}

	public void nextStatus(String description, IStatusInfo statusInfo) {
		StatusMessage status = new StatusMessage();
		status.setDescription(description);
		status.setStatusInfo(statusInfo);
		status.setEventType(EventType.EXPERIMENT_EXECUTION);

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

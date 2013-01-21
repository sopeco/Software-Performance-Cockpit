package org.sopeco.engine.status;

import java.util.HashMap;
import java.util.Map;

import org.sopeco.config.Configuration;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class StatusBroker {

	private static Map<String, StatusManager> managerMap;

	private StatusBroker() {
	}

	public static StatusManager getManager(String controllerURL) {
		if (managerMap == null) {
			managerMap = new HashMap<String, StatusManager>();
		}
		if (!managerMap.containsKey(controllerURL)) {
			managerMap.put(controllerURL, new StatusManager(controllerURL));
		}
		return managerMap.get(controllerURL);
	}

	public static StatusManager getManagerViaSessionID(String id) {
		String controller = Configuration.getSessionSingleton(id).getMeasurementControllerURIAsStr();
		return getManager(controller);
	}
}

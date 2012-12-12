package org.sopeco.engine.status;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class StatusSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatusSender.class);
	private static final String REST_NAME = "statusService";
	private InitializePackage initializePackage;
	private long hostLastReachable = 0;
	private static final long HOST_TIMEOUT_LIMIT = 2 * 60 * 1000;

	public StatusSender(InitializePackage pInitializePackage) {
		initializePackage = pInitializePackage;
	}

	private boolean sendMessage() {
		if (initializePackage == null) {
			LOGGER.debug("Skip sending status message. No initialize package.");
			return false;
		} else if (System.currentTimeMillis() - hostLastReachable < HOST_TIMEOUT_LIMIT) {
			LOGGER.debug("Host has not answered. Next attempt earliest in {} milliseconds.", HOST_TIMEOUT_LIMIT
					- (System.currentTimeMillis() - hostLastReachable));
			return false;
		}
		return true;
	}

	public void next(EventType eventType) {
		if (!sendMessage()) {
			return;
		}
		StatusMessage statusMessage = new StatusMessage();
		statusMessage.setEventType(eventType);
		statusMessage.setStatusInfo(null);
		statusMessage.setToken(initializePackage.getToken());
		next(statusMessage);
	}

	public void next(EventType eventType, IStatusInfo statusInfo) {
		if (!sendMessage()) {
			return;
		}
		StatusMessage statusMessage = new StatusMessage();
		statusMessage.setEventType(eventType);
		statusMessage.setStatusInfo(statusInfo);
		statusMessage.setToken(initializePackage.getToken());
		next(statusMessage);
	}

	public void next(EventType eventType, Throwable throwable) {
		if (!sendMessage()) {
			return;
		}
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setThrowable(throwable);

		StatusMessage statusMessage = new StatusMessage();
		statusMessage.setEventType(eventType);
		statusMessage.setStatusInfo(errorInfo);
		statusMessage.setToken(initializePackage.getToken());
		next(statusMessage);
	}

	public void next(StatusMessage statusMessage) {
		if (!sendMessage()) {
			return;
		}
		try {
			StatusMessageWrapper wrapper = new StatusMessageWrapper();
			wrapper.setStatusMessageObject(statusMessage);

			Client client = Client.create();
			WebResource restService = client.resource(initializePackage.getHostUrl());
			restService = restService.path(REST_NAME).path(initializePackage.getToken());
			restService.type(MediaType.APPLICATION_XML).put(wrapper);
		} catch (Exception e) {
			hostLastReachable = System.currentTimeMillis();
			LOGGER.warn("Faild sending StatusMessage (" + statusMessage.getEventType() + ").");
		}
	}
}

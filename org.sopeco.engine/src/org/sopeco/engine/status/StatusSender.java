package org.sopeco.engine.status;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * This class is used by the MEController to send the StatusMessages to SoPeCo.
 * 
 * @author Marius Oehler
 * 
 */
public final class StatusSender {

	private static StatusSender sender;

	/**
	 * Returns the current StatusSender.<br>
	 * Note: Before it can be used, it have to be initialized with an
	 * InitializePackage.
	 * 
	 * @return the sender
	 */
	public static StatusSender get() {
		if (sender == null) {
			sender = new StatusSender();
		}
		return sender;
	}

	/**
	 * Returns a temporary StatusSender with the given InitializePackage.
	 * 
	 * @param pInitializePackage
	 * @return
	 */
	public static StatusSender get(InitializePackage pInitializePackage) {
		StatusSender tempSender = new StatusSender();
		tempSender.init(pInitializePackage);
		return tempSender;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(StatusSender.class);
	private static final String REST_NAME = "statusService";
	private InitializePackage initializePackage = null;
	private long hostLastReachable = 0;
	private static final long HOST_TIMEOUT_LIMIT = 2 * 60 * 1000;

	/**
	 * Constructor.
	 */
	private StatusSender() {
	}

	/**
	 * Needs a InitializePackage object, which contains the URL of SoPeCo, where
	 * the StatusMessages will be send to.
	 * 
	 * @param pInitializePackage
	 */
	public void init(InitializePackage pInitializePackage) {
		initializePackage = pInitializePackage;
	}

	/**
	 * 
	 * @return
	 */
	private boolean ableToSendMessages() {
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
		if (!ableToSendMessages()) {
			return;
		}
		StatusMessage statusMessage = new StatusMessage();
		statusMessage.setEventType(eventType);
		statusMessage.setStatusInfo(null);
		statusMessage.setToken(initializePackage.getToken());
		next(statusMessage);
	}

	public void next(EventType eventType, IStatusInfo statusInfo) {
		if (!ableToSendMessages()) {
			return;
		}
		StatusMessage statusMessage = new StatusMessage();
		statusMessage.setEventType(eventType);
		statusMessage.setStatusInfo(statusInfo);
		statusMessage.setToken(initializePackage.getToken());
		next(statusMessage);
	}

	public void next(EventType eventType, Throwable throwable) {
		if (!ableToSendMessages()) {
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
		if (!ableToSendMessages()) {
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

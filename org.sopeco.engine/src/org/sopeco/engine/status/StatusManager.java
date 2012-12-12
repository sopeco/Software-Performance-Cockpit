package org.sopeco.engine.status;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class StatusManager {
	private String controllerUrl;
	private int lastFetchedStatus = -1;
	private long lastPackageReceivedTime;
	private List<StatusMessage> statusMessages;

	/**
	 * Constructor.
	 * 
	 * @param pControllerUrl
	 *            URL of the controller.
	 */
	public StatusManager(String pControllerUrl) {
		controllerUrl = pControllerUrl;
		statusMessages = new ArrayList<StatusMessage>();
		lastPackageReceivedTime = System.currentTimeMillis();
	}

	/**
	 * Returns the controllerUrl.
	 */
	public String getControllerUrl() {
		return controllerUrl;
	}

	/**
	 * Returns the latest {@link StatusMessage}.
	 * 
	 * @return StatusMessage
	 */
	public StatusMessage getCurrentStatus() {
		if (statusMessages.isEmpty()) {
			return null;
		}
		return statusMessages.get(statusMessages.size() - 1);
	}

	/**
	 * Returns the timestamp when the manager received the last
	 * {@link StatusMessage}.
	 * 
	 * @return
	 */
	public long getLastPackageReceivedTime() {
		return lastPackageReceivedTime;
	}

	/**
	 * Returns whether the manager has StatusMessages, which are not fetched by
	 * the {@link #next()} method.
	 * 
	 * @return
	 */
	public synchronized boolean hasNext() {
		return lastFetchedStatus + 1 < statusMessages.size();
	}

	/**
	 * Adds the given {@link StatusMessage} to the current statusManager.
	 * 
	 * @param statusMessage
	 */
	public void newStatus(StatusMessage statusMessage) {
		System.out.println("new message of '" + controllerUrl + "': " + statusMessage.getEventType());
		lastPackageReceivedTime = System.currentTimeMillis();
		statusMessages.add(statusMessage);
	}

	public void newStatus(EventType eventType) {
		StatusMessage message = new StatusMessage();
		message.setEventType(eventType);
		newStatus(message);
	}

	/**
	 * Returns the oldest, not fetched {@link StatusMessage}.
	 * 
	 * @return
	 */
	public synchronized StatusMessage next() {
		if (lastFetchedStatus + 1 < statusMessages.size()) {
			lastFetchedStatus++;
			return statusMessages.get(lastFetchedStatus);
		}
		return null;
	}

}

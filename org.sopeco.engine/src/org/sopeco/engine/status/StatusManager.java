package org.sopeco.engine.status;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class StatusManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatusManager.class);

	private int lastFetchedStatus = -1;
	private List<StatusMessage> statusList;

	private String controllerURL;

	private List<IStatusListener> statusListenerList;

	/**
	 * Constructor.
	 */
	public StatusManager(String pControllerURL) {
		statusList = new ArrayList<StatusMessage>();
		controllerURL = pControllerURL;
		statusListenerList = new ArrayList<IStatusListener>();
	}

	public void addStatusListener(IStatusListener listener) {
		statusListenerList.add(listener);
	}

	public void removeStatusListener(IStatusListener listener) {
		statusListenerList.remove(listener);
	}

	/**
	 * Returns the timestamp when the manager received the last
	 * {@link StatusMessage}.
	 * 
	 * @return
	 */
	public long getLastPackageReceivedTime() {
		StatusMessage message = getLatestStatus();
		if (message == null) {
			return -1;
		} else {
			return message.getTimestamp();
		}
	}

	/**
	 * Returns the latest {@link StatusMessage}.
	 * 
	 * @return StatusMessage
	 */
	public StatusMessage getLatestStatus() {
		if (statusList.isEmpty()) {
			return null;
		}
		return statusList.get(statusList.size() - 1);
	}

	/**
	 * Returns whether the manager has StatusMessages, which are not fetched by
	 * the {@link #next()} method.
	 * 
	 * @return
	 */
	public synchronized boolean hasNext() {
		return lastFetchedStatus + 1 < statusList.size();
	}

	public void newStatus(EventType eventType, Throwable throwable) {
		ErrorInfo info = new ErrorInfo();
		info.setThrowable(throwable);

		StatusMessage message = new StatusMessage();
		message.setEventType(eventType);
		message.setStatusInfo(info);

		newStatus(message);
	}

	public void newStatus(EventType eventType) {
		StatusMessage message = new StatusMessage();
		message.setEventType(eventType);
		
		newStatus(message);
	}

	/**
	 * Adds the given {@link StatusMessage} to the current statusManager.
	 * 
	 * @param statusMessage
	 */
	public void newStatus(StatusMessage statusMessage) {
		LOGGER.debug("New Status on '{}': {}", controllerURL, statusMessage.getEventType());
		statusList.add(statusMessage);

		for (IStatusListener listener : new ArrayList<IStatusListener>(statusListenerList)) {
			listener.onNewStatus(statusMessage);
		}
	}

	/**
	 * Returns the oldest, not fetched {@link StatusMessage}.
	 * 
	 * @return
	 */
	public synchronized StatusMessage next() {
		if (lastFetchedStatus + 1 < statusList.size()) {
			lastFetchedStatus++;
			return statusList.get(lastFetchedStatus);
		}
		return null;
	}
}

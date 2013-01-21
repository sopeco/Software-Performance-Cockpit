package org.sopeco.engine.status;

import java.io.Serializable;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class StatusMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long timestamp;
	private EventType eventType;

	private String description;
	private IStatusInfo statusInfo;

	public StatusMessage() {
		timestamp = System.currentTimeMillis();
	}

	/**
	 * @return the eventType
	 */
	public EventType getEventType() {
		return eventType;
	}

	/**
	 * @return the statusInfo
	 */
	public IStatusInfo getStatusInfo() {
		return statusInfo;
	}



	public String getDescription() {
		return description;
	}

	public void setDescription(String pDescription) {
		this.description = pDescription;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param pEventType
	 *            the eventType to set
	 */
	public void setEventType(EventType pEventType) {
		eventType = pEventType;
	}

	/**
	 * @param pStatusInfo
	 *            the statusInfo to set
	 */
	public void setStatusInfo(IStatusInfo pStatusInfo) {
		statusInfo = pStatusInfo;
	}

	/**
	 * @param pTimestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(long pTimestamp) {
		timestamp = pTimestamp;
	}

}

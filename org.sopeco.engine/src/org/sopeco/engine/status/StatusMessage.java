package org.sopeco.engine.status;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Container class. A object of this class contains all necessary information
 * about the controller status.
 * 
 * @author Marius Oehler
 * 
 */
@XmlRootElement
public class StatusMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private EventType eventType;
	private IStatusInfo statusInfo;
	private String token;

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

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
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
	 * @param pToken
	 *            the token to set
	 */
	public void setToken(String pToken) {
		token = pToken;
	}

}

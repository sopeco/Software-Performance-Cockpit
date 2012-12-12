package org.sopeco.engine.status;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
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

	public EventType getEventType() {
		return eventType;
	}

	public IStatusInfo getStatusInfo() {
		return statusInfo;
	}

	public String getToken() {
		return token;
	}

	public void setEventType(EventType pEventType) {
		eventType = pEventType;
	}

	public void setStatusInfo(IStatusInfo pStatusInfo) {
		statusInfo = pStatusInfo;
	}

	public void setToken(String pToken) {
		token = pToken;
	}

}

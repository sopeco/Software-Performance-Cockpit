package org.sopeco.util.session;

import java.io.Serializable;

/**
 * A session-aware object has always a session id.
 * 
 * @author Alexander Wert
 * 
 */
public abstract class SessionAwareObject implements ISessionAwareObject, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7480363911549705845L;
	private String id;

	/**
	 * Super-Constructor for all session-aware objects.
	 * @param sessionId Session id to be set.
	 */
	public SessionAwareObject(String sessionId) {
		id = sessionId;
	}

	@Override
	public String getSessionId() {
		return id;
	}

}

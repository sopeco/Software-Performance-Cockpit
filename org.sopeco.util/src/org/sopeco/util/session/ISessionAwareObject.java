package org.sopeco.util.session;

/**
 * A session-aware object has always a session id.
 * 
 * @author Alexander Wert
 * 
 */
public interface ISessionAwareObject {
	/**
	 * Returns the session id of this object.
	 * 
	 * @return Returns the session id of this object.
	 */
	String getSessionId();
}

package org.sopeco.engine.status;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface IStatusListener {
	void onNewStatus(StatusMessage statusMessage);
}

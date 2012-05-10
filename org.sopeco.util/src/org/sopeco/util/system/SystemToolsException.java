/**
 * Project: SoPeCo
 * 
 * (c) 2011 Roozbeh Farahbod, roozbeh.farahbod@sap.com
 * 
 */
package org.sopeco.util.system;

import org.slf4j.Logger;


/**
 * Represents OS-related exceptions. 
 * 
 * @author Roozbeh Farahbod
 *
 */
public class SystemToolsException extends Exception {

	public SystemToolsException(String msg, Logger logger) {
		super(msg);
		log(logger, msg);
	}

	public SystemToolsException(Throwable throwable) {
		super(throwable);
	}

	public SystemToolsException(String msg, Throwable throwable, Logger logger) {
		super(msg, throwable);
		log(logger, msg);
	}
	
	private void log(Logger logger, String msg) {
		if (logger != null)
			logger.warn(msg);
	}

}

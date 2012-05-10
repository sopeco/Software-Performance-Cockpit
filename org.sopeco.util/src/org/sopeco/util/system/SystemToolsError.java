/**
 * Project: SoPeCo
 * 
 * (c) 2011 Roozbeh Farahbod, roozbeh.farahbod@sap.com
 * 
 */
package org.sopeco.util.system;

import org.slf4j.Logger;


/**
 * Represents OS-related errors. 
 * 
 * @author Roozbeh Farahbod
 *
 */
public class SystemToolsError extends Error {

	public SystemToolsError(String msg, Logger logger) {
		super(msg);
		log(logger, msg);
	}

	public SystemToolsError(Throwable throwable) {
		super(throwable);
	}

	public SystemToolsError(String msg, Throwable throwable, Logger logger) {
		super(msg, throwable);
		log(logger, msg);
	}
	
	private void log(Logger logger, String msg) {
		if (logger != null)
			logger.warn(msg);
	}

}

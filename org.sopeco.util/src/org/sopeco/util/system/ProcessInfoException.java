/**
 * Project: SoPeCo
 * 
 * (c) 2011 Roozbeh Farahbod, roozbeh.farahbod@sap.com
 * 
 */
package org.sopeco.util.system;

import org.slf4j.Logger;

/**
 * Represents exceptions regarding process information retrieval. 
 * 
 * @author Roozbeh Farahbod
 *
 */
public class ProcessInfoException extends SystemToolsException {

	public ProcessInfoException(String msg, Logger logger) {
		super(msg, logger);
	}

	public ProcessInfoException(Throwable throwable) {
		super(throwable);
	}

	public ProcessInfoException(String msg, Throwable throwable, Logger logger) {
		super(msg, throwable, logger);
	}
	
}

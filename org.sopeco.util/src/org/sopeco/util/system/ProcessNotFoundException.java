/**
 * Project: SoPeCo
 * 
 * (c) 2011 Roozbeh Farahbod, roozbeh.farahbod@sap.com
 * 
 */
package org.sopeco.util.system;

import org.slf4j.Logger;


/**
 * Thrown if a system process that is expected to exist does not exist. 
 * 
 * @author Roozbeh Farahbod
 *
 */
public class ProcessNotFoundException extends ProcessInfoException {

	private static final long serialVersionUID = 1L;

	public ProcessNotFoundException(String msg, Logger logger) {
		super(msg, logger);
	}

	public ProcessNotFoundException(Throwable throwable) {
		super(throwable);
	}

	public ProcessNotFoundException(String msg, Throwable throwable,
			Logger logger) {
		super(msg, throwable, logger);
	}

}

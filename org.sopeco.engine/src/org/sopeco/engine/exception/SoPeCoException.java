/**
 * 
 */
package org.sopeco.engine.exception;

/**
 * The framework exception is thought as a generic exception
 * for all framework related stuff which might be displayed to 
 * the user.
 * 
 * @author Roozbeh Farahbod
 */

public class SoPeCoException extends Exception {
	private static final long serialVersionUID = 1L;

	public SoPeCoException() {
		super();
	}

	public SoPeCoException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public SoPeCoException(String msg) {
		super(msg);
	}

	
	public SoPeCoException(Throwable cause) {
		super(cause);
	}

	
}

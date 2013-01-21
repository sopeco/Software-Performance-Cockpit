/**
 * 
 */
package org.sopeco.engine.exception;

/**
 * The framework exception is thought as a generic exception for all framework
 * related stuff which might be displayed to the user.
 * 
 * @author Roozbeh Farahbod
 */

public class SoPeCoException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Standard Constructor.
	 */
	public SoPeCoException() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param msg
	 *            error message
	 * @param cause
	 *            root cause of the error
	 */
	public SoPeCoException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructor.
	 * 
	 * @param msg
	 *            error message
	 */
	public SoPeCoException(String msg) {
		super(msg);
	}

	/**
	 * Constructor.
	 * 
	 * @param cause
	 *            root cause of the error
	 */
	public SoPeCoException(Throwable cause) {
		super(cause);
	}

}

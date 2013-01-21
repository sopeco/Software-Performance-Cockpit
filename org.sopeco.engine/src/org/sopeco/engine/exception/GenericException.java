/**
 * 
 */
package org.sopeco.engine.exception;

/**
 * This is a placeholder for all the exceptions that we don't know how to handle
 * yet!
 * 
 * TODO Jens is going to prepare a talk on this! ;)
 * 
 * @author Roozbeh Farahbod
 * 
 */
public class GenericException extends RuntimeException {

	/**
	 * Standard Constructor.
	 */
	public GenericException() {
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
	public GenericException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructor.
	 * 
	 * @param msg
	 *            error message
	 */
	public GenericException(String msg) {
		super(msg);
	}

	/**
	 * Constructor.
	 * 
	 * @param cause
	 *            root cause of the error
	 */
	public GenericException(Throwable cause) {
		super(cause);
	}

}

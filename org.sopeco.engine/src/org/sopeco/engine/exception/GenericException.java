/**
 * 
 */
package org.sopeco.engine.exception;

/**
 * This is a placeholder for all the exceptions that we don't know how to handle yet! 
 * 
 * TODO Jens is going to prepare a talk on this! ;)
 * 
 * @author Roozbeh Farahbod
 *
 */
public class GenericException extends RuntimeException {

	public GenericException() {
		super();
	}

	public GenericException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public GenericException(String msg) {
		super(msg);
	}

	public GenericException(Throwable cause) {
		super(cause);
	}

}

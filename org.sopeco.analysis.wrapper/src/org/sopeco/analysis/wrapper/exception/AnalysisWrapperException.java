package org.sopeco.analysis.wrapper.exception;

/**
 * This exception is thrown if communication with the analysis implementation
 * fails.
 * 
 * @author Alexander Wert
 * 
 */
public class AnalysisWrapperException extends Exception {

	/**
	 * Standard Constructor.
	 */
	public AnalysisWrapperException() {

	}

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            error message
	 */
	public AnalysisWrapperException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * 
	 * @param cause
	 *            the cause of the created exception
	 */
	public AnalysisWrapperException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            error message
	 * @param cause
	 *            the cause of the created exception
	 */
	public AnalysisWrapperException(String message, Throwable cause) {
		super(message, cause);
	}
}

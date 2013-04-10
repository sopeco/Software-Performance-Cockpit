package org.sopeco.engine.exception;

public class ExperimentAbortException extends RuntimeException {
	/**
	 * Standard Constructor.
	 */
	public ExperimentAbortException() {
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
	public ExperimentAbortException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructor.
	 * 
	 * @param msg
	 *            error message
	 */
	public ExperimentAbortException(String msg) {
		super(msg);
	}

	/**
	 * Constructor.
	 * 
	 * @param cause
	 *            root cause of the error
	 */
	public ExperimentAbortException(Throwable cause) {
		super(cause);
	}
}

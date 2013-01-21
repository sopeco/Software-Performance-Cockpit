/**
 * 
 */
package org.sopeco.engine.measurementenvironment;

/**
 * Thrown by the measurement environment controller (see
 * {@link IMeasurementEnvironmentController}) if an experiment fails.
 * 
 * @author Roozbeh Farahbod
 * 
 */
public class ExperimentFailedException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public ExperimentFailedException() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param msg
	 *            error message
	 * @param cause
	 *            error cause
	 */
	public ExperimentFailedException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructor.
	 * 
	 * @param msg
	 *            error message
	 */
	public ExperimentFailedException(String msg) {
		super(msg);
	}

	/**
	 * Constructor.
	 * 
	 * @param cause
	 *            error cause
	 */
	public ExperimentFailedException(Throwable cause) {
		super(cause);
	}

}

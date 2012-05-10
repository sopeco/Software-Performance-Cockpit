/**
 * 
 */
package org.sopeco.engine.measurementenvironment;

/**
 * Thrown by the measurement environment controller (see {@link IMeasurementEnvironmentController}) if an experiment fails.
 *  
 * @author Roozbeh Farahbod
 *
 */
public class ExperimentFailedException extends Exception {

	private static final long serialVersionUID = 1L;

	public ExperimentFailedException() {
		super();
	}

	public ExperimentFailedException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ExperimentFailedException(String msg) {
		super(msg);
	}

	public ExperimentFailedException(Throwable cause) {
		super(cause);
	}

	
}

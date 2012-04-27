/**
 * 
 */
package org.sopeco.config.exception;

/**
 * Thrown if there is a configuration problem in SoPeCo.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class ConfigurationException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConfigurationException() {
	}

	public ConfigurationException(String msg) {
		super(msg);
	}

	public ConfigurationException(Throwable throwable) {
		super(throwable);
	}

	public ConfigurationException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

}

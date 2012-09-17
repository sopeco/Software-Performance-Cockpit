package org.sopeco.persistence.exceptions;

public class WrongCredentialsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 784508984415513485L;
	
	public WrongCredentialsException(String message) {
		super(message);
	}

}

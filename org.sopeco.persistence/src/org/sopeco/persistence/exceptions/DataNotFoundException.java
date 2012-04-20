/**
 * 
 */
package org.sopeco.persistence.exceptions;


/**
 * This exception is thrown when a database lookup has no result.
 * 
 * @author Dennis Westermann
 * 
 */
public class DataNotFoundException extends Exception {

	private static final long serialVersionUID = -683407607437894702L;

	private String message;

	public DataNotFoundException() {
		super();
		this.message = "The specified data was not found in database.";
	}

	public DataNotFoundException(String message) {
		super(message);
		this.message = message;
	}
	
	public DataNotFoundException(String message, Exception e){
		super(message, e);
	}

	@Override
	public String getMessage() {
		return message;
	}

}

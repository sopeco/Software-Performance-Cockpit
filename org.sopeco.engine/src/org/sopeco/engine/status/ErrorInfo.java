package org.sopeco.engine.status;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Marius Oehler
 * 
 */
@XmlRootElement
public class ErrorInfo implements IStatusInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private Throwable throwable;

	public ErrorInfo() {
	}

	public String getMessage() {
		return message;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setMessage(String pMessage) {
		message = pMessage;
	}

	public void setThrowable(Throwable pThrowable) {
		throwable = pThrowable;
	}

}

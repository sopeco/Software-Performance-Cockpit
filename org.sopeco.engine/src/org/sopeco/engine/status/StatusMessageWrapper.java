package org.sopeco.engine.status;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Marius Oehler
 * 
 */
@XmlRootElement
public class StatusMessageWrapper {

	private byte[] statusMessageByteArray;

	public byte[] getStatusMessageByteArray() {
		return statusMessageByteArray;
	}

	public Object getStatusMessageObject() {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(statusMessageByteArray);
			ObjectInputStream is = new ObjectInputStream(in);
			return is.readObject();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void setStatusMessageByteArray(byte[] pStatusMessageByteArray) {
		statusMessageByteArray = pStatusMessageByteArray;
	}

	public void setStatusMessageObject(StatusMessage statusMessage) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(statusMessage);
			statusMessageByteArray = bos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

package org.sopeco.engine.measurementenvironment.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Marius Oehler
 * 
 */
@XmlRootElement
public class TransferPackage {

	/** */
	public enum State {
		WAITING, ACQUIRED, RELEASED, RUNFINISHED
	}

	@XmlElement
	private byte[] attachmentA;

	@XmlElement
	private byte[] attachmentB;

	@XmlElement
	private byte[] attachmentC;

	/**
	 * Constructor.
	 */
	public TransferPackage() {
		this(null);
	}

	public TransferPackage(Object objectA) {
		this(objectA, null);
	}

	public TransferPackage(Object objectA, Object objectB) {
		this(objectA, objectB, null);
	}

	public TransferPackage(Object objectA, Object objectB, Object objectC) {
		setAttachmentA(objectA);
		setAttachmentB(objectB);
		setAttachmentC(objectC);
	}

	public void setAttachmentA(Object pAttachmentA) {
		attachmentA = objectToByteArray(pAttachmentA);

	}

	public void setAttachmentB(Object pAttachmentB) {
		attachmentB = objectToByteArray(pAttachmentB);

	}

	public void setAttachmentC(Object pAttachmentC) {
		attachmentC = objectToByteArray(pAttachmentC);
	}

	public Object getA() {
		return byteArrayToObject(attachmentA);
	}

	public Object getB() {
		return byteArrayToObject(attachmentB);
	}

	public Object getC() {
		return byteArrayToObject(attachmentC);
	}

	public <T> T getA(Class<T> clazz) {
		return (T) getA();
	}

	public <T> T getB(Class<T> clazz) {
		return (T) getB();
	}

	public <T> T getC(Class<T> clazz) {
		return (T) getC();
	}

	private static Object byteArrayToObject(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			ObjectInputStream is = new ObjectInputStream(in);
			return is.readObject();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private static byte[] objectToByteArray(Object object) {
		if (object == null) {
			return null;
		}
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(object);
			return bos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

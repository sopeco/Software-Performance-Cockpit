/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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

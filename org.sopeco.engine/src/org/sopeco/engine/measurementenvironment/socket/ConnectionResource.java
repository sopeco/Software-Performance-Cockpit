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
package org.sopeco.engine.measurementenvironment.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class ConnectionResource {

	private Socket socket;

	private ObjectInputStream oiStream;
	private ObjectOutputStream ooStream;

	public void bind(Socket pSocket) throws IOException {
		socket = pSocket;

		ooStream = new ObjectOutputStream(socket.getOutputStream());
		oiStream = new ObjectInputStream(socket.getInputStream());
	}

	public void bind(ConnectionResource connectionResource) {
		socket = connectionResource.getSocket();

		ooStream = connectionResource.getOoStream();
		oiStream = connectionResource.getOiStream();
	}

	public Socket getSocket() {
		return socket;
	}

	public ObjectInputStream getOiStream() {
		return oiStream;
	}

	public ObjectOutputStream getOoStream() {
		return ooStream;
	}

	public <T> T callMethod(String methodName, Object... parameter) {
		return callMethod(null, methodName, parameter);
	}

	@SuppressWarnings("unchecked")
	public <T> T callMethod(String controllerName, String methodName, Object... parameter) {
		Object inObject = null;

		synchronized (socket) {
			try {
				ooStream.writeObject(controllerName);
				ooStream.writeObject(methodName);
				ooStream.writeObject(parameter);
				ooStream.flush();
				inObject = oiStream.readObject();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		return (T) inObject;
	}

	public boolean isAlive() {
		synchronized (socket) {
			try {
				ooStream.writeObject(true);
				ooStream.flush();
				return true;
			} catch (IOException e) {
				return false;
			}
		}
	}
}

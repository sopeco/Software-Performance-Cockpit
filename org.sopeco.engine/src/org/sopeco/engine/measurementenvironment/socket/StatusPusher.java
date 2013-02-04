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
import java.io.ObjectOutputStream;

import org.sopeco.engine.measurementenvironment.status.StatusProvider;

public class StatusPusher implements Runnable {

	private boolean loop;

	private ObjectOutputStream ooStream;
	private String controllerName;

	private Object experimentResults;

	public StatusPusher(String pControllerName, ObjectOutputStream stream) {
		ooStream = stream;
		controllerName = pControllerName;
	}

	@Override
	public void run() {
		loop = true;
		while (loop) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			if (experimentResults != null) {
				loop = false;
			}

			synchronized (StatusProvider.getProvider(controllerName)) {
				while (StatusProvider.getProvider(controllerName).hasNext()) {
					try {
						ooStream.writeObject(StatusProvider.getProvider(controllerName).next());
						ooStream.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		try {
			ooStream.writeObject(experimentResults);
			ooStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setExperimentResults(Object experimentResults) {
		this.experimentResults = experimentResults;
	}

}

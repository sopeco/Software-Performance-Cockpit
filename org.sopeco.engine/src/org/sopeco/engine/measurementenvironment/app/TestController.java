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
package org.sopeco.engine.measurementenvironment.app;

import java.util.List;

import org.sopeco.engine.measurementenvironment.AbstractMEController;
import org.sopeco.engine.measurementenvironment.InputParameter;
import org.sopeco.engine.status.StatusMessage;
import org.sopeco.persistence.entities.exceptions.ExperimentFailedException;

public class TestController extends AbstractMEController {

	@InputParameter(namespace="test")
	int abc = 0;

	@Override
	protected void defineResultSet() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void prepareExperimentSeries() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void runExperiment() throws ExperimentFailedException {
System.out.println(1);
		
		for (int i = 0; i < abc; i++) {
			System.out.println(2);

			try {

				sendInformation("started");

				Thread.sleep(2500);
				System.out.println(3);

				sendInformation("1");
				sendInformation("2");

				Thread.sleep(2500);
				System.out.println(4);

				sendInformation("ending");

			} catch (Exception e) {
			}

		}
		System.out.println(5);
	}

	@Override
	protected void finalizeExperimentSeries() {
	}

	public static void main(String[] args) {
		MECApplication mecapp = MECApplication.get();
		mecapp.addMeasurementController("ABC", new TestController());
		mecapp.addMeasurementController("Testcontroller", new TestController());
		mecapp.addMeasurementController("DasGleicheNochmal", new TestController());
//		mecapp.startREST(1300);
//		mecapp.startRMI();
		mecapp.socketConnect("10.55.145.113", 11300);
//		mecapp.socketConnect("127.0.0.1", 11300);
	}

}

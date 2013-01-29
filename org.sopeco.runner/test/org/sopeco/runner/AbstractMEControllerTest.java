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
package org.sopeco.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueFactory;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.util.ParameterCollectionFactory;

/**
 * Tests the ME Controller functionality encapsulated in the
 * AbstractMEController class.
 * 
 * @author Alexander Wert
 * 
 */
/* CHECKSTYLE:OFF*/
public class AbstractMEControllerTest {
	private static final int INIT_PARAMETER_VALUE = 11;

	/**
	 * Test MEDefinition interpretation of MEController.
	 * 
	 * @throws RemoteException Thrown if MEController throws an exception.
	 */
	@Test
	public void testMEDefinitionInterpretation() throws RemoteException {
		DummyMEController dummyController = new DummyMEController();
		MeasurementEnvironmentDefinition meDef = dummyController.getMEDefinition();
		assertEquals(meDef.getRoot().getName(), "");

		ParameterNamespace testNS = null;
		for (ParameterNamespace pns : meDef.getRoot().getChildren()) {
			if (pns.getName().equals("test")) {
				testNS = pns;
			}
		}
		assertNotNull(testNS);

		boolean found = false;
		for (ParameterNamespace pns : testNS.getChildren()) {
			if (pns.getName().equals("input")) {
				assertNotNull(pns.getParameter("inputParameter"));
				found = true;
			}
		}
		assertTrue(found);

		found = false;
		for (ParameterNamespace pns : testNS.getChildren()) {
			if (pns.getName().equals("observation")) {
				assertNotNull(pns.getParameter("observationParameterOne"));
				assertNotNull(pns.getParameter("observationParameterTwo"));
				found = true;
			}
		}
		assertTrue(found);

	}

	/**
	 * Test execution of MEController.
	 * 
	 * @throws RemoteException Thrown if MEController throws an exception.
	 */
	@Test
	public void testMECExecution() throws RemoteException {
		DummyMEController dummyController = new DummyMEController();
		ParameterNamespace initNS = dummyController.getMEDefinition().getNamespace("init");
		ParameterValue<Integer> initPV = ParameterValueFactory.createParameterValue(
				initNS.getParameter("initParameter"), INIT_PARAMETER_VALUE);
		List<ParameterValue<?>> pvc = new ArrayList<ParameterValue<?>>();
		pvc.add(initPV);
		dummyController.acquireMEController("myID", 0);
		dummyController.initialize("myID", ParameterCollectionFactory.createParameterValueCollection(pvc));
		dummyController.releaseMEController("myID");
	}
}
/* CHECKSTYLE:ON*/

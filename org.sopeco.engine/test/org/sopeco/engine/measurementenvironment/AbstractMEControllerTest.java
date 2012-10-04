package org.sopeco.engine.measurementenvironment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.sopeco.engine.util.test.DummyMEController;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueFactory;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.util.ParameterCollectionFactory;

public class AbstractMEControllerTest {
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

	@Test
	public void testMECExecution() throws RemoteException {
		DummyMEController dummyController = new DummyMEController();
		ParameterNamespace initNS = dummyController.getMEDefinition().getNamespace("init");
		ParameterValue<Integer> initPV = ParameterValueFactory.createParameterValue(
				initNS.getParameter("initParameter"), 11);
		List<ParameterValue<?>> pvc = new ArrayList<ParameterValue<?>>();
		pvc.add(initPV);
		dummyController.initialize(ParameterCollectionFactory.createParameterValueCollection(pvc));
	}
}

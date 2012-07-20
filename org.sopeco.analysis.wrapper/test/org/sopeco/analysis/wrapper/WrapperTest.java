package org.sopeco.analysis.wrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WrapperTest {

	private AnalysisWrapper wrapper;

	@Before
	public void initAnalysisWrapper() {
		wrapper = new AnalysisWrapper();
	}

	@After
	public void shutdownAnalysisWrapper() {
		wrapper.shutdown();
	}

	@Test
	public void testExecuteCommandString() {
		String result = wrapper.executeCommandString("2+3");
		assertEquals("5.0", result);
	}

	@Test
	public void testExecuteCommandStringArray() {
		wrapper.executeCommandString("a <- c(\"x\",\"y\",\"z\")");
		String[] result = wrapper.executeCommandStringArray("a");
		assertEquals("x", result[0]);
		assertEquals("y", result[1]);
		assertEquals("z", result[2]);
	}

	@Test
	public void testExecuteCommandDouble() {
		double result = wrapper.executeCommandDouble("2.5+3.5");
		assertTrue(6.0 == result);
	}

	@Test
	public void testExecuteCommandDoubleArray() {
		wrapper.executeCommandString("a <- c(2,3,4,5)");
		double[] result = wrapper.executeCommandDoubleArray("a");
		assertTrue(2.0 == result[0]);
		assertTrue(3.0 == result[1]);
		assertTrue(4.0 == result[2]);
		assertTrue(5.0 == result[3]);
	}

	@Test
	public void testInitVariables() {
		wrapper.initVariables("x", new double[] { 0, 1, 2 });
		assertEquals(0.0, wrapper.executeCommandDouble("x0"), 0.001);
		assertEquals(1.0, wrapper.executeCommandDouble("x1"), 0.001);
		assertEquals(2.0, wrapper.executeCommandDouble("x2"), 0.001);
	}

	@Test
	public void testMultipleWrapperInstances() {
		wrapper.shutdown();
		Calculator c1 = new Calculator(1, 2);
		Calculator c2 = new Calculator(10, 20);

		Thread t1 = new Thread(c1);
		Thread t2 = new Thread(c2);

		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		assertEquals(1.0, c1.getResult(), 0.0001);
		assertEquals(2.0, c1.getResult2(), 0.0001);
		assertEquals(10.0, c2.getResult(), 0.0001);
		assertEquals(200.0, c2.getResult2(), 0.0001);

	}

}

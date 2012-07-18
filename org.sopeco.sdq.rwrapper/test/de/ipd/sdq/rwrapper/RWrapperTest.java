package de.ipd.sdq.rwrapper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RWrapperTest {

	private RWrapper wrapper;

	@Before
	public void initialize() {
		wrapper = new RWrapper();
	}

	@Test
	public void testInitVariables() {
		wrapper.initVariables("a", new double[] { 0, 1, 2 });
		Assert.assertEquals(0.0, wrapper.executeRCommandDouble("a0"), 0.001);
		Assert.assertEquals(1.0, wrapper.executeRCommandDouble("a1"), 0.001);
		Assert.assertEquals(2.0, wrapper.executeRCommandDouble("a2"), 0.001);
	}

	@Test
	public void testExecuteRCommandString() {
		wrapper.initVariables("a", new double[] { 0, 1, 2 });
		wrapper.executeRCommandString("a4 <- a0 + a1 + a2");
		Assert.assertEquals(3.0, wrapper.executeRCommandDouble("a4"), 0.001);
	}

	@Test
	public void testExecuteRCommandDouble() {
		Assert.assertEquals(3.141593, wrapper.executeRCommandDouble("pi"), 0.001);
	}

	@Test
	public void testShutdown() {
		wrapper.shutdown();
	}

	@Test
	public void testGetConsole() {
		RTextConsole console = wrapper.getConsole();
		Assert.assertNotNull(console);
	}

	@After
	public void tearDown() {
		wrapper.shutdown();
	}

}

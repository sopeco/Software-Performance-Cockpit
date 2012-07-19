package org.sopeco.util.system;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SystemToolsTest {

	static boolean passed = false;
	
	@Before
	public void setUp() throws Exception {
		SystemTools.loadNativeLibraries();
		passed = true;
	}

	@Test
	public void test() {
		assertTrue(passed);
	}
	

}

package org.sopeco.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ToolsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetConfidenceIntervalWidthIntDoubleDouble() {
		System.out.println(Tools.getConfidenceIntervalWidth(50, 26.345, 0.9));
	}

}

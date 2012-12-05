package org.sopeco.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ToolsTest {

	@Before
	public void setUp() throws Exception {
	}

	public void testGetConfidenceIntervalWidthIntDoubleDouble() {
		System.out.println(Tools.getConfidenceIntervalWidth(50, 26.345, 0.9));
	}

	@Test
	public void testOutlierRemoval() {
		Double[] values01 = new Double[] {140d, 101d, 100d, 95d, 104d, 105d, 102.5};

		List<Double> results = Tools.filterOutliersUsingIQR(Arrays.asList(values01));

		assertEquals(values01.length - 1, results.size());

		Double[] values02 = new Double[] {-5d, 1d, 5d, 2d, 10d, 3d, 4d, 4.5, 20d, -15d};

		results = Tools.filterOutliersUsingIQR(Arrays.asList(values02));

		assertEquals(values02.length - 2, results.size());

	}
}

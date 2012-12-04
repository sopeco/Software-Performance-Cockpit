package org.sopeco.util;

import static org.junit.Assert.*;

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
		double[] values01 = new double[] {140, 101, 100, 95, 104, 105, 102.5};

		List<Double> results = Tools.filterOutliersUsingIQR(values01, 1.5);

		assertEquals(values01.length - 1, results.size());

		double[] values02 = new double[] {-5, 1, 5, 2, 10, 3, 4, 4.5, 20, -15};

		results = Tools.filterOutliersUsingIQR(values02, 1.5);

		assertEquals(values02.length - 2, results.size());

	}
}

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

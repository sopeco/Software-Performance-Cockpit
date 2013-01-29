package org.test.github;

import org.junit.Test;

public class MyUnitTest {
	@Test
	public void test() {
		// comment
		TestClass t = new TestClass();
		org.junit.Assert.assertEquals(t.getA(), t.getB());
	}
}

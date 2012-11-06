package org.sopeco.config;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.config.exception.ConfigurationException;

public class ConfigurationTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		IConfiguration config = Configuration.getSessionUnrelatedSingleton();
		config.setAppRootDirectory("rsc/diffRoot");
		try {
			config.loadDefaultConfiguration("sopeco-defaults.conf");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		
		assertEquals("TestValueA", config.getProperty("sopeco.test.testParamA"));
	}

}

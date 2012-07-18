/**
 * 
 */
package org.sopeco.hyperic.sigar;

import static org.junit.Assert.*;

import org.sopeco.util.system.SystemTools;
import org.hyperic.sigar.Sigar;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Roozbeh Farahbod
 *
 */
public class SigarTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		SystemTools.loadNativeLibraries();
	}

	@Test
	public void test() {
		Sigar sigar = new Sigar();
		
		assertTrue(sigar.getPid() > 0);
		
		System.out.println("Current process id: " + sigar.getPid());
	}

}

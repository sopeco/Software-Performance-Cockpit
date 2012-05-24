package org.sopeco.plugin.std.exploration.screening.util;

import de.ipd.sdq.rwrapper.RWrapper;

/**
 * This class provides helper methods to access the R environment.
 * 
 * @author Dennis Westermann, Jens Happe
 *
 */
public abstract class RAdapter {
	private static RWrapper rWrapper = null;

	/**
	 * RWrapper is a singleton.
	 * 
	 * @return Instance of the RWrapper
	 */
	public static synchronized RWrapper getWrapper() {
		if (rWrapper == null) {
			rWrapper = new RWrapper();
			rWrapper.executeRCommandString(".Library <- file.path(R.home(), 'library')");
		}
		return rWrapper;
	}

	/**
	 * Closes the connection to R and removes its wrapper.
	 */
	public static synchronized void shutDown() {
		if (rWrapper != null) {
			rWrapper.shutdown();
			rWrapper = null;
		}
	}

}

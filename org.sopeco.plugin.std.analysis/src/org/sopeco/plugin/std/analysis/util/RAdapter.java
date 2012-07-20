package org.sopeco.plugin.std.analysis.util;

import org.sopeco.analysis.wrapper.AnalysisWrapper;

/**
 * This class provides helper methods to access the R environment.
 * 
 * @author Dennis Westermann, Jens Happe
 * 
 */
public class RAdapter {
	private static AnalysisWrapper rWrapper = null;

	/**
	 * RWrapper is a singleton.
	 * 
	 * @return Instance of the RWrapper
	 */
	public static synchronized AnalysisWrapper getWrapper() {
		if (rWrapper == null) {
			rWrapper = AnalysisWrapper.getDefaultWrapper();
			rWrapper.executeCommandString(".Library <- file.path(R.home(), 'library')");
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

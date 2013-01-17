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

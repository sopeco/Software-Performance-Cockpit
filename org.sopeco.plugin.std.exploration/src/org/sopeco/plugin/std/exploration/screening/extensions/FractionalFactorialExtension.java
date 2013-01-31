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
package org.sopeco.plugin.std.exploration.screening.extensions;

import org.sopeco.plugin.std.exploration.screening.config.ScreeningConfiguration;

/**
 * The extension that provides the fractional factorial exploration strategy.
 * 
 * @author Dennis Westermann
 *
 */
public class FractionalFactorialExtension extends AbstractScreeningExplorationExtension {

	/**
	 * The name of the provided extension artifact.
	 */
	public static final String NAME = ScreeningConfiguration.FRACTIONAL_FACTORIAL;

	
	@Override
	public String getName() {
		return NAME;
	}

	
	@Override
	protected void prepareConfigurationParameterMap() {
		
		
		/* Common Properties */
		// Replications not yet supported by screening analysis 
//		configParams.put(ScreeningConfiguration.USE_REPLICATION, "false");
//		configParams.put(ScreeningConfiguration.NUM_REPLICATIONS, "");
		configParams.put(ScreeningConfiguration.RANDOMIZE_RUNS, "false");
		
		/* Only for Fractional Factorial */
		configParams.put(ScreeningConfiguration.RESOLUTION, "3");
		configParams.put(ScreeningConfiguration.USE_NUM_RUNS_INSTEAD_OF_RESOLUTION, "false");
		configParams.put(ScreeningConfiguration.NUMBER_OF_RUNS, "");
		
	}
}
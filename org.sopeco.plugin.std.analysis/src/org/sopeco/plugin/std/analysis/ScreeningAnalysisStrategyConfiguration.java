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
package org.sopeco.plugin.std.analysis;

import org.sopeco.persistence.entities.definition.AnalysisConfiguration;

/**
 * This class holds all the configuration parameter names and default values of
 * the screening analysis strategy. Its methods either return the value configured in
 * the scenario definition of SoPeCo or the default value of a parameter.
 * 
 * @author Dennis Westermann
 * 
 */
public abstract class ScreeningAnalysisStrategyConfiguration {

	
	protected static final String RANDOMIZE_RUNS = "RandomizeRuns";
	protected static final boolean RANDOMIZE_RUNS_DEFAULT_VALUE = false;
	
	protected final static String INTERACTION_DEPTH = "InteractionDepth";
	protected final static int INTERACTION_DEPTH_DEFAULT_VALUE = 1;

	/**
	 * @param strategyConfig
	 *            - the configuration of the analysis strategy provided by
	 *            the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static boolean randomizeRuns(AnalysisConfiguration strategyConfig) {
		String value = strategyConfig.getConfiguration().get(RANDOMIZE_RUNS);
		if (value != null) {
			return Boolean.parseBoolean(value);
		} else {
			return RANDOMIZE_RUNS_DEFAULT_VALUE;
		}
	}
	
	/**
	 * @param strategy
	 *            - the configuration of the analysis strategy provided by
	 *            the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static int getInteractionDepth(AnalysisConfiguration strategy) {
		String value = strategy.getConfiguration().get(INTERACTION_DEPTH);
		if (value != null) {
			return Integer.parseInt(value);
		} else {
			return INTERACTION_DEPTH_DEFAULT_VALUE;
		}
	}


}

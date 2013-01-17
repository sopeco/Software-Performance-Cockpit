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
package org.sopeco.plugin.std.exploration.screening.config;

import org.sopeco.persistence.entities.definition.ExplorationStrategy;

/**
 * This class holds all the configuration parameter names and default values of
 * the screening explorations. Its methods either return the value configured in
 * the scenario definition of SoPeCo or the default value of a parameter.
 * 
 * @author Dennis Westermann
 * 
 */
public abstract class ScreeningConfiguration {

	public static final String FRACTIONAL_FACTORIAL = "Fractional Factorial";
	public static final String FULL_FACTORIAL = "Full Factorial";
	public static final String PLACKETT_BURMAN = "Plackett Burman";
	
	
	/*
	 * Required for all Screening Explorations
	 */
	public static final String RANDOMIZE_RUNS = "RandomizeRuns";
	private static final boolean RANDOMIZE_RUNS_DEFAULT_VALUE = false;
	
	public static final String USE_REPLICATION = "UseReplication";
	private static final boolean USE_REPLICATION_DEFAULT_VALUE = false;
	
	public static final String NUM_REPLICATIONS = "NumberOfReplications";
	private static final int NUM_REPLICATIONS_DEFAULT_VALUE = 1;

	
	/*
	 * Required for Fractional Factorial
	 */
	public static final String RESOLUTION = "Resolution";
	private static final int RESOLUTION_DEFAULT_VALUE = 3;
	
	public static final String USE_NUM_RUNS_INSTEAD_OF_RESOLUTION = "UseNumRunsInsteadOfResolution";
	private static final boolean USE_NUM_RUNS_INSTEAD_OF_RESOLUTION_DEFAULT_VALUE = false;
	
	public static final String NUMBER_OF_RUNS = "NumberOfRuns";
	private static final int NUMBER_OF_RUNS_DEFAULT_VALUE = 2;
	
	/**
	 * @param strategyConfig
	 *            - the configuration of the exploration strategy provided by
	 *            the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static boolean randomizeRuns(ExplorationStrategy strategyConfig) {
		String value = strategyConfig.getConfiguration().get(RANDOMIZE_RUNS);
		if (value != null) {
			return Boolean.parseBoolean(value);
		} else {
			return RANDOMIZE_RUNS_DEFAULT_VALUE;
		}
	}
	
	/**
	 * @param strategyConfig
	 *            - the configuration of the exploration strategy provided by
	 *            the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static boolean useReplication(ExplorationStrategy strategyConfig) {
		String value = strategyConfig.getConfiguration().get(USE_REPLICATION);
		if (value != null) {
			return Boolean.parseBoolean(value);
		} else {
			return USE_REPLICATION_DEFAULT_VALUE;
		}
	}
	
	/**
	 * @param strategyConfig
	 *            - the configuration of the exploration strategy provided by
	 *            the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static int getNumberOfReplications(ExplorationStrategy strategyConfig) {
		String value = strategyConfig.getConfiguration().get(NUM_REPLICATIONS);
		if (value != null) {
			return Integer.parseInt(value);
		} else {
			return NUM_REPLICATIONS_DEFAULT_VALUE;
		}
	}

	/**
	 * @param strategyConfig
	 *            - the configuration of the exploration strategy provided by
	 *            the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static int getResolution(ExplorationStrategy strategyConfig) {
		String value = strategyConfig.getConfiguration().get(RESOLUTION);
		if (value != null) {
			return Integer.parseInt(value);
		} else {
			return RESOLUTION_DEFAULT_VALUE;
		}
	}
	
	/**
	 * @param strategyConfig
	 *            - the configuration of the exploration strategy provided by
	 *            the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static boolean useNumRunsInsteadOfResolution(ExplorationStrategy strategyConfig) {
		String value = strategyConfig.getConfiguration().get(USE_NUM_RUNS_INSTEAD_OF_RESOLUTION);
		if (value != null) {
			return Boolean.parseBoolean(value);
		} else {
			return USE_NUM_RUNS_INSTEAD_OF_RESOLUTION_DEFAULT_VALUE;
		}
	}
	
	
	/**
	 * @param strategyConfig
	 *            - the configuration of the exploration strategy provided by
	 *            the users of SoPeCo
	 * @return the value of the configuration parameter
	 */
	public static int getNumberOfRuns(ExplorationStrategy strategyConfig) {
		String value = strategyConfig.getConfiguration().get(NUMBER_OF_RUNS);
		if (value != null) {
			return Integer.parseInt(value);
		} else {
			return NUMBER_OF_RUNS_DEFAULT_VALUE;
		}
	
	}

}

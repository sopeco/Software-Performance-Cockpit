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
package org.sopeco.plugin.std.exploration.screening.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * 
 * @author Pascal Meier
 * @author Dennis Westermann
 *
 */
public class ExpDesign {
	
	/**
	 * Parameters analysed by the design.
	 */
	private ArrayList<ParameterDefinition> parameters;
	/**
	 * number of runs of the generated design
	 */
	private int numberOfRuns;
	/**
	 * Used to determine if the runs of the design will be executed multiple
	 * times
	 */
	private boolean useReplication;
	/**
	 * Used to determine if the order of the runs will be randomized
	 */
	private boolean randomizeRuns;
	/**
	 * Number of replications. Determines how often each of the design run will
	 * be repeated.
	 */
	private int numberOfReplications;
	/**
	 * Maps a list to each parameter containing the run level of the parameter
	 * for each run of the design
	 */
	private HashMap<ParameterDefinition, ParameterRunLevels> parameterRunLevelsMap;
	/**
	 * HashMap of ParameterFactorValues for each parameter.
	 */
	private HashMap<ParameterDefinition, ParameterFactorValues> parameterFactorLevelMap;
	/**
	 * Type of the design
	 */
	private ExpDesignType type;
	/**
	 * Resolution of the experimental design
	 */
	private int resolution;

	/**
	 * Constructor.
	 */
	public ExpDesign() {
		super();
		parameterFactorLevelMap = new HashMap<ParameterDefinition, ParameterFactorValues>();
		parameterRunLevelsMap = new HashMap<ParameterDefinition, ParameterRunLevels>();
		parameters = new ArrayList<ParameterDefinition>();
	}

	public void addParameter(ParameterDefinition parameter) {
		this.parameters.add(parameter);
	}

	public List<ParameterDefinition> getParameters() {
		return parameters;
	}

	public int getNumberOfRuns() {
		return numberOfRuns;
	}

	public boolean isUseReplication() {
		return useReplication;
	}

	public void setUseReplication(boolean useReplication) {
		this.useReplication = useReplication;
	}

	public boolean isRandomizeRuns() {
		return randomizeRuns;
	}

	public void setRandomizeRuns(boolean randomizeRuns) {
		this.randomizeRuns = randomizeRuns;
	}

	public int getNumberOfReplications() {
		return numberOfReplications;
	}

	public void setNumberOfReplications(int numberOfReplications) {
		this.numberOfReplications = numberOfReplications;
	}

	public void addFactorValuesByParameter(ParameterDefinition parameter,
			ParameterFactorValues factorValues) {
		parameterFactorLevelMap.put(parameter, factorValues);
	}

	public int getNumberOfParameters() {
		return parameters.size();
	}

	public void addRunLevelsByParameter(ParameterDefinition parameter,
			ParameterRunLevels runLevels) {
		parameterRunLevelsMap.put(parameter, runLevels);
	}

	/**
	 * Returns a list of parameter values for a specified run of the design.
	 * 
	 * @param runNumber
	 *            number of the run for which the parameter values will be
	 *            returned
	 * @return list of parameter values for a specified run of the design
	 * @throws FrameworkException
	 */
	public List<ParameterValue<?>> getParameterValuesByRun(int runNumber) {
		List<ParameterValue<?>> parameterValues = new ArrayList<ParameterValue<?>>();
		for (ParameterDefinition param : parameters) {
			int level = parameterRunLevelsMap.get(param).getLevelByRunNumber(
					runNumber);
			parameterValues.add(parameterFactorLevelMap.get(param)
					.getParameterValueByLevel(level));
		}
		return parameterValues;
	}

	public int getLevelOfRunByParam(ParameterDefinition parameter, int runNumber) {
		return parameterRunLevelsMap.get(parameter).getLevelByRunNumber(
				runNumber);
	}

	public List<Integer> getLevelsOfAllRunsByParam(ParameterDefinition parameter) {
		return parameterRunLevelsMap.get(parameter).getAllRunLevels();
	}

	public ExpDesignType getType() {
		return type;
	}

	public void setType(ExpDesignType type) {
		this.type = type;
	}

	public int getResolution() {
		return resolution;
	}

	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	/**
	 * Returns a String-representation of the design.
	 * 
	 * @return String-representation of the design
	 */
	public String toString() {
		StringBuilder resultBuilder = new StringBuilder();
		resultBuilder.append("------------------------------------------\n");
		resultBuilder.append("Design Type: " + type.toString() + "\n");
		resultBuilder.append("Resolution: " + resolution + "\n");
		resultBuilder.append("Runs: " + numberOfRuns + ",\t Parameters: "
				+ parameters.size() + "\n");
		resultBuilder.append("Replications: [" + useReplication + "] ("
				+ numberOfReplications + ")\n");
		resultBuilder.append("Randomization: [" + randomizeRuns + "]\n");
		for (ParameterDefinition param : parameters) {
			resultBuilder.append(param.getName() + "\t");
		}
		resultBuilder.append("\n");
		for (int i = 0; i < numberOfRuns; i++) {
			for (ParameterDefinition param : parameters) {
				resultBuilder.append(parameterRunLevelsMap.get(param)
						.getLevelByRunNumber(i) + "\t");
			}
			resultBuilder.append("\n");
		}
		return resultBuilder.toString();
	}

	/**
	 * Used to update the number of runs of the design after it has been
	 * generated.
	 * 
	 * @throws FrameworkException
	 */
	public void updateNumberOfRuns() {
		boolean first = true;
		for (ParameterRunLevels runLevels : parameterRunLevelsMap.values()) {
			if (first) {
				numberOfRuns = runLevels.getNumberOfRunLevels();
				first = false;
			} else {
				if (numberOfRuns != runLevels.getNumberOfRunLevels()) {
					throw new IllegalStateException(
							"Number of run levels of the parameters is not equal. Design is inconsistent!");
				}
			}
		}
	}

	public ParameterFactorValues getFactorValuesOfParameter(ParameterDefinition param) {
		return parameterFactorLevelMap.get(param);
	}

}

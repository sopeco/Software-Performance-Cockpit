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
package org.sopeco.engine.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueFactory;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * This class provides an implementation of {@link IPredictionFunctionResult}
 * that can interpret any mathematical function written in JavaScript syntax
 * (@see http://www.w3schools.com/js/).
 * 
 * @author Dennis Westermann
 * 
 */
public class PredictionFunctionResult implements IPredictionFunctionResult {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(PredictionFunctionResult.class);

	private String resultId;
	private String predictionFunction;
	private ScriptEngineManager scriptEngineManager;
	private ScriptEngine javaScriptEngine;
	private ParameterDefinition dependentParameter;
	private AnalysisConfiguration analysisConfiguration;
	private Map<ParameterDefinition, Map<Object, Integer>> nonNumericParameterEncodings = new HashMap<ParameterDefinition, Map<Object, Integer>>();

	/**
	 * Creates an instance of {@link PredictionFunctionResult} based on the
	 * given prediction function, the dependent parameter definition, and the
	 * analysis configuration.
	 * 
	 * @param predictionFunction
	 *            - a string representation of the prediction function in
	 *            JavaScript syntax (@see http://www.w3schools.com/js/). The
	 *            name of the independent parameters in the function has to be
	 *            derived by the getFullName("_") prediction function where "_"
	 *            is the namespace delimiters
	 * @param dependentParameter
	 *            - the definition of the dependent parameter in the
	 *            predictionFunction.
	 * @param analysisConfiguration
	 *            - the configuration based on which the prediction function has
	 *            been derived by SoPeCo
	 * @param nonNumericParameterEncodings
	 *            - a map that holds the information how the values of a
	 *            non-numeric parameter have been encoded to an Integer
	 *            representation
	 */
	public PredictionFunctionResult(String predictionFunction, ParameterDefinition dependentParameter,
			AnalysisConfiguration analysisConfiguration,
			Map<ParameterDefinition, Map<Object, Integer>> nonNumericParameterEncodings) {
		this.predictionFunction = predictionFunction;
		scriptEngineManager = new ScriptEngineManager();
		javaScriptEngine = scriptEngineManager.getEngineByName("js");
		this.dependentParameter = dependentParameter;
		this.analysisConfiguration = analysisConfiguration;
		this.nonNumericParameterEncodings = nonNumericParameterEncodings;
	}

	@Override
	public ParameterValue<?> predictOutputParameter(List<ParameterValue<?>> inputParameters) {

		// Initialize parameters in script engine
		logger.debug("Function: {}", predictionFunction);
		logger.debug("Predict output parameter for input parameters: ");

		for (ParameterValue<?> pv : inputParameters) {

			if (pv.getParameter().isNumeric()) {
				javaScriptEngine.put(pv.getParameter().getFullName("_"), pv.getValueAsDouble());
			} else {
				javaScriptEngine.put(pv.getParameter().getFullName("_"),
						getNonNumericParameterEncodings().get(pv.getParameter()).get(pv.getValue()));
			}
			logger.debug("{}: {}", pv.getParameter().getFullName("_"), pv.getValueAsString());
		}

		logger.debug("Function: {}", predictionFunction);
		// Get result
		try {
			Double result = (Double) javaScriptEngine.eval(predictionFunction);
			logger.debug("--> Result: {}", result);
			return ParameterValueFactory.createParameterValue(dependentParameter, result);

		} catch (ScriptException se) {
			throw new IllegalStateException("Could not evaluate function based on the given parameters", se);
		}

	}

	@Override
	public ParameterValue<?> predictOutputParameter(ParameterValue<?> inputParameter) {

		List<ParameterValue<?>> paramList = new ArrayList<ParameterValue<?>>();
		paramList.add(inputParameter);

		return predictOutputParameter(paramList);
	}

	@Override
	public String getFunctionAsString() {

		return predictionFunction;
	}

	@Override
	public AnalysisConfiguration getAnalysisStrategyConfiguration() {
		return analysisConfiguration;
	}

	@Override
	public String getId() {
		return this.resultId;
	}

	@Override
	public void setId(String id) {
		this.resultId = id;
	}

	@Override
	public Map<ParameterDefinition, Map<Object, Integer>> getNonNumericParameterEncodings() {
		return nonNumericParameterEncodings;
	}

}

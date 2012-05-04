package org.sopeco.plugin.std.analysis.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.analysis.IPredictionFunctionResult;
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

	private static Logger logger = LoggerFactory.getLogger(PredictionFunctionResult.class);
	String predictionFunction;
	ScriptEngineManager scriptEngineManager;
	ScriptEngine javaScriptEngine;
	ParameterDefinition dependentParameter;
	HashSet<String> indepParameterNames = new HashSet<String>();
	HashSet<ParameterDefinition> indepPaparameters = new HashSet<ParameterDefinition>();
	AnalysisConfiguration analysisConfiguration;

	/**
	 * Creates an instance of {@link PredictionFunctionResult} based on the
	 * given prediction function, the dependent parameter definition, and the
	 * analysis configuration.
	 * 
	 * @param predictionFunction
	 *            - a string representation of the prediction function in
	 *            JavaScript syntax (@see http://www.w3schools.com/js/). The
	 *            name of the independent parameters in the function has to be
	 *            derived by the getFullName("_") prediction function where "_" is
	 *            the namespace delimiters
	 * @param dependentParameter
	 *            - the definition of the dependent parameter in the
	 *            predictionFunction.
	 * @param analysisConfiguration
	 *            - the configuration based on which the prediction function has
	 *            been derived by SoPeCo
	 */
	public PredictionFunctionResult(String predictionFunction, ParameterDefinition dependentParameter, AnalysisConfiguration analysisConfiguration) {
		this.predictionFunction = predictionFunction;
		scriptEngineManager = new ScriptEngineManager();
		javaScriptEngine = scriptEngineManager.getEngineByName("js");
		this.dependentParameter = dependentParameter;
		this.analysisConfiguration = analysisConfiguration;
	}

	@Override
	public ParameterValue<?> predictOutputParameter(List<ParameterValue<?>> inputParameters) {

		// Initialize parameters in script engine
		logger.debug("Function: {}", predictionFunction);
		logger.debug("Predict output parameter for input parameters: ");

		for (ParameterValue<?> pv : inputParameters) {

			javaScriptEngine.put(pv.getParameter().getFullName("_"), pv.getValueAsDouble());
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
}

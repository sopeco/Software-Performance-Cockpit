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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.analysis.wrapper.common.CSVStringGenerator;
import org.sopeco.analysis.wrapper.exception.AnalysisWrapperException;
import org.sopeco.engine.analysis.IParameterInfluenceResult;
import org.sopeco.engine.analysis.IParameterInfluenceStrategy;
import org.sopeco.engine.analysis.IPredictionFunctionResult;
import org.sopeco.engine.analysis.IPredictionFunctionStrategy;
import org.sopeco.engine.analysis.ParameterInfluenceResult;
import org.sopeco.engine.analysis.ParameterRegressionCoefficient;
import org.sopeco.engine.analysis.PredictionFunctionResult;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.analysis.common.AbstractAnalysisStrategy;

/**
 * This analysis strategy allows executing a linear regression in R.
 * 
 * @author Dennis Westermann, Jens Happe
 * 
 */
public class LinearRegressionStrategy extends AbstractAnalysisStrategy implements IPredictionFunctionStrategy,
		IParameterInfluenceStrategy {

	Logger logger = LoggerFactory.getLogger(LinearRegressionStrategy.class);

	protected LinearRegressionStrategy(LinearRegressionStrategyExtension provider) {
		super(provider);
		try {
			loadLibraries(analysisWrapper);
		} catch (AnalysisWrapperException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void analyse(DataSetAggregated dataset, AnalysisConfiguration config) {
		try {
			logger.debug("Starting linear regression analysis.");

			deriveDependentAndIndependentParameters(dataset, config);

			DataSetAggregated analysisDataSet = extractAnalysisDataSet(dataset);

			DataSetAggregated numericAnalysisDataSet = createNumericDataSet(analysisDataSet);

			loadDataSetInR(analysisWrapper, numericAnalysisDataSet.convertToSimpleDataSet());

			// build and execute R command
			StringBuilder cmdBuilder = new StringBuilder();
			cmdBuilder.append(getId());
			cmdBuilder.append(" <- lm(");
			cmdBuilder.append(dependentParameterDefintion.getFullName("_"));
			cmdBuilder.append(" ~ ");
			cmdBuilder.append(CSVStringGenerator.generateParameterString(" + ", independentParameterDefinitions));
			cmdBuilder.append(")");
			logger.debug("Running R Command: {}", cmdBuilder.toString());
			analysisWrapper.executeCommandString(cmdBuilder.toString());
		} catch (AnalysisWrapperException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public IPredictionFunctionResult getPredictionFunctionResult() {
		// create and return result object
		try {
			return buildResultObject();
		} catch (AnalysisWrapperException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return the analysis result as a function string that conforms to the
	 *         JavaScript syntax
	 * @throws AnalysisWrapperException
	 */
	private String getFunctionAsString() throws AnalysisWrapperException {
		StringBuilder functionBuilder = new StringBuilder();
		functionBuilder.append(analysisWrapper.executeCommandString(getId() + "$coefficients[1]"));
		int index = 1;
		for (ParameterDefinition inputParameter : independentParameterDefinitions) {
			index++;
			functionBuilder.append(" + ");
			functionBuilder.append(analysisWrapper.executeCommandString(getId() + "$coefficients[" + index + "]"));
			functionBuilder.append(" * ");
			functionBuilder.append(inputParameter.getFullName("_"));
		}
		/*
		 * Workaround for bug in linear regression implementation of R. R
		 * returns a function of type 10 + NA * paramID if the value of the
		 * parameter has not been varied. Fixing this by replacing NA with 0, to
		 * get the single value.
		 */
		String functionString = functionBuilder.toString();
		if (functionString.contains("NA")) {
			functionString = functionString.replaceAll("NA", "0");
		}

		return functionString;
	}

	private IPredictionFunctionResult buildResultObject() throws AnalysisWrapperException {
		return new PredictionFunctionResult(getFunctionAsString(), dependentParameterDefintion, config,
				nonNumericParameterEncodings);
	}

	/**
	 * Returns the coefficient of the specified parameter from the linear model.
	 * 
	 * @param parameter
	 *            parameter for which the model coefficient is desired
	 * @return coefficient of the specified parameter
	 * @throws AnalysisWrapperException
	 */
	public ParameterRegressionCoefficient getCoefficientByParameter(ParameterDefinition parameter)
			throws AnalysisWrapperException {

		int index = independentParameterDefinitions.indexOf(parameter);
		double coeff = analysisWrapper.executeCommandDouble(getId() + "$coefficients[" + (index + 2) + "]");

		return new ParameterRegressionCoefficient(parameter, dependentParameterDefintion, coeff);
	}

	/**
	 * Returns the coefficient of all parameters specified in the
	 * parameterDependency from the linear model in the order as they are
	 * described in the dependency.
	 * 
	 * @param parameter
	 *            parameter for which the model coefficient is desired
	 * @return coefficient of the specified parameter
	 * @throws AnalysisWrapperException
	 */
	public List<ParameterRegressionCoefficient> getAllParameterCoefficients() throws AnalysisWrapperException {
		ArrayList<ParameterRegressionCoefficient> resultList = new ArrayList<ParameterRegressionCoefficient>();
		int index = 1;

		for (int i = 0; i < independentParameterDefinitions.size(); i++) {
			ParameterDefinition parameter = independentParameterDefinitions.get(i);
			index++;
			double coeff = analysisWrapper.executeCommandDouble(getId() + "$coefficients[" + index + "]");
			resultList.add(new ParameterRegressionCoefficient(parameter, dependentParameterDefintion, coeff));
		}

		return resultList;
	}

	@Override
	public IParameterInfluenceResult getParameterInfluenceResult() {
		ParameterInfluenceResult result = new ParameterInfluenceResult(config);
		try {
			for (ParameterRegressionCoefficient prc : getAllParameterCoefficients()) {
				result.addParameterInfluenceDescriptor(prc);
			}
			return result;
		} catch (AnalysisWrapperException e) {
			throw new RuntimeException(e);
		}

	}

}

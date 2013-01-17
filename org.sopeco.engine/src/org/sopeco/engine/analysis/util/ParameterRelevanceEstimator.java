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
package org.sopeco.engine.analysis.util;

import java.util.List;

import org.sopeco.config.Configuration;
import org.sopeco.engine.analysis.IParameterInfluenceDescriptor;
import org.sopeco.engine.analysis.IParameterInfluenceResult;
import org.sopeco.persistence.dataset.AbstractDataSetColumn;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * This class is a utilization class that holds different algorithms to classify
 * a set of independent parameters in relevant and irrelevant with respect to
 * their influence on a dependent parameter. If a parameter is classified as
 * relevant it means that its influence on the dependent parameter is considered
 * as significant.
 * 
 * 
 * @author Dennis Westermann
 * @author Pascal Meier
 * 
 */
public final class ParameterRelevanceEstimator {

	/**
	 * It is an utility class, so use a private constructor.
	 */
	private ParameterRelevanceEstimator() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Threshold for the sum of the relative parameter influences that must be
	 * reached before all other parameter influences are classified as
	 * irrelevant.
	 */
	private static final String INFLUENCE_SUM_TH = "sopeco.config.engine.analysis.sumOfInfluenceValueThreshold";

	/**
	 * Threshold used to classify additional parameters as relevant if they are
	 * (nearly) as import as the last relevant parameter that was added by using
	 * the relativeInfluenceThreshold. This ensures to include relevant
	 * parameters that would be omitted just because the
	 * relativeInfluenceThreshold was reached a bit too early.
	 */
	private static final String STILL_CONSIDER_RELEVANT_TH = "sopeco.config.engine.analysis.stillConsiderRelevantThreshold";

	/**
	 * Interprets the result of a parameter influence analysis and classifies
	 * the independent parameters in relevant and irrelevant parameters
	 * according to the influence descriptors contained in the result.
	 * 
	 * 
	 * @param sessionId
	 *            session to be used to retrieve ParameterRelevanceEstimator
	 *            configuration settings
	 * @param parameterInfluenceAnalysisResult
	 *            result object derived by a parameter influence analysis
	 * 
	 * @return a result object that holds the list of relevant and irrelevant
	 *         parameters
	 */
	public static ParameterRelevanceResult interpretParameterInfluenceResult(String sessionId,
			IParameterInfluenceResult parameterInfluenceAnalysisResult) {

		List<IParameterInfluenceDescriptor> parameterInfluences = parameterInfluenceAnalysisResult
				.getAllParameterInfluenceDescriptors();

		ParameterDefinition dependentParameter = parameterInfluenceAnalysisResult.getAnalysisStrategyConfiguration()
				.getDependentParameters().get(0);

		ParameterRelevanceResult result = new ParameterRelevanceResult(dependentParameter);

		double totalSumOfInfluences = 0;
		double sumOfRelevantInfluences = 0;

		for (IParameterInfluenceDescriptor influenceDescriptor : parameterInfluences) {
			totalSumOfInfluences = totalSumOfInfluences + Math.abs(influenceDescriptor.getInfluenceValue());
		}

		int index = parameterInfluences.size() - 1;
		double lastRelevantParamInfluence = 0.0;
		while (sumOfRelevantInfluences < Double.parseDouble(Configuration.getSessionSingleton(sessionId)
				.getPropertyAsStr(INFLUENCE_SUM_TH)) * totalSumOfInfluences) {
			IParameterInfluenceDescriptor influenceDescriptor = parameterInfluences.get(index);
			index--;
			sumOfRelevantInfluences = sumOfRelevantInfluences + Math.abs(influenceDescriptor.getInfluenceValue());
			result.addRelevantParameter(influenceDescriptor.getIndependentParameter());
			lastRelevantParamInfluence = Math.abs(influenceDescriptor.getInfluenceValue()) / totalSumOfInfluences;
		}

		while (index >= 0) {

			IParameterInfluenceDescriptor influenceDescriptor = parameterInfluences.get(index);
			index--;

			if ((Math.abs(influenceDescriptor.getInfluenceValue()) / totalSumOfInfluences) >= (Double
					.parseDouble(Configuration.getSessionSingleton(sessionId).getPropertyAsStr(
							STILL_CONSIDER_RELEVANT_TH)) * lastRelevantParamInfluence)) {
				result.addRelevantParameter(influenceDescriptor.getIndependentParameter());
			} else {
				result.addRelevantParameter(influenceDescriptor.getIndependentParameter());
			}
		}

		return result;
	}

	/**
	 * This method compares the values of the dependent parameter and checks
	 * whether there is a significant difference between the minimal value and
	 * the maximal value. The distance is considered as significant if the
	 * difference between the min and the max value is greater than the result
	 * of minValue * relevanceFactor.
	 * 
	 * @param dataSet
	 *            the data set that contains the rows that should be compared
	 * 
	 * @param dependentParameter
	 *            the parameter for which the difference should be calculated
	 * @param relevanceFactor
	 *            indicator for the relevance. If the difference between the min
	 *            and the max value is greater than the result of minValue *
	 *            relevanceFactor, it is considered as significant.
	 * @return <code>true</code> if the values of the dependent parameter differ
	 *         significantly, otherwise <code>false</code>
	 */
	public static boolean isRelevantBasedOnRelativeFactor(DataSetAggregated dataSet,
			ParameterDefinition dependentParameter, double relevanceFactor) {

		// derive min and max value of the dependent parameter
		AbstractDataSetColumn<?> depParamCol = dataSet.getColumn(dependentParameter);
		double minValue = depParamCol.getMin();
		double maxValue = depParamCol.getMax();

		// calculate max distance
		double distance = maxValue - minValue;

		// determine relevance
		return distance >= (minValue * relevanceFactor);

	}

	/**
	 * This method compares the values of the dependent parameter and checks
	 * whether there is a significant difference between the minimal value and
	 * the maximal value. The difference is considered as significant if the
	 * difference between the min and the max value is greater than the given
	 * differenceThreshold.
	 * 
	 * @param dataSet
	 *            the data set that contains the rows that should be compared
	 * @param dependentParameter
	 *            the parameter for which the difference should be calculated
	 * @param differenceThreshold
	 *            indicator for the relevance. If the difference between the min
	 *            and the max value is greater than the differenceThreshold, it
	 *            is considered as significant.
	 * @return <code>true</code> if the values of the dependent parameter differ
	 *         significantly, otherwise <code>false</code>
	 */
	public static boolean isRelevantBasedOnAbsoluteDifference(DataSetAggregated dataSet,
			ParameterDefinition dependentParameter, double differenceThreshold) {

		// derive min and max value of the dependent parameter
		AbstractDataSetColumn<?> depParamCol = dataSet.getColumn(dependentParameter);
		double minValue = depParamCol.getMin();
		double maxValue = depParamCol.getMax();

		// calculate max distance
		double distance = maxValue - minValue;

		// determine relevance
		return distance >= differenceThreshold;
	}

}

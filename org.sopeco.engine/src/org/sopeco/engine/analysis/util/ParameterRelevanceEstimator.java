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
public class ParameterRelevanceEstimator {

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
	 * @param parameterInfluenceAnalysisResult
	 *            result object derived by a parameter influence analysis
	 * 
	 * @return a result object that holds the list of relevant and irrelevant
	 *         parameters
	 */
	public static ParameterRelevanceResult interpretParameterInfluenceResult(
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
		while (sumOfRelevantInfluences < Double.parseDouble(Configuration.getSingleton().getPropertyAsStr(
				INFLUENCE_SUM_TH))
				* totalSumOfInfluences) {
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
					.parseDouble(Configuration.getSingleton().getPropertyAsStr(STILL_CONSIDER_RELEVANT_TH)) * lastRelevantParamInfluence)) {
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
	 *            indicator for the relevance. If the difference between the
	 *            min and the max value is greater than the result of minValue *
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
		if (distance >= minValue * relevanceFactor) {
			return true;
		} else {
			return false;
		}

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
	 *            indicator for the relevance. If the difference between the
	 *            min and the max value is greater than the differenceThreshold,
	 *            it is considered as significant.
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
		if (distance >= differenceThreshold) {
			return true;
		} else {
			return false;
		}
	}

}

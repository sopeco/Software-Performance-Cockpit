package org.sopeco.engine.analysis.util;

import java.util.LinkedList;
import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * This class is a container class that holds the result of a parameter
 * relevance estimation (@see {@link ParameterRelevanceEstimator}).
 * 
 * @author Dennis Westermann
 * 
 */
public class ParameterRelevanceResult {

	ParameterDefinition dependentParameter;

	List<ParameterDefinition> relevantParameters;

	List<ParameterDefinition> irrelevantParameters;

	/**
	 * Adds a parameter to the list of relevant parameters in this result object.
	 * 
	 * @param relevantParameter
	 *            a parameter that is considered as relevant with
	 *            respect to its influence on the dependent parameter of this
	 *            result object
	 */
	public void addRelevantParameter(ParameterDefinition relevantParameter) {
		this.relevantParameters.add(relevantParameter);
	}

	/**
	 * Adds a parameter to the list of irrelevant parameters in this result object.
	 * 
	 * @param irrelevantParameter
	 *            a parameter that is considered as irrelevant with
	 *            respect to its influence on the dependent parameter of this
	 *            result object
	 */
	public void addIrrelevantParameter(ParameterDefinition irrelevantParameter) {
		this.irrelevantParameters.add(irrelevantParameter);
	}
	
	/**
	 * @param dependentParameter
	 *            the dependent parameter for which the independent parameters
	 *            are classified in relevant and irrelevant
	 */
	public ParameterRelevanceResult(ParameterDefinition dependentParameter) {
		relevantParameters = new LinkedList<ParameterDefinition>();
		irrelevantParameters = new LinkedList<ParameterDefinition>();
	}

	/**
	 * @return a list of parameter that are considered as relevant with respect
	 *         to their influence on the dependent parameter
	 */
	public List<ParameterDefinition> getRelevantParameters() {
		return relevantParameters;
	}

	/**
	 * @param relevantParameters
	 *            a list of parameters that are considered as relevant with
	 *            respect to their influence on the dependent parameter of this
	 *            result object
	 */
	public void setRelevantParameters(List<ParameterDefinition> relevantParameters) {
		this.relevantParameters = relevantParameters;
	}

	/**
	 * @return a list of parameter that are considered as irrelevant with respect
	 *         to their influence on the dependent parameter
	 */
	public List<ParameterDefinition> getIrrelevantParameters() {
		return irrelevantParameters;
	}

	/**
	 * @param relevantParameters
	 *            a list of parameters that are considered as irrelevant with
	 *            respect to their influence on the dependent parameter of this
	 *            result object
	 */
	public void setIrrelevantParameters(List<ParameterDefinition> irrelevantParameters) {
		this.irrelevantParameters = irrelevantParameters;
	}

}

package org.sopeco.engine.analysis;

import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Contains the result of a regression based analysis which holds the
 * coefficients that describe the influence of independent parameters on a
 * dependent parameter.
 * 
 * @author Dennis Westermann
 */
public interface IRegressionCoefficientResult extends IParameterInfluenceResult {

	/**
	 * Returns a list of all ParameterRegressionCoefficient-Objects describing
	 * the correlation of the parameters with the dependent parameter.
	 * 
	 * @return list of ParameterRegressionCoefficients
	 */
	List<ParameterRegressionCoefficient> getAllParameterRegressionCoefficients();

	/**
	 * Returns the ParameterCorrelation-Object of the specified parameter.
	 * 
	 * @param parameter
	 *            parameter for which the correlation should be retrievedF
	 * @return list of ParameterCorrelations
	 */
	ParameterCorrelation getParameterRegressionCoefficientByParam(ParameterDefinition parameter);

}

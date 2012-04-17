package org.sopeco.core.analysis;

import java.util.List;

/**
 * Contains the result of a correlation based analysis of the influence of
 * parameters on a dependent parameter.
 * 
 * @author Pascal Meier
 */
public interface ICorrelationResult extends IPredictionFunctionResult {

	/**
	 * Returns a list of all ParameterCorrelation-Objects describing the
	 * correlation of the parameters with the dependent parameter.
	 * 
	 * @return list of ParameterCorrelations
	 */
	public List<ParameterInfluence> getAllParameterCorrelations();

	/**
	 * Returns the ParameterCorrelation-Object of the specified parameter(s).
	 * 
	 * @return list of ParameterCorrelations
	 */
	public ParameterInfluence getParameterCorrelationByParam(
			ParameterUsage parameter);

}

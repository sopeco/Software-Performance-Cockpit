package org.sopeco.engine.analysis;

import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Contains the result of a correlation based analysis of the influence of
 * parameters on a dependent parameter.
 * 
 * @author Dennis Westermann, Pascal Meier
 */
public interface ICorrelationResult extends IParameterInfluenceResult {

	/**
	 * Returns a list of all ParameterCorrelation-Objects describing the
	 * correlation of the independent parameters with the dependent parameter.
	 * 
	 * @return list of ParameterCorrelations
	 */
	public List<ParameterCorrelation> getAllParameterCorrelations();

	/**
	 * Returns the ParameterCorrelation-Object of the specified parameter(s).
	 * 
	 * @param parameter
	 *            an independent parameter that has been part of the correlation
	 *            analysis.
	 * @return a {@link ParameterCorrelation} that describes the correlation
	 *         between the given independent parameter and the dependent
	 *         parameter used in the correlation analysis, <code>null</code> if
	 *         the given parameter is not in the list of independent parameters
	 *         of the correlation analysis that created the result.
	 */
	public ParameterCorrelation getParameterCorrelationByParam(ParameterDefinition parameter);

}

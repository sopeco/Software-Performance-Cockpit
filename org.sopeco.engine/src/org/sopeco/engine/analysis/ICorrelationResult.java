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
	 * correlation of the parameters with the dependent parameter.
	 * 
	 * @return list of ParameterCorrelations
	 */
	public List<ParameterCorrelation> getAllParameterCorrelations();

	/**
	 * Returns the ParameterCorrelation-Object of the specified parameter(s).
	 * 
	 * @return list of ParameterCorrelations
	 */
	public ParameterCorrelation getParameterCorrelationByParam(
			ParameterDefinition parameter);

}

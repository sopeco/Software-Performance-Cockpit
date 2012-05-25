package org.sopeco.engine.analysis;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Interface that encapsulates different metrics (e.g. a correlation value or
 * the the coefficients of a linear regression) all describing the influence of
 * an independent parameter to a dependent parameter.
 * 
 * @author Dennis Westermann
 * 
 */
public interface IParameterInfluenceDescriptor {

	/**
	 * @return the independent parameter definition
	 */
	public ParameterDefinition getIndependentParameter();

	/**
	 * @return the dependent parameter definition
	 */
	public ParameterDefinition getDependentParameter();

	/**
	 * @return the value that describes the influence of the independent
	 *         parameter on the dependent parameter
	 */
	double getInfluenceValue();

}

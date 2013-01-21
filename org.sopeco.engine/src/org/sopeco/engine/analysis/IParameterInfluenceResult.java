package org.sopeco.engine.analysis;

import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Interface that encapsulates all result objects that describe parameter
 * influences.
 * 
 * @author Dennis Westermann
 * 
 */
public interface IParameterInfluenceResult extends IAnalysisResult {

	/**
	 * Returns a list of all ParameterInfluence-Objects describing the influence
	 * of the independent parameters on the dependent parameter.
	 * 
	 * @return list of IParameterInfluenceDescriptor
	 */
	List<IParameterInfluenceDescriptor> getAllParameterInfluenceDescriptors();

	/**
	 * Returns the ParameterInfluenceDescriptor-Object of the specified
	 * parameter.
	 * 
	 * @param parameter
	 *            parameter for which the influence descriptor should be
	 *            returned
	 * @return a descriptor that describes the influence of the given
	 *         independent parameter on the dependent parameter of the analysis
	 */
	IParameterInfluenceDescriptor getParameterInfluenceDescriptorByParam(ParameterDefinition parameter);
}

package org.sopeco.engine.analysis;

/**
 * Interface for strategies that analyse the influence of a set of independent
 * parameters on a dependent parameter.
 * 
 * @author Dennis Westermann
 * 
 */
public interface IParameterInfluenceStrategy extends IAnalysisStrategy {

	/**
	 * Returns an influence result provided by this parameter influence
	 * strategy.
	 * 
	 * @return an influence result
	 */
	IParameterInfluenceResult getParameterInfluenceResult();
}

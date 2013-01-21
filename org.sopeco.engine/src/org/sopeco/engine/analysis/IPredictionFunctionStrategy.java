package org.sopeco.engine.analysis;

/**
 * Interface for analysis strategies that derive a prediction function during
 * the analysis.
 * 
 * @author Dennis Westermann
 * 
 */
public interface IPredictionFunctionStrategy extends IAnalysisStrategy {

	/**
	 * Returns a prediction function result provided by this prediction function
	 * strategy.
	 * 
	 * @return prediction function
	 */
	IPredictionFunctionResult getPredictionFunctionResult();

}

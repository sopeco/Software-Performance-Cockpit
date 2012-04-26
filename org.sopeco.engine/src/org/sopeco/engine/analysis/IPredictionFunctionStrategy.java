package org.sopeco.engine.analysis;


/**
 * Interface for analysis strategies that derive a prediction function during the analysis.
 * 
 * @author Dennis Westermann
 *
 */
public interface IPredictionFunctionStrategy extends IAnalysisStrategy {

	IPredictionFunctionResult getPredictionFunctionResult();
	
}

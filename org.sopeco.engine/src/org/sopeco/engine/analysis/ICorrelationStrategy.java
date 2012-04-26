package org.sopeco.engine.analysis;



/**
 * Interface for analysis strategies that identify correlations between parameters during the analysis.
 * 
 * @author Dennis Westermann
 *
 */
public interface ICorrelationStrategy extends IAnalysisStrategy {

	ICorrelationResult getCorrelationResult(); 
	
}

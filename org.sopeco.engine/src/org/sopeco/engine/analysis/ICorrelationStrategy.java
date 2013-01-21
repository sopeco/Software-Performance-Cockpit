package org.sopeco.engine.analysis;

/**
 * Interface for analysis strategies that identify correlations between
 * parameters during the analysis.
 * 
 * @author Dennis Westermann
 * 
 */
public interface ICorrelationStrategy extends IAnalysisStrategy {

	/**
	 * Returns a correlation result provided by this correlation strategy.
	 * 
	 * @return a correlation result
	 */
	ICorrelationResult getCorrelationResult();

}

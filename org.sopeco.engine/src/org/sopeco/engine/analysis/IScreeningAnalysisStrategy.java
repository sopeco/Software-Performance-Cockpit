package org.sopeco.engine.analysis;

/**
 * Interface for analysis strategies that interprets the results of a screening
 * design exploration strategy.
 * 
 * @author Dennis Westermann
 * 
 */
public interface IScreeningAnalysisStrategy extends IAnalysisStrategy {

	/**
	 * Returns a screening analysis result provided by this strategy.
	 * 
	 * @return screening analysis result
	 */
	IScreeningAnalysisResult getScreeningAnalysisResult();

}

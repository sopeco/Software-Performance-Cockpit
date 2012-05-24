package org.sopeco.engine.analysis;



/**
 * Interface for analysis strategies that interprets the results of a screening design exploration strategy.
 * 
 * @author Dennis Westermann
 *
 */
public interface IScreeningAnalysisStrategy extends IAnalysisStrategy {

	IScreeningAnalysisResult getScreeningAnalysisResult(); 
	
}

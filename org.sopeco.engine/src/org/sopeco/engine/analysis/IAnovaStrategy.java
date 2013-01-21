package org.sopeco.engine.analysis;

/**
 * Interface for analysis strategies that implement ANOVA tests. See
 * http://www.itl.nist.gov/div898/handbook/prc/section4/prc43.htm for details on
 * ANOVA.
 * 
 * @author Dennis Westermann
 * 
 */
public interface IAnovaStrategy extends IAnalysisStrategy {

	/**
	 * Returns an ANOVA result provided by this ANOVA strategy.
	 * @return ANOVA result.
	 */
	IAnovaResult getAnovaResult();

}

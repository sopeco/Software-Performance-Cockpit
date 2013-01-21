package org.sopeco.engine.analysis;

import org.sopeco.persistence.entities.analysis.IStorableAnalysisResult;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;

/**
 * This interface represents a result of any analysis strategy.
 * 
 * @author Dennis Westermann
 * 
 */
public interface IAnalysisResult extends IStorableAnalysisResult {

	/**
	 * Returns the configuration that was used to derive this result.
	 * 
	 * @return Configuration used for the analysis.
	 */
	AnalysisConfiguration getAnalysisStrategyConfiguration();
}

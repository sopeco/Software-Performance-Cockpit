package org.sopeco.engine.analysis;

import org.sopeco.persistence.entities.analysis.IStorableAnalysisResult;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;

public interface IAnalysisResult extends IStorableAnalysisResult {

	/**
	 * Returns the configuration that was used to derive this result.
	 * 
	 * @return Configuration used for the analysis.
	 */
	public AnalysisConfiguration getAnalysisStrategyConfiguration();
}

package org.sopeco.engine.analysis;

import org.sopeco.model.configuration.analysis.AnalysisConfiguration;
import org.sopeco.persistence.dataset.DataSetAggregated;

public interface ICorrelationStrategy extends IAnalysisStrategy {

	@Override
	public ICorrelationResult analyse(DataSetAggregated dataset, AnalysisConfiguration config);
	
	
}

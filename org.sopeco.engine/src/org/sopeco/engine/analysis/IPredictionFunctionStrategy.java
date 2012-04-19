package org.sopeco.engine.analysis;

import org.sopeco.model.configuration.analysis.AnalysisConfiguration;
import org.sopeco.persistence.dataset.DataSetAggregated;

public interface IPredictionFunctionStrategy extends IAnalysisStrategy {

	@Override
	public IPredictionFunctionResult analyse(DataSetAggregated dataset, AnalysisConfiguration config);
	
	
}

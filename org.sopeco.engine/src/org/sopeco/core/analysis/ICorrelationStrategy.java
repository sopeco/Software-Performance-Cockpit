package org.sopeco.core.analysis;

public interface ICorrelationStrategy extends IAnalysisStrategy {

	@Override
	public ICorrelationResult analyse(DataSetAggregated dataset, AnalysisConfiguration config);
	
	
}

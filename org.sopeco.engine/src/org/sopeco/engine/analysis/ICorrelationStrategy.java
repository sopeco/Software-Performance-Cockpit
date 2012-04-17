package org.sopeco.engine.analysis;

public interface ICorrelationStrategy extends IAnalysisStrategy {

	@Override
	public ICorrelationResult analyse(DataSetAggregated dataset, AnalysisConfiguration config);
	
	
}

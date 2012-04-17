package org.sopeco.engine.analysis;

public interface IPredictionFunctionStrategy extends IAnalysisStrategy {

	@Override
	public IPredictionFunctionResult analyse(DataSetAggregated dataset, AnalysisConfiguration config);
	
	
}

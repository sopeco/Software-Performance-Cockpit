package org.sopeco.core.analysis;

public interface IPredictionFunctionStrategy extends IAnalysisStrategy {

	@Override
	public IPredictionFunctionResult analyse(DataSetAggregated dataset, AnalysisConfiguration config);
	
	
}

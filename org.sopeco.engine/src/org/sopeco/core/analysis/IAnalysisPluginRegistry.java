package org.sopeco.core.analysis;

import java.util.List;

public interface IAnalysisPluginRegistry {

	List<IAnalysisStrategy> getAnalysisStrategies();
	
	List<IPredictionFunctionStrategy> getPredictionFunctionStrategies();
	
	List<ICorrelationStrategy> getCorrelationStrategies();

	List<IStrategy> getAnalysisStrategy(Class<IAnalysisStrategy> interface_);
	
	
}

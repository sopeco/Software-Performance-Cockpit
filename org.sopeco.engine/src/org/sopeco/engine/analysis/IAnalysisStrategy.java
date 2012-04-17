package org.sopeco.engine.analysis;

import org.sopeco.engine.registry.ISoPeCoExtension;


public interface IAnalysisStrategy extends ISoPeCoExtension {

	public IAnalysisResult analyse(DataSetAggregated dataset, AnalysisConfiguration config);

	boolean supports(AnalysisConfiguration config);
}

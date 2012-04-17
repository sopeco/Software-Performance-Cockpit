package org.sopeco.core.analysis;

import org.sopeco.core.registry.ISoPeCoExtension;


public interface IAnalysisStrategy extends ISoPeCoExtension {

	public IAnalysisResult analyse(DataSetAggregated dataset, AnalysisConfiguration config);

	boolean supports(AnalysisConfiguration config);
}

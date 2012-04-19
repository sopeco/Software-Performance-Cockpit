package org.sopeco.engine.analysis;

import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.model.configuration.analysis.AnalysisConfiguration;
import org.sopeco.persistence.dataset.DataSetAggregated;


public interface IAnalysisStrategy extends ISoPeCoExtension {

	public IAnalysisResult analyse(DataSetAggregated dataset, AnalysisConfiguration config);

	boolean supports(AnalysisConfiguration config);
}

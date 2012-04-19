package org.sopeco.engine.analysis;

import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;
import org.sopeco.model.configuration.analysis.AnalysisConfiguration;
import org.sopeco.persistence.dataset.DataSetAggregated;


public interface IAnalysisStrategy extends ISoPeCoExtensionArtifact {

	public IAnalysisResult analyse(DataSetAggregated dataset, AnalysisConfiguration config);

	boolean supports(AnalysisConfiguration config);
}

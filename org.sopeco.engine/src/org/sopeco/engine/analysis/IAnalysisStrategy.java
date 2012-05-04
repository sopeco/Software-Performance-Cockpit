package org.sopeco.engine.analysis;

import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;


public interface IAnalysisStrategy extends ISoPeCoExtensionArtifact {

	public void analyse(DataSetAggregated dataset, AnalysisConfiguration config);

	boolean supports(AnalysisConfiguration config);
}

package org.sopeco.engine.analysis;

import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;

/**
 * This interface has to implemented by any analysis strategy applied on a
 * result dataset.
 * 
 * @author Alexander Wert
 * 
 */
public interface IAnalysisStrategy extends ISoPeCoExtensionArtifact {

	/**
	 * Conducts the corresponding analysis implemented by the underlying class
	 * to the given <code>dataset</code> with the given analysis configuration
	 * <code>config</code>.
	 * 
	 * @param dataset
	 *            dataset on which the analysis should be executed
	 * @param config
	 *            configuration for the analysis strategy
	 */
	void analyse(DataSetAggregated dataset, AnalysisConfiguration config);

	/**
	 * Indicates whether the given analysis configuration is supported by this
	 * strategy.
	 * 
	 * @param config
	 *            analysis configuration to be checked
	 * @return <code>true</code>, if the given analysis configuration is
	 *         supported by this strategy, otherwise returns <code>false</code>
	 */
	boolean supports(AnalysisConfiguration config);
}

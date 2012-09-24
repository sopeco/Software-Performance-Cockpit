package org.sopeco.engine.experimentseries;

import java.util.List;

import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;

/**
 * Plugin interface for exploration strategies.
 * 
 * @author Dennis Westermann
 * 
 */
public interface IExplorationStrategy extends ISoPeCoExtensionArtifact {

	/**
	 * Indicates whether the given experiment series definition (
	 * {@link ExperimentSeriesDefinition}) is ready to be executed.
	 * 
	 * @param expSeries
	 *            experiment series definition (
	 *            {@link ExperimentSeriesDefinition}) to be checked
	 * @return true if the given experiment series definition fulfills all
	 *         requirements to be executed
	 */
	boolean canRun(ExperimentSeriesDefinition expSeries);

	/**
	 * Executes an experiment series.
	 * 
	 * @param expSeriesRun
	 *            experiment series run where to store results
	 * @param parameterVariations
	 *            list of parameter variations to be used for series execution
	 */
	void runExperimentSeries(ExperimentSeriesRun expSeriesRun, List<IParameterVariation> parameterVariations);

	/**
	 * 
	 * @param persistenceProvider
	 */
	void setPersistenceProvider(IPersistenceProvider persistenceProvider);

	void setExperimentController(IExperimentController experimentController);

	void setExtensionRegistry(IExtensionRegistry registry);

}

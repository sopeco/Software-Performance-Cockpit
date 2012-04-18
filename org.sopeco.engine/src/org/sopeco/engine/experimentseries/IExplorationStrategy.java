package org.sopeco.engine.experimentseries;

import java.util.List;

import org.sopeco.core.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.registry.ISoPeCoExtension;

/**
 * Plugin interface for exploration strategies.  
 * 
 * @author D053711
 *
 */
public interface IExplorationStrategy extends ISoPeCoExtension {

	public boolean canRun(ExperimentSeriesDefinition expSeries);
	
	public void runExperimentSeries(ExperimentSeriesDefinition expSeries, List<IParameterVariation> parameterVariation);
	
	public void setPersistenceProvider(IPersistenceProvider persistenceProvider);
	
	public void setAnalysisManager(IAnalysisManager analysisManager);
	
	public void setExperimentController(IExperimentController experimentController);
	
}

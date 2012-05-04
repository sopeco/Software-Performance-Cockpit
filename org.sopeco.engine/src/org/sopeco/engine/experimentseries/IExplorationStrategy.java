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

	public boolean canRun(ExperimentSeriesDefinition expSeries);
	
	public void runExperimentSeries(ExperimentSeriesRun expSeriesRun, List<IParameterVariation> parameterVariations);
	
	public void setPersistenceProvider(IPersistenceProvider persistenceProvider);
	
	public void setExperimentController(IExperimentController experimentController);
	
	public void setExtensionRegistry(IExtensionRegistry registry);

}

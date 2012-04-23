package org.sopeco.engine.experimentseries;

import java.util.List;

import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;
import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.entities.ExperimentSeries;

/**
 * Plugin interface for exploration strategies.  
 * 
 * @author D053711
 *
 */
public interface IExplorationStrategy extends ISoPeCoExtensionArtifact {

	public boolean canRun(ExperimentSeriesDefinition expSeries);
	
	public void runExperimentSeries(ExperimentSeries expSeries, List<IParameterVariation> parameterVariations);
	
	public void setPersistenceProvider(IPersistenceProvider persistenceProvider);
	
	public void setExperimentController(IExperimentController experimentController);
	
	public void setExtensionRegistry(IExtensionRegistry registry);

}

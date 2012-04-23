/**
 * 
 */
package org.sopeco.engine;

import org.sopeco.config.IConfiguration;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experimentseries.IExperimentSeriesManager;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.model.configuration.ScenarioDefinition;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.entities.ScenarioInstance;

/**
 * Interface of the SoPeCo Engine.
 * 
 * @author Roozbeh Farahbod
 *
 */
public interface IEngine {

	/**
	 * Returns the global configuration element.
	 */
	public IConfiguration getConfiguration();
	
	/**
	 * Returns the SoPeCo extension registry.
	 */
	public IExtensionRegistry getExtensionRegistry();

	// TODO comments and imps
	
	public IExperimentController getExperimentController();
	
	public IExperimentSeriesManager getExperimentSeriesManager();
	
	public IPersistenceProvider getPersistenceProvider();
	
	/**
	 * Runs the scenario; i.e., runs the measurements defined
	 * in the scenario against the target system. 
	 * 
	 * @param scenario a scenario definition
	 * @return a scenario instance
	 */
	public ScenarioInstance run(ScenarioDefinition scenario);
}

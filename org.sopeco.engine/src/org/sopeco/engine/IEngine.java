/**
 * 
 */
package org.sopeco.engine;

import org.sopeco.config.IConfiguration;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experimentseries.IExperimentSeriesManager;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.util.session.ISessionAwareObject;

/**
 * Interface of the SoPeCo Engine.
 * 
 * @author Roozbeh Farahbod
 * 
 */
public interface IEngine extends ISessionAwareObject {

	/**
	 * Returns the configuration element used for the session this engine
	 * belongs to.
	 * 
	 * @return the configuration element used for the session this engine
	 *         belongs to.
	 */
	IConfiguration getConfiguration();

	/**
	 * Returns the SoPeCo extension registry.
	 * 
	 * @return the SoPeCo extension registry.
	 */
	IExtensionRegistry getExtensionRegistry();

	// TODO comments and imps

	/**
	 * Returns the experiment controller used by this engine.
	 * 
	 * @return the experiment controller used by this engine.
	 */
	IExperimentController getExperimentController();

	/**
	 * Returns the experiment series manager used by this engine.
	 * 
	 * @return the experiment series manager used by this engine.
	 */
	IExperimentSeriesManager getExperimentSeriesManager();

	/**
	 * Returns the persistence provider used for the session this engine belongs
	 * to.
	 * 
	 * @return the persistence provider used for the session this engine belongs
	 *         to.
	 */
	IPersistenceProvider getPersistenceProvider();

	/**
	 * Runs the scenario; i.e., runs the measurements defined in the scenario
	 * against the target system.
	 * 
	 * @param scenario
	 *            a scenario definition
	 * @return a scenario instance
	 */
	ScenarioInstance run(ScenarioDefinition scenario);
}

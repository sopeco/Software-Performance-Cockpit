/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * 
 */
package org.sopeco.engine;

import java.util.Collection;

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
	ScenarioInstance run(ScenarioDefinition scenarioDefinition);
	
	/**
	 * Triggers abort of the currently running experiment run.
	 */
	void abortExperimentRun();
	
	/**
	 * Runs the scenario; i.e., runs the measurements defined in the scenario
	 * against the target system.
	 * 
	 * @param scenario
	 *            a scenario definition
	 * @return a scenario instance
	 */
	ScenarioInstance run(ScenarioDefinition scenarioDefinition, Collection<String> experimentSeries);
}

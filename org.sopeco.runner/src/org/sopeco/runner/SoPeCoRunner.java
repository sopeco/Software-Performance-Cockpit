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
package org.sopeco.runner;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.config.exception.ConfigurationException;
import org.sopeco.engine.EngineFactory;
import org.sopeco.engine.IEngine;
import org.sopeco.engine.model.ScenarioDefinitionReader;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.util.Tools;
import org.sopeco.util.session.SessionAwareObject;

/**
 * The main runner class of SoPeCo.
 * 
 * @author Roozbeh Farahbod
 * 
 */
public class SoPeCoRunner extends SessionAwareObject implements Runnable {

	/** */
	private static final long serialVersionUID = 1L;

	protected static final Logger LOGGER = LoggerFactory.getLogger(SoPeCoRunner.class);

	protected String[] args = null;

	/** Holds the last executed scenario instance. */
	protected ScenarioInstance lastExecutedScenarioInstance = null;

	/**
	 * Holds the last instance of SoPeCo lastEngine that was used by this
	 * runner.
	 */
	private IEngine lastEngine = null;

	private Map<String, Object> executionProperties = null;

	private Collection<String> experimentSeriesNames = null;

	/**
	 * Use this constructor to provide a session id.
	 * 
	 * @param sessionId
	 *            session id to be used for retrieving configuration properties
	 *            for this runner and all it triggered subprocesses.
	 */
	public SoPeCoRunner(String sessionId) {
		this(sessionId, new HashMap<String, Object>());
	}

	/**
	 * Use this constructor to provide a session id and a properties map,
	 * required for experiment execution.
	 * 
	 * @param sessionId
	 *            session id to be used for retrieving configuration properties
	 *            for this runner and all it triggered subprocesses.
	 * 
	 * @param executionProperties
	 *            A map containing properties objects specifying experiment
	 *            execution and used configurations
	 */
	public SoPeCoRunner(String sessionId, Map<String, Object> executionProperties) {
		super(sessionId);
		this.executionProperties = executionProperties;
		if (this.executionProperties == null) {
			throw new IllegalArgumentException("executionProperties must not be null!");
		}
	}

	/**
	 * Use this constructor to provide a session id and a properties map,
	 * required for experiment execution. Additionally you can specify which
	 * experiments are executed.
	 * 
	 * @param sessionId
	 *            session id to be used for retrieving configuration properties
	 *            for this runner and all it triggered subprocesses.
	 * 
	 * @param executionProperties
	 *            A map containing properties objects specifying experiment
	 *            execution and used configurations
	 * 
	 * @param experimentSeriesNames
	 *            A collection of string containing the names of the experiments
	 *            which should be executed
	 */
	public SoPeCoRunner(String sessionId, Map<String, Object> executionProperties,
			Collection<String> experimentSeriesNames) {
		this(sessionId, executionProperties);
		this.experimentSeriesNames = experimentSeriesNames;
		if (this.experimentSeriesNames == null || experimentSeriesNames.isEmpty()) {
			throw new IllegalArgumentException("experimentSeriesNames must not be null or empty!");
		}
	}

	/**
	 * 
	 * Assuming that the required command-line arguments (if any) are set
	 * already, using {@link SoPeCoRunner#setArguments(String[]).
	 */
	@Override
	public synchronized void run() {
		lastExecutedScenarioInstance = null;
		IConfiguration config = Configuration.getSessionSingleton(getSessionId());
		config.overwrite(executionProperties);
		// if the user has set any command-line arguments
		if (args != null) {
			try {
				config.processCommandLineArguments(args);
			} catch (ConfigurationException e) {
				return;
			}
		}

		ScenarioDefinition scenario = retrieveScenarioDefinition(config);

		lastEngine = EngineFactory.getInstance().createEngine(getSessionId());

		if (experimentSeriesNames == null || experimentSeriesNames.isEmpty()) {
			lastExecutedScenarioInstance = lastEngine.run(scenario);
		} else {
			lastExecutedScenarioInstance = lastEngine.run(scenario, experimentSeriesNames);
		}

		executionProperties = null;
		LOGGER.info("SoPeCo run finished.");

	}

	private ScenarioDefinition retrieveScenarioDefinition(IConfiguration config) {
		ScenarioDefinition scenario = null;

		Object scenarioObj = config.getScenarioDescription();
		if (scenarioObj == null) {
			String fileName = config.getScenarioDescriptionFileName();

			// if the scenario definition is not set as an object,
			// then a filename should have been given
			if (fileName == null) {
				throw new RuntimeException(new ConfigurationException("Scenario definition is not provided."));
			}

			if (!Tools.isAbsolutePath(fileName)) {
				fileName = Tools.concatFileName(config.getAppRootDirectory(), fileName);
			}
			MeasurementEnvironmentDefinition meDefinition;
			try {
				meDefinition = EngineFactory.getInstance().retrieveMEController(getSessionId()).getMEDefinition();
			} catch (RemoteException e) {
				throw new RuntimeException(e);
			}
			ScenarioDefinitionReader scenarioReader = new ScenarioDefinitionReader(meDefinition, getSessionId());
			scenario = scenarioReader.readFromFile(fileName);
			LOGGER.debug("Scenario definition file loaded.");

		} else {
			if (scenarioObj instanceof ScenarioDefinition) {
				scenario = (ScenarioDefinition) scenarioObj;
			} else {
				final String msg = "Scenario definition object is not of class " + ScenarioDefinition.class.getName()
						+ ".";
				throw new RuntimeException(new ConfigurationException(msg));
			}

			LOGGER.debug("Scenario definition is passed as an object.");
		}
		return scenario;
	}

	/**
	 * Removes the experiment series runs of the previously run scenario. If
	 * this method is called before any "run", it does nothing.
	 */
	public void deleteLastExperimentSeriesRuns() {
		LOGGER.debug("Deleting last experiment series runs...");

		if (lastExecutedScenarioInstance != null) {
			for (ExperimentSeries es : lastExecutedScenarioInstance.getExperimentSeriesList()) {
				ExperimentSeriesRun esr = es.getLatestExperimentSeriesRun();

				if (esr != null) {
					try {
						lastEngine.getPersistenceProvider().remove(esr);
					} catch (DataNotFoundException e) {
						LOGGER.warn("Cannot remove experiment series run '{}'. Experiment series run not found.",
								esr.getLabel());
					}
				}
			}

			LOGGER.debug("Deleting last experiment series runs... done.");
		} else {
			LOGGER.warn("Deleting last experiment series runs... Nothing to do.");
		}
	}

	/**
	 * Sets the command-line arguments before running SoPeCo.
	 * 
	 * @param args
	 *            arguments for SoPeCo execution
	 */
	public void setArguments(String[] args) {
		this.args = args;
	}

	/**
	 * Returns the result of the last run of SoPeCo. If there was no successful
	 * run, this method returns <code>null</code>.
	 * 
	 * @return result of last run, or <code>null</code> if there was no
	 *         successful run.
	 */
	public ScenarioInstance getScenarioInstance() {
		return lastExecutedScenarioInstance;
	}
}

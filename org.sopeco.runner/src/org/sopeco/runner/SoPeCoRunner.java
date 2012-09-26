/**
 * 
 */
package org.sopeco.runner;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.config.exception.ConfigurationException;
import org.sopeco.engine.EngineFactory;
import org.sopeco.engine.IEngine;
import org.sopeco.model.util.EMFUtil;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.util.Tools;

/**
 * The main runner class of SoPeCo.
 * 
 * @author Roozbeh Farahbod
 * 
 */
public class SoPeCoRunner implements Runnable {

	protected static final Logger logger = LoggerFactory.getLogger(SoPeCoRunner.class);

	protected String[] args = null;

	protected ScenarioInstance lastExecutedScenarioInstance = null;

	private String sessionId;

	/**
	 * Use this constructor with caution! This constructor creates an own
	 * session id. Session related configurations made outside will not be
	 * propagated to the created SoPeCoRunner object, as the created object uses
	 * an own configuration with an own session id.
	 */
	public SoPeCoRunner() {
		this.sessionId = UUID.randomUUID().toString();
	}

	/**
	 * Use this constructor to provide a session id.
	 * 
	 * @param sessionId
	 *            session id to be used for retrieving configuration properties
	 *            for this runner and all it triggered subprocesses.
	 */
	public SoPeCoRunner(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * Runs SoPeCo with the given command-line arguments.
	 * 
	 * @param args
	 *            command-line arguments
	 * 
	 * @see #setArguments(String[])
	 * @see #run()
	 */
	public static void main(String[] args) {
		final SoPeCoRunner runner = new SoPeCoRunner();

		runner.setArguments(args);
		runner.run();
	}

	/**
	 * 
	 * Assuming that the required command-line arguments (if any) are set
	 * already, using {@link SoPeCoRunner#setArguments(String[]).
	 */
	@Override
	public synchronized void run() {
		lastExecutedScenarioInstance = null;
		IConfiguration config = Configuration.getSessionSingleton(sessionId);

		// if the user has set any command-line arguments
		if (args != null) {
			try {
				config.processCommandLineArguments(args);
			} catch (ConfigurationException e) {
				return;
			}
		}

		ScenarioDefinition scenario = null;

		Object scenarioObj = config.getScenarioDescription();
		if (scenarioObj == null) {
			String fileName = config.getScenarioDescriptionFileName();

			// if the scenario definition is not set as an object,
			// then a filename should have been given
			if (fileName == null) {
				throw new RuntimeException(new ConfigurationException("Scenario definition is not provided."));
			}

			try {
				if (!Tools.isAbsolutePath(fileName)) {
					fileName = Tools.concatFileName(config.getAppRootDirectory(), fileName);
				}
				scenario = (ScenarioDefinition) EMFUtil.loadFromFilePath(fileName);

				logger.debug("Scenario definition file loaded.");

			} catch (IOException e) {
				logger.error("Cannot load scenario definition file ({}). Reason: ({}) {}", fileName, e.getMessage());
				logger.debug("IO Exception occured.", e);
				return;
			}
		} else {
			if (scenarioObj instanceof ScenarioDefinition)
				scenario = (ScenarioDefinition) scenarioObj;
			else {
				final String msg = "Scenario definition object is not of class " + ScenarioDefinition.class.getName()
						+ ".";
				// TODO do it properly
				throw new RuntimeException(new ConfigurationException(msg));
			}

			logger.debug("Scenario definition is passed as an object.");
		}

		IEngine engine = EngineFactory.getInstance().createEngine(sessionId);

		lastExecutedScenarioInstance = engine.run(scenario);

		logger.info("SoPeCo run finished.");
	}

	/**
	 * Sets the command-line arguments before running SoPeCo.
	 * 
	 * @param args
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

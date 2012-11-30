/**
 * 
 */
package org.sopeco.runner;

import java.rmi.RemoteException;

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

/**
 * The main runner class of SoPeCo.
 * 
 * @author Roozbeh Farahbod
 * 
 */
public class SoPeCoRunner implements Runnable {

	protected static final Logger logger = LoggerFactory.getLogger(SoPeCoRunner.class);

	protected String[] args = null;

	/** Holds the last executed scenario instance. */
	protected ScenarioInstance lastExecutedScenarioInstance = null;

	/** Holds the last instance of SoPeCo lastEngine that was used by this runner. */
	private IEngine lastEngine = null;
	
	private String sessionId;

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

		ScenarioDefinition scenario = retrieveScenarioDefinition(config);

		lastEngine = EngineFactory.getInstance().createEngine(sessionId);

		lastExecutedScenarioInstance = lastEngine.run(scenario);

		logger.info("SoPeCo run finished.");
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
				meDefinition = EngineFactory.getInstance().retrieveMEController(sessionId).getMEDefinition();
			} catch (RemoteException e) {
				throw new RuntimeException(e);
			}
			ScenarioDefinitionReader scenarioReader = new ScenarioDefinitionReader(meDefinition);
			scenario = scenarioReader.readFromFile(fileName);
			logger.debug("Scenario definition file loaded.");

		} else {
			if (scenarioObj instanceof ScenarioDefinition)
				scenario = (ScenarioDefinition) scenarioObj;
			else {
				final String msg = "Scenario definition object is not of class " + ScenarioDefinition.class.getName()
						+ ".";
				throw new RuntimeException(new ConfigurationException(msg));
			}

			logger.debug("Scenario definition is passed as an object.");
		}
		return scenario;
	}

	/**
	 * Removes the experiment series runs of the previously run scenario. 
	 * If this method is called before any "run", it does nothing.
	 */
	public void deleteLastExperimentSeriesRuns() {
		logger.debug("Deleting last experiment series runs...");

		if (lastExecutedScenarioInstance != null) {
			for (ExperimentSeries es: lastExecutedScenarioInstance.getExperimentSeriesList()) {
				ExperimentSeriesRun esr = es.getLatestExperimentSeriesRun();
				
				if (esr != null) {
					try {
						lastEngine.getPersistenceProvider().remove(esr);
					} catch (DataNotFoundException e) {
						logger.warn("Cannot remove experiment series run '{}'. Experiment series run not found.", esr.getLabel());
					}
				}
			}

			logger.debug("Deleting last experiment series runs... done.");
		} else {
			logger.warn("Deleting last experiment series runs... Nothing to do.");
		}
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

/**
 * 
 */
package org.sopeco.core;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.config.exception.ConfigurationException;
import org.sopeco.engine.EngineFactory;
import org.sopeco.engine.IEngine;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * The main runner class of SoPeCo. 
 * 
 * @author Roozbeh Farahbod
 *
 */
public class SoPeCoRunner implements Runnable {

	protected static final Logger logger = LoggerFactory.getLogger(SoPeCoRunner.class);
	
	protected String[] args = new String[] {};
	protected ScenarioInstance lastExecutedScenarioInstance = null;
	
	/**
	 * Runs SoPeCo with the given command-line arguments. 
	 * 
	 * @param args command-line arguments
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
	 * Assuming that the required parameters are set in 
	 */
	@Override
	public void run() {
		lastExecutedScenarioInstance = null;
		IConfiguration config = Configuration.getSingleton();
		
		try {
			config.processCommandLineArguments(args);
		} catch (ConfigurationException e) {
			return;
		}
		
		ScenarioDefinition scenario = null;
		final String fileName = config.getProperty(IConfiguration.CONF_SCENARIO_DESCRIPTION_FILE_NAME).toString();
		try {
			scenario = (ScenarioDefinition) EMFUtil.loadFromFilePath(fileName);
			
			logger.debug("Scenario definition file loaded.");
			
		} catch (IOException e) {
			logger.error("Cannot load scenario definition file ({}). Reason: ({}) {}", fileName, e.getMessage());
			logger.debug("IO Exception occured.", e);
			return;
		}
		
		IEngine engine = EngineFactory.INSTANCE.createEngine();
		
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
	 * Returns the result of the last run of SoPeCo. 
	 * If there was no successful run, this method returns <code>null</code>.
	 * 
	 * @return result of last run, or <code>null</code> if there was no successful run.
	 */
	public ScenarioInstance getScenarioInstance() {
		return lastExecutedScenarioInstance;
	}
}


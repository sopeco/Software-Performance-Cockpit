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
import org.sopeco.persistence.util.EMFUtil;

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
	 * Assuming that the required command-line arguments (if any) are set already, using {@link SoPeCoRunner#setArguments(String[]). 
	 */
	@Override
	public void run() {
		lastExecutedScenarioInstance = null;
		IConfiguration config = Configuration.getSingleton();
	
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
			final String fileName = config.getScenarioDescriptionFileName();
			
			// if the scenario definition is not set as an object,
			// then a filename should have been given
			if (fileName == null) {
				throw new RuntimeException(new ConfigurationException("Scenario definition is not provided."));
			}
			
			try {
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
				final String msg = "Scenario definition object is not of class " + ScenarioDefinition.class.getName() + ".";
				// TODO do it properly
				throw new RuntimeException(new ConfigurationException(msg));
			}
			
			logger.debug("Scenario definition is passed as an object.");
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


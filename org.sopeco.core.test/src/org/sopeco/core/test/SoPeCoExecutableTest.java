/**
 * 
 */
package org.sopeco.core.test;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.engine.EngineFactory;
import org.sopeco.engine.IEngine;
import org.sopeco.model.configuration.ScenarioDefinition;
import org.sopeco.model.util.EMFUtil;

/**
 * This is a test for running SoPeCo as a simple Java application.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class SoPeCoExecutableTest {

	private static final Logger logger = LoggerFactory.getLogger(SoPeCoExecutableTest.class);
	
	public static void main(String[] args) {
		IConfiguration config = Configuration.getSingleton();
		
		String[] testArgs = new String[] {
//				"rsc/test.configuration"
				"-meClass", "org.sopeco.engine.helper.DummyMEController",
				"-sd", "rsc/test.configuration"
		};
		
		config.processCommandLineArguments(testArgs);
		
		ScenarioDefinition scenario = null;
		try {
			scenario = (ScenarioDefinition) EMFUtil.loadFromFilePath(config.getProperty(IConfiguration.CONF_SCENARIO_DESCRIPTION_FILE_NAME).toString());
			
			logger.debug("Scenario definition file loaded.");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IEngine engine = EngineFactory.INSTANCE.createEngine();
		
//		ScenarioInstance scenarioInstance = engine.run(scenario);
//		
//		System.out.println(scenarioInstance.getName());
		
	}

}

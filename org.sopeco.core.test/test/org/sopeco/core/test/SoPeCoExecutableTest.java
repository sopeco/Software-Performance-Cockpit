/**
 * 
 */
package org.sopeco.core.test;

import java.net.URI;
import java.net.URISyntaxException;

import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.engine.EngineFactory;
import org.sopeco.engine.IEngine;
import org.sopeco.model.configuration.ScenarioDefinition;
import org.sopeco.model.util.EMFUtil;
import org.sopeco.persistence.entities.ScenarioInstance;

/**
 * This is a test for running SoPeCo as a simple Java application.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class SoPeCoExecutableTest {

	public static void main(String[] args) {
		IConfiguration config = Configuration.getSingleton();
		
		String[] testArgs = new String[] {
				"rsc/test.configuration"
		};
		
		config.processCommandLineArguments(testArgs);

		ScenarioDefinition scenario = null;
		try {
			scenario = EMFUtil.loadFromURI(new URI("file://" + config.getProperty(IConfiguration.CONF_MEASUREMENT_SPEC_FILE_NAME)));
		} catch (URISyntaxException e) {
			// TODO proper exception handling
			e.printStackTrace();
		}
		
		IEngine engine = EngineFactory.INSTANCE.createEngine();
		
		ScenarioInstance scenarioInstance = engine.run(scenario);
		
		System.out.println(scenarioInstance.getName());
		
	}

}

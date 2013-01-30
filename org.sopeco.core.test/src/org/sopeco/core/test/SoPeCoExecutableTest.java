/**
 * 
 */
package org.sopeco.core.test;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.config.exception.ConfigurationException;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetInputColumn;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.runner.SoPeCoRunner;

/**
 * This is a test for running SoPeCo as a simple Java application.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class SoPeCoExecutableTest {

	private static final Logger logger = LoggerFactory.getLogger(SoPeCoExecutableTest.class);
	
	public static void main(String[] args) {
		run();
	}

	/* 
	 * Test one run of SoPeCo
	 */
	public static void run() {
		
		IConfiguration config = Configuration.getSingleton(SoPeCoExecutableTest.class);

		try {
			config.loadDefaultConfiguration("rsc" + File.separator + "sopeco-test.conf");
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("Application file name is: " + config.getApplicationName());
		
		// setting the MEController class name
		config.setMeasurementControllerClassName("org.sopeco.core.test.SampleMEController");
		
		// setting the scenario definition file name
		config.setScenarioDescriptionFileName("rsc" + File.separator + "test.configuration");

		SoPeCoRunner runner = new SoPeCoRunner();
		runner.run();
		
		ScenarioInstance si = runner.getScenarioInstance();
		
		DataSetAggregated data = si.getExperimentSeriesList().get(0).getExperimentSeriesRuns().get(0).getSuccessfulResultDataSet();
		
		logger.debug("Logging the result of the first experiement series run:");
		
		for (DataSetInputColumn<?> ic: data.getInputColumns()) {
			logger.debug("{}: {}", ic.getParameter().getName(), ic.getValueList());
		}

	}
	
}

/*
 * If you would like to mimic the command-line call of the runner,
 * you would use the following code:
 *
 
	String[] arguments = new String[] {
			"-meClass", "org.sopeco.core.test.SampleMEController",
			"-sd", "rsc/test.configuration"
	};
	
	SoPeCoRunner runner = new SoPeCoRunner();
	runner.setArguments(arguments);
	runner.run();
/**/


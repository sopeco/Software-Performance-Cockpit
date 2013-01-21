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
package org.sopeco.engine;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.config.exception.ConfigurationException;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experiment.impl.ExperimentController;
import org.sopeco.engine.experimentseries.IExperimentSeriesManager;
import org.sopeco.engine.experimentseries.impl.ExperimentSeriesManager;
import org.sopeco.engine.imp.EngineImp;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.connector.MEConnectorFactory;

/**
 * The factory is responsible for creating a valid instance of the SoPeCo
 * engine.
 * 
 * @author Dennis Westermann
 * @author Roozbeh Farahbod
 * @author Alexander Wert
 */
public final class EngineFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(EngineFactory.class);

	private static EngineFactory instance;

	private static final String DEFAULT_ENGINE_CONFIG_FILE_NAME = "sopeco-engine-defaults.conf";

	/**
	 * Private constructor for singleton.
	 */
	private EngineFactory() {

	}

	/**
	 * Creates an instance for {@link IEngine} using the given sessionId for all
	 * its session-aware objects .
	 * 
	 * @param sessionId
	 *            Session id to be used for the new engine and all its
	 *            session-aware elements.
	 * @return Returns the newly created engine instance.
	 * 
	 * 
	 */
	public IEngine createEngine(String sessionId) {
		final IConfiguration config = Configuration.getSessionSingleton(sessionId);

		loadDefaultConfigValues(sessionId);

		IExperimentController experimentController;
		String meClassName = config.getMeasurementControllerClassName();
		IMeasurementEnvironmentController meController = retrieveMEController(sessionId);

		experimentController = createExperimentController(sessionId, meController);

		LOGGER.debug("Experiment controller is created.");

		IExperimentSeriesManager expSeriesManager;
		expSeriesManager = new ExperimentSeriesManager();

		IEngine engine = new EngineImp(sessionId, experimentController, expSeriesManager);
		((ExperimentSeriesManager) expSeriesManager).setEngine(engine);
		return engine;
	}

	public IMeasurementEnvironmentController retrieveMEController(String sessionId) {
		final IConfiguration config = Configuration.getSessionSingleton(sessionId);

		String meClassName = config.getMeasurementControllerClassName();
		IMeasurementEnvironmentController meController = null;
		// if the measurement environment class is not set
		if (meClassName == null) {
			
			final URI meURI = config.getMeasurementControllerURI();
			meController = MEConnectorFactory.connectTo(meURI);

			LOGGER.debug("Connected to the measurement environment controller service.");
		} else {
			// load the given class
			try {
				Class<?> mec = Class.forName(meClassName);
				Object o = mec.newInstance();
				if (o instanceof IMeasurementEnvironmentController) {
					LOGGER.debug("Measurement environment controller is instantiated.");
					meController = (IMeasurementEnvironmentController) o;
				} else {
					throw new RuntimeException("The measurement environment class must implement "
							+ IMeasurementEnvironmentController.class.getName() + ".");
				}
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Cannot load the measurement environment class (" + meClassName + ").");
			} catch (InstantiationException e) {
				throw new RuntimeException("Cannot instantiate the measurement environment object. Error: "
						+ e.getMessage());
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Cannot instantiate the measurement environment object. Error: "
						+ e.getMessage());
			}
		}
		return meController;
	}

	/**
	 * Loads the configuration defaults from the specified path into the SoPeCo
	 * configuration.
	 */
	private void loadDefaultConfigValues(String sessionId) {
		try {
			Configuration.getSessionSingleton(sessionId).loadDefaultConfiguration(this.getClass().getClassLoader(),
					DEFAULT_ENGINE_CONFIG_FILE_NAME);
		} catch (ConfigurationException e) {
			LOGGER.error("Unable to read default config.");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates an experiment controller based on the given measurement
	 * environment controller.
	 * 
	 * @param meController
	 * @return an instance of experiment controller
	 */
	protected IExperimentController createExperimentController(String sessionId,
			IMeasurementEnvironmentController meController) {
		ExperimentController expController = new ExperimentController(sessionId);
		expController.setMeasurementEnvironmentController(meController);
		return expController;
	}

	/**
	 * Returns the singleton instance of the factory.
	 * 
	 * @return Returns the singleton instance of the factory.
	 */
	public static EngineFactory getInstance() {
		if (instance == null) {
			instance = new EngineFactory();
		}
		return instance;
	}

}

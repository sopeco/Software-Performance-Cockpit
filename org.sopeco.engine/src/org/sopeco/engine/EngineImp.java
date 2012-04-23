/**
 * 
 */
package org.sopeco.engine;

import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experimentseries.IExperimentSeriesManager;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.model.configuration.ScenarioDefinition;
import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.exceptions.DataNotFoundException;

/**
 * The default implementation of SoPeCo engine.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class EngineImp implements IEngine {

	private IConfiguration configuration = null; 
	
	private IExtensionRegistry registry = null; 

	final private IExperimentController experimentController;
	final private IExperimentSeriesManager experimentSeriesManager;
	final private IPersistenceProvider persistenceProvider;
	
	public EngineImp(IExperimentController experimentController, IExperimentSeriesManager experimentSeriesManager, IPersistenceProvider persistenceProvider) {
		super();
		this.experimentController = experimentController;
		this.experimentSeriesManager = experimentSeriesManager;
		this.persistenceProvider = persistenceProvider;
	}

	@Override
	public IConfiguration getConfiguration() {
		if (configuration == null)
			configuration = Configuration.getSingleton();
		return configuration;
	}

	@Override
	public IExtensionRegistry getExtensionRegistry() {
		if (registry == null)
			registry = ExtensionRegistry.getSingleton();
		return registry;
	}

	@Override
	public ScenarioInstance run(ScenarioDefinition scenario) {
		
		ScenarioInstance scenarioInstance = new ScenarioInstance();
		
		// TODO set the URL
		//instance.setMeasurementEnvironmentUrl()
		
		scenarioInstance.setName(scenario.getName());
		
		persistenceProvider.store(scenarioInstance);
		
		// TODO do the exp controller init
//		experimentController.initialize(scenario.getMeasurementSpecification().getInitializationAssignemts());
		
		// loop over all the experiment series in the spec
		for (ExperimentSeriesDefinition esd: scenario.getMeasurementSpecification().getExperimentSeriesDefinitions()) {
			if (experimentSeriesManager.canRun(esd)) {
				ExperimentSeries series = new ExperimentSeries();
				series.setExperimentSeriesDefinition(esd);
				//TODO get it from the def
				series.setName(esd.getName());
				series.setScenarioInstance(scenarioInstance);
				
				persistenceProvider.store(series);
				
				scenarioInstance.getExperimentSeries().add(series);
				
				experimentSeriesManager.runExperimentSeries(series);
			} else {
				// TODO logger message
			}
		}
		
		try {
			ScenarioInstance loadedScenario = persistenceProvider.loadScenarioInstance(scenarioInstance.getPrimaryKey());
			return loadedScenario;
		} catch (DataNotFoundException e) {
			// TODO logger message
			throw new RuntimeException("Something went wrong");
		}
	}

	@Override
	public IExperimentController getExperimentController() {
		return experimentController;
	}

	@Override
	public IExperimentSeriesManager getExperimentSeriesManager() {
		return experimentSeriesManager;
	}

	@Override
	public IPersistenceProvider getPersistenceProvider() {
		return persistenceProvider;
	}

}

/**
 * 
 */
package org.sopeco.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experimentseries.IConstantAssignment;
import org.sopeco.engine.experimentseries.IConstantAssignmentExtension;
import org.sopeco.engine.experimentseries.IExperimentSeriesManager;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.model.configuration.ScenarioDefinition;
import org.sopeco.model.configuration.measurements.ConstantValueAssignment;
import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.dataset.ParameterValue;
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

	private static final Logger logger = LoggerFactory.getLogger(EngineImp.class);
	
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
		
		scenarioInstance.setMeasurementEnvironmentUrl(getConfiguration().getProperty(IConfiguration.CONF_MEASUREMENT_CONTROLLER_URI).toString());
		
		scenarioInstance.setName(scenario.getName());
		
		persistenceProvider.store(scenarioInstance);
		
		experimentController.initialize(getParameterValues(
				scenario.getMeasurementSpecification().getInitializationAssignemts()));
		
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
				// TODO throw proper runtime error
			}
		}
		
		try {
			ScenarioInstance loadedScenario = persistenceProvider.loadScenarioInstance(scenarioInstance.getPrimaryKey());
			return loadedScenario;
		} catch (DataNotFoundException e) {
			logger.error("Cannot load the scenario from the persistnce provider. Something is seriously gone wrong.");
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

	// TODO put the following method in its proper place!
	/**
	 * Returns a list of parameter value assignments based on the given constant value assignments.
	 * 
	 * @param constantValueAssignments
	 * @return
	 */
	public List<ParameterValue<?>> getParameterValues(Collection<ConstantValueAssignment> constantValueAssignments) {
		ArrayList<ParameterValue<?>> result = new ArrayList<ParameterValue<?>>();
		
		for (ConstantValueAssignment cva: constantValueAssignments) {
			IConstantAssignment ca = registry.getExtensionArtifact(IConstantAssignmentExtension.class, cva.getParameter().getType());
			if (ca.canAssign(cva))
				result.add(ca.createParameterValue(cva));
			else {
				// TODO throw runtime error
			}
		}
		return result;
	}
}

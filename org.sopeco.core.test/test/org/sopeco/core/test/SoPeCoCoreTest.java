package org.sopeco.core.test;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.config.Configuration;
import org.sopeco.engine.EngineFactory;
import org.sopeco.engine.IEngine;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experimentseries.IExplorationStrategy;
import org.sopeco.engine.experimentseries.IExplorationStrategyExtension;
import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.experimentseries.IParameterVariationExtension;
import org.sopeco.engine.measurementenvironment.DummyMEController;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder.AssignmentType;

public class SoPeCoCoreTest {

	// Global variables
	IEngine engine;
	IExtensionRegistry registry;
	
	// Temporary variables, to be replaced later
	private DummyMEController meController;
	private IExperimentController expController;
	private ExperimentSeriesDefinition expSeriesDef; 
	private ScenarioDefinitionBuilder builder;
	private ParameterDefinition pdef;
	private DynamicValueAssignment dva;

	// Constants
	static final String FULL_EXPLORATION_STRATEGY_NAME = "Full Exploration Strategy";
	static final String VARIATION_NAME = "Linear Numeric Variation";
	
	@Before
	public void setUp() throws Exception {
		Configuration.getSingleton().setMeasurementControllerClassName(DummyMEController.class.getName());
		engine = EngineFactory.INSTANCE.createEngine();
		registry = engine.getExtensionRegistry();

		builder = new ScenarioDefinitionBuilder("test", "testDef");
		builder.createNewNamespace("initialization");
		pdef = builder.createParameter("initParameter", ParameterType.DOUBLE, ParameterRole.INPUT);
		
		builder.createExperimentSeriesDefinition("experiment series");
		builder.createNumberOfRunsCondition(2);
		builder.createExplorationStrategy(FULL_EXPLORATION_STRATEGY_NAME, Collections.EMPTY_MAP);
		
		Map<String, String> config = new HashMap<String, String>();
		config.put("min", String.valueOf(4));
		config.put("max", String.valueOf(28));
		config.put("step", String.valueOf(12));
		dva = builder.createDynamicValueAssignment(AssignmentType.Experiment, VARIATION_NAME, pdef, config);

		expSeriesDef = builder.getCurrentExperimentSeriesDefinition();
		
		expController = engine.getExperimentController();
	}

	@Test
	public void testFullExplorationStrategy() {
		
		
		
		IParameterVariation ipv = registry.getExtensionArtifact(IParameterVariationExtension.class, VARIATION_NAME);
		assertNotNull(ipv);
		ipv.initialize(dva);
		
		List<IParameterVariation> ipvList = new ArrayList<IParameterVariation>();
		ipvList.add(ipv);
		
		IExplorationStrategy fes = registry.getExtensionArtifact(IExplorationStrategyExtension.class, FULL_EXPLORATION_STRATEGY_NAME);
		assertNotNull(fes);

		fes.setExperimentController(expController);
		ExperimentSeries es = new ExperimentSeries();
		es.setName(expSeriesDef.getName());
		
		// TODO Test the run
		//fes.runExperimentSeries(es, ipvList);
		// TODO test the results
	}

}

package org.sopeco.plugin.std.exploration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
import org.sopeco.engine.experimentseries.IExplorationStrategy;
import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.experimentseries.IParameterVariationExtension;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.engine.util.test.DummyMEController;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder.AssignmentType;
import org.sopeco.plugin.std.exploration.full.FullExplorationStrategyExtension;

public class FullExplorationStrategyTest {

	static final double MIN = 4;
	static final double MAX = 12;
	static final double STEP = 4;
	private static final String VARIATION_NAME = "Linear Numeric Variation";

	IExplorationStrategy fes = null;
	DynamicValueAssignment dva;

	private ExperimentSeriesDefinition expSeriesDef; 
	private ScenarioDefinitionBuilder builder;

	
	private IEngine engine;
	
	@Before
	public void setUp() throws Exception {
		
//		Configuration.getSingleton().setMeasurementControllerClassName(DummyMEController.class.getName());
//		engine = EngineFactory.INSTANCE.createEngine();
//		
//		builder = new ScenarioDefinitionBuilder("test");
//		builder.createNewNamespace("initialization");
//		ParameterDefinition pdef = builder.createParameter("initParameter", ParameterType.DOUBLE, ParameterRole.INPUT);
//
//		FullExplorationStrategyExtension fese = new FullExplorationStrategyExtension();
//		fes = fese.createExtensionArtifact();
//		
//		builder.createMeasurementSpecification("test specification");
//		
//		builder.createExperimentSeriesDefinition("experiment series");
//		builder.createNumberOfRunsCondition(2);
//		builder.createExplorationStrategy(FullExplorationStrategyExtension.NAME, Collections.EMPTY_MAP);
//		
//		Map<String, String> config = new HashMap<String, String>();
//		config.put("min", String.valueOf(MIN));
//		config.put("max", String.valueOf(MAX));
//		config.put("step", String.valueOf(STEP));
//		
//		dva = builder.createDynamicValueAssignment(AssignmentType.Experiment, VARIATION_NAME, pdef, config);
//		
//		expSeriesDef  = builder.getCurrentExperimentSeriesDefinition();
	}

	@Test
	public void testCanRun() {
//		fes.canRun(expSeriesDef);
	}

	@Test
	public void testRunExperimentSeries() {
//		IExtensionRegistry er = engine.getExtensionRegistry();
		
// TODO: Roozbeh will look into this:		
		
//		IParameterVariation ipv = er.getExtensionArtifact(IParameterVariationExtension.class, VARIATION_NAME);
//		assertNotNull(ipv);
//		ipv.initialize(dva);
//		List<IParameterVariation> ipvList = new ArrayList<IParameterVariation>();
//		ipvList.add(ipv);
		
//		TODO: create ExperimentSeries and ExperimentSeriesRun properly via EntityFactory
//		fail("TODO: create ExperimentSeries and ExperimentSeriesRun properly via EntityFactory");
//		ExperimentSeries es = new ExperimentSeries();
//		es.setName(expSeriesDef.getName());
//		
//		ExperimentSeriesRun esr = new ExperimentSeriesRun();
//		esr.setTimestamp(System.nanoTime());
//		esr.setExperimentSeries(es);
//		es.getExperimentSeriesRuns().add(esr);
//		
//		fes.runExperimentSeries(esr, ipvList);
	}
	
}

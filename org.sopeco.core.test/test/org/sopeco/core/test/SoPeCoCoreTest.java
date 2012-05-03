package org.sopeco.core.test;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.engine.EngineFactory;
import org.sopeco.engine.IEngine;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experimentseries.IExplorationStrategy;
import org.sopeco.engine.experimentseries.IExplorationStrategyExtension;
import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.experimentseries.IParameterVariationExtension;
import org.sopeco.engine.helper.ConfigurationBuilder;
import org.sopeco.engine.helper.DummyMEController;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.environment.ParameterRole;
import org.sopeco.model.configuration.measurements.DynamicValueAssignment;
import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.model.configuration.measurements.ExplorationStrategy;
import org.sopeco.model.configuration.measurements.MeasurementsFactory;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.ExperimentSeries;

public class SoPeCoCoreTest {

	// Global variables
	IEngine engine;
	IExtensionRegistry registry;
	
	// Temporary variables, to be replaced later
	private DummyMEController meController;
	private IExperimentController expController;
	private ExperimentSeriesDefinition expSeriesDef; 
	private ConfigurationBuilder builder;
	private ParameterDefinition pdef;
	private DynamicValueAssignment dva;

	// Constants
	static final String FULL_EXPLORATION_STRATEGY_NAME = "Full Exploration Strategy";
	static final String VARIATION_NAME = "Linear Numeric Variation";
	
	@Before
	public void setUp() throws Exception {
		// TODO replace the null, later!
		engine = EngineFactory.INSTANCE.createEngine();
		registry = engine.getExtensionRegistry();

		builder = new ConfigurationBuilder("test");
		builder.createNamespace("initialization");
		pdef = builder.createParameter("initParameter", ParameterType.DOUBLE, ParameterRole.INPUT);

		meController = new DummyMEController();
		expController = builder.createExperimentController(meController);
		
		expSeriesDef = MeasurementsFactory.eINSTANCE.createExperimentSeriesDefinition();
		builder.createNumberOfRunsCondition(2);
		expSeriesDef.setExperimentTerminationCondition(builder.getTerminationCondition());
		
		ExplorationStrategy es = MeasurementsFactory.eINSTANCE.createExplorationStrategy();
		es.setName(FULL_EXPLORATION_STRATEGY_NAME);
		expSeriesDef.setExplorationStrategy(es);
		
		expSeriesDef.setName("experiment series");
	}

	@Test
	public void testFullExplorationStrategy() {
		Map<String, String> config = new HashMap<String, String>();
		config.put("min", String.valueOf(4));
		config.put("max", String.valueOf(28));
		config.put("step", String.valueOf(12));
		
		dva = builder.createDynamicValueAssignment(VARIATION_NAME, pdef, config);

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

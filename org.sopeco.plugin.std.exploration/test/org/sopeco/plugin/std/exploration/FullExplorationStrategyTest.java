package org.sopeco.plugin.std.exploration;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.engine.EngineFactory;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experimentseries.IExplorationStrategy;
import org.sopeco.engine.helper.ConfigurationBuilder;
import org.sopeco.engine.helper.DummyMEController;
import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.model.configuration.measurements.ExplorationStrategy;
import org.sopeco.model.configuration.measurements.MeasurementsFactory;

public class FullExplorationStrategyTest {

	IExplorationStrategy fes = null;
	
	private DummyMEController meController;
	private IExperimentController expController;
	private ExperimentSeriesDefinition expSeriesDef; 
	private ConfigurationBuilder builder;
	
	@Before
	public void setUp() throws Exception {
		builder = new ConfigurationBuilder("test");

		FullExplorationStrategyExtension fese = new FullExplorationStrategyExtension();
		fes = fese.createExtensionArtifact();

		meController = new DummyMEController();
		expController = EngineFactory.INSTANCE.createExperimentController(meController);
		
		expSeriesDef = MeasurementsFactory.eINSTANCE.createExperimentSeriesDefinition();
		builder.createNumberOfRunsCondition(2);
		expSeriesDef.setExperimentTerminationCondition(builder.getTerminationCondition());
		
		ExplorationStrategy es = MeasurementsFactory.eINSTANCE.createExplorationStrategy();
		es.setName(FullExplorationStrategyExtension.NAME);
		expSeriesDef.setExplorationStrategy(es);
		
		expSeriesDef.setName("experiment series");
		
	}

	@Test
	public void testCanRun() {
		fes.canRun(expSeriesDef);
	}

	// TODO test runExperimentSeries (requires parameter variation)
	
//	@Test
//	public void testRunExperimentSeries() {
//		fes.runExperimentSeries(expSeriesDef, parameterVariations, builder.getTerminationCondition());
//	}

}

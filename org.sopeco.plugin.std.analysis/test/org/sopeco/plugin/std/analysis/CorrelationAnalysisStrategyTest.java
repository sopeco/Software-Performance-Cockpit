package org.sopeco.plugin.std.analysis;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sopeco.engine.analysis.ICorrelationResult;
import org.sopeco.engine.analysis.ICorrelationStrategy;
import org.sopeco.engine.analysis.ParameterCorrelation;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetColumnBuilder;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.util.EMFUtil;

/**
 * Test class for implementations of the {@link ICorrelationStrategy}
 * interface that implement a correlation analysis.
 * 
 * @author Dennis Westermann
 * 
 */
@RunWith(Parameterized.class)
public class CorrelationAnalysisStrategyTest {

	private ICorrelationStrategy strategy;
	private AnalysisConfiguration analysisConfiguration;
	private static ScenarioDefinition scenarioDefinition;
	private DataSetAggregated dataset;
	
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ (ICorrelationStrategy) new CorrelationAnalysisStrategyExtension().createExtensionArtifact(), "Pearson Correlation", true},
				{ (ICorrelationStrategy) new CorrelationAnalysisStrategyExtension().createExtensionArtifact(), "Pearson Correlation", false}
		};
		return Arrays.asList(data);
	}

	public CorrelationAnalysisStrategyTest(ICorrelationStrategy strategy, String name, boolean correlation) throws IOException {
		this.strategy = strategy;

		scenarioDefinition = loadScenarioDefinition();
		this.analysisConfiguration = EntityFactory.createAnalysisConfiguration(name, new HashMap<String, String>());
		this.analysisConfiguration.getDependentParameters().add(scenarioDefinition.getParameterDefinition("default.DummyOutput"));
		this.analysisConfiguration.getIndependentParameters().add(scenarioDefinition.getParameterDefinition("default.DummyInput"));
		
		if(correlation) {
			this.dataset = createCorrelatedDummyDataSet();
		} else {
			this.dataset = createUncorrelatedDummyDataSet();
		}
		System.out.println("\n*** Running test for " + name + "and correlation is " + correlation);
	}

	@Before
	public void setUp() throws Exception {
		scenarioDefinition = loadScenarioDefinition();
	}

	@Test
	public void testAnalysis() {

			assertTrue(strategy.supports(analysisConfiguration));

			strategy.analyse(dataset, analysisConfiguration);

			ICorrelationResult result = strategy.getCorrelationResult();

			assertNotNull(result);
			assertEquals(analysisConfiguration.getName(), result.getAnalysisStrategyConfiguration().getName());


			ParameterCorrelation dummyInputCorrelation = result.getParameterCorrelationByParam(scenarioDefinition.getParameterDefinition("default.DummyInput"));
			assertNotNull(dummyInputCorrelation);
			assertEquals(result.getAllParameterCorrelations().size(), 1);
			
			assertNotNull(dummyInputCorrelation.getCorrelation());
			assertNotNull(dummyInputCorrelation.getPValue());

			
			System.out.println("Correlation: " + dummyInputCorrelation.getCorrelation());
			System.out.println("P-value:" + dummyInputCorrelation.getPValue());
			System.out.println("IsCorrelated (at 95% significance level): " + dummyInputCorrelation.isCorrelated(0.95));

	}
	

	private static ScenarioDefinition loadScenarioDefinition() throws IOException {
		return (ScenarioDefinition) EMFUtil.loadFromFilePath("test/dummy.configuration");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static DataSetAggregated createCorrelatedDummyDataSet() throws IOException {
		if (scenarioDefinition == null)
			scenarioDefinition = loadScenarioDefinition();

		DataSetColumnBuilder builder = new DataSetColumnBuilder();
		builder.startInputColumn(scenarioDefinition.getParameterDefinition("default.DummyInput"));
		builder.addInputValue(1);
		builder.addInputValue(2);
//		builder.addInputValue(3);
		builder.finishColumn();

		ParameterDefinition paramDef = scenarioDefinition.getParameterDefinition("default.DummyOutput");
		ArrayList<ParameterValueList> obsValueLists = new ArrayList<ParameterValueList>();
		builder.startObservationColumn(paramDef);
		ArrayList<Object> obsValues1 = new ArrayList<Object>();
		obsValues1.add(10);
		obsValues1.add(10);
		obsValues1.add(10);
		obsValues1.add(10);
		obsValueLists.add(new ParameterValueList<Object>(paramDef, obsValues1));

		ArrayList<Object> obsValues2 = new ArrayList<Object>();
		obsValues2.add(11);
		obsValues2.add(11);
		obsValues2.add(11);
		obsValues2.add(11);
		obsValueLists.add(new ParameterValueList(paramDef, obsValues2));

//		ArrayList<Object> obsValues3 = new ArrayList<Object>();
//		obsValues3.add(30);
//		obsValues3.add(30);
//		obsValues3.add(30);
//		obsValues3.add(30);
//		obsValueLists.add(new ParameterValueList(paramDef, obsValues3));

		
		builder.addObservationValueLists(obsValueLists);
		builder.finishColumn();

		return builder.createDataSet();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static DataSetAggregated createUncorrelatedDummyDataSet() throws IOException {
		if (scenarioDefinition == null)
			scenarioDefinition = loadScenarioDefinition();

		DataSetColumnBuilder builder = new DataSetColumnBuilder();
		builder.startInputColumn(scenarioDefinition.getParameterDefinition("default.DummyInput"));
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.finishColumn();

		ParameterDefinition paramDef = scenarioDefinition.getParameterDefinition("default.DummyOutput");
		ArrayList<ParameterValueList> obsValueLists = new ArrayList<ParameterValueList>();
		builder.startObservationColumn(paramDef);
		ArrayList<Object> obsValues1 = new ArrayList<Object>();
		obsValues1.add(10);
		obsValues1.add(10);
		obsValues1.add(10);
		obsValues1.add(10);
		obsValueLists.add(new ParameterValueList<Object>(paramDef, obsValues1));

		ArrayList<Object> obsValues2 = new ArrayList<Object>();
		obsValues2.add(10);
		obsValues2.add(10);
		obsValues2.add(10);
		obsValues2.add(10);
		obsValueLists.add(new ParameterValueList(paramDef, obsValues2));

		builder.addObservationValueLists(obsValueLists);
		builder.finishColumn();

		return builder.createDataSet();
	}
	
	

}

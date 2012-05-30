package org.sopeco.plugin.std.analysis;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sopeco.engine.analysis.IPredictionFunctionResult;
import org.sopeco.engine.analysis.IPredictionFunctionStrategy;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetColumnBuilder;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueFactory;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.plugin.std.analysis.util.DummyScenarioDefinitionFactory;

/**
 * Test class for implementations of the {@link IPredictionFunctionStrategy}
 * interface that implement a prediction function derivation analysis.
 * 
 * @author Dennis Westermann
 * 
 */
@RunWith(Parameterized.class)
public class PredictionFunctionAnalysisStrategyTest {

	private IPredictionFunctionStrategy strategy;
	private AnalysisConfiguration analysisConfiguration;
	private static ScenarioDefinition scenarioDefinition;
	private DataSetAggregated dataset;
	
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ (IPredictionFunctionStrategy) new LinearRegressionStrategyExtension().createExtensionArtifact(), "Linear Regression", "Small" },
				{ (IPredictionFunctionStrategy) new LinearRegressionStrategyExtension().createExtensionArtifact(), "Linear Regression", "Large" },
				{ (IPredictionFunctionStrategy) new LinearRegressionStrategyExtension().createExtensionArtifact(), "Linear Regression", "NoVariation" },
				{ (IPredictionFunctionStrategy) new MarsStrategyExtension().createExtensionArtifact(), "MARS", "Small" },
				{ (IPredictionFunctionStrategy) new MarsStrategyExtension().createExtensionArtifact(), "MARS", "Large" },
				{ (IPredictionFunctionStrategy) new MarsStrategyExtension().createExtensionArtifact(), "MARS", "NoVariation" },
		};
		return Arrays.asList(data);
	}

	public PredictionFunctionAnalysisStrategyTest(IPredictionFunctionStrategy strategy, String name, String dataSet) throws IOException {
		this.strategy = strategy;
		this.analysisConfiguration = EntityFactory.createAnalysisConfiguration(name, new HashMap<String, String>());
		if (dataSet.equalsIgnoreCase("Small")) {
			this.dataset = createSmallDummyDataSet();
		} else if (dataSet.equalsIgnoreCase("Large")) {
			this.dataset = createLargeDummyDataSet();
		} else if (dataSet.equalsIgnoreCase("NoVariation")) {
			this.dataset = createNoVariationDummyDataSet();
		}
		System.out.println("\n*** Running test for " + name + " with dataset size " + dataSet);
	}

	@Before
	public void setUp() throws Exception {
		scenarioDefinition = loadScenarioDefinition();
	}

	@Test
	public void testAnalysis() {

			assertTrue(strategy.supports(analysisConfiguration));

			strategy.analyse(dataset, analysisConfiguration);

			IPredictionFunctionResult result = strategy.getPredictionFunctionResult();

			assertNotNull(result);
			assertEquals(analysisConfiguration.getName(), result.getAnalysisStrategyConfiguration().getName());

			assertNotNull(result.getFunctionAsString());
			
			assertTrue(!result.getFunctionAsString().contains("NA"));
			
			ParameterValue<?> inputParam = ParameterValueFactory.createParameterValue(
					scenarioDefinition.getParameterDefinition("default.DummyInput"), 1);
			ParameterValue<?> predParam1 = result.predictOutputParameter(inputParam);
			assertNotNull(predParam1);
			List<ParameterValue<?>> inputParamList = new ArrayList<ParameterValue<?>>();
			inputParamList.add(inputParam);
			ParameterValue<?> predParam2 = result.predictOutputParameter(inputParamList);
			assertEquals(predParam1.getValue(), predParam2.getValue());

			

			System.out.println(result.getFunctionAsString());

	}
	
	@Test
	public void testAnalysisWithConfiguredDependentAndIndependentParameter() {

		this.analysisConfiguration.getDependentParameters().add(scenarioDefinition.getParameterDefinition("default.DummyOutput"));
		this.analysisConfiguration.getIndependentParameters().add(scenarioDefinition.getParameterDefinition("default.DummyInput"));
		
		this.testAnalysis();

	}
	
	

	private static ScenarioDefinition loadScenarioDefinition() throws IOException {

		return DummyScenarioDefinitionFactory.createScenarioDefinition();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static DataSetAggregated createLargeDummyDataSet() throws IOException {
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
		obsValues2.add(20);
		obsValues2.add(20);
		obsValues2.add(20);
		obsValues2.add(20);
		obsValueLists.add(new ParameterValueList(paramDef, obsValues2));

		builder.addObservationValueLists(obsValueLists);
		builder.finishColumn();

		return builder.createDataSet();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static DataSetAggregated createSmallDummyDataSet() throws IOException {
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
		obsValueLists.add(new ParameterValueList<Object>(paramDef, obsValues1));

		ArrayList<Object> obsValues2 = new ArrayList<Object>();
		obsValues2.add(20);
		obsValueLists.add(new ParameterValueList(paramDef, obsValues2));

		builder.addObservationValueLists(obsValueLists);
		builder.finishColumn();

		return builder.createDataSet();
	}
	
	
	private static DataSetAggregated createNoVariationDummyDataSet() throws IOException {
	
		if (scenarioDefinition == null)
			scenarioDefinition = loadScenarioDefinition();

		DataSetColumnBuilder builder = new DataSetColumnBuilder();
		builder.startInputColumn(scenarioDefinition.getParameterDefinition("default.DummyInput"));
		builder.addInputValue(1);
		builder.finishColumn();

		ParameterDefinition paramDef = scenarioDefinition.getParameterDefinition("default.DummyOutput");
		@SuppressWarnings("rawtypes")
		ArrayList<ParameterValueList> obsValueLists = new ArrayList<ParameterValueList>();
		builder.startObservationColumn(paramDef);
		ArrayList<Object> obsValues1 = new ArrayList<Object>();
		obsValues1.add(10);
		obsValueLists.add(new ParameterValueList<Object>(paramDef, obsValues1));

		builder.addObservationValueLists(obsValueLists);
		builder.finishColumn();

		return builder.createDataSet();
	}
	

}

package org.sopeco.plugin.std.analysis;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.engine.analysis.IPredictionFunctionResult;
import org.sopeco.engine.analysis.IPredictionFunctionStrategy;
import org.sopeco.model.configuration.ScenarioDefinition;
import org.sopeco.model.configuration.SoPeCoModelFactoryHandler;
import org.sopeco.model.configuration.analysis.AnalysisConfiguration;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.util.EMFUtil;
import org.sopeco.model.util.ScenarioDefinitionUtil;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetColumnBuilder;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueFactory;
import org.sopeco.persistence.dataset.ParameterValueList;

/**
 * Test class for implementations of the {@link IPredictionFunctionStrategy} interface
 * that implement a prediction function derivation analysis.
 * 
 * @author Dennis Westermann
 * 
 */
public class PredictionFunctionAnalysisStrategyTest {

	private static IPredictionFunctionStrategy strategy;
	private static ScenarioDefinition scenarioDefinition;
	private static AnalysisConfiguration analysisConfiguration;

	@Before
	public void setUp() throws Exception {
		SoPeCoModelFactoryHandler.initFactories();
		scenarioDefinition = loadScenarioDefinition();
		
		/*
		 * This is the only implementation specific part of the test
		 */
		strategy = (IPredictionFunctionStrategy) new LinearRegressionStrategyExtension().createExtensionArtifact();
		analysisConfiguration = SoPeCoModelFactoryHandler.getAnalysisFactory().createAnalysisConfiguration();
		analysisConfiguration.setName("Linear Regression");
	}

	@Test
	public void testAnalysis() {

		try {

			assertTrue(strategy.supports(analysisConfiguration));
			
			DataSetAggregated dataset = createDummyDataSet();

			strategy.analyse(dataset, analysisConfiguration);
			
			IPredictionFunctionResult result = strategy.getPredictionFunctionResult();
			
			assertNotNull(result);
			assertEquals(analysisConfiguration.getName(), result.getAnalysisStrategyConfiguration().getName());
			
			ParameterValue<?> inputParam = ParameterValueFactory.createParameterValue(ScenarioDefinitionUtil.getParameterDefinition("default.DummyInput", scenarioDefinition), 1);
			ParameterValue<?> predParam1 = result.predictOutputParameter(inputParam);
			assertNotNull(predParam1);
			List<ParameterValue<?>> inputParamList = new ArrayList<ParameterValue<?>>();
			inputParamList.add(inputParam);
			ParameterValue<?> predParam2 = result.predictOutputParameter(inputParamList);
			assertEquals(predParam1.getValue(), predParam2.getValue());
			
			assertNotNull(result.getFunctionAsString());
			
			System.out.println(result.getFunctionAsString());
			
		} catch (IOException e) {
			fail(e.getMessage());
		}

	}

	private static ScenarioDefinition loadScenarioDefinition() throws IOException {
		return (ScenarioDefinition) EMFUtil.loadFromFilePath("test/dummy.configuration");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static DataSetAggregated createDummyDataSet() throws IOException {
		if (scenarioDefinition == null)
			scenarioDefinition = loadScenarioDefinition();

		DataSetColumnBuilder builder = new DataSetColumnBuilder();
		builder.startInputColumn(ScenarioDefinitionUtil.getParameterDefinition("default.DummyInput", scenarioDefinition));
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.finishColumn();

		ParameterDefinition paramDef = ScenarioDefinitionUtil.getParameterDefinition("default.DummyOutput", scenarioDefinition);
		ArrayList<ParameterValueList> obsValueLists = new ArrayList<ParameterValueList>();
		builder.startObservationColumn(paramDef);
		ArrayList<Object> obsValues1 = new ArrayList<Object>();
		obsValues1.add(10);
		obsValues1.add(10);
		obsValueLists.add(new ParameterValueList<Object>(paramDef, obsValues1));

		ArrayList<Object> obsValues2 = new ArrayList<Object>();
		obsValues2.add(20);
		obsValues2.add(20);
		obsValueLists.add(new ParameterValueList(paramDef, obsValues2));

		builder.addObservationValueLists(obsValueLists);
		builder.finishColumn();

		return builder.createDataSet();
	}

}

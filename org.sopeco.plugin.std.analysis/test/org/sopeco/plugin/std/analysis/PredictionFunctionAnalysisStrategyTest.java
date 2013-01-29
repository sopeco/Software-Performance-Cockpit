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
package org.sopeco.plugin.std.analysis;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.AfterClass;
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
import org.sopeco.plugin.std.analysis.util.RAdapter;

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
	
	private static boolean skipUnitTest = false;
	
	@Parameters
	public static Collection<Object[]> data() {
		try {
			skipUnitTest = false;
			
			Object[][] data = new Object[][] {
					{ (IPredictionFunctionStrategy) new LinearRegressionStrategyExtension().createExtensionArtifact(), "Linear Regression", "Small" },
					{ (IPredictionFunctionStrategy) new LinearRegressionStrategyExtension().createExtensionArtifact(), "Linear Regression", "Large" },
					{ (IPredictionFunctionStrategy) new LinearRegressionStrategyExtension().createExtensionArtifact(), "Linear Regression", "NoVariation" },
					{ (IPredictionFunctionStrategy) new MarsStrategyExtension().createExtensionArtifact(), "MARS", "Small" },
					{ (IPredictionFunctionStrategy) new MarsStrategyExtension().createExtensionArtifact(), "MARS", "Large" },
					{ (IPredictionFunctionStrategy) new MarsStrategyExtension().createExtensionArtifact(), "MARS", "NoVariation" },
			};
		
			return Arrays.asList(data);
		} catch (RuntimeException x) {
			if (x.getMessage().equals("failed calling R service")) {
				skipUnitTest = true;
				System.err.println("failed calling R service. skip tests.");
				return Arrays.asList(new Object[0][0]);
			} else {
				throw x;
			}
		}
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
			if (skipUnitTest) {
				return;
			}
		
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
		if (skipUnitTest) {
			return;
		}
		
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

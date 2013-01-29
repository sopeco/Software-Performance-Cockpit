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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sopeco.engine.analysis.AnovaCalculatedEffect;
import org.sopeco.engine.analysis.IAnovaResult;
import org.sopeco.engine.analysis.IAnovaStrategy;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetColumnBuilder;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.plugin.std.analysis.util.DummyScenarioDefinitionFactory;

/**
 * Test class for implementations of the {@link IAnovaStrategy} interface that
 * implement an anova analysis.
 * 
 * Example for Anova in R: <br>
 * a <- c(1, 1, 1, 1, 2, 2, 2, 2) <br>
 * b <- c(1, 1, 2, 2, 1, 1, 2, 2) <br>
 * c <- a*b + 2*b + 0.1*a*b + b <br>
 * c <- jitter(c) <br>
 * data <- data.frame(a,b,c) <br>
 * anova(lm(c ~ a * b, data)) <br>
 * 
 * 
 * @author Dennis Westermann
 * 
 */
@RunWith(Parameterized.class)
public class AnovaAnalysisStrategyTest {

	private IAnovaStrategy strategy;
	private AnalysisConfiguration analysisConfiguration;
	private static ScenarioDefinition scenarioDefinition;
	private DataSetAggregated dataset;

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ (IAnovaStrategy) new AnovaStrategyExtension().createExtensionArtifact(), AnovaStrategyExtension.NAME,
						"Correlated" },
						{ (IAnovaStrategy) new AnovaStrategyExtension().createExtensionArtifact(), AnovaStrategyExtension.NAME,
						"Uncorrelated" },
				{ (IAnovaStrategy) new AnovaStrategyExtension().createExtensionArtifact(), AnovaStrategyExtension.NAME,
						"NotEnoughData" } 
						};
		return Arrays.asList(data);
	}

	public AnovaAnalysisStrategyTest(IAnovaStrategy strategy, String name, String dataSetType) throws IOException {
		this.strategy = strategy;

		scenarioDefinition = loadScenarioDefinition();
		this.analysisConfiguration = EntityFactory.createAnalysisConfiguration(name, new HashMap<String, String>());
		this.analysisConfiguration.getDependentParameters().add(
				scenarioDefinition.getParameterDefinition("default.DummyOutput"));
		this.analysisConfiguration.getIndependentParameters().add(
				scenarioDefinition.getParameterDefinition("default.DummyInput1"));
		this.analysisConfiguration.getIndependentParameters().add(
				scenarioDefinition.getParameterDefinition("default.DummyInput2"));

		if (dataSetType.equalsIgnoreCase("Correlated")) {
			this.dataset = createCorrelatedDummyDataSet();
		} else if (dataSetType.equalsIgnoreCase("Uncorrelated")){
			this.dataset = createUncorrelatedDummyDataSet();
		} else if (dataSetType.equalsIgnoreCase("NotEnoughData")){
			this.dataset = createNotEnoughDataDummyDataSet();
		}
		System.out.println("\n*** Running test for " + name + ", using data set type " + dataSetType);
	}

	@Before
	public void setUp() throws Exception {
		scenarioDefinition = loadScenarioDefinition();
	}

	@Test
	public void testAnalysis() {

		assertTrue(strategy.supports(analysisConfiguration));

		try {
		strategy.analyse(dataset, analysisConfiguration);
		} catch (RuntimeException x) {
			if (x.getMessage().equals("failed calling R service")) {
				System.err.println("failed calling R service. skip test");
				return;
			} else {
				throw x;
			}
		}
		
		IAnovaResult result = strategy.getAnovaResult();

		assertNotNull(result);
		assertEquals(analysisConfiguration.getName(), result.getAnalysisStrategyConfiguration().getName());

		for (AnovaCalculatedEffect effect : result.getAllMainEffects()) {
			System.out.println("Parameter: " + effect.getIndependentParameter().getFullName());
			System.out.println("F-value: " + effect.getfValue());
			System.out.println("P-value:" + effect.getpValue());
			System.out.println("IsSignificant (at 95% significance level): " + effect.isSignificant(0.95));
		}
		
		for (AnovaCalculatedEffect effect : result.getAllInteractionEffects()) {
			System.out.print("Parameter: ");
			for(ParameterDefinition pd : effect.getIndependentParameters()) {
				System.out.print(pd.getFullName() + ":");
			}
			System.out.println();
			System.out.println("F-value: " + effect.getfValue());
			System.out.println("P-value:" + effect.getpValue());
			System.out.println("IsSignificant (at 95% significance level): " + effect.isSignificant(0.95));
		}

	}

	private static ScenarioDefinition loadScenarioDefinition() throws IOException {

		return DummyScenarioDefinitionFactory.createScenarioDefinition();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static DataSetAggregated createCorrelatedDummyDataSet() throws IOException {
		if (scenarioDefinition == null)
			scenarioDefinition = loadScenarioDefinition();

		/*
		 * Example for Anova in R: <br>
		 * a <- c(1, 1, 1, 1, 2, 2, 2, 2) <br>
		 * b <- c(1, 1, 2, 2, 1, 1, 2, 2) <br>
		 * c <- a*b + 2*b + 0.1*a*b + b <br>
		 * c <- jitter(c) <br>
		 * data <- data.frame(a,b,c) <br>
		 * anova(lm(c ~ a * b, data)) <br>
		 */
		DataSetColumnBuilder builder = new DataSetColumnBuilder();
		builder.startInputColumn(scenarioDefinition.getParameterDefinition("default.DummyInput1"));
		builder.addInputValue(1);
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.addInputValue(2);
		builder.finishColumn();
		
		builder.startInputColumn(scenarioDefinition.getParameterDefinition("default.DummyInput2"));
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.finishColumn();

		ParameterDefinition paramDef = scenarioDefinition.getParameterDefinition("default.DummyOutput");
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
		
		ArrayList<Object> obsValues3 = new ArrayList<Object>();
		obsValues3.add(20);
		obsValues3.add(20);
		obsValueLists.add(new ParameterValueList(paramDef, obsValues3));
		
		ArrayList<Object> obsValues4 = new ArrayList<Object>();
		obsValues4.add(40);
		obsValues4.add(40);
		obsValueLists.add(new ParameterValueList(paramDef, obsValues4));

		builder.addObservationValueLists(obsValueLists);
		builder.finishColumn();

		return builder.createDataSet();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static DataSetAggregated createUncorrelatedDummyDataSet() throws IOException {
		if (scenarioDefinition == null)
			scenarioDefinition = loadScenarioDefinition();

		DataSetColumnBuilder builder = new DataSetColumnBuilder();
		builder.startInputColumn(scenarioDefinition.getParameterDefinition("default.DummyInput1"));
		builder.addInputValue(1);
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.addInputValue(2);
		builder.finishColumn();
		
		builder.startInputColumn(scenarioDefinition.getParameterDefinition("default.DummyInput2"));
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.finishColumn();

		ParameterDefinition paramDef = scenarioDefinition.getParameterDefinition("default.DummyOutput");
		ArrayList<ParameterValueList> obsValueLists = new ArrayList<ParameterValueList>();
		builder.startObservationColumn(paramDef);
		ArrayList<Object> obsValues1 = new ArrayList<Object>();
		obsValues1.add(10);
		obsValues1.add(10);
		obsValueLists.add(new ParameterValueList<Object>(paramDef, obsValues1));

		ArrayList<Object> obsValues2 = new ArrayList<Object>();
		obsValues2.add(10);
		obsValues2.add(10);
		obsValueLists.add(new ParameterValueList(paramDef, obsValues2));
		
		ArrayList<Object> obsValues3 = new ArrayList<Object>();
		obsValues3.add(10);
		obsValues3.add(10);
		obsValueLists.add(new ParameterValueList(paramDef, obsValues3));
		
		ArrayList<Object> obsValues4 = new ArrayList<Object>();
		obsValues4.add(10);
		obsValues4.add(10);
		obsValueLists.add(new ParameterValueList(paramDef, obsValues4));

		builder.addObservationValueLists(obsValueLists);
		builder.finishColumn();

		return builder.createDataSet();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static DataSetAggregated createNotEnoughDataDummyDataSet() throws IOException {
		if (scenarioDefinition == null)
			scenarioDefinition = loadScenarioDefinition();

		DataSetColumnBuilder builder = new DataSetColumnBuilder();
		builder.startInputColumn(scenarioDefinition.getParameterDefinition("default.DummyInput1"));
		builder.addInputValue(1);
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.addInputValue(2);
		builder.finishColumn();
		
		builder.startInputColumn(scenarioDefinition.getParameterDefinition("default.DummyInput2"));
		builder.addInputValue(1);
		builder.addInputValue(2);
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
		obsValues2.add(10);
		obsValueLists.add(new ParameterValueList(paramDef, obsValues2));
		
		ArrayList<Object> obsValues3 = new ArrayList<Object>();
		obsValues3.add(10);
		obsValueLists.add(new ParameterValueList(paramDef, obsValues3));
		
		ArrayList<Object> obsValues4 = new ArrayList<Object>();
		obsValues4.add(10);
		obsValueLists.add(new ParameterValueList(paramDef, obsValues4));

		builder.addObservationValueLists(obsValueLists);
		builder.finishColumn();

		return builder.createDataSet();
	}

}

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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.analysis.wrapper.exception.AnalysisWrapperException;
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
import org.sopeco.plugin.std.analysis.util.DummyScenarioDefinitionFactory;

/**
 * Test class for implementations of the {@link ICorrelationStrategy} interface
 * that implement a correlation analysis.
 * 
 * @author Dennis Westermann
 * 
 */
@RunWith(Parameterized.class)
public class CorrelationAnalysisStrategyTest {
	private static Logger LOGGER = LoggerFactory.getLogger(CorrelationAnalysisStrategyTest.class);
	private ICorrelationStrategy strategy;
	private AnalysisConfiguration analysisConfiguration;
	private static ScenarioDefinition scenarioDefinition;
	private DataSetAggregated dataset;
	private static boolean skipTests = true;

	@Parameters
	public static Collection<Object[]> data() {
		try {
			Object[][] data = new Object[][] {
					{ (ICorrelationStrategy) new CorrelationAnalysisStrategyExtension().createExtensionArtifact(),
							CorrelationAnalysisStrategyExtension.NAME, true },
					{ (ICorrelationStrategy) new CorrelationAnalysisStrategyExtension().createExtensionArtifact(),
							CorrelationAnalysisStrategyExtension.NAME, false } };
			return Arrays.asList(data);
		} catch (RuntimeException e) {
			if (e.getCause() instanceof AnalysisWrapperException) {
				skipTests = true;
				LOGGER.error("Can't connect to analysis server. Analysis related Unit tests will be skipped.");

				return new ArrayList<Object[]>();
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	public CorrelationAnalysisStrategyTest(ICorrelationStrategy strategy, String name, boolean correlation)
			throws IOException {
		if (skipTests) {
			return;
		}
		this.strategy = strategy;

		scenarioDefinition = loadScenarioDefinition();
		this.analysisConfiguration = EntityFactory.createAnalysisConfiguration(name, new HashMap<String, String>());
		this.analysisConfiguration.getDependentParameters().add(
				scenarioDefinition.getParameterDefinition("default.DummyOutput"));
		this.analysisConfiguration.getIndependentParameters().add(
				scenarioDefinition.getParameterDefinition("default.DummyInput"));

		if (correlation) {
			this.dataset = createCorrelatedDummyDataSet();
		} else {
			this.dataset = createUncorrelatedDummyDataSet();
		}
		System.out.println("\n*** Running test for " + name + "and correlation is " + correlation);
	}

	@Before
	public void setUp() throws Exception {
		if (skipTests) {
			return;
		}
		scenarioDefinition = loadScenarioDefinition();
	}

	@After
	public void cleanUp() {
		if (skipTests) {
			return;
		}
		this.strategy.releaseAnalysisResources();
	}

	@Test
	public void testAnalysis() {
		if (skipTests) {
			return;
		}
		assertTrue(strategy.supports(analysisConfiguration));

		strategy.analyse(dataset, analysisConfiguration);

		ICorrelationResult result = strategy.getCorrelationResult();

		assertNotNull(result);
		assertEquals(analysisConfiguration.getName(), result.getAnalysisStrategyConfiguration().getName());

		ParameterCorrelation dummyInputCorrelation = result.getParameterCorrelationByParam(scenarioDefinition
				.getParameterDefinition("default.DummyInput"));
		assertNotNull(dummyInputCorrelation);
		assertEquals(result.getAllParameterCorrelations().size(), 1);

		assertNotNull(dummyInputCorrelation.getCorrelation());
		assertNotNull(dummyInputCorrelation.getPValue());

		System.out.println("Correlation: " + dummyInputCorrelation.getCorrelation());
		System.out.println("P-value:" + dummyInputCorrelation.getPValue());
		System.out.println("IsCorrelated (at 95% significance level): " + dummyInputCorrelation.isCorrelated(0.95));

	}

	private static ScenarioDefinition loadScenarioDefinition() throws IOException {

		return DummyScenarioDefinitionFactory.createScenarioDefinition();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static DataSetAggregated createCorrelatedDummyDataSet() throws IOException {
		if (scenarioDefinition == null)
			scenarioDefinition = loadScenarioDefinition();

		DataSetColumnBuilder builder = new DataSetColumnBuilder();
		builder.startInputColumn(scenarioDefinition.getParameterDefinition("default.DummyInput"));
		builder.addInputValue(1);
		builder.addInputValue(2);
		// builder.addInputValue(3);
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

		// ArrayList<Object> obsValues3 = new ArrayList<Object>();
		// obsValues3.add(30);
		// obsValues3.add(30);
		// obsValues3.add(30);
		// obsValues3.add(30);
		// obsValueLists.add(new ParameterValueList(paramDef, obsValues3));

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

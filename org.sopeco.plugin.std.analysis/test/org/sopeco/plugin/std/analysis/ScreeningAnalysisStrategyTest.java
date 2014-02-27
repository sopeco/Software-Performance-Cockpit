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
import org.sopeco.engine.analysis.ICorrelationStrategy;
import org.sopeco.engine.analysis.IScreeningAnalysisResult;
import org.sopeco.engine.analysis.IScreeningAnalysisStrategy;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetColumnBuilder;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * Test class for implementations of the {@link ICorrelationStrategy} interface
 * that implement a correlation analysis.
 * 
 * @author Dennis Westermann
 * 
 */
@RunWith(Parameterized.class)
public class ScreeningAnalysisStrategyTest {
	private static Logger LOGGER = LoggerFactory.getLogger(ScreeningAnalysisStrategyTest.class);

	private IScreeningAnalysisStrategy strategy;
	private AnalysisConfiguration analysisConfiguration;
	private DataSetAggregated dataset;

	private ParameterDefinition dummyInput1;
	private ParameterDefinition dummyInput2;
	private ParameterDefinition dummyOutput;

	private static boolean skipTests = true;

	@Parameters
	public static Collection<Object[]> data() {
		try {
			Object[][] data = new Object[][] { { (IScreeningAnalysisStrategy) new ScreeningAnalysisStrategyExtension()
					.createExtensionArtifact() } };
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

	public ScreeningAnalysisStrategyTest(IScreeningAnalysisStrategy strategy) throws IOException {
		this.strategy = strategy;

		this.dataset = createScreeningDesignDummyDataSet();

		this.analysisConfiguration = EntityFactory.createAnalysisConfiguration(ScreeningAnalysisStrategyExtension.NAME,
				new HashMap<String, String>());
		this.analysisConfiguration.getDependentParameters().add(dummyOutput);
		this.analysisConfiguration.getIndependentParameters().add(dummyInput1);
		this.analysisConfiguration.getIndependentParameters().add(dummyInput2);
	}

	@Before
	public void setUp() throws Exception {
		if (skipTests) {
			return;
		}
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

		IScreeningAnalysisResult result = strategy.getScreeningAnalysisResult();

		assertNotNull(result);
		assertEquals(analysisConfiguration.getName(), result.getAnalysisStrategyConfiguration().getName());

		assertEquals(2, result.getAllMainEffects().size());
		assertNotNull(result.getMainEffectByParam(dummyInput1));
		assertTrue(result.getMainEffectByParam(dummyInput1).getEffectValue() < result.getMainEffectByParam(dummyInput2)
				.getEffectValue());

		System.out.println("MainEffect of " + dummyInput1.getName() + ": "
				+ result.getMainEffectByParam(dummyInput1).getEffectValue());
		System.out.println("MainEffect of " + dummyInput2.getName() + ": "
				+ result.getMainEffectByParam(dummyInput2).getEffectValue());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private DataSetAggregated createScreeningDesignDummyDataSet() throws IOException {

		// Full Factorial Design DataSet

		ScenarioDefinition sd = EntityFactory.createScenarioDefinition("DummyScenario");
		MeasurementEnvironmentDefinition med = EntityFactory.createMeasurementEnvironmentDefinition();
		sd.setMeasurementEnvironmentDefinition(med);
		ParameterNamespace pns = EntityFactory.createNamespace("default");
		dummyInput1 = EntityFactory.createParameterDefinition("DummyInput1", "Integer", ParameterRole.INPUT);
		dummyInput1.setNamespace(pns);
		pns.getParameters().add(dummyInput1);
		dummyInput2 = EntityFactory.createParameterDefinition("DummyInput2", "Integer", ParameterRole.INPUT);
		dummyInput2.setNamespace(pns);
		pns.getParameters().add(dummyInput2);
		dummyOutput = EntityFactory.createParameterDefinition("DummyOutput", "Integer", ParameterRole.OBSERVATION);
		dummyOutput.setNamespace(pns);
		pns.getParameters().add(dummyOutput);
		med.setRoot(pns);

		DataSetColumnBuilder builder = new DataSetColumnBuilder();
		builder.startInputColumn(dummyInput1);
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.addInputValue(2);
		builder.addInputValue(1);
		builder.finishColumn();

		builder.startInputColumn(dummyInput2);
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.addInputValue(2);
		builder.addInputValue(1);
		builder.addInputValue(1);
		builder.addInputValue(2);
		builder.finishColumn();

		ArrayList<ParameterValueList> obsValueLists = new ArrayList<ParameterValueList>();
		builder.startObservationColumn(dummyOutput);
		ArrayList<Object> obsValues1 = new ArrayList<Object>();
		obsValues1.add(30);
		obsValueLists.add(new ParameterValueList<Object>(dummyOutput, obsValues1));

		ArrayList<Object> obsValues2 = new ArrayList<Object>();
		obsValues2.add(60);
		obsValueLists.add(new ParameterValueList(dummyOutput, obsValues2));

		ArrayList<Object> obsValues3 = new ArrayList<Object>();
		obsValues3.add(50);
		obsValueLists.add(new ParameterValueList<Object>(dummyOutput, obsValues3));

		ArrayList<Object> obsValues4 = new ArrayList<Object>();
		obsValues4.add(40);
		obsValueLists.add(new ParameterValueList(dummyOutput, obsValues4));

		ArrayList<Object> obsValues5 = new ArrayList<Object>();
		obsValues5.add(40);
		obsValueLists.add(new ParameterValueList<Object>(dummyOutput, obsValues5));

		ArrayList<Object> obsValues6 = new ArrayList<Object>();
		obsValues6.add(50);
		obsValueLists.add(new ParameterValueList(dummyOutput, obsValues6));

		builder.addObservationValueLists(obsValueLists);
		builder.finishColumn();

		return builder.createDataSet();
	}

}

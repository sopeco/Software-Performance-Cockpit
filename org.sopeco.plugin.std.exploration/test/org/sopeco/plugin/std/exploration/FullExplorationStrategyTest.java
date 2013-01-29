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
package org.sopeco.plugin.std.exploration;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.engine.IEngine;
import org.sopeco.engine.experimentseries.IExplorationStrategy;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.util.ScenarioDefinitionBuilder;

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

		// Configuration.getSingleton().setMeasurementControllerClassName(DummyMEController.class.getName());
		// engine = EngineFactory.INSTANCE.createEngine();
		//
		// builder = new ScenarioDefinitionBuilder("test");
		// builder.createNewNamespace("initialization");
		// ParameterDefinition pdef = builder.createParameter("initParameter",
		// ParameterType.DOUBLE, ParameterRole.INPUT);
		//
		// FullExplorationStrategyExtension fese = new
		// FullExplorationStrategyExtension();
		// fes = fese.createExtensionArtifact();
		//
		// builder.createMeasurementSpecification("test specification");
		//
		// builder.createExperimentSeriesDefinition("experiment series");
		// builder.createNumberOfRunsCondition(2);
		// builder.createExplorationStrategy(FullExplorationStrategyExtension.NAME,
		// Collections.EMPTY_MAP);
		//
		// Map<String, String> config = new HashMap<String, String>();
		// config.put("min", String.valueOf(MIN));
		// config.put("max", String.valueOf(MAX));
		// config.put("step", String.valueOf(STEP));
		//
		// dva = builder.createDynamicValueAssignment(AssignmentType.Experiment,
		// VARIATION_NAME, pdef, config);
		//
		// expSeriesDef = builder.getCurrentExperimentSeriesDefinition();
	}

	@Test
	public void testCanRun() {
		// fes.canRun(expSeriesDef);
	}

	@Test
	public void testRunExperimentSeries() {
		// IExtensionRegistry er = engine.getExtensionRegistry();

		// TODO: Roozbeh will look into this:

		// IParameterVariation ipv =
		// er.getExtensionArtifact(IParameterVariationExtension.class,
		// VARIATION_NAME);
		// assertNotNull(ipv);
		// ipv.initialize(dva);
		// List<IParameterVariation> ipvList = new
		// ArrayList<IParameterVariation>();
		// ipvList.add(ipv);

		// TODO: create ExperimentSeries and ExperimentSeriesRun properly via
		// EntityFactory
		// fail("TODO: create ExperimentSeries and ExperimentSeriesRun properly via EntityFactory");
		// ExperimentSeries es = new ExperimentSeries();
		// es.setName(expSeriesDef.getName());
		//
		// ExperimentSeriesRun esr = new ExperimentSeriesRun();
		// esr.setTimestamp(System.nanoTime());
		// esr.setExperimentSeries(es);
		// es.getExperimentSeriesRuns().add(esr);
		//
		// fes.runExperimentSeries(esr, ipvList);
	}

}

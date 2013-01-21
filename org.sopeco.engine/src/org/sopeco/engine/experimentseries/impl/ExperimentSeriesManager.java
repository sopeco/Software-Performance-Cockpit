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
/**
 * 
 */
package org.sopeco.engine.experimentseries.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.IEngine;
import org.sopeco.engine.exception.GenericException;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experimentseries.IExperimentSeriesManager;
import org.sopeco.engine.experimentseries.IExplorationStrategy;
import org.sopeco.engine.experimentseries.IExplorationStrategyExtension;
import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.experimentseries.IParameterVariationExtension;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.engine.util.EngineTools;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;
import org.sopeco.persistence.util.ParameterCollection;

/**
 * The default implementation of {@link ExperimentSeriesManager}.
 * 
 * @author Roozbeh Farahbod, Dennis Westermann
 * 
 */
public class ExperimentSeriesManager implements IExperimentSeriesManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentSeriesManager.class);

	private IEngine engine;

	/**
	 * Sets the engine.
	 * 
	 * @param usedEngine
	 *            engine to be used
	 */
	public void setEngine(IEngine usedEngine) {
		this.engine = usedEngine;
	}

	@Override
	public void runExperimentSeries(ExperimentSeries expSeries) {
		if (engine == null) {
			LOGGER.error("Engine has not been set!");
			throw new RuntimeException("Engine is not set!");
		}
		final IExtensionRegistry registry = engine.getExtensionRegistry();
		final ExperimentSeriesDefinition expSeriesDef = expSeries.getExperimentSeriesDefinition();
		IExperimentController expController = engine.getExperimentController();

		IExplorationStrategy expStrategy = registry.getExtensionArtifact(IExplorationStrategyExtension.class,
				expSeriesDef.getExplorationStrategy().getName());
		if (expStrategy == null) {
			throw new RuntimeException("Could not find required exploration strategy!");
		}

		expStrategy.setExperimentController(expController);
		expStrategy.setPersistenceProvider(engine.getPersistenceProvider());
		expStrategy.setExtensionRegistry(registry);

		if (!expStrategy.canRun(expSeriesDef)) {
			throw new RuntimeException("Exploration strategy is not ready to be executed!");
		}

		ExperimentSeriesRun seriesRun = EntityFactory.createExperimentSeriesRun();
		seriesRun.setExperimentSeries(expSeries);
		seriesRun.setLabel(expSeries.getName() + " " + seriesRun.getLabel());
		expSeries.getExperimentSeriesRuns().add(seriesRun);

		engine.getPersistenceProvider().store(seriesRun);

		// prepare the experiment series
		ParameterCollection<ParameterValue<?>> constantParamValues = EngineTools.getConstantParameterValues(expSeries
				.getExperimentSeriesDefinition().getPreperationAssignments());
		expController.prepareExperimentSeries(seriesRun, constantParamValues);

		List<IParameterVariation> pvList = new ArrayList<IParameterVariation>();

		for (ParameterValueAssignment pva : expSeriesDef.getExperimentAssignments()) {
			IParameterVariation pv = null;
			if (pva instanceof DynamicValueAssignment) {
				final DynamicValueAssignment dva = (DynamicValueAssignment) pva;
				pv = registry.getExtensionArtifact(IParameterVariationExtension.class, dva.getName());
				if (pv == null) {
					throw new RuntimeException("Could not find required parameter variation!");
				}

			} else if (pva instanceof ConstantValueAssignment) {
				pv = registry.getExtensionArtifact(IParameterVariationExtension.class,
						IParameterVariationExtension.CONSTANT_VARIATION_EXTENSION_NAME);
			} else {
				LOGGER.error("Parameter value assignment {} is not supported.", pva.getClass().getSimpleName());
				throw new RuntimeException("Parameter value assignment is not supported.");
			}

			// if a parameter value is achieved
			if (pv != null) {
				if (pv.canVary(pva)) {
					pv.initialize(pva);
					pvList.add(pv);
				} else {
					LOGGER.error("{} does not support parameter variation for {}.", pv.getProvider().getName(), pva
							.getParameter().getFullName());
					throw new GenericException();
				}
			}

		}

		expStrategy.runExperimentSeries(seriesRun, pvList);

		expController.finalizeExperimentSeries();
	}

}

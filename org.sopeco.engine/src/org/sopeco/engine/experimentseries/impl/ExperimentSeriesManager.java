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

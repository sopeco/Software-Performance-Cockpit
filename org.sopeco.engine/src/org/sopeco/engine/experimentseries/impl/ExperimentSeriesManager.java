/**
 * 
 */
package org.sopeco.engine.experimentseries.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.EngineFactory;
import org.sopeco.engine.IEngine;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experimentseries.IExperimentSeriesManager;
import org.sopeco.engine.experimentseries.IExplorationStrategy;
import org.sopeco.engine.experimentseries.IExplorationStrategyExtension;
import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.experimentseries.IParameterVariationExtension;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.engine.util.EngineTools;
import org.sopeco.model.configuration.measurements.ConstantValueAssignment;
import org.sopeco.model.configuration.measurements.DynamicValueAssignment;
import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.model.configuration.measurements.ParameterValueAssignment;
import org.sopeco.persistence.PersistenceProviderFactory;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;

/**
 * The default implementation of {@link ExperimentSeriesManager}.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class ExperimentSeriesManager implements IExperimentSeriesManager {

	private final static Logger logger = LoggerFactory.getLogger(ExperimentSeriesManager.class);
	
	@Override
	public boolean canRun(ExperimentSeriesDefinition expSeries) {
		// TODO Do we need this? It would not be efficient.
		return false;
	}

	@Override
	public void runExperimentSeries(ExperimentSeries expSeries) {
		final IEngine engine = EngineFactory.INSTANCE.getEngine();
		final IExtensionRegistry registry = engine.getExtensionRegistry();
		final ExperimentSeriesDefinition expSeriesDef = expSeries.getExperimentSeriesDefinition();
		IExperimentController expController = engine.getExperimentController();
		
		IExplorationStrategy expStrategy = registry.getExtensionArtifact(IExplorationStrategyExtension.class, 
				expSeriesDef.getExplorationStrategy().getName());
		// TODO what if it's null
		expStrategy.setExperimentController(expController);
		expStrategy.setPersistenceProvider(engine.getPersistenceProvider());
		expStrategy.setExtensionRegistry(registry);
		
		if (!expStrategy.canRun(expSeriesDef)) {
			// TODO throw runtime exception
		}

		// TODO factory for persistence entities
		ExperimentSeriesRun seriesRun = PersistenceProviderFactory.createExperimentSeriesRun(expSeries);
		
		// prepare the experiment series
		List<ParameterValue<?>> constantParamValues = EngineTools.getConstantParameterValues(expSeries.getExperimentSeriesDefinition().getPreperationAssignments());
		expController.prepareExperimentSeries(seriesRun, constantParamValues);
		
		List<IParameterVariation> pvList = new ArrayList<IParameterVariation>();
		
		for (ParameterValueAssignment pva: expSeriesDef.getExperimentAssignments()) {
			IParameterVariation pv = null;
			if (pva instanceof DynamicValueAssignment) {
				final DynamicValueAssignment dva = (DynamicValueAssignment)pva;
				pv = registry.getExtensionArtifact(IParameterVariationExtension.class, dva.getName());
			
				// TODO error if it is null

			} else 
				if (pva instanceof ConstantValueAssignment) {
					pv = registry.getExtensionArtifact(IParameterVariationExtension.class, IParameterVariationExtension.CONSTANT_VARIATION_EXTENSION_NAME);
				} else {
					// TODO error?
					logger.error("Parameter value assignment {} is not supported.", pva.getClass().getSimpleName());
				}
			
			// if a parameter value is achieved
			if (pv != null) {
				if (pv.canVary(pva)) { 
					pv.initialize(pva);
					pvList.add(pv);
				}
				// else
				// TODO error if it is not supported
			} 

		}

		expStrategy.runExperimentSeries(expSeries, pvList);
		
		expController.finalizeExperimentSeries();
	}

}

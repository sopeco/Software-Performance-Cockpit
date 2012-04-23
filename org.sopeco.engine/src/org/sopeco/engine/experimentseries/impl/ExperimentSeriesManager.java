/**
 * 
 */
package org.sopeco.engine.experimentseries.impl;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.engine.EngineFactory;
import org.sopeco.engine.IEngine;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experimentseries.IExperimentSeriesManager;
import org.sopeco.engine.experimentseries.IExplorationStrategy;
import org.sopeco.engine.experimentseries.IExplorationStrategyExtension;
import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.experimentseries.IParameterVariationExtension;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.model.configuration.measurements.ConstantValueAssignment;
import org.sopeco.model.configuration.measurements.DynamicValueAssignment;
import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.model.configuration.measurements.ParameterValueAssignment;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;

/**
 * The default implementation of {@link ExperimentSeriesManager}.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class ExperimentSeriesManager implements IExperimentSeriesManager {

	@Override
	public boolean canRun(ExperimentSeriesDefinition expSeries) {
		// TODO implement!
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
		ExperimentSeriesRun seriesRun = new ExperimentSeriesRun(System.currentTimeMillis());
		
		// TODO do the preparation
		//expController.prepareExperimentSeries(seriesRun, expSeries.getExperimentSeriesDefinition().getPreperationAssignments());
		
		List<IParameterVariation> pvList = new ArrayList<IParameterVariation>();
		
		for (ParameterValueAssignment pva: expSeriesDef.getExperimentAssignments()) {
			if (pva instanceof DynamicValueAssignment) {
				final DynamicValueAssignment dva = (DynamicValueAssignment)pva;
				final IParameterVariation pv = registry.getExtensionArtifact(IParameterVariationExtension.class, dva.getName());
				// TODO what if it's null
				pvList.add(pv);
			} else 
				if (pva instanceof ConstantValueAssignment) {
					// TODO take care of constants
				} else {
					// TODO logger message
				}
		}

		expStrategy.runExperimentSeries(expSeries, pvList);
		
		expController.finalizeExperimentSeries();
	}

}

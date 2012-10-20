package org.sopeco.plugin.std.exploration.screening;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experimentseries.IExplorationStrategy;
import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.persistence.util.ParameterCollectionFactory;
import org.sopeco.plugin.std.exploration.screening.adapter.FractionalFactorialAdapter;
import org.sopeco.plugin.std.exploration.screening.adapter.FullFactorialAdapter;
import org.sopeco.plugin.std.exploration.screening.adapter.IScreeningAdapter;
import org.sopeco.plugin.std.exploration.screening.adapter.PlackettBurmanAdapter;
import org.sopeco.plugin.std.exploration.screening.config.ScreeningConfiguration;

/**
 * A ScreeningExplorationImpl explores the parameter space according to a
 * screening design created with R, using a minimal amount of experiment runs.
 * 
 * @author Pascal Meier
 */
public final class ScreeningDesignExploration extends AbstractSoPeCoExtensionArtifact implements IExplorationStrategy {

	
	/**
	 * Adapter used to generate the screening design.
	 */
	private IScreeningAdapter adapter = null;
	/**
	 * ExperimentController used to execute the measurements.
	 */
	private IExperimentController experimentController = null;

	private int numberOfMeasurements;
	
	private IExtensionRegistry sopecoExtensionRegistry;
	
	private ExplorationStrategy strategyConfig;

	
	public ScreeningDesignExploration(ISoPeCoExtension<?> provider) {
		super(provider);
	}



	@Override
	public boolean canRun(ExperimentSeriesDefinition expSeries) {
		String strategyName = expSeries.getExplorationStrategy().getName();
		return strategyName.equalsIgnoreCase(ScreeningConfiguration.FULL_FACTORIAL)
				|| strategyName.equalsIgnoreCase(ScreeningConfiguration.FRACTIONAL_FACTORIAL)
				|| strategyName.equalsIgnoreCase(ScreeningConfiguration.PLACKETT_BURMAN);
	}

	@Override
	public void runExperimentSeries(ExperimentSeriesRun expSeriesRun, List<IParameterVariation> parameterVariations) {
		
		if (parameterVariations == null || parameterVariations.isEmpty()) {
			throw new IllegalArgumentException("No information about parameter variation provided!");
		}
		
		strategyConfig = expSeriesRun.getExperimentSeries().getExperimentSeriesDefinition().getExplorationStrategy();
		
		if (strategyConfig.getName().equalsIgnoreCase(ScreeningConfiguration.FULL_FACTORIAL)) {
			adapter = new FullFactorialAdapter();
		} else if (strategyConfig.getName().equalsIgnoreCase(ScreeningConfiguration.FRACTIONAL_FACTORIAL)) {
			adapter = new FractionalFactorialAdapter();
		} else if (strategyConfig.getName().equalsIgnoreCase(ScreeningConfiguration.PLACKETT_BURMAN)) {
			adapter = new PlackettBurmanAdapter();
		} else {
			throw new IllegalArgumentException("Strategy not supported: " + strategyConfig.getName());
		}
		
		adapter.setParameterVariations(parameterVariations);
		adapter.setExplorationConf(strategyConfig);
		adapter.generateDesign();
		
		numberOfMeasurements = 0;
		
		executeExperimentSeries(expSeriesRun);
		
	}
	
	/**
	 * Execute the experiments series.
	 *
	 */
	private void executeExperimentSeries(ExperimentSeriesRun expSeriesRun) {

		// As long as not all experiment runs have been executed:
		// determine next parameters and execute the experiment-run

		numberOfMeasurements = adapter.getExpDesign().getNumberOfRuns();

		for (int i = 0; i < adapter.getExpDesign().getNumberOfRuns(); i++) {
			List<ParameterValue<?>> valuesOfRun = new ArrayList<ParameterValue<?>>();
			valuesOfRun.addAll(adapter.getConstantParameterValues());
			valuesOfRun.addAll(adapter.getExpDesign()
					.getParameterValuesByRun(i));
			ParameterCollection<ParameterValue<?>> paramValueCollection = ParameterCollectionFactory.createParameterValueCollection(valuesOfRun);
			
			experimentController.runExperiment(paramValueCollection, expSeriesRun.getExperimentSeries().getExperimentSeriesDefinition()
					.getExperimentTerminationCondition());
		}
	}

	/**
	 * Returns the number of measurements of the last ExperimentSeries.
	 * 
	 * @return number of measurements
	 */
	public int getNumberOfMeasurements() {
		return numberOfMeasurements;
	}

	@Override
	public void setExperimentController(
			IExperimentController experimentController) {
		this.experimentController = experimentController;
	}

	@Override
	public void setPersistenceProvider(IPersistenceProvider persistenceProvider) {
		// not used
		
	}

	@Override
	public void setExtensionRegistry(IExtensionRegistry registry) {
		this.sopecoExtensionRegistry = registry;
		
	}
}

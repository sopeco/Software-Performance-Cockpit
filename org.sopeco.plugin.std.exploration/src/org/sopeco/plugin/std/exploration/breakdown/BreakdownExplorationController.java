package org.sopeco.plugin.std.exploration.breakdown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sopeco.engine.analysis.IPredictionFunctionStrategy;
import org.sopeco.engine.analysis.IPredictionFunctionStrategyExtension;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experimentseries.IExplorationStrategy;
import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.engine.registry.Extensions;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.exploration.breakdown.environment.AlgorithmsEnvironment;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess;
import org.sopeco.plugin.std.exploration.breakdown.stop.AbstractStopController;
import org.sopeco.plugin.std.exploration.breakdown.stop.StopControllerFactory;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration;
import org.sopeco.plugin.std.exploration.breakdown.util.IMeasurementSeriesAwareable;

/**
 * This class implements the SoPeCo extension for the different breakdown
 * exploration strategies.
 * 
 * @author Dennis Westermann, Rouven Krebs
 */
public class BreakdownExplorationController extends AbstractSoPeCoExtensionArtifact implements IMeasurementSeriesAwareable, IExplorationStrategy {

	IExperimentController experimentController;
	IPersistenceProvider persistenceProvider;
	IExtensionRegistry extensionRegistry;
	ExplorationStrategy strategyConfig;

	public BreakdownExplorationController(ISoPeCoExtension<?> provider) {
		super(provider);
	}

	IBreakdownExploration breakdownImpl;

	@Override
	public long getNumberOfMeasurements() {
		return breakdownImpl.getNumberOfMeasurements();
	}

	@Override
	public double getSizeOfParameterSpace() {
		return breakdownImpl.getSizeOfParameterSpace();
	}

	@Override
	public boolean canRun(ExperimentSeriesDefinition expSeries) {
		String strategyName = expSeries.getExplorationStrategy().getName();
		return strategyName.equalsIgnoreCase(BreakdownConfiguration.EQUIDISTANT_BREAKDOWN)
				|| strategyName.equalsIgnoreCase(BreakdownConfiguration.RANDOM_BREAKDOWN)
				|| strategyName.equalsIgnoreCase(BreakdownConfiguration.ADAPTIVE_EQUIDISTANT_BREAKDOWN)
				|| strategyName.equalsIgnoreCase(BreakdownConfiguration.ADAPTIVE_RANDOM_BREAKDOWN);
	}

	@Override
	public void runExperimentSeries(ExperimentSeriesRun expSeriesRun, List<IParameterVariation> parameterVariations) {

		if (canRun(expSeriesRun.getExperimentSeries().getExperimentSeriesDefinition())) {
			strategyConfig = expSeriesRun.getExperimentSeries().getExperimentSeriesDefinition().getExplorationStrategy();
		} else {
			throw new IllegalArgumentException("Strategy ("
					+ expSeriesRun.getExperimentSeries().getExperimentSeriesDefinition().getExplorationStrategy().getName() + ") not supported.");
		}
		createBreakdownImplementation(parameterVariations, expSeriesRun);

		// initialise
		breakdownImpl.initialise();

		// maybe we reached a stop condition like time.
		if (breakdownImpl.reachedStopCondition()) {
			breakdownImpl.finishWork();
			return;
		}

		// we measure all the borders if configured
		breakdownImpl.measureBordersOfParameterSpace();

		while (!breakdownImpl.reachedStopCondition()) {

			breakdownImpl.doIteration();
			breakdownImpl.validate();

		}

		breakdownImpl.finishWork();

	}

	@Override
	public void setPersistenceProvider(IPersistenceProvider persistenceProvider) {
		this.persistenceProvider = persistenceProvider;
	}

	@Override
	public void setExperimentController(IExperimentController experimentController) {
		this.experimentController = experimentController;

	}

	@Override
	public void setExtensionRegistry(IExtensionRegistry registry) {
		this.extensionRegistry = registry;

	}

	private void createBreakdownImplementation(List<IParameterVariation> parameterVariations, ExperimentSeriesRun expSeriesRun) {
		AlgorithmsEnvironment algorithmEnvironment = createAlgorithmEnvironment(parameterVariations, expSeriesRun);
		EnvironmentCachedAccess environmentCachedAcess = new EnvironmentCachedAccess(algorithmEnvironment);
		AbstractStopController stopController = StopControllerFactory.createStopController(strategyConfig, this, algorithmEnvironment);
		List<ParameterDefinition> parameterDefinitions = getParameterDefinitions(parameterVariations);

		if (strategyConfig.getName().equalsIgnoreCase(BreakdownConfiguration.EQUIDISTANT_BREAKDOWN)) {
			breakdownImpl = new EquidistantBreakdownImpl(strategyConfig, environmentCachedAcess, parameterDefinitions, stopController, provider);
		} else if (strategyConfig.getName().equalsIgnoreCase(BreakdownConfiguration.RANDOM_BREAKDOWN)) {
			breakdownImpl = new RandomBreakdownImpl(strategyConfig, environmentCachedAcess, parameterDefinitions, stopController, provider);
		} else if (strategyConfig.getName().equalsIgnoreCase(BreakdownConfiguration.ADAPTIVE_EQUIDISTANT_BREAKDOWN)) {
			breakdownImpl = new AdaptiveEquidistantBreakdownImpl(strategyConfig, environmentCachedAcess, parameterDefinitions, stopController, provider);
		} else if (strategyConfig.getName().equalsIgnoreCase(BreakdownConfiguration.ADAPTIVE_RANDOM_BREAKDOWN)) {
			breakdownImpl = new AdaptiveRandomBreakdownImpl(strategyConfig, environmentCachedAcess, parameterDefinitions, stopController, provider);
		} else {
			throw new IllegalArgumentException("Strategy not supported: " + strategyConfig.getName());
		}
	}

	private AlgorithmsEnvironment createAlgorithmEnvironment(List<IParameterVariation> parameterVariations, ExperimentSeriesRun expSeriesRun) {

		return new AlgorithmsEnvironment(experimentController, persistenceProvider, getAnalyzer(), strategyConfig.getAnalysisConfigurations().get(0),
				parameterVariations, BreakdownConfiguration.getMainPerformanceIndicator(strategyConfig, expSeriesRun), expSeriesRun);

	}

	private List<ParameterDefinition> getParameterDefinitions(List<IParameterVariation> parameterVariations) {
		List<ParameterDefinition> allParameters = new ArrayList<ParameterDefinition>(parameterVariations.size());
		for (IParameterVariation pv : parameterVariations) {
			allParameters.add(pv.getParameter());
		}

		return allParameters;
	}

	private IPredictionFunctionStrategy getAnalyzer() {

		if (strategyConfig.getAnalysisConfigurations().isEmpty()) {
			// try to get any analysis TODO: change from any to a specific from
			// our std.analyses?
			Extensions<IPredictionFunctionStrategyExtension> availableExtensions = extensionRegistry.getExtensions(IPredictionFunctionStrategyExtension.class);
			if (availableExtensions.getList().isEmpty()) {
				throw new IllegalStateException("Cannot find an appropriate analysis strategy extension. "
						+ "Exploration strategy requires an IPredictionFunctionStrategy.");
			} else {
				IPredictionFunctionStrategyExtension defaultExtension = availableExtensions.getList().get(0);
				AnalysisConfiguration analysisConfig = EntityFactory.createAnalysisConfiguration(defaultExtension.getName(), new HashMap<String, String>());
				strategyConfig.getAnalysisConfigurations().add(analysisConfig);
				return defaultExtension.createExtensionArtifact();
			}
		} else {
			return extensionRegistry.getExtensionArtifact(IPredictionFunctionStrategyExtension.class, strategyConfig.getAnalysisConfigurations().get(0)
					.getName());
		}

	}

}

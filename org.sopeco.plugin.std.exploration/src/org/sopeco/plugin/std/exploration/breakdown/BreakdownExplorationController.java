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
			breakdownImpl = new EquidistantBreakdownImpl(strategyConfig, environmentCachedAcess, parameterDefinitions, stopController, getProvider());
		} else if (strategyConfig.getName().equalsIgnoreCase(BreakdownConfiguration.RANDOM_BREAKDOWN)) {
			breakdownImpl = new RandomBreakdownImpl(strategyConfig, environmentCachedAcess, parameterDefinitions, stopController, getProvider());
		} else if (strategyConfig.getName().equalsIgnoreCase(BreakdownConfiguration.ADAPTIVE_EQUIDISTANT_BREAKDOWN)) {
			breakdownImpl = new AdaptiveEquidistantBreakdownImpl(strategyConfig, environmentCachedAcess, parameterDefinitions, stopController, getProvider());
		} else if (strategyConfig.getName().equalsIgnoreCase(BreakdownConfiguration.ADAPTIVE_RANDOM_BREAKDOWN)) {
			breakdownImpl = new AdaptiveRandomBreakdownImpl(strategyConfig, environmentCachedAcess, parameterDefinitions, stopController, getProvider());
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

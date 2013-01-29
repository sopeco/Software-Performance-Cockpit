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
		IScreeningAdapter adapter = null;
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

		executeExperimentSeries(expSeriesRun, adapter);

		adapter.releaseAnalysisResources();

	}

	/**
	 * Execute the experiments series.
	 * 
	 */
	private void executeExperimentSeries(ExperimentSeriesRun expSeriesRun, IScreeningAdapter adapter) {

		// As long as not all experiment runs have been executed:
		// determine next parameters and execute the experiment-run

		numberOfMeasurements = adapter.getExpDesign().getNumberOfRuns();

		for (int i = 0; i < adapter.getExpDesign().getNumberOfRuns(); i++) {
			List<ParameterValue<?>> valuesOfRun = new ArrayList<ParameterValue<?>>();
			valuesOfRun.addAll(adapter.getConstantParameterValues());
			valuesOfRun.addAll(adapter.getExpDesign().getParameterValuesByRun(i));
			ParameterCollection<ParameterValue<?>> paramValueCollection = ParameterCollectionFactory
					.createParameterValueCollection(valuesOfRun);

			experimentController.runExperiment(paramValueCollection);
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
	public void setExperimentController(IExperimentController experimentController) {
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

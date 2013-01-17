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
package org.sopeco.plugin.std.exploration.full;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experimentseries.IExplorationStrategy;
import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.persistence.util.ParameterCollectionFactory;
import org.sopeco.util.Tools;

/**
 * A FullExplorationStrategy explores all possible combinations of values for
 * the given parameters.
 * 
 * [Chris:] I find two possible ways to implement the exploration: 1)
 * Create a List of all possible combinations Pro: A future handling of
 * candidates to skip might be easier Con: Big initial overhead + memory usage
 * 2) Derive only the next combination of values Pro: Less Memory Usage Con:
 * Evtl. harder to handle skipable combinations
 * 
 * Right now 2) is implemented, only "LinearIntegerVariation" are supported.
 * 
 * @author Roozbeh Farahbod, Chris
 * 
 */
public class FullExplorationStrategy extends AbstractSoPeCoExtensionArtifact implements IExplorationStrategy {

	/** The list of the ParameterVariations that have to be explored. **/
	private ArrayList<IParameterVariation> variationImplementations = new ArrayList<IParameterVariation>();
	/** The list of ParameterValues that have last been explored. **/
	private List<ParameterValue<?>> parameterValues = new ArrayList<ParameterValue<?>>();
	/** The index of the parameter to be varied. **/
	private int index = 0;

	private static Logger logger = LoggerFactory.getLogger(FullExplorationStrategy.class);
	
	private IExperimentController expController = null;

	protected FullExplorationStrategy(FullExplorationStrategyExtension provider) {
		super(provider);
	}
	
	@Override
	public void setPersistenceProvider(IPersistenceProvider persistenceProvider) {
		// don't need it
	}

	@Override
	public void setExperimentController(IExperimentController experimentController) {
		this.expController = experimentController;
	}

	@Override
	public boolean canRun(ExperimentSeriesDefinition expSeries) {
		return Tools.strEqualName(getProvider().getName(), expSeries.getExplorationStrategy().getName());
	}

	@Override
	public void runExperimentSeries(ExperimentSeriesRun expSeriesRun, List<IParameterVariation> parameterVariations) {
		initialiseExplorationStrategy(parameterVariations);
		executeExperimentSeries();
	}

	public ParameterCollection<ParameterValue<?>> getCurrentParameterValues() {
		return ParameterCollectionFactory.createParameterValueCollection(parameterValues);
	}

	private void initialiseExplorationStrategy(
			List<IParameterVariation> variationImplementations) {
		
		// Precondition: There may not be no information about how to vary the parameters
		assert variationImplementations != null : "Cannot explore Parameters: No informations about how to vary!";
		
		this.variationImplementations = new ArrayList<IParameterVariation>();
		this.parameterValues = new ArrayList<ParameterValue<?>>();
		for (IParameterVariation parameterVariationImplementation : variationImplementations) {
			// Add VariationImplementation to List
			this.variationImplementations.add(parameterVariationImplementation);
			
			// Create new ParameterValueAssignment and assign minimum Value
			ParameterValue<?> parameterValueAssignment = parameterVariationImplementation.iterator().next();
				
			// Add ParameterValueAssignment to List
			parameterValues.add(parameterValueAssignment);
		}
		
	}

	private void executeExperimentSeries() {

		// List InputParameter' ParameterValues for the runs
		ParameterCollection<ParameterValue<?>> inputParameterValues = getCurrentParameterValues();
		
		int count = 1;
		// As long as parameters can be varied: determine next parameters and execute the experiment-run
		while (!(inputParameterValues == null)) {
			
			logger.debug("Executing experiment run {}.", count++);
			
			expController.runExperiment(ParameterCollectionFactory.createParameterValueCollection(parameterValues));
			
			inputParameterValues = ParameterCollectionFactory.createParameterValueCollection(getNextParameterValues());
		}
		
	}

	
	/**
	 * Calculate and return the next list of ParameterValueAssignments.
	 */
	private List<ParameterValue<?>> getNextParameterValues() {
		
		// Vary the next parameter
		index = 0;
		while (index < variationImplementations.size()) {
			// If the maximum Value for a parameter is reached set it back to default and increase the next one
			if (!variationImplementations.get(index).iterator().hasNext()) {
				variationImplementations.get(index).reset();
				parameterValues.set(index, variationImplementations.get(index).iterator().next());
				index++;
			}
			// Increase the Parameter and return the whole list
			else {
				parameterValues.set(index, variationImplementations.get(index).iterator().next());
				return parameterValues;
			}
		}
		return null;
	}

	@Override
	public void setExtensionRegistry(IExtensionRegistry registry) {
		// don't need it
	}


}

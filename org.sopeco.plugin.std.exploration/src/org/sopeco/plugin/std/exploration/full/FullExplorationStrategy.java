
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
import org.sopeco.engine.util.ParameterCollection;
import org.sopeco.engine.util.ParameterCollectionFactory;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
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
		return Tools.strEqualName(provider.getName(), expSeries.getExplorationStrategy().getName());
	}

	@Override
	public void runExperimentSeries(ExperimentSeriesRun expSeriesRun, List<IParameterVariation> parameterVariations) {
		initialiseExplorationStrategy(parameterVariations);
		executeExperimentSeries(expSeriesRun.getExperimentSeries().getExperimentSeriesDefinition().getExperimentTerminationCondition());
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

	private void executeExperimentSeries(ExperimentTerminationCondition terminationCondition) {

		// List InputParameter' ParameterValues for the runs
		ParameterCollection<ParameterValue<?>> inputParameterValues = getCurrentParameterValues();
		
		int count = 1;
		// As long as parameters can be varied: determine next parameters and execute the experiment-run
		while (!(inputParameterValues == null)) {
			
			logger.debug("Executing experiment run {}.", count++);
			
			expController.runExperiment(ParameterCollectionFactory.createParameterValueCollection(parameterValues), terminationCondition);
			
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

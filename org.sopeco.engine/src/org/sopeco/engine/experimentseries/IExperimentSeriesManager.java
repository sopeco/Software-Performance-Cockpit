package org.sopeco.engine.experimentseries;

import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.ExperimentSeries;

/**
 * The ExperimentSeriesManager manages the execution of any single experiment
 * series.
 * 
 * For that purpose it creates VariationStrategies for InputParameters, creates
 * and selects ExplorationStrategy plugins, can create Analysis- and
 * ValidationStrategies and runs the ExplorationStrategy (which gets
 * TerminationCondition).
 * 
 * @author Jens Happe, Roozbeh Farahbod
 * 
 */
public interface IExperimentSeriesManager {

	/**
	 * Checks if this implementation can run the given experiment series definition.
	 * 
	 * @param expSeries an experiment series definition
	 * 
	 * @return <code>true</code> if it can run the series.
	 */
	public boolean canRun(ExperimentSeriesDefinition expSeries);

	/**
	 * Runs the experiment series represented by the given experiment series instance.
	 * 
	 * @param expSeries an experiment series instance
	 */
	public void runExperimentSeries(ExperimentSeries expSeries);

}

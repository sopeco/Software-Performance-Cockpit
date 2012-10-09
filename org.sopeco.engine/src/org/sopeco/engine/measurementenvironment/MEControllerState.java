package org.sopeco.engine.measurementenvironment;

/**
 * Possible states of an MEController.
 * 
 * @author Alexander Wert
 * 
 */
public enum MEControllerState {
	/**
	 * The MEController is available and not used by another process.
	 */
	AVAILABLE,
	/**
	 * The MEController is currently in use.
	 */
	IN_USE,
	/**
	 * The MEController is currently in an initialization phase.
	 */
	INITIALIZATION,
	/**
	 * The MEController prepares an experiment series.
	 */
	SERIES_PREPARATION,
	/**
	 * The MEController conducts an experiment.
	 */
	EXPERIMENT_EXECUTION,
	/**
	 * The MEController is finalizing an experiment series.
	 */
	FINALIZING_SERIES
}

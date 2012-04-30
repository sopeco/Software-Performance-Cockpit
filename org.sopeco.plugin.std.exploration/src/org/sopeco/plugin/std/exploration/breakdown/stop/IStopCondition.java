package org.sopeco.plugin.std.exploration.breakdown.stop;

/**
 * Decides if the Algorithm should stop further processing.
 * 
 * @author Rouven Krebs
 */
interface IStopCondition 
{
	/**
	 * called once before the exploration starts.
	 */
	void init();
	
	/**
	 * called in a periodic way to resolve if the series should be stopped.
	 * @return true if further exploration should be stopped.
	 */
	boolean shouldStop();
}

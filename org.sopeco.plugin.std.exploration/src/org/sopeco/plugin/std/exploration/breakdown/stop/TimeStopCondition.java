package org.sopeco.plugin.std.exploration.breakdown.stop;

/**
 * Condition stops the series / exploration if the time is over. 
 *
 */
class TimeStopCondition implements IStopCondition 
{
	
	private long duration;
	private long startTimestamp;

	/**
	 * 
	 * @param milliseconds how long the exploration should run.
	 */
	TimeStopCondition(long milliseconds)
	{
		this.duration = milliseconds;
	}
	
	@Override
	public void init() 
	{
		this.startTimestamp = System.currentTimeMillis();
	}

	@Override
	public boolean shouldStop() 
	{
		return this.startTimestamp + this.duration < System.currentTimeMillis();
	}

}

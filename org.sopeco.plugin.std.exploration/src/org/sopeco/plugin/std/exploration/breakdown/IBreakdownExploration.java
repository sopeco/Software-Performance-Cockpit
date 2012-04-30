package org.sopeco.plugin.std.exploration.breakdown;

import org.sopeco.plugin.std.exploration.breakdown.util.IMeasurementSeriesAwareable;


public interface IBreakdownExploration extends IMeasurementSeriesAwareable{
	void initialise();
	void measureBordersOfParameterSpace();
	void doIteration();
	void finishWork();
	boolean hasMoreIterations();
	void validate();
	boolean reachedStopCondition();
	public long getNumberOfMeasurements();
}

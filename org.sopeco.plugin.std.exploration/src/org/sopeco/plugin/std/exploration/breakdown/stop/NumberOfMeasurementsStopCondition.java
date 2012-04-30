package org.sopeco.plugin.std.exploration.breakdown.stop;

import org.sopeco.plugin.std.exploration.breakdown.util.IMeasurementSeriesAwareable;

/**
 * Stops if a defined number of measurements are done.
 * 
 * @author Rouven Krebs
 *
 */
class NumberOfMeasurementsStopCondition implements IStopCondition {
	
	private long maxNumberOfMeasurements;
	private IMeasurementSeriesAwareable info;

	/**
	 * 
	 * @param maxNumberOfMeasurements number of measurements allowed.
	 * @param info the object giving information about the number of executed measurements.
	 */
	NumberOfMeasurementsStopCondition(long maxNumberOfMeasurements, IMeasurementSeriesAwareable info) {
		this.maxNumberOfMeasurements = maxNumberOfMeasurements;
		this.info = info;
	}
	
	@Override
	public void init() {
		
	}

	@Override
	public boolean shouldStop() {
		return this.info.getNumberOfMeasurements() >= this.maxNumberOfMeasurements;
	}


}

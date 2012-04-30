package org.sopeco.plugin.std.exploration.breakdown.util;

/**
 * Interface for Entities having knowledge about the Measurement Series.
 * 
 * @author Rouven Krebs
 *
 */
public interface IMeasurementSeriesAwareable 
{

	/**
	 * 
	 * @return the number of executed measurements.
	 */
	long getNumberOfMeasurements();
	
	double getSizeOfParameterSpace();

}

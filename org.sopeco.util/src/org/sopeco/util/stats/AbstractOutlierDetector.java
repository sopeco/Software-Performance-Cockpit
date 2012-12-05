/**
 * 
 */
package org.sopeco.util.stats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The abstract class of outlier detectors.
 * 
 * @author Roozbeh Farahbod
 *
 */
public abstract class AbstractOutlierDetector {

	/**
	 * Filters outliers from the given set of values.
	 * 
	 * @param values a list of numeric values
	 * @return a filtered set of the input values without the outliers
	 */
	public abstract List<Double> filterOutliers(double[] values);
	
	/**
	 * Filters outliers from the given set of values.
	 * 
	 * @param values a list of numeric values
	 * @return a filtered set of the input values without the outliers
	 * 
	 * @see #filterOutliers(double[])
	 * 
	 * @throws IllegalArgumentException if any of the values are null.
	 */
	public List<Double> filterOutliers(List<Double> values) {
		return filterOutliers(convertListToArray(values));
	}
	
	/**
	 * Marks the outliers in the given list of values.
	 * 
	 * @param values a list of numeric data
	 * @return  a map from values to a Boolean flag which indicates if a value is an outlier (<code>true</code>) or not (<code>false</code>).
	 */
	public Map<Double, Boolean> markOutliersUsingIQR(double[] values) {

		List<Double> filtered = filterOutliers(values);
		
		Map<Double, Boolean> resultMap = new HashMap<Double, Boolean>();
		// first mark all as outliers
		for (double value: values) {
			resultMap.put(value, true);
		}
		// unmark the surviving ones as non-outlier
		for (Double nonOutlier: filtered) {
			resultMap.put(nonOutlier, false);
		}
		
		return resultMap;
	}

	/**
	 * Marks the outliers in the given list of values.
	 * 
	 * @param values a list of numeric data
	 * @return  a map from values to a Boolean flag which indicates if a value is an outlier (<code>true</code>) or not (<code>false</code>).
	 * 
	 * @see #markOutliersUsingIQR(double[])
	 * 
	 * @throws IllegalArgumentException if any of the values are null.
	 */
	public Map<Double, Boolean> markOutliersUsingIQR(List<Double> values) {
		return markOutliersUsingIQR(convertListToArray(values));
	}

	/**
	 * Converts a list of values to an array.
	 * 
	 * @param values list of values
	 * @return an array of values
	 * 
	 * @throws IllegalArgumentException if any of the values are null.
	 */
	protected double[] convertListToArray(List<Double> values) {
		double[] dValues = new double[values.size()];
		int i = 0;
		for (Double value: values) {
			if (value == null) {
				throw new IllegalArgumentException("Values in the array cannot be null.");
			} else {
				dValues[i] = value;
			}
			i++;
		}
		return dValues;
	}
}

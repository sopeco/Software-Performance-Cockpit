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

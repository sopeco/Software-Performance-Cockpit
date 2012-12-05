/**
 * 
 */
package org.sopeco.util.stats;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * The implementation of Inner Quartile Range outlier detector.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class IQROutlierDetector extends AbstractOutlierDetector {

	private static final int UPPER_QUARTILE = 75;

	private static final int LOWER_QUARTILE = 25;

	public static final double DEFAULT_IQR_FACTOR = 1.5;
	
	protected double iqrFactor = DEFAULT_IQR_FACTOR;
	
	/**
	 * Creates a new IQR outlier detector with the default value for the 
	 * IQR factor (i.e., {@value #DEFAULT_IQR_FACTOR}).
	 * 
	 * @see #DEFAULT_IQR_FACTOR
	 */
	public IQROutlierDetector() {
	}

	/**
	 * Creates a new IQR outlier detector with the given value for the 
	 * IQR factor.
	 *
	 * @param iqrFactor the IQR factor
	 */
	public IQROutlierDetector(double iqrFactor) {
		this.iqrFactor = iqrFactor;
	}
	
	@Override
	public List<Double> filterOutliers(double[] values) {
		DescriptiveStatistics ds = new DescriptiveStatistics(values);
		
		double firstQuartile = ds.getPercentile(LOWER_QUARTILE);
		double thirdQuartile = ds.getPercentile(UPPER_QUARTILE);
		double iqr = thirdQuartile - firstQuartile;
		double lowerRange = firstQuartile - iqrFactor * iqr;
		double higherRange = thirdQuartile + iqrFactor * iqr;
		
		List<Double> results = new ArrayList<Double>();
		
		for (Double value: values) {
			if (value <= higherRange && value >= lowerRange) {
				results.add(value);
			}
		}
		
		return results;
	}

}

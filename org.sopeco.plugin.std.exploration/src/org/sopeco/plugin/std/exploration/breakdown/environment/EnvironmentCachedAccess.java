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
package org.sopeco.plugin.std.exploration.breakdown.environment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.plugin.std.exploration.breakdown.space.RelativePosition;

/**
 * This class represent an proxy which caches measurements and data points added
 * to the model to prevent a second measurement of the same position or adding
 * the same position two times to the model.
 * 
 * @author Rouven Krebs
 * 
 * 
 */
public class EnvironmentCachedAccess {

	/**
	 * 
	 */
	private List<Double> invalidValues;

	private int numberOfValidMeasurements = 0;

	/**
	 * The target for the proxy. It is the environment doing the real stuff.
	 */
	private AlgorithmsEnvironment environment;

	/**
	 * the cached measurements which can be reused and don't have to measured
	 * again.
	 */
	private Map<Map<String, Object>, MeasurementCacheResult> measurementPoints = 
			new HashMap<Map<String, Object>, EnvironmentCachedAccess.MeasurementCacheResult>();

	/**
	 * the points added to the model / analysis before. To prevent the add of
	 * the same measurement two times
	 */
	private Set<Map<String, Object>> modelPoints = new HashSet<Map<String, Object>>();

	/**
	 * 
	 * @param environment
	 *            The environment which can be used by the Algorithm
	 */
	public EnvironmentCachedAccess(AlgorithmsEnvironment environment) {
		this.environment = environment;

	}

	/**
	 * Adds a measurement value to the data model with help of the
	 * {@link AlgorithmsEnvironment} if it is not yet added.
	 * 
	 * @param position
	 *            The position of the measurement
	 * @param value
	 *            the value measured at this position
	 * @throws FrameworkException
	 *             if the model add could not be completed by any reason.
	 */
	public boolean addToModel(RelativePosition position, AbstractEnvironmentValue value) {
		List<ParameterValue<?>> realPosition = this.environment.getRealPosition(position);
		Map<String, Object> cachePosition = this.getCachePosition(realPosition);

		if (!this.modelPoints.contains(cachePosition)) {
			this.environment.addToModel(position, value);
			this.modelPoints.add(cachePosition);
			return true;
		}

		return false;
	}

	/**
	 * Measures the observation data at the given position with help of the
	 * {@link AlgorithmsEnvironment} if it is not cached.
	 * 
	 * @param position
	 * @return the measured or cached Data
	 * @throws EnvironmentNotConfiguredException
	 *             if the environment is not correct configured
	 * @throws ParameterTypeNotSupportedException
	 *             if a String ParameterUsage was used as position
	 * @throws UnsupportedDataType
	 */
	public MeasurementCacheResult measure(RelativePosition position) {
		List<ParameterValue<?>> realPosition = this.environment.getRealPosition(position);

		Map<String, Object> cachePosition = this.getCachePosition(realPosition);
		if (this.measurementPoints.containsKey(cachePosition)) {
			MeasurementCacheResult mr = this.measurementPoints.get(cachePosition);
			return mr;
		} else {
			MeasurementCacheResult mr = new MeasurementCacheResult();
			mr.value = this.environment.measure(position);
			this.measurementPoints.put(this.getCachePosition(realPosition), mr);

			// if valid: increase counter -
			if (invalidValues != null) {
				if (!invalidMeasurementResult(mr.value)) {
					numberOfValidMeasurements++;
				}
			}

			return mr;
		}

	}

	/**
	 * The number of all Measurements executed. This is the same value as the
	 * number of measurements available in the cache.
	 * 
	 * @return the number of executed Measurements
	 */
	public long getNumberOfCachedMeasurements() {
		return this.measurementPoints.size();
	}

	/**
	 * The number of data points available in the model.
	 * 
	 * @return the number of points in model
	 */
	public long getNumberOfPointsInModel() {
		return this.modelPoints.size();
	}

	/**
	 * A class for storing a value. special class used because of the
	 * expectation that more features have to be added in the future.
	 */
	public static class MeasurementCacheResult {
		public AbstractEnvironmentValue value;
	}

	/**
	 * creates a key for the caching.
	 * 
	 * @param realPosition
	 * @return a Map which can be used as key for caching.
	 */
	private Map<String, Object> getCachePosition(List<ParameterValue<?>> realPosition) {
		Map<String, Object> returnMap = new TreeMap<String, Object>();
		for (ParameterValue<?> p : realPosition) {
			returnMap.put(p.getParameter().getFullName(), p.getValue());
		}

		return returnMap;
	}

	/**
	 * Checks if a position was added to the model before.
	 * 
	 * @param middlePosition
	 *            the position to be checked.
	 * @return true if the position is already existing in the model.
	 */
	public boolean isInModel(RelativePosition middlePosition) {
		Map<String, Object> position = this.getCachePosition(this.environment.getRealPosition(middlePosition));
		return this.modelPoints.contains(position);
	}

	/**
	 * @throws FrameworkException
	 * @see AlgorithmsEnvironment
	 */
	public AbstractEnvironmentValue predictValue(RelativePosition position) {
		return this.environment.predictValue(position);
	}

	// public double getGCVError(){
	//
	// return this.environment.getGCVError();
	// }

	/**
	 * adds a measurement explicit to the storage. The algorithm must call if it
	 * implicit calculates the result of a point. Points really measured are
	 * stored automatic.
	 * 
	 * @param measurement
	 *            the measurement
	 * @param position
	 *            the position
	 * @throws FrameworkException
	 */
	public void addToRepository(MeasurementCacheResult measurement, RelativePosition position) {
		List<ParameterValue<?>> realPosition = this.environment.getRealPosition(position);
		Map<String, Object> cachePosition = this.getCachePosition(realPosition);

		// this means there is no measurement result known on this position.
		// and therefore we might have to add this value to the repository and
		// into the cache.
		if (!this.measurementPoints.containsKey(cachePosition)) {
			this.measurementPoints.put(cachePosition, measurement);
			this.environment.storeCalculatedExperimentRunResult(measurement.value, position);
		}
	}

	private boolean invalidMeasurementResult(AbstractEnvironmentValue value) {
		for (Double invalidValue : invalidValues) {
			double doubleValue = invalidValue;
			if (value instanceof DoubleEnvironmentValue) {
				doubleValue = ((DoubleEnvironmentValue) value).getValue().getValueAsDouble();
			} else if (value instanceof IntegerEnvironmentValue) {
				doubleValue = ((IntegerEnvironmentValue) value).getValue().getValueAsDouble();
			}

			if (Math.abs(doubleValue - invalidValue) < 0.0001) {
				return true;
			}
		}
		return false;
	}

	public int getNumberOfValidMeasurements() {
		return numberOfValidMeasurements;
	}

	public void setInvalidValues(List<Double> invalidValues) {
		this.invalidValues = invalidValues;
	}

	public double getSizeOfParameterSpace() {
		return environment.getSizeOfParameterSpace();
	}

}

package org.sopeco.plugin.std.exploration.breakdown.environment;

import org.sopeco.persistence.dataset.ParameterValue;

/**
 * These values are used to present a generic view onto a
 * {@link ParameterValue}. It support the opportunity to execute
 * calculations between different {@link ParameterValue}s without knowledge
 * about the real instance type of {@link ParameterValue}.
 * 
 * @author Rouven Krebs, Dennis Westermann
 * 
 */
public abstract class AbstractEnvironmentValue {
	private ParameterValue<?> value;

	/**
	 * Default threshold for a relative error.
	 */
	public static final double RELATIVE_ERROR_THRESHOLD = 0.0001;

	// package
	AbstractEnvironmentValue(ParameterValue<?> valueList) {
		this.value = valueList;
	}

	/**
	 * @return the full value list
	 */
	public ParameterValue<?> getValue() {
		return this.value;
	}

	/**
	 * calculates the average difference in percent reflected to the parameters
	 * values.
	 * 
	 * @return value between 0.0 and 1.0
	 * @throws UnsupportedDataType
	 *             if the datatype is not supported
	 */
	public abstract double calculateDifferenceInPercent(
			AbstractEnvironmentValue referenceValue);

	/**
	 * calculates the difference of the two values in relation to the average of
	 * them.
	 * 
	 * Be Z the return value and X and Y the two input values. Thus the result
	 * is calculated as follows.
	 * 
	 * Z = |X-Y|/((|X|+|Y|)/2)
	 * 
	 * @return the difference as defined.
	 * @throws UnsupportedDataType
	 *             if the datatype is not supported
	 */
	public abstract double calculateDifferenceToAverage(
			AbstractEnvironmentValue referenceValue);

	/**
	 * decides which which {@link AbstractEnvironmentValue} is the lower one.
	 * 
	 * @param value
	 *            the value to be referenced.
	 * @return true if this instance is lower.
	 */
	public abstract boolean isLower(AbstractEnvironmentValue value);
}

package org.sopeco.plugin.std.exploration.screening.container;

import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Container to store parameter values for high, low factor levels.
 * 
 * @author Pascal Meier
 * @author Dennis Westermann
 * 
 */
public class ParameterFactorValues {
	/**
	 * Minimal value the parameter can reach
	 */
	private ParameterValue<?> low;
	/**
	 * Maximal value the parameter can reach
	 */
	private ParameterValue<?> high;
	/**
	 * Parameter described by this object
	 */
	private ParameterDefinition parameter;

	/**
	 * Constructor.
	 * 
	 * @param low
	 *            low parameter value used for screening
	 * @param high
	 *            high parameter value used for screening
	 * @param parameter
	 *            the parameter that is described
	 */
	public ParameterFactorValues(ParameterValue<?> low, ParameterValue<?> high,
			ParameterDefinition parameter) {
		this.low = low;
		this.high = high;
		this.parameter = parameter;
	}

	/**
	 * Returns the parameter value of a parameter according to the specified
	 * factor level.
	 * 
	 * @param level
	 *            factor level for which the parameter value is desired. Has to
	 *            be 1 or -1.
	 */
	public ParameterValue<?> getParameterValueByLevel(int level) {
		if (level == 1) {
			return high;
		} else if (level == -1) {
			return low;
		} else {
			throw new IllegalStateException("Unknown factor level provided.");
		}
	}

	public ParameterDefinition getParameter() {
		return parameter;
	}

}
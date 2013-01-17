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

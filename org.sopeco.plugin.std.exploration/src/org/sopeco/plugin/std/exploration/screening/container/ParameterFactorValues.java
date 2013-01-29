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
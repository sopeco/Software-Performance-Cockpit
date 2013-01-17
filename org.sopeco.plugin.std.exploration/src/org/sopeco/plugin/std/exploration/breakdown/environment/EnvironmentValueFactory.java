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
import org.sopeco.persistence.dataset.util.ParameterUtil;

/**
 * Factory class do create AbstractEnvironmentValues.
 * 
 * @author Rouven Krebs
 */
public final class EnvironmentValueFactory {
	/**
	 * Singleton.
	 */
	private static final  EnvironmentValueFactory INSTANCE = new EnvironmentValueFactory();

	private EnvironmentValueFactory() {	
	}

	public AbstractEnvironmentValue caseIntegerValue(
			ParameterValue<Integer> object) {
		return new IntegerEnvironmentValue(object);
	}

	public AbstractEnvironmentValue caseDoubleValue(
			ParameterValue<Double> object) {
		return new DoubleEnvironmentValue(object);
	}

	/**
	 * Creates a new Environment Value based on the class of the value.
	 * 
	 * @param runInfo
	 *            corresponding to the measurement of this value.
	 * @param value
	 *            to be encapsulated.
	 * @return the environmental value encapsulating the value itself.
	 */
	/*
	 * expected to be faster this way instead of creating a new Object every
	 * time.
	 */
	@SuppressWarnings("unchecked")
	public static synchronized AbstractEnvironmentValue createAbstractEnvironmentValue(ParameterValue<?> value) {
		

		AbstractEnvironmentValue environmentValue = null;
		switch (ParameterUtil.getTypeEnumeration(value.getParameter().getType())) {
		case DOUBLE:
			environmentValue = INSTANCE
					.caseDoubleValue((ParameterValue<Double>) value);
			break;
		case INTEGER:
			environmentValue = INSTANCE
					.caseIntegerValue((ParameterValue<Integer>) value);
			break;
		default:
			throw new IllegalArgumentException("Exploration supports only Integer and Double values.");
		}

		return environmentValue;
	}

}

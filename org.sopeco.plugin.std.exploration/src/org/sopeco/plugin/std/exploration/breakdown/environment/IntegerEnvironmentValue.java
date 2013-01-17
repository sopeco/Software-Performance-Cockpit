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
 * The integer environment value.
 */
public class IntegerEnvironmentValue extends AbstractEnvironmentValue {

	IntegerEnvironmentValue(ParameterValue<Integer> value) {
		super(value);
	}

	@Override
	public double calculateDifferenceInPercent(
			AbstractEnvironmentValue referenceValue)  {
		if (referenceValue instanceof DoubleEnvironmentValue)
			return this.calculateDoubleDifference(referenceValue);
		else if (referenceValue instanceof IntegerEnvironmentValue)
			return this.calculateIntegerDifference(referenceValue);

		throw new RuntimeException(
				"AbstractEnvironmentValue is an unsupported type. Can only calculate difference of observation " +
						"parameter values of type Double or Integer.");

	}

	/**
	 * calculate the absolute relative error for a
	 * {@link DoubleEnvironmentValue}.
	 * 
	 * @param referenceValue
	 * @return the difference
	 */
	private double calculateDoubleDifference(
			AbstractEnvironmentValue referenceValue) {
		double referencedDoubleValue = ((DoubleEnvironmentValue) referenceValue).getValue()
						.getValueAsDouble();
		double thisValue = (this.getValue().getValueAsDouble());
		double relativeError;
		if (thisValue < RELATIVE_ERROR_THRESHOLD
				&& thisValue > -RELATIVE_ERROR_THRESHOLD) {
			relativeError = Math.abs(referencedDoubleValue - thisValue);
		} else {
			relativeError = Math.abs((referencedDoubleValue - thisValue)
					/ thisValue);
		}
		return relativeError;
	}

	/**
	 * calculate the absolute relative error for a
	 * {@link IntegerEnvironmentValue}.
	 * 
	 * @param referenceValue
	 * @return the difference
	 */
	private double calculateIntegerDifference(
			AbstractEnvironmentValue referenceValue) {
		double referencedDoubleValue = referenceValue.getValue().getValueAsDouble();

		double thisValue = this.getValue().getValueAsDouble();
		double relativeError;
		if (thisValue < RELATIVE_ERROR_THRESHOLD
				&& thisValue > -RELATIVE_ERROR_THRESHOLD) {
			relativeError = Math.abs(referencedDoubleValue - thisValue);
		} else {
			relativeError = Math.abs((referencedDoubleValue - thisValue)
					/ thisValue);
		}

		return relativeError;
	}

	@Override
	public boolean isLower(AbstractEnvironmentValue value) {

		if (value instanceof DoubleEnvironmentValue)
			return this.getValue().getValueAsDouble() < ((DoubleEnvironmentValue) value)
					.getValue().getValueAsDouble();
		else if (value instanceof IntegerEnvironmentValue)
			return this.getValue().getValueAsDouble() < ((IntegerEnvironmentValue) value)
					.getValue().getValueAsDouble();

		throw new RuntimeException(
				"AbstractEnvironmentValue is an unsupported type");
	}

	@Override
	public double calculateDifferenceToAverage(AbstractEnvironmentValue value)
			{
		double secondValue = 0d;

		if (value instanceof DoubleEnvironmentValue)
			secondValue = ((DoubleEnvironmentValue) value).getValue()
					.getValueAsDouble();
		else if (value instanceof IntegerEnvironmentValue)
			secondValue = ((IntegerEnvironmentValue) value).getValue()
					.getValueAsDouble();
		else
			throw new RuntimeException(
					"AbstractEnvironmentValue is an unsupported type");

		return Math.abs(secondValue - this.getValue().getValueAsDouble())
				/ ((Math.abs(secondValue) + Math
						.abs(this.getValue().getValueAsDouble())) / 2);

	}

}

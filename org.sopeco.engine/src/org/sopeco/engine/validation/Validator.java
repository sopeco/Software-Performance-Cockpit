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
package org.sopeco.engine.validation;

import org.sopeco.engine.analysis.IPredictionFunctionResult;

/**
 * This class provides access to the validation component. It runs a validation
 * based on a {@link ValidationObject} and a {@link IPredictionFunctionResult}.
 * 
 * @author Dennis Westermann
 * 
 */
public final class Validator {

	/**
	 * Utility class, only static methods, thus private constructor
	 */
	private Validator() {

	}

	/**
	 * Runs a validation based on the given {@link ValidationObject} and the
	 * given {@link IPredictionFunctionResult}. It compares measurements and
	 * predictions and returns the calculated error measures in a
	 * {@link ValidationResult} object.
	 * 
	 * @param predictionObject
	 *            the object that holds the prediction function
	 * @param validationObject
	 *            the object that holds the measured data against which the
	 *            prediction function should be validated
	 * @return a {@link ValidationResult} that provides different error measures
	 *         derived by the comparison of measured and predicted data
	 */
	public static ValidationResult validate(IPredictionFunctionResult predictionObject,
			ValidationObject validationObject) {
		return new ValidationResult(predictionObject, validationObject);
	}

}

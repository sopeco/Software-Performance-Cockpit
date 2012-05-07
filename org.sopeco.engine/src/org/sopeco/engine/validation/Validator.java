package org.sopeco.engine.validation;

import org.sopeco.engine.analysis.IPredictionFunctionResult;

/**
 * This class provides access to the validation component. It runs a validation
 * based on a {@link ValidationObject} and a {@link IPredictionFunctionResult}.
 * 
 * @author Dennis Westermann
 * 
 */
public class Validator {

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
	public static ValidationResult validate(IPredictionFunctionResult predictionObject, ValidationObject validationObject) {
		return new ValidationResult(predictionObject, validationObject);
	}

}

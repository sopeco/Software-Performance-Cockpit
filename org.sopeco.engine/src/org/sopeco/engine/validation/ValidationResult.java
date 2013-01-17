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
 * This class calculates and provides different measures derived by the
 * comparison of measured and predicted data.
 * 
 * @author Dennis Westermann
 * 
 */
public class ValidationResult {

	private IPredictionFunctionResult predictionObject;
	private ValidationObject validationObject;

	private int numPredictions; // n
	private int numIndepParameters; // k
	// private int numExperimentRunsPerPrediction = 10; // m

	// private double[][] validationIndepParamValues; // matrix of xPred
	private double[] predictedDepParamValues; // yPredicted
	private double[] expectedDepParamValues; // yExpected
	private double sumOfPredDepParamValues = 0; // SUM(yPredicted) -> required
												// to calculate yPredMean
	private double sumOfSquaresOfPredDepParam = 0; // SSY = SUM(yPred^2)
	private double sumOfSquaresOfPredDepParamMean = 0; // SS0 = n*(yPredMean^2)
	private double[] absoluteErrors; // ae = yPredicted - yExpected
	private double[] relativeErrors; // re = ae/yExpected
	private double sumOfAbsoluteErrors = 0; // SUM(ae)
	private double sumOfRelativeErrors = 0; // SUM(re)
	private double meanAbsoluteError; // mae = SUM(ae)/n -> n number of
										// predictions
	private double meanRelativeError; // mre = SUM(re)/n
	private double sumOfSquaredAbsoluteErrors = 0; // SSAE = SUM(ae^2)
	private double sumOfSquaredRelativeErrors = 0; // SSRE = SUM(re^2)
	private double totalSumOfSquares = 0; // SST = SUM(yPred - yMean) or SST =
											// SSY - SS0 aka variation of y
	private double sumOfSquaresExplainedByRegression = 0; // SSR = SST - SSAE
	private double coefficientOfDetermination; // R^2 = SSR/SST aka goodness of
												// regression
	private double coefficientOfCorrelation; // R = SQUAREROOT(R)
	private double meanSquaredError; // aka variance of y, s^2, MSE =
										// SSAE/(n-k-1) -> k number of indep
										// variables (-1 for constant value in
										// regression function)
										// for IDW MSE = SSAE/n or see [Li2008]
	private double standardDeviationOfErrors; // sError = SQUAREROOT(MSE)
	// private double[] standardDeviationOfSinglePrediction; // syPred = sError
	// * [1/m + 1/n + ((xPred-xMean)^2)/(SUM(xi^2)-n*xMean^2)] -> m number of
	// measurements at xPred
	// private double[][] confidenceIntervalOfSinglePrediction90; // lookup in
	// t-Distribution-Table
	// private double[][] confidenceIntervalOfSinglePrediction95; // lookup in
	// t-Distribution-Table
	// private double meanStandardDeviationOfPredictions; // msyPred =
	// SUM(syPred)/n
	private double meanSquareOfRegression; // MSR = SSR/k

	// private double tComputed; //
	// private double tTestProbability; // aka confidence level: The probability
	// that the Null-Hypotheses can be rejected
	// private static double T_Test_Target_Confidence_Level = 0.95;
	// private boolean tTestPassed;

	// private double fComputed; // MSR/MSE
	// private double fTable; // lookup in F-distribution table
	// private double fTestProbability; // aka confidence level: The probability
	// that the Null-Hypotheses can be
	// rejected
	// private static double F_Test_Target_Confidence_Level = 0.95;
	// private boolean fTestPassed;

	// F-Test (Analysis of Variance): if ((MSR/MSE) > F[k, n-k-1]) then
	// predictor variables are assumed to explain a significant fraction of the
	// response variation

	/**
	 * 
	 * 
	 * @param predictionObject
	 * @param validationObject
	 * @throws IllegalArgumentException
	 */
	protected ValidationResult(IPredictionFunctionResult predictionObject, ValidationObject validationObject) {
		super();
		this.predictionObject = predictionObject;
		this.validationObject = validationObject;
		if (validationObject != null && validationObject.getValidationRow(0) != null) {
			numPredictions = validationObject.getSize();
			numIndepParameters = validationObject.getValidationRow(0).getIndependentParameterValues().size();

			// validationIndepParamValues = new
			// double[numPredictions][numIndepParameters];
			predictedDepParamValues = new double[numPredictions];
			expectedDepParamValues = new double[numPredictions];
			absoluteErrors = new double[numPredictions];
			relativeErrors = new double[numPredictions];
			// standardDeviationOfSinglePrediction = new double[numPredictions];
			// confidenceIntervalOfSinglePrediction90 = new
			// double[numPredictions][2];
			// confidenceIntervalOfSinglePrediction95 = new
			// double[numPredictions][2];
		}
		validate();
	}

	private void validate() {
		calculateErrors();
	}

	private void calculateErrors() {

		int i = 0;
		for (ValidationRow validationRow : validationObject) {
			// addToXMatrix(validationRow.getIndependentParameterValues(), i);
			double predicted = predictionObject.predictOutputParameter(validationRow.getIndependentParameterValues())
					.getValueAsDouble();
			double expected = validationRow.getDependentParameterValue().getValueAsDouble();

			predictedDepParamValues[i] = predicted;
			sumOfPredDepParamValues += predicted;
			sumOfSquaresOfPredDepParam += predicted * predicted;
			expectedDepParamValues[i] = expected;
			absoluteErrors[i] = Math.abs(predicted - expected);
			if (expected != 0) {
				relativeErrors[i] = Math.abs(absoluteErrors[i] / expected);
			} else {
				relativeErrors[i] = Math.abs(absoluteErrors[i]);
			}

			sumOfAbsoluteErrors += absoluteErrors[i];
			sumOfRelativeErrors += relativeErrors[i];
			sumOfSquaredAbsoluteErrors += Math.pow(absoluteErrors[i], 2);
			sumOfSquaredRelativeErrors += Math.pow(relativeErrors[i], 2);

			numIndepParameters = validationRow.getIndependentParameterValues().size();
			i++;
		}
		numPredictions = i;
		sumOfSquaresOfPredDepParamMean = numPredictions * Math.pow((sumOfPredDepParamValues / numPredictions), 2);
		meanAbsoluteError = sumOfAbsoluteErrors / numPredictions;
		meanRelativeError = sumOfRelativeErrors / numPredictions;
		totalSumOfSquares = sumOfSquaresOfPredDepParam - sumOfSquaresOfPredDepParamMean;
		sumOfSquaresExplainedByRegression = totalSumOfSquares - sumOfSquaredAbsoluteErrors;
		coefficientOfDetermination = sumOfSquaresExplainedByRegression / totalSumOfSquares;
		coefficientOfCorrelation = Math.sqrt(coefficientOfDetermination);
		meanSquaredError = calculateMeanSquaredError();
		standardDeviationOfErrors = Math.sqrt(meanSquaredError);

	}

	private Double calculateMeanSquaredError() {

		// TODO: set a flag via configuration about how to calculate the MSE
		// (depends on the analysis strategy)
		return sumOfSquaredAbsoluteErrors / (numPredictions - numIndepParameters - 1);

	}

	/**
	 * returns the prediction object.
	 * 
	 * @return the prediction object
	 */
	public IPredictionFunctionResult getPredictionObject() {
		return predictionObject;
	}

	/**
	 * returns the validation object.
	 * 
	 * @return the validation object
	 */
	public ValidationObject getValidationData() {
		return validationObject;
	}

	/**
	 * Returns the number of predictions.
	 * 
	 * @return number of predictions
	 */
	public int getNumPredictions() {
		return numPredictions;
	}

	/**
	 * Returns the number of independent parameters.
	 * 
	 * @return number of indipendent parameters
	 */
	public int getNumIndepParameters() {
		return numIndepParameters;
	}

	/**
	 * Returns an array of predicted values for the dependent parameter.
	 * 
	 * @return an array of predicted values for the dependent parameter
	 */
	public double[] getPredictedDepParamValues() {
		return predictedDepParamValues;
	}

	/**
	 * Returns an array of expected values for the dependent parameter.
	 * 
	 * @return an array of expected values for the dependent parameter
	 */
	public double[] getExpectedDepParamValues() {
		return expectedDepParamValues;
	}

	/**
	 * Returns the sum of all predicted values for the dependent parameter.
	 * 
	 * @return sum of all predicted values for the dependent parameter
	 */
	public double getSumOfPredDepParamValues() {
		return sumOfPredDepParamValues;
	}

	/**
	 * Returns the sum of squares of all predicted values for the dependent
	 * parameter.
	 * 
	 * @return sum of squares of all predicted values for the dependent
	 *         parameter
	 */
	public double getSumOfSquaresOfPredDepParam() {
		return sumOfSquaresOfPredDepParam;
	}

	/**
	 * Returns the sum of squares of the mean of predicted values for the
	 * dependent parameter.
	 * 
	 * @return sum of squares of the mean of predicted values for the dependent
	 *         parameter
	 */
	public double getSumOfSquaresOfPredDepParamMean() {
		return sumOfSquaresOfPredDepParamMean;
	}

	/**
	 * Returns an array of absolute errors.
	 * 
	 * @return an array of absolute errors.
	 */
	public double[] getAbsoluteErrors() {
		return absoluteErrors;
	}

	/**
	 * Returns an array of relative errors.
	 * 
	 * @return an array of relative errors.
	 */
	public double[] getRelativeErrors() {
		return relativeErrors;
	}

	/**
	 * Returns the sum of absolute errors.
	 * 
	 * @return sum of absolute errors
	 */
	public double getSumOfAbsoluteErrors() {
		return sumOfAbsoluteErrors;
	}

	/**
	 * Returns the sum of relative errors.
	 * 
	 * @return sum of relative errors
	 */
	public double getSumOfRelativeErrors() {
		return sumOfRelativeErrors;
	}

	/**
	 * Returns the mean absolute error.
	 * 
	 * @return mean absolute error
	 */
	public double getMeanAbsoluteError() {
		return meanAbsoluteError;
	}

	/**
	 * Returns the mean relative error.
	 * 
	 * @return mean relative error
	 */
	public double getMeanRelativeError() {
		return meanRelativeError;
	}

	/**
	 * Returns the sum of squared absolute errors.
	 * 
	 * @return sum of squared absolute errors
	 */
	public double getSumOfSquaredAbsoluteErrors() {
		return sumOfSquaredAbsoluteErrors;
	}

	/**
	 * Returns the sum of squared relative errors.
	 * 
	 * @return sum of squared relative errors
	 */
	public double getSumOfSquaredRelativeErrors() {
		return sumOfSquaredRelativeErrors;
	}

	/**
	 * Returns the total sum of all squares.
	 * 
	 * @return total sum of all squares.
	 */
	public double getTotalSumOfSquares() {
		return totalSumOfSquares;
	}

	/**
	 * Returns the sum of squares explained by regression.
	 * 
	 * @return sum of squares explained by regression.
	 */
	public double getSumOfSquaresExplainedByRegression() {
		return sumOfSquaresExplainedByRegression;
	}

	/**
	 * Returns the coefficient of determination.
	 * 
	 * @return coefficient of determination
	 */
	public double getCoefficientOfDetermination() {
		return coefficientOfDetermination;
	}

	/**
	 * Returns the coefficient of correlation.
	 * 
	 * @return coefficient of correlation
	 */
	public double getCoefficientOfCorrelation() {
		return coefficientOfCorrelation;
	}

	/**
	 * rReturns the standard deviation of errors.
	 * 
	 * @return standard deviation of errors
	 */
	public double getStandardDeviationOfErrors() {
		return standardDeviationOfErrors;
	}

	/**
	 * Returns the mean square of regression.
	 * 
	 * @return mean square of regression
	 */
	public double getMeanSquareOfRegression() {
		return meanSquareOfRegression;
	}

	/**
	 * Returns the mean of squared errors.
	 * 
	 * @return mean of squared errors
	 */
	public double getMeanSquaredError() {
		return meanSquaredError;
	}

}

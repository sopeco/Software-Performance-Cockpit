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
	protected ValidationResult(IPredictionFunctionResult predictionObject, ValidationObject validationObject) throws IllegalArgumentException {
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

	private void validate() throws IllegalArgumentException {
		calculateErrors();
	}

	private void calculateErrors() throws IllegalArgumentException {

		int i = 0;
		for (ValidationRow validationRow : validationObject) {
			// addToXMatrix(validationRow.getIndependentParameterValues(), i);
			double predicted = predictionObject.predictOutputParameter(validationRow.getIndependentParameterValues()).getValueAsDouble();
			double expected = validationRow.getDependentParameterValue().getValueAsDouble();

			predictedDepParamValues[i] = predicted;
			sumOfPredDepParamValues += predicted;
			sumOfSquaresOfPredDepParam += predicted * predicted;
			expectedDepParamValues[i] = expected;
			absoluteErrors[i] = Math.abs(predicted - expected);
			if (expected != 0)
				relativeErrors[i] = Math.abs(absoluteErrors[i] / expected);
			else
				relativeErrors[i] = Math.abs(absoluteErrors[i]);

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

	public IPredictionFunctionResult getPredictionObject() {
		return predictionObject;
	}

	public ValidationObject getValidationData() {
		return validationObject;
	}

	public int getNumPredictions() {
		return numPredictions;
	}

	public int getNumIndepParameters() {
		return numIndepParameters;
	}

	public double[] getPredictedDepParamValues() {
		return predictedDepParamValues;
	}

	public double[] getExpectedDepParamValues() {
		return expectedDepParamValues;
	}

	public double getSumOfPredDepParamValues() {
		return sumOfPredDepParamValues;
	}

	public double getSumOfSquaresOfPredDepParam() {
		return sumOfSquaresOfPredDepParam;
	}

	public double getSumOfSquaresOfPredDepParamMean() {
		return sumOfSquaresOfPredDepParamMean;
	}

	public double[] getAbsoluteErrors() {
		return absoluteErrors;
	}

	public double[] getRelativeErrors() {
		return relativeErrors;
	}

	public double getSumOfAbsoluteErrors() {
		return sumOfAbsoluteErrors;
	}

	public double getSumOfRelativeErrors() {
		return sumOfRelativeErrors;
	}

	public double getMeanAbsoluteError() {
		return meanAbsoluteError;
	}

	public double getMeanRelativeError() {
		return meanRelativeError;
	}

	public double getSumOfSquaredAbsoluteErrors() {
		return sumOfSquaredAbsoluteErrors;
	}

	public double getSumOfSquaredRelativeErrors() {
		return sumOfSquaredRelativeErrors;
	}

	public double getTotalSumOfSquares() {
		return totalSumOfSquares;
	}

	public double getSumOfSquaresExplainedByRegression() {
		return sumOfSquaresExplainedByRegression;
	}

	public double getCoefficientOfDetermination() {
		return coefficientOfDetermination;
	}

	public double getCoefficientOfCorrelation() {
		return coefficientOfCorrelation;
	}

	public double getStandardDeviationOfErrors() {
		return standardDeviationOfErrors;
	}

	public double getMeanSquareOfRegression() {
		return meanSquareOfRegression;
	}

	public double getMeanSquaredError() {
		return meanSquaredError;
	}

}

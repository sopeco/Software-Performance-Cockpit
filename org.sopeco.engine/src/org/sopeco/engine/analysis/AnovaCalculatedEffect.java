package org.sopeco.engine.analysis;

import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * A {@link ParameterEffect} class with additional effect information specific
 * to ANOVA analyses.
 * 
 * @author Dennis Westermann
 * 
 */
public class AnovaCalculatedEffect extends ParameterEffect {

	int degreesOfFreedom;
	double sumOfSquares;
	double meanSquare;
	double fValue;
	double pValue;

	public AnovaCalculatedEffect(List<ParameterDefinition> analysedParameters,
			ParameterDefinition observationParameter) {
		super(analysedParameters, observationParameter, 0);
	}

	public int getDegreesOfFreedom() {
		return degreesOfFreedom;
	}

	public void setDegreesOfFreedom(int degreesOfFreedom) {
		this.degreesOfFreedom = degreesOfFreedom;
	}

	public double getSumOfSquares() {
		return sumOfSquares;
	}

	public void setSumOfSquares(double sumOfSquares) {
		this.sumOfSquares = sumOfSquares;
	}

	public double getMeanSquare() {
		return meanSquare;
	}

	public void setMeanSquare(double meanSquare) {
		this.meanSquare = meanSquare;
	}

	public double getfValue() {
		return fValue;
	}

	public void setfValue(double fValue) {
		this.fValue = fValue;
		this.setEffectValue(fValue);
	}

	public double getpValue() {
		return pValue;
	}

	public void setpValue(double pValue) {
		this.pValue = pValue;
	}

	/**
	 * Checks whether the effect is significant at the given significance level.
	 * Example: If significanceLevel is 0.95 and return value is
	 * <code>true</code>, we can say that the correlation between the
	 * independent parameters is significant at the 95% level.
	 * 
	 * @param significanceLevel
	 *            value in the range [0, 1] describing the target significance
	 *            level.
	 * @return <code>true</code> if the effect is significant at the given
	 *         significance level, otherwise <code>false</code>.
	 */
	public boolean isSignificant(double significanceLevel) {
		if (pValue < 1 - significanceLevel) {
			return true;
		} else {
			return false;
		}
	}
}

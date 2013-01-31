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

	private int degreesOfFreedom;
	private double sumOfSquares;
	private double meanSquare;
	private double fValue;
	private double pValue;

	/**
	 * Constructor.
	 * 
	 * @param analysedParameters
	 *            a list of independent parameters whose effects should be
	 *            analysed
	 * @param observationParameter
	 *            the observation parameter where to observe the effect
	 */
	public AnovaCalculatedEffect(List<ParameterDefinition> analysedParameters, ParameterDefinition observationParameter) {
		super(analysedParameters, observationParameter, 0);
	}

	/**
	 * Returns the degree of freedom for ANOVA.
	 * 
	 * @return the degree of freedom for ANOVA.
	 */
	public int getDegreesOfFreedom() {
		return degreesOfFreedom;
	}

	/**
	 * Sets the degrees of freedom to be used for ANOVA.
	 * 
	 * @param degreesOfFreedom
	 *            degrees of freedom
	 */
	public void setDegreesOfFreedom(int degreesOfFreedom) {
		this.degreesOfFreedom = degreesOfFreedom;
	}

	/**
	 * Returns the sum of squares of the residuals.
	 * 
	 * @return the sum of squares of the residuals.
	 */
	public double getSumOfSquares() {
		return sumOfSquares;
	}

	/**
	 * Sets sum of squares.
	 * 
	 * @param sumOfSquares
	 *            sum of squares
	 */
	public void setSumOfSquares(double sumOfSquares) {
		this.sumOfSquares = sumOfSquares;
	}

	/**
	 * Returns the mean square.
	 * 
	 * @return the mean square.
	 */
	public double getMeanSquare() {
		return meanSquare;
	}

	/**
	 * Sets the mean square.
	 * 
	 * @param meanSquare
	 *            the mean square
	 */
	public void setMeanSquare(double meanSquare) {
		this.meanSquare = meanSquare;
	}

	/**
	 * Returns the F-value indicating whether the difference in values is due to
	 * an effect of the parameters.
	 * 
	 * @return the F-value.
	 */
	public double getfValue() {
		return fValue;
	}

	/**
	 * Sets the F-value.
	 * 
	 * @param fValue
	 *            the F-value
	 */
	public void setfValue(double fValue) {
		this.fValue = fValue;
		this.setEffectValue(fValue);
	}

	/**
	 * Returns the p-value.
	 * 
	 * @return the p-value.
	 */
	public double getpValue() {
		return pValue;
	}

	/**
	 * Sets the p-Value . * @param pValue the p-Value
	 */
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
		return pValue < (1 - significanceLevel);
	}
}

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

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Represents the correlation between an independent parameter and a dependent
 * parameter.
 * 
 * @author Dennis Westermann, Pascal Meier
 */
public class ParameterCorrelation implements Comparable<ParameterCorrelation>, IParameterInfluenceDescriptor {

	private static final double SMALL_NUMBER = 0.000001;
	/**
	 * Independent parameter of which the effect is estimated
	 */
	private ParameterDefinition indepParameter;
	/**
	 * Dependent parameter (possibly) correlated with the independent parameter.
	 */
	private ParameterDefinition depParameter;
	/**
	 * Influence of an independent parameter on the observation Parameter.
	 */
	private double corrValue;

	/**
	 * Used to describe the significance of the Pearson's correlation
	 * coefficient between the two columns of data. If this value is less than
	 * .01, we can say that the correlation between the two columns of data is
	 * significant at the 99% level.
	 */
	private double pValue;

	/**
	 * Constructor.
	 * 
	 * @param indepParameter
	 *            independent parameter of which the effect is estimated
	 * @param corrValue
	 *            Percent value of the estimated influence
	 * @param depParameter
	 *            dependent parameter where to observe the effect
	 * @param pValue
	 *            the calculated p-Value
	 */
	public ParameterCorrelation(ParameterDefinition indepParameter, ParameterDefinition depParameter, double corrValue,
			double pValue) {
		super();
		this.indepParameter = indepParameter;
		this.corrValue = corrValue;
		this.depParameter = depParameter;
		this.pValue = pValue;
	}

	/**
	 * @return the independent parameter definition
	 */
	@Override
	public ParameterDefinition getIndependentParameter() {
		return indepParameter;
	}

	/**
	 * @return the dependent parameter definition
	 */
	@Override
	public ParameterDefinition getDependentParameter() {
		return depParameter;
	}

	/**
	 * Returns the same result as the getCorrelation() method.
	 * 
	 * @return the correlation value (see getCorrelation())
	 */
	@Override
	public double getInfluenceValue() {
		return this.getCorrelation();
	}

	/**
	 * @return a value between -1 and 1 that describes the correlation between
	 *         the dependent and the independent parameter. The closer the value
	 *         is to zero, the less correlated are the two variables.
	 */
	public double getCorrelation() {
		return corrValue;
	}

	/**
	 * The p-value describe the significance of the Pearson's correlation
	 * coefficient between the two columns of data. If this value is less than
	 * .01, we can say that the correlation between the two columns of data is
	 * significant at the 99% level.
	 * 
	 * @return p-value describing the significance of the Pearson's correlation
	 *         coefficient.
	 */
	public double getPValue() {
		return pValue;
	}

	/**
	 * Checks whether independent and dependent parameter are correlated with a
	 * given significance level. Example: If significanceLevel is 0.95 and
	 * return value is <code>true</code>, we can say that the correlation
	 * between the two columns of data is significant at the 95% level.
	 * 
	 * @param significanceLevel
	 *            value in the range [0, 1] describing the target significance
	 *            level.
	 * @return <code>true</code> if there is a correlation with the given
	 *         significance level, otherwise <code>false</code>.
	 */
	public boolean isCorrelated(double significanceLevel) {
		return pValue < (1 - significanceLevel);
	}

	@Override
	public int compareTo(ParameterCorrelation otherParameterInfluence) {
		if (Math.abs(this.corrValue - otherParameterInfluence.getCorrelation()) < SMALL_NUMBER) {
			return 0;
		} else if (Math.abs(this.corrValue) > Math.abs(otherParameterInfluence.getCorrelation())) {
			return 1;
		}
		return -1;
	}

	@Override
	public String toString() {
		return indepParameter.getName() + " " + corrValue;
	}

}

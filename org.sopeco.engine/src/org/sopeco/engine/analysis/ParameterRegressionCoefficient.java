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
 * Represents the coefficient of a parameter as a result of a linear regression.
 * The coefficient describes the dependency between an independent parameter and
 * a dependent parameter.
 * 
 * @author Dennis Westermann
 */
public class ParameterRegressionCoefficient implements Comparable<ParameterRegressionCoefficient>,
		IParameterInfluenceDescriptor {

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
	private double regressionCoefficient;

	/**
	 * Constructor.
	 * 
	 * @param indepParameter
	 *            the independent parameter
	 * @param depParameter
	 *            the observed parameter
	 * @param regressionCoefficient
	 *            regression coefficient
	 */
	public ParameterRegressionCoefficient(ParameterDefinition indepParameter, ParameterDefinition depParameter,
			double regressionCoefficient) {
		super();
		this.indepParameter = indepParameter;
		this.regressionCoefficient = regressionCoefficient;
		this.depParameter = depParameter;

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
	 * Returns the same result as the getCoefficient() method.
	 * 
	 * @return the coefficient value (see getCoefficient())
	 */
	@Override
	public double getInfluenceValue() {
		return this.getCoefficient();
	}

	/**
	 * @return a value that describes the correlation between the dependent and
	 *         the independent parameter based on a linear regression.
	 */
	public double getCoefficient() {
		return regressionCoefficient;
	}

	@Override
	public int compareTo(ParameterRegressionCoefficient otherParameterInfluence) {
		if (Math.abs(this.regressionCoefficient - otherParameterInfluence.getCoefficient()) < SMALL_NUMBER) {
			return 0;
		} else if (Math.abs(this.regressionCoefficient) > Math.abs(otherParameterInfluence.getCoefficient())) {
			return 1;
		}
		return -1;
	}

	@Override
	public String toString() {
		return indepParameter.getName() + " " + regressionCoefficient;
	}

}

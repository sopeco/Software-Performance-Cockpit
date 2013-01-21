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
 * Represents the estimated influence (effect) of one or more independent
 * parameters on a dependent observation parameter.
 * 
 * @author Pascal Meier
 * @author Dennis Westermann
 */
public class ParameterEffect implements Comparable<ParameterEffect>, IParameterInfluenceDescriptor {

	private static final double SMALL_NUMBER = 0.000001;
	/**
	 * List of independent parameters
	 */
	private List<ParameterDefinition> independentParameters = null;
	/**
	 * Dependent observation parameter
	 */
	private ParameterDefinition dependentParameter;
	/**
	 * estimation of the effect on the observation parameter
	 */
	private double effectValue = 0;

	/**
	 * Constructor.
	 * 
	 * @param analysedParameters
	 *            list of the independent paramters
	 * @param effectValue
	 *            estimation of the effect of the independent parameters on the
	 *            observation parameter
	 * @param observationParameter
	 *            the dependent parameter
	 */
	public ParameterEffect(List<ParameterDefinition> analysedParameters, ParameterDefinition observationParameter,
			double effectValue) {
		super();
		this.independentParameters = analysedParameters;
		this.effectValue = effectValue;
		this.dependentParameter = observationParameter;
	}

	@Override
	public ParameterDefinition getDependentParameter() {
		return dependentParameter;
	}

	/**
	 * @return the dependent parameter definition (Note: This method is only
	 *         supported for main effects. If the number of independent
	 *         parameters is greater than 1, an {@link IllegalStateException} is
	 *         thrown)
	 */
	@Override
	public ParameterDefinition getIndependentParameter() {

		if (independentParameters.size() != 1) {
			throw new IllegalStateException(
					"Method is only supported for main effects (i.e. exactly one independent parameter).");
		} else {
			return independentParameters.get(0);
		}

	}

	/**
	 * Returns a list of independent parameters.
	 * 
	 * @return a list of independent parameters
	 */
	public List<ParameterDefinition> getIndependentParameters() {
		return independentParameters;
	}

	/**
	 * Returns the effect value.
	 * 
	 * @return the effect value
	 */
	public double getEffectValue() {
		return effectValue;
	}

	protected void setEffectValue(double effectValue) {
		this.effectValue = effectValue;
	}

	/**
	 * Returns the same result as the getEffectValue() method.
	 * 
	 * @return the correlation value (see getCorrelation())
	 */
	@Override
	public double getInfluenceValue() {

		return this.getEffectValue();
	}

	@Override
	public int compareTo(ParameterEffect otherEffect) {
		if (Math.abs(this.effectValue - otherEffect.getEffectValue()) < SMALL_NUMBER) {
			return 0;
		} else if (Math.abs(this.effectValue) > Math.abs(otherEffect.getEffectValue())) {
			return 1;
		}
		return -1;
	}

}

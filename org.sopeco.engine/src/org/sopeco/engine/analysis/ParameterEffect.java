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
public class ParameterEffect implements Comparable<ParameterEffect> {
	/**
	 * List of independent parameters
	 */
	private List<ParameterDefinition> analysedParameters = null;
	/**
	 * Dependent observation parameter
	 */
	private ParameterDefinition observationParameter;
	/**
	 * estimation of the effect on the observation parameter
	 */
	private double effectValue = 0;


	/**
	 * Constructor
	 * 
	 * @param analysedParameters
	 *            list of the independent paramters
	 * @param effectValue
	 *            estimation of the effect of the independent parameters on the
	 *            observation parameter
	 * @param parameterBoundaries
	 *            List containing information-objects describing the boundaries
	 *            of the parameters.
	 * @param observationParameter
	 *            the dependent parameter
	 */
	public ParameterEffect(List<ParameterDefinition> analysedParameters,
			ParameterDefinition observationParameter, double effectValue) {
		super();
		this.analysedParameters = analysedParameters;
		this.effectValue = effectValue;
		this.observationParameter = observationParameter;
	}

	public ParameterDefinition getObservationParameter() {
		return observationParameter;
	}

	public List<ParameterDefinition> getAnalysedParameters() {
		return analysedParameters;
	}

	public double getEffectValue() {
		return effectValue;
	}


	@Override
	public int compareTo(ParameterEffect otherEffect) {
		if (Math.abs(this.effectValue - otherEffect.getEffectValue()) < 0.000001) {
			return 0;
		} else if (Math.abs(this.effectValue) > Math.abs(otherEffect
				.getEffectValue())) {
			return 1;
		}
		return -1;
	}

}

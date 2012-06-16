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
	public ParameterEffect(List<ParameterDefinition> analysedParameters, ParameterDefinition observationParameter, double effectValue) {
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
			throw new IllegalStateException("Method is only supported for main effects (i.e. exactly one independent parameter).");
		} else {
			return independentParameters.get(0);
		}

	}

	public List<ParameterDefinition> getIndependentParameters() {
		return independentParameters;
	}

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
		if (Math.abs(this.effectValue - otherEffect.getEffectValue()) < 0.000001) {
			return 0;
		} else if (Math.abs(this.effectValue) > Math.abs(otherEffect.getEffectValue())) {
			return 1;
		}
		return -1;
	}

}

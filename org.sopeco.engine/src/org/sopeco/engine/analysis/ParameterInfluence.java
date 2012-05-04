package org.sopeco.engine.analysis;

import org.sopeco.persistence.entities.definition.ParameterDefinition;



/**
 * Represents the influence of an independent parameter on a dependent
 * observation parameter.
 * 
 * @author Dennis Westermann, Pascal Meier
 */
public class ParameterInfluence implements Comparable<ParameterInfluence> {
	/**
	 * Independent parameter of which the effect is estimated
	 */
	private ParameterDefinition parameter;
	/**
	 * Dependent parameter (possibly) correlated with the independent parameter.
	 */
	private ParameterDefinition observationParameter;
	/**
	 * Influence of an independent parameter on the observation Parameter.
	 */
	private double corrValue;
	/**
	 * Information about the boundaries of the parameter
	 */
//	private ParameterBoundary parameterBoundary;

	/**
	 * Constructor
	 * 
	 * @param parameter
	 *            independent parameter of which the effect is estimated
	 * @param corrValue
	 *            Percent value of the estimated influence
	 * @param parameterBoundary
	 *            information about the boundaries of the parameter
	 */
	public ParameterInfluence(ParameterDefinition parameter,
			ParameterDefinition observationParameter, double corrValue
//			,ParameterBoundary parameterBoundary
			) {
		super();
		this.parameter = parameter;
		this.corrValue = corrValue;
//		this.parameterBoundary = parameterBoundary;
		this.observationParameter = observationParameter;
	}

	public ParameterDefinition getObservationParameter() {
		return observationParameter;
	}

	public ParameterDefinition getParameter() {
		return parameter;
	}

	public double getInfluence() {
		return corrValue;
	}

//	public ParameterBoundary getParameterBoundary() {
//		return parameterBoundary;
//	}

	@Override
	public int compareTo(ParameterInfluence otherParameterInfluence) {
		if (Math.abs(this.corrValue - otherParameterInfluence.getInfluence()) < 0.000001) {
			return 0;
		} else if (Math.abs(this.corrValue) > Math.abs(otherParameterInfluence
				.getInfluence())) {
			return 1;
		}
		return -1;
	}

	public String toString() {
		return parameter.getName() + " " + corrValue;
	}

}

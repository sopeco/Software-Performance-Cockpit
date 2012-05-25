package org.sopeco.engine.analysis;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Represents the correlation between an independent parameter and a dependent
 * parameter.
 * 
 * @author Dennis Westermann, Pascal Meier
 */
public class ParameterCorrelation implements Comparable<ParameterCorrelation>, IParameterInfluenceDescriptor {
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
	 * Constructor
	 * 
	 * @param parameter
	 *            independent parameter of which the effect is estimated
	 * @param corrValue
	 *            Percent value of the estimated influence
	 * @param parameterBoundary
	 *            information about the boundaries of the parameter
	 */
	public ParameterCorrelation(ParameterDefinition indepParameter, ParameterDefinition depParameter, double corrValue, double pValue) {
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
		if (pValue < 1 - significanceLevel) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int compareTo(ParameterCorrelation otherParameterInfluence) {
		if (Math.abs(this.corrValue - otherParameterInfluence.getCorrelation()) < 0.000001) {
			return 0;
		} else if (Math.abs(this.corrValue) > Math.abs(otherParameterInfluence.getCorrelation())) {
			return 1;
		}
		return -1;
	}

	public String toString() {
		return indepParameter.getName() + " " + corrValue;
	}

	

}

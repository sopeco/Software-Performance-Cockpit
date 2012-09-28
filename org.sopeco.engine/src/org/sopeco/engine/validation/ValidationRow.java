package org.sopeco.engine.validation;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.persistence.dataset.ParameterValue;

/**
 * Represents a row of a {@link ValidationObject} dataset.
 * 
 * @author Dennis Westermann
 * 
 */
public class ValidationRow {
	private ParameterValue<?> dependentParameterValue;
	private String dependentParameterName;
	private List<ParameterValue<?>> independentParameterValues = new ArrayList<ParameterValue<?>>();

	/**
	 * Returns the parameter value for the dependent parameter.
	 * 
	 * @return the parameter value for the dependent parameter
	 */
	public ParameterValue<?> getDependentParameterValue() {
		return dependentParameterValue;
	}

	/**
	 * Returns a list of parameter values for the independent parameters.
	 * 
	 * @return a list of parameter values for the independent parameters.
	 */
	public List<ParameterValue<?>> getIndependentParameterValues() {
		return independentParameterValues;
	}

	/**
	 * Sets the value for the dependent parameter.
	 * 
	 * @param value
	 *            the parameter value object for the dependent parameter
	 */
	public void setDependentParameterValue(ParameterValue<?> value) {
		this.dependentParameterValue = value;
	}

	/**
	 * Adds a value for an independent parameter.
	 * 
	 * @param value
	 *            the parameter value object for an independent parameter
	 */
	public void addIndependentParameterValue(ParameterValue<?> value) {
		independentParameterValues.add(value);
	}

	/**
	 * Sets the name of the dependent parameter.
	 * 
	 * @param independentParameterName
	 *            name of the dependent parameter.
	 */
	public void setDependentParameterName(String independentParameterName) {
		this.dependentParameterName = independentParameterName;
	}

	/**
	 * Returns the name of the dependent parameter.
	 * 
	 * @return the name of the dependent parameter
	 */
	public String getDependentParameterName() {
		return dependentParameterName;
	}
}

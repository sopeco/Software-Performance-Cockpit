package org.sopeco.engine.validation;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.persistence.dataset.ParameterValue;

public class ValidationRow {
	private ParameterValue<?> dependentParameterValue;
	private String dependentParameterName;
	private List<ParameterValue<?>> independentParameterValues = new ArrayList<ParameterValue<?>>();

	
	public ParameterValue<?> getDependentParameterValue() {
		return dependentParameterValue;
	}

	public List<ParameterValue<?>> getIndependentParameterValues() {
		return independentParameterValues;
	}

	public void setDependentParameterValue(ParameterValue<?> value) {
		this.dependentParameterValue = value;
	}

	public void addIndependentParameterValue(ParameterValue<?> value) {
		independentParameterValues.add(value);
	}

	public void setDependentParameterName(String independentParameterName) {
		this.dependentParameterName = independentParameterName;
	}

	public String getDependentParameterName() {
		return dependentParameterName;
	}
}

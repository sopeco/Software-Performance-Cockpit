package org.sopeco.persistence.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.dataset.util.ParameterUtil;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ParameterValueList<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6730179918476230975L;
	private ParameterDefinition parameter;
	private List<T> values;

	protected ParameterValueList(ParameterDefinition parameter) {
		super();
		this.parameter = parameter;
		values = new ArrayList<T>();
	}

	public ParameterValueList(ParameterDefinition parameter, List<T> values) {
		super();
		this.parameter = parameter;
		this.values = values;
	}

	public ParameterDefinition getParameter() {
		return parameter;
	}

	public List<T> getValues() {
		return values;
	}

	public double getMean(){
		double mean = 0.0;
		if (getValues().size() > 0) {
			for (T value : getValues()) {
				if (ParameterUtil.getTypeEnumeration(parameter.getType()).equals(ParameterType.DOUBLE)) {
					mean += (Double) value;
				} else if (ParameterUtil.getTypeEnumeration(parameter.getType()).equals(ParameterType.INTEGER)) {
					mean += ((Integer) value).doubleValue();
				} else
					throw new IllegalStateException(
							"The functions getMin() and getMax() are not supported for columns associated with a parameter type other than Double or Integer!");

			}
		}
		return mean/getValues().size();
	}
	
	public ParameterValue<?> getMeanAsParameterValue(){
		 return ParameterValueFactory.createParameterValue(parameter, getMean());
	}
	
	public void merge(ParameterValueList other) {
		if (!parameter.equals(other.getParameter())) {
			throw new IllegalArgumentException(
					"Cannot merge ParameterValueLists of different ParameterDefinitions!");
		}
		this.values.addAll(other.getValues());
	}

	protected void addValue(Object value) {
		T convertedValue = (T) ParameterValueFactory.convertValue(value,
				ParameterUtil.getTypeEnumeration(parameter.getType()));
		this.values.add(convertedValue);
	}

	protected void addValues(List<Object> values) {
		for (Object value : values) {
			addValue(value);
		}
	}

	public List<String> getValueStrings() {
		List<String> result = new ArrayList<String>();
		for (Object value : values) {
			if (value instanceof Double) {
				result.add(Double.toString((Double) value));
			} else if (value instanceof Integer) {
				result.add(Integer.toString((Integer) value));
			} else if (value instanceof Boolean) {
				result.add(Boolean.toString((Boolean) value));
			} else if (value instanceof String) {
				result.add((String) value);
			}
		}

		return result;
	}

	public List<Double> getValuesAsDouble() {
		List<Double> result = new ArrayList<Double>();
		for (Object value : values) {
			if (value instanceof Double) {
				result.add((Double) value);
			} else if (value instanceof Integer) {
				result.add(((Integer) value).doubleValue());
			} else
				throw new IllegalStateException(
						"The function getValueAsDouble is supported only by Double and Integer ParameterValues!");
		}
		return result;
	}

	public List<Integer> getValuesAsInteger() {
		List<Integer> result = new ArrayList<Integer>();
		for (Object value : values) {
			if (value instanceof Double) {
				result.add(((Double) value).intValue());
			} else if (value instanceof Integer) {
				result.add((Integer) value);
			} else
				throw new IllegalStateException(
						"The function getValueAsInteger is supported only by Double and Integer ParameterValues!");
		}

		return result;
	}
	
	public int getSize(){
		return values.size();
	}
}

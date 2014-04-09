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
package org.sopeco.persistence.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.dataset.util.ParameterUtil;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

// TODO documentation!

/**
 * Objects of this class contain a list of values for a certain parameter.
 * 
 * @author Alexander Wert
 * 
 * @param <T>
 *            Type of the values.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ParameterValueList<T> implements Serializable {

	private static final long serialVersionUID = -6730179918476230975L;
	private ParameterDefinition parameter;
	private List<T> values;

	public ParameterValueList(ParameterDefinition parameter) {
		super();
		this.parameter = parameter;
		values = new ArrayList<T>();
	}

	public ParameterValueList(ParameterDefinition parameter, List<T> values) {
		super();
		this.parameter = parameter;
		this.values = values;
	}

	/**
	 * Creates a single value parameter value list.
	 * 
	 * @param singleParameterValue
	 *            a single parameter value instance
	 */
	public ParameterValueList(ParameterValue<T> singleParameterValue) {
		super();
		this.parameter = singleParameterValue.getParameter();
		this.values = new ArrayList<T>();
		this.values.add(singleParameterValue.getValue());
	}

	public ParameterDefinition getParameter() {
		return parameter;
	}

	public List<T> getValues() {
		return values;
	}

	public double getMean() {
		double mean = 0.0;
		if (getValues().size() > 0) {
			for (T value : getValues()) {
				if (ParameterUtil.getTypeEnumeration(parameter.getType()).equals(ParameterType.DOUBLE)) {
					mean += (Double) value;
				} else if (ParameterUtil.getTypeEnumeration(parameter.getType())
						.equals(ParameterType.INTEGER)) {
					mean += ((Integer) value).doubleValue();
				} else {
					throw new IllegalStateException(
							"The functions getMin() and getMax() are not supported for columns associated with a parameter type other than Double or Integer!");
				}

			}
		}
		return mean / getValues().size();
	}

	public ParameterValue<?> getMeanAsParameterValue() {
		return ParameterValueFactory.createParameterValue(parameter, getMean());
	}

	public void merge(ParameterValueList other) {
		if (!parameter.equals(other.getParameter())) {
			throw new IllegalArgumentException("Cannot merge ParameterValueLists of different ParameterDefinitions!");
		}
		this.values.addAll(other.getValues());
	}

	public void addValue(Object value) {
		T convertedValue = (T) ParameterValueFactory.convertValue(value,
				ParameterUtil.getTypeEnumeration(parameter.getType()));
		this.values.add(convertedValue);
	}

	public void addValues(List<Object> values) {
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
			} else {
				throw new IllegalStateException(
						"The function getValueAsDouble is supported only by Double and Integer ParameterValues!");
			}
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
			} else {
				throw new IllegalStateException(
						"The function getValueAsInteger is supported only by Double and Integer ParameterValues!");
			}
		}

		return result;
	}

	public int getSize() {
		return values.size();
	}
}

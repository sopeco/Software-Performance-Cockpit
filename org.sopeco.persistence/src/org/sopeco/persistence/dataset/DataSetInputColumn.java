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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.dataset.util.ParameterUtil;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;

/**
 * DataSetColumn represents a column in a DataSet consisting of a parameter and
 * its associated values.
 * 
 * @author Jens Happe
 * 
 * @param <T>
 *            Type of the column's data.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DataSetInputColumn<T> extends AbstractDataSetColumn<T> implements Iterable<ParameterValue<T>>,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Values associated to the parameter.
	 */
	private List<T> valueList;

	/**
	 * Constructor. To be used by builders & factories only.
	 * 
	 * @param parameter
	 * @param valueList
	 */
	protected DataSetInputColumn(ParameterDefinition parameter, List<T> values) {
		super();
		if (!parameter.getRole().equals(ParameterRole.INPUT)) {
			throw new IllegalArgumentException("Cannot create a DataSetInputColumn for a non input parameter.");
		}
		setParameter(parameter);
		this.valueList = values;
	}

	/**
	 * @return Values of the column associated to the parameter.
	 */
	public List<T> getValueList() {
		return valueList;
	}

	/**
	 * @param row
	 *            Row of interest.
	 * @return Value at the given row.
	 */

	public ParameterValue<T> getParameterValue(int row) {
		if (row >= this.valueList.size()) {
			throw new IllegalArgumentException("Index exceeds row length. Index: " + row + " row length: "
					+ valueList.size());
		}
		T value = valueList.get(row);
		return (ParameterValue<T>) ParameterValueFactory.createParameterValue(getParameter(), value);
	}

	/**
	 * @param row
	 *            Row of interest.
	 * @return Value at the given row.
	 */
	public List<ParameterValue<?>> getParameterValues() {

		List<ParameterValue<?>> result = new ArrayList<ParameterValue<?>>();
		for (T value : valueList) {
			ParameterValue<T> pv = (ParameterValue<T>) ParameterValueFactory
					.createParameterValue(getParameter(), value);
			result.add(pv);
		}
		return result;
	}

	/**
	 * Iterator over the values of the column.
	 */
	@Override
	public Iterator<ParameterValue<T>> iterator() {
		return new Iterator<ParameterValue<T>>() {
			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < size();
			}

			@Override
			public ParameterValue<T> next() {
				return getParameterValue(i++);
			}

			@Override
			public void remove() {
				throw new IllegalStateException("DataSet cannot be edited.");
			}
		};
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((valueList == null) ? 0 : valueList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DataSetInputColumn) {
			DataSetInputColumn other = (DataSetInputColumn) obj;
			return this.getParameter().equals(other.getParameter()) && this.getValueList().equals(other.getValueList());
		}
		return false;
	}

	public boolean isVaried() {
		if (getValueList().size() > 0) {
			T first = getValueList().get(0);
			for (T value : getValueList()) {
				if (!first.equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	public double getMin() {
		double min = Double.MAX_VALUE;
		if (getValueList().size() > 0) {
			for (T value : getValueList()) {
				if (ParameterUtil.getTypeEnumeration(getParameter().getType()).equals(ParameterType.DOUBLE)) {
					if ((Double) value < min) {
						min = (Double) value;
					}
				} else if (ParameterUtil.getTypeEnumeration(getParameter().getType()).equals(ParameterType.INTEGER)) {
					if (((Integer) value).doubleValue() < min) {
						min = ((Integer) value).doubleValue();
					}
				} else {
					throw new IllegalStateException(
							"The functions getMin() and getMax() are not supported for columns associated with a parameter type other than Double or Integer!");
				}

			}
		}
		return min;
	}

	public double getMax() {
		double max = Double.MIN_VALUE;
		if (getValueList().size() > 0) {
			for (T value : getValueList()) {
				if (ParameterUtil.getTypeEnumeration(getParameter().getType()).equals(ParameterType.DOUBLE)) {
					if ((Double) value > max) {
						max = (Double) value;
					}
				} else if (ParameterUtil.getTypeEnumeration(getParameter().getType()).equals(ParameterType.INTEGER)) {
					if (((Integer) value).doubleValue() > max) {
						max = ((Integer) value).doubleValue();
					}
				} else {
					throw new IllegalStateException(
							"The functions getMin() and getMax() are not supported for columns associated with a parameter type other than Double or Integer!");
				}

			}
		}
		return max;
	}

	public Set<T> getValueSet() {
		Set<T> valueSet = new TreeSet<T>();
		valueSet.addAll(getValueList());
		return valueSet;
	}

	public DataSetInputColumn<T> getCopy() {
		DataSetInputColumn<T> copy = new DataSetInputColumn<T>(getParameter(), new ArrayList());
		for (T val : valueList) {
			copy.getValueList().add(val);
		}
		return copy;
	}

	protected void addValue(T value) {
		valueList.add(value);
	}

	@Override
	public int size() {
		if (valueList != null) {
			return valueList.size();
		} else {
			return 0;
		}
	}
}

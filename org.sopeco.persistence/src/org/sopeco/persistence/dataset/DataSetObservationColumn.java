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

import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.dataset.util.ParameterUtil;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;

/**
 * Column class for storing observation values.
 * 
 * @author Alexander Wert
 * 
 * @param <T> Type of the observation values
 */
public class DataSetObservationColumn<T> extends AbstractDataSetColumn<T> implements Iterable<ParameterValueList<T>>,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Values associated to the parameter.
	 */
	private List<ParameterValueList<T>> valueLists;

	/**
	 * Constructor. To be used by builders & factories only.
	 * 
	 * @param parameter
	 * @param valueLists
	 */
	protected DataSetObservationColumn(ParameterDefinition parameter, List<ParameterValueList<T>> valueLists) {
		super();
		if (!parameter.getRole().equals(ParameterRole.OBSERVATION)) {
			throw new IllegalArgumentException("Cannot create a DataSetInputColumn for a non observation parameter.");
		}
		setParameter(parameter);
		this.valueLists = valueLists;
	}

	/**
	 * @return Values of the column associated to the parameter.
	 */
	public List<ParameterValueList<T>> getValueLists() {
		return valueLists;
	}

	/**
	 * @return Values of the column associated to the parameter.
	 */
	public List<T> getAllValues() {
		List<T> result = new ArrayList<T>();
		for (ParameterValueList<T> pvl : valueLists) {
			result.addAll(pvl.getValues());
		}
		return result;
	}

	/**
	 * @return Values of the column associated to the parameter.
	 */
	private List<ParameterValue<?>> getAllValuesAsParameterValues() {
		List<ParameterValue<?>> result = new ArrayList<ParameterValue<?>>();
		for (ParameterValueList<T> pvl : valueLists) {
			for (Object value : pvl.getValues()) {

				result.add(ParameterValueFactory.createParameterValue(getParameter(), value));
			}

		}
		return result;
	}

	/**
	 * @return Number of values of the column.
	 */
	public int size() {
		if (valueLists != null) {
			return valueLists.size();
		} else {
			return 0;
		}
	}

	public double getMin() {
		double min = Double.MAX_VALUE;
		if (getAllValues().size() > 0) {
			for (T value : getAllValues()) {
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
		if (getAllValues().size() > 0) {
			for (T value : getAllValues()) {
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

	/**
	 * @param row
	 *            Row of interest.
	 * @return Value at the given row.
	 */
	public ParameterValueList<T> getParameterValues(int row) {
		if (row >= this.valueLists.size()) {
			throw new IllegalArgumentException("Index exceeds row length. Index: " + row + " row length: "
					+ valueLists.size());
		}
		ParameterValueList<T> values = valueLists.get(row);
		return values;
	}

	/**
	 * Iterator over the values of the column.
	 */
	@Override
	public Iterator<ParameterValueList<T>> iterator() {
		return new Iterator<ParameterValueList<T>>() {
			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < size();
			}

			@Override
			public ParameterValueList<T> next() {
				return getParameterValues(i++);
			}

			@Override
			public void remove() {
				throw new IllegalStateException("DataSet cannot be edited.");
			}
		};
	}

	protected void addValues(List<T> values) {

		ParameterValueList<T> pvl = new ParameterValueList<T>(getParameter(), values);
		valueLists.add(pvl);
	}

	protected void addValues(ParameterValueList<T> values) {

		valueLists.add(values);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addValue(Object value) {
		List list = new ArrayList();
		list.add(value);
		ParameterValueList<T> pvl = new ParameterValueList<T>(getParameter(), list);
		valueLists.add(pvl);
	}

	@Override
	public List<ParameterValue<?>> getParameterValues() {
		return getAllValuesAsParameterValues();
	}

}

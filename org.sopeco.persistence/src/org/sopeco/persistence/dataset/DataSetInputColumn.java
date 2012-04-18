package org.sopeco.persistence.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.sopeco.configuration.parameter.ParameterRole;
import org.sopeco.configuration.parameter.ParameterType;
import org.sopeco.configuration.parameter.ParameterUsage;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueFactory;

/**
 * DataSetColumn represents a column in a DataSet consisting of a parameter and
 * its associated values.
 * 
 * @author Jens Happe
 * 
 * @param <T>
 *            Type of the column's data.
 */
public class DataSetInputColumn<T> extends AbstractDataSetColumn<T> implements
		Iterable<ParameterValue<T>>, Serializable {

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
	protected DataSetInputColumn(ParameterUsage parameter, List<T> valueList) {
		super();
		if (!parameter.getRole().equals(ParameterRole.INPUT)) {
			throw new IllegalArgumentException(
					"Cannot create a DataSetInputColumn for a non input parameter.");
		}
		this.parameter = parameter;
		this.valueList = valueList;
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
		if (row >= this.valueList.size())
			throw new IllegalArgumentException(
					"Index exceeds row length. Index: " + row + " row length: "
							+ valueList.size());
		T value = valueList.get(row);
		return (ParameterValue<T>) ParameterValueFactory.createParameterValue(
				parameter, value);
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
					.createParameterValue(parameter, value);
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

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DataSetInputColumn) {
			DataSetInputColumn other = (DataSetInputColumn) obj;
			return this.getParameter().equals(other.getParameter())
					&& this.getValueList().equals(other.getValueList());
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
				if (parameter.getType().equals(ParameterType.DOUBLE)) {
					if ((Double) value < min) {
						min = (Double) value;
					}
				} else if (parameter.getType().equals(ParameterType.INTEGER)) {
					if (((Integer) value).doubleValue() < min) {
						min = ((Integer) value).doubleValue();
					}
				} else
					throw new IllegalStateException(
							"The functions getMin() and getMax() are not supported for columns associated with a parameter type other than Double or Integer!");

			}
		}
		return min;
	}

	public double getMax() {
		double max = Double.MIN_VALUE;
		if (getValueList().size() > 0) {
			for (T value : getValueList()) {
				if (parameter.getType().equals(ParameterType.DOUBLE)) {
					if ((Double) value > max) {
						max = (Double) value;
					}
				} else if (parameter.getType().equals(ParameterType.INTEGER)) {
					if (((Integer) value).doubleValue() > max) {
						max = ((Integer) value).doubleValue();
					}
				} else
					throw new IllegalStateException(
							"The functions getMin() and getMax() are not supported for columns associated with a parameter type other than Double or Integer!");

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
		DataSetInputColumn<T> copy = new DataSetInputColumn<T>(parameter,
				new ArrayList());
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

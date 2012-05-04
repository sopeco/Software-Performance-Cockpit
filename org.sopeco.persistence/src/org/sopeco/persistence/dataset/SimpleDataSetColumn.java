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

/**
 * DataSetColumn represents a column in a DataSet consisting of a parameter and
 * its associated values.
 * 
 * @author Jens Happe
 * 
 * @param <T>
 *            Type of the column's data.
 */
public class SimpleDataSetColumn<T> implements Iterable<ParameterValue<T>>,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parameter / identifier of the column in the DataSet.
	 */
	private ParameterDefinition parameter;

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
	protected SimpleDataSetColumn(ParameterDefinition parameter, List<T> valueList) {
		super();
		this.parameter = parameter;
		this.valueList = valueList;
	}

	/**
	 * @return Parameter / Identifier of the column.
	 */
	public ParameterDefinition getParameter() {
		return parameter;
	}

	/**
	 * @return Values of the column associated to the parameter.
	 */
	public List<T> getValueList() {
		return valueList;
	}

	/**
	 * @return Number of values of the column.
	 */
	public int size() {
		if (valueList != null) {
			return valueList.size();
		} else {
			return 0;
		}
	}

	/**
	 * @param row
	 *            Row of interest.
	 * @return Value at the given row.
	 */
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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

	@SuppressWarnings({ "rawtypes" })
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SimpleDataSetColumn) {
			SimpleDataSetColumn other = (SimpleDataSetColumn) obj;
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
				if (ParameterUtil.getTypeEnumeration(parameter.getType()).equals(ParameterType.DOUBLE)) {
					if ((Double) value < min) {
						min = (Double) value;
					}
				} else if (ParameterUtil.getTypeEnumeration(parameter.getType()).equals(ParameterType.INTEGER)) {
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
				if (ParameterUtil.getTypeEnumeration(parameter.getType()).equals(ParameterType.DOUBLE)) {
					if ((Double) value > max) {
						max = (Double) value;
					}
				} else if (ParameterUtil.getTypeEnumeration(parameter.getType()).equals(ParameterType.INTEGER)) {
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SimpleDataSetColumn<T> getCopy() {
		SimpleDataSetColumn<T> copy = new SimpleDataSetColumn<T>(parameter,
				new ArrayList());
		for (T val : valueList) {
			copy.getValueList().add(val);
		}
		return copy;
	}

	protected void addValue(T value) {
		valueList.add(value);
	}
}

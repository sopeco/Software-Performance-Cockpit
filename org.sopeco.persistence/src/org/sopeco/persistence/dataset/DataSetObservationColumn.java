package org.sopeco.persistence.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.sopeco.configuration.parameter.ParameterRole;
import org.sopeco.configuration.parameter.ParameterType;
import org.sopeco.configuration.parameter.ParameterUsage;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueFactory;

public class DataSetObservationColumn<T> extends AbstractDataSetColumn<T>
		implements Iterable<ParameterValueList<T>>, Serializable {

	/**
	 * Values associated to the parameter.
	 */
	private List<ParameterValueList<T>> valueList;

	/**
	 * Constructor. To be used by builders & factories only.
	 * 
	 * @param parameter
	 * @param valueList
	 */
	protected DataSetObservationColumn(ParameterUsage parameter,
			List<ParameterValueList<T>> valueList) {
		super();
		if (!parameter.getRole().equals(ParameterRole.OBSERVATION) && !parameter.getRole().equals(ParameterRole.OBSERVABLE_TIME_SERIES)) {
			throw new IllegalArgumentException(
					"Cannot create a DataSetInputColumn for a non observation parameter.");
		}
		this.parameter = parameter;
		this.valueList = valueList;
	}

	/**
	 * @return Values of the column associated to the parameter.
	 */
	public List<ParameterValueList<T>> getValueLists() {
		return valueList;
	}

	/**
	 * @return Values of the column associated to the parameter.
	 */
	public List<T> getAllValues() {
		List<T> result = new ArrayList<T>();
		for (ParameterValueList<T> pvl : valueList) {
			result.addAll(pvl.getValues());
		}
		return result;
	}

	/**
	 * @return Values of the column associated to the parameter.
	 */
	private List<ParameterValue<?>> getAllValuesAsParameterValues() {
		List<ParameterValue<?>> result = new ArrayList<ParameterValue<?>>();
		for (ParameterValueList<T> pvl : valueList) {
			for (Object value : pvl.getValues()) {

				result.add(ParameterValueFactory.createParameterValue(
						parameter, value));
			}

		}
		return result;
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

	public double getMin() {
		double min = Double.MAX_VALUE;
		if (getAllValues().size() > 0) {
			for (T value : getAllValues()) {
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
		if (getAllValues().size() > 0) {
			for (T value : getAllValues()) {
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

	/**
	 * @param row
	 *            Row of interest.
	 * @return Value at the given row.
	 */
	public ParameterValueList<T> getParameterValues(int row) {
		if (row >= this.valueList.size())
			throw new IllegalArgumentException(
					"Index exceeds row length. Index: " + row + " row length: "
							+ valueList.size());
		ParameterValueList<T> values = valueList.get(row);
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
		if(isTimeSeries()){
			throw new IllegalStateException("Use the addTimeSeriesValues() method for setting the value of a timeSeries column");
		}
		ParameterValueList<T> pvl = new ParameterValueList<T>(parameter, values);
		valueList.add(pvl);
	}

	protected void addValues(ParameterValueList<T> values) {
		if(isTimeSeries()){
			throw new IllegalStateException("Use the addTimeSeries() method for setting the values of a timeSeries column");
		}
		valueList.add(values);
	}
	
	protected void addValue(Object value) {
		List list = new ArrayList();
		list.add(value);
		ParameterValueList<T> pvl = new ParameterValueList<T>(parameter, list);
		valueList.add(pvl);
	}

	@Override
	public List<ParameterValue<?>> getParameterValues() {
		return getAllValuesAsParameterValues();
	}
	
	public boolean isTimeSeries(){
		return parameter.getRole().equals(ParameterRole.OBSERVABLE_TIME_SERIES);
	}
	
	protected void addTimeSeriesValues(List<T> values, List<Double> timestamps) {
		if(!isTimeSeries()){
			throw new IllegalStateException("TimeSeries value can not be added to a non TimeSeries Column!");
		}
		TimeSeries<T> pvl = new TimeSeries<T>(parameter, values, timestamps);
		valueList.add(pvl);
	}

	protected void addTimeSeries(TimeSeries<T> values) {
		if(!isTimeSeries()){
			throw new IllegalStateException("TimeSeries value can not be added to a non TimeSeries Column!");
		}
		valueList.add(values);
	}
	
	public List<Double> getAllTimestamps() {
		if(!isTimeSeries()){
			throw new IllegalStateException("Cannot retrieve timestamps for non time series column!");
		}
		List<Double> result = new ArrayList<Double>();
		for (ParameterValueList<T> pvl : valueList) {
			result.addAll(((TimeSeries)pvl).getTimeStamps());
		}
		return result;
	}
	

	
	protected void addTimeSeriesValue(Double timestamp, Object value) {
		List valueList = new ArrayList();
		List timeList = new ArrayList();
		valueList.add(value);
		timeList.add(timestamp);
		ParameterValueList<T> pvl = new TimeSeries<T>(parameter, valueList, timeList);
		this.valueList.add(pvl);
	}
	
}

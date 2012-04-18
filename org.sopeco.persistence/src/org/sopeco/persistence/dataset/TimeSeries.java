package org.sopeco.persistence.dataset;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.configuration.parameter.ParameterUsage;

public class TimeSeries<T> extends ParameterValueList<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7898418092531623722L;

	private List<Double> timeStamps;

	public TimeSeries(ParameterUsage parameter) {
		super(parameter);
		timeStamps = new ArrayList<Double>();
	}

	public TimeSeries(ParameterUsage parameter, List<T> values,
			List<Double> times) {
		super(parameter, values);
		if (times.size() != values.size()) {
			throw new IllegalArgumentException("The number of timestamp "
					+ "entries is not equal to the number of values!");
		}
		timeStamps = times;
	}

	public List<Double> getTimeStamps() {
		return timeStamps;
	}

	public void merge(ParameterValueList other) {
		if (!(other instanceof TimeSeries)) {
			throw new IllegalArgumentException(
					"Cannot merge TimeSeries with a general ParameterValueList!");
		}
		super.merge(other);
		this.timeStamps.addAll(((TimeSeries) other).getTimeStamps());
	}

	public void addTimeValuePair(Double timestamp, Object value) {
		super.addValue(value);
		this.timeStamps.add(timestamp);
	}

	public void addTimeValuePairs(List<Double> timestamps,
			List<Object> values) {
		if (timestamps.size() != values.size()) {
			throw new IllegalArgumentException("The number of timestamp "
					+ "entries is not equal to the number of values!");
		}
		for (int i = 0; i < values.size(); i++) {
			addTimeValuePair(timestamps.get(i), values.get(i));
		}
	}

}

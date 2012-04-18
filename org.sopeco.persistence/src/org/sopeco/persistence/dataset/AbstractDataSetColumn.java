package org.sopeco.persistence.dataset;

import java.util.List;

import org.sopeco.configuration.parameter.ParameterType;
import org.sopeco.configuration.parameter.ParameterUsage;
import org.sopeco.persistence.dataset.ParameterValue;

public abstract class AbstractDataSetColumn<T> {
	/**
	 * Parameter / identifier of the column in the DataSet.
	 */
	protected ParameterUsage parameter;

	/**
	 * @return Parameter / Identifier of the column.
	 */
	public ParameterUsage getParameter() {
		return parameter;
	}

	/**
	 * @return Number of values of the column.
	 */
	public abstract int size();
	
	public abstract double getMin();

	public abstract double getMax();
	
	public abstract List<ParameterValue<?>> getParameterValues(); 
}

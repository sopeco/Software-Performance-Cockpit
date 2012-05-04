package org.sopeco.persistence.dataset;

import java.io.Serializable;
import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

public abstract class AbstractDataSetColumn<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Parameter / identifier of the column in the DataSet.
	 */
	protected ParameterDefinition parameter;

	/**
	 * @return Parameter / Identifier of the column.
	 */
	public ParameterDefinition getParameter() {
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

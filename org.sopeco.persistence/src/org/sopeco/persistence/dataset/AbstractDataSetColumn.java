package org.sopeco.persistence.dataset;

import java.io.Serializable;
import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Comprises common members and abstract functions for all concrete column implementations.
 * @author Alexander Wert
 *
 * @param <T> Type of the values stored in that column
 */
public abstract class AbstractDataSetColumn<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Parameter / identifier of the column in the DataSet.
	 */
	private ParameterDefinition parameter;

	/**
	 * @return Parameter / Identifier of the column.
	 */
	public ParameterDefinition getParameter() {
		return parameter;
	}

	protected void setParameter(ParameterDefinition pDef) {
		parameter = pDef;
	}

	/**
	 * @return Number of values of the column.
	 */
	public abstract int size();

	public abstract double getMin();

	public abstract double getMax();

	public abstract List<ParameterValue<?>> getParameterValues();
}

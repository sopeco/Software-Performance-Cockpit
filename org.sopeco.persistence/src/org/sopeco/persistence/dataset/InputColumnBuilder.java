package org.sopeco.persistence.dataset;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;



public class InputColumnBuilder<T> {

	private DataSetInputColumn<T> column;

	public InputColumnBuilder() {
	}

	/**
	 * Create a new column for the given parameter. The column is empty and not
	 * part of the new DataSet yet. Values are added by method addValue. Once
	 * all values have been added, the column can be added to the new DataSet by
	 * calling finishColumn.
	 * 
	 * @param parameter
	 *            Parameter for which new data will be provided.
	 */
	public void setParameter(ParameterDefinition parameter) {
		column = new DataSetInputColumn<T>(parameter, new ArrayList<T>());

	}

	/**
	 * Adds a new value to the column under construction (created by
	 * startColumn).
	 * 
	 * @param value
	 *            Value to be added.
	 */
	public void addValue(T value) {
		if (column == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		column.getValueList().add(value);
	}

	/**
	 * Adds a set of new values to the column under construction (created by
	 * startColumn).
	 * 
	 * @param valueList
	 *            Values to be added.
	 */
	public void addValueList(List<T> valueList) {
		if (column == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		column.getValueList().addAll(valueList);
	}

	/**
	 * Finish the column under construction and add it to the newly created
	 * DataSet.
	 */
	public DataSetInputColumn<T> createColumn() {
		if (column == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		return column;
	}
}

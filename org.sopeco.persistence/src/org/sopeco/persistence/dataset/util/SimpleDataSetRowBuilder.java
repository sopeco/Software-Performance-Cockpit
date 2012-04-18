package org.sopeco.persistence.dataset.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sopeco.configuration.parameter.ParameterUsage;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.SimpleDataSetColumn;
import org.sopeco.persistence.dataset.SimpleDataSetRow;

/**
 * Builder for DataSets using rows.
 * 
 * @author Jens Happe
 * 
 */
@SuppressWarnings("unchecked")
public class SimpleDataSetRowBuilder extends SimpleAbstractDataSetRowBuilder {

	/**
	 * Row under construction using methods startRow, addParameterValue, and
	 * finishRow.
	 */
	private List<ParameterValue> nextRow;

	/**
	 * Create a new row. New ParameterValues can be added to the row calling
	 * addParameterValue. finishRow finished the current row and adds it the the
	 * new DataSet.
	 */
	public void startRow() {
		nextRow = new ArrayList<ParameterValue>();
	}

	/**
	 * Adds a new parameterValue to the row under construction.
	 * 
	 * @param parameter
	 *            Parameter for which a value is added.
	 * @param value
	 *            Value for the parameter in the new row.
	 */
	public void addParameterValue(ParameterUsage parameter, Object value) {
		if (nextRow == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		ParameterValue parameterValue = ParameterValueFactory
				.createParameterValue(parameter, value);

		nextRow.add(parameterValue);
	}

	/**
	 * Finishes the row under construction and adds it to the new DataSet.
	 */
	public void finishRow() {
		if (nextRow == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		appendRow(nextRow);
		nextRow = null;
	}

	/**
	 * Appends a row to the new DataSet. The row must contain the same
	 * parameters as all rows added to the DataSet before.
	 * 
	 * @param row
	 *            Row to be added.
	 */
	public void appendRow(Collection<ParameterValue> row) {
		if (columnMap.isEmpty()) {
			for (ParameterValue value : row) {
				SimpleDataSetColumn colum = new SimpleDataSetColumn(
						value.getParameter(), new ArrayList());
				columnMap.put(value.getParameter().getID(), colum);
			}
		}

		checkParameters(row);
		for (ParameterValue value : row) {
			SimpleDataSetColumn column = columnMap.get(value.getParameter()
					.getID());
			column.getValueList().add(value.getValue());
		}
	}

	/**
	 * Appends a row to the new DataSet. The row must contain the same
	 * parameters as all rows added to the DataSet before.
	 * 
	 * @param row
	 *            Row to be added.
	 */
	public void appendRow(SimpleDataSetRow row) {
		appendRow(row.getRowValues());
	}

	/**
	 * Appends a set of rows to the new DataSet. The rows must contain the same
	 * parameters as all rows added to the DataSet before.
	 * 
	 * @param rowList
	 *            Rows to be added.
	 */
	public void appendRows(List<SimpleDataSetRow> rowList) {
		for (SimpleDataSetRow row : rowList) {
			appendRow(row);
		}
	}
}

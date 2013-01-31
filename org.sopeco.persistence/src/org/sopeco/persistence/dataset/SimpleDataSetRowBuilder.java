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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Builder for DataSets using rows.
 * 
 * @author Jens Happe
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
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
	public void addParameterValue(ParameterDefinition parameter, Object value) {
		if (nextRow == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		ParameterValue parameterValue = ParameterValueFactory.createParameterValue(parameter, value);

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
		if (getColumnMap().isEmpty()) {
			for (ParameterValue value : row) {
				SimpleDataSetColumn colum = new SimpleDataSetColumn(value.getParameter(), new ArrayList());
				getColumnMap().put(value.getParameter().getFullName(), colum);
			}
		}

		checkParameters(row);
		for (ParameterValue value : row) {
			SimpleDataSetColumn column = getColumnMap().get(value.getParameter().getFullName());
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

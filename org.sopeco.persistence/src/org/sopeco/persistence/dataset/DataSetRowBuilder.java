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
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.util.ParameterCollection;

/**
 * Builder for DataSets using rows.
 * 
 * @author Jens Happe
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DataSetRowBuilder extends AbstractDataSetRowBuilder {

	/**
	 * Row under construction using methods startRow, addParameterValue, and
	 * finishRow.
	 */
	private List<ParameterValue<?>> nextRowInput;

	/**
	 * Row under construction using methods startRow, addParameterValue, and
	 * finishRow.
	 */
	private List<ParameterValueList<?>> nextRowObservation;

	/**
	 * Create a new row. New ParameterValues can be added to the row calling
	 * addParameterValue. finishRow finished the current row and adds it the the
	 * new DataSet.
	 */
	public void startRow() {
		nextRowInput = new ArrayList<ParameterValue<?>>();
		nextRowObservation = new ArrayList<ParameterValueList<?>>();
	}

	/**
	 * Adds a new parameterValue to the row under construction.
	 * 
	 * @param parameter
	 *            Parameter for which a value is added.
	 * @param value
	 *            Value for the parameter in the new row.
	 */
	public void addInputParameterValue(ParameterDefinition parameter, Object value) {
		if (nextRowInput == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		if (!parameter.getRole().equals(ParameterRole.INPUT)) {
			throw new IllegalArgumentException("Parameter must be an input parameter!");
		}
		ParameterValue parameterValue = ParameterValueFactory.createParameterValue(parameter, value);

		nextRowInput.add(parameterValue);
	}

	public List<ParameterValue<?>> getConfigurationValues() {
		return nextRowInput;
	}

	/**
	 * Adds a new parameterValue to the row under construction.
	 * 
	 * @param parameter
	 *            Parameter for which a value is added.
	 * @param value
	 *            Value for the parameter in the new row.
	 */
	public void addObservationParameterValues(ParameterDefinition parameter, List values) {
		if (nextRowObservation == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		if (!parameter.getRole().equals(ParameterRole.OBSERVATION)) {
			throw new IllegalArgumentException("Parameter must be an observation parameter!");
		}
		ParameterValueList pvl = getPVL(parameter);
		if (pvl == null) {
			pvl = new ParameterValueList(parameter, values);
			nextRowObservation.add(pvl);
		} else {
			pvl.addValues(values);
		}

	}

	public void addObservationParameterValues(ParameterValueList values) {
		if (nextRowObservation == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		if (!values.getParameter().getRole().equals(ParameterRole.OBSERVATION)) {
			throw new IllegalArgumentException("Parameter must be an observation parameter!");
		}
		ParameterValueList pvl = getPVL(values.getParameter());
		if (pvl == null) {
			nextRowObservation.add(values);
		} else {
			throw new IllegalStateException("Values already set for this row!");
		}

	}

	private ParameterValueList getPVL(ParameterDefinition p) {
		for (ParameterValueList pvl : nextRowObservation) {
			if (pvl.getParameter().equals(p)) {
				return pvl;
			}
		}
		return null;
	}

	/**
	 * Adds a new parameterValue to the row under construction.
	 * 
	 * @param parameter
	 *            Parameter for which a value is added.
	 * @param value
	 *            Value for the parameter in the new row.
	 */
	public void addObservationParameterValue(ParameterDefinition parameter, Object value) {
		if (nextRowObservation == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		if (!parameter.getRole().equals(ParameterRole.OBSERVATION)) {
			throw new IllegalArgumentException("Parameter must be an observation parameter!");
		}
		ParameterValueList pvl = getPVL(parameter);
		if (pvl == null) {
			pvl = new ParameterValueList(parameter, new ArrayList());
			pvl.addValue(value);
			nextRowObservation.add(pvl);
		} else {
			pvl.addValue(value);
		}
	}

	/**
	 * Finishes the row under construction and adds it to the new DataSet.
	 */
	public void finishRow() {
		if (nextRowInput == null || nextRowObservation == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		appendRow(nextRowInput, nextRowObservation);
		nextRowInput = null;
		nextRowObservation = null;
	}

	/**
	 * Appends a row to the new DataSet. The row must contain the same
	 * parameters as all rows added to the DataSet before.
	 * 
	 * @param row
	 *            Row to be added.
	 */
	public void appendRow(Collection<ParameterValue<?>> inputValues, List<ParameterValueList<?>> observationValues) {

		if (getInputColumnMap().isEmpty()) {
			for (ParameterValue value : inputValues) {
				DataSetInputColumn colum = new DataSetInputColumn(value.getParameter(), new ArrayList());
				getInputColumnMap().put(value.getParameter(), colum);
			}
		}

		checkInputParameters(inputValues);
		for (ParameterValue value : inputValues) {
			DataSetInputColumn column = getInputColumnMap().get(value.getParameter());
			column.getValueList().add(value.getValue());
		}

		if (getObservationColumnMap().isEmpty()) {
			for (ParameterValueList pvl : observationValues) {
				DataSetObservationColumn colum = new DataSetObservationColumn(pvl.getParameter(), new ArrayList());
				getObservationColumnMap().put(pvl.getParameter(), colum);
			}
		}

		checkOutputParameters(observationValues);
		for (ParameterValueList pvl : observationValues) {
			DataSetObservationColumn column = getObservationColumnMap().get(pvl.getParameter());
			column.getValueLists().add(pvl);
		}

	}

	/**
	 * Appends a row to the new DataSet. The row must contain the same
	 * parameters as all rows added to the DataSet before.
	 * 
	 * @param row
	 *            Row to be added.
	 */
	public void appendRow(DataSetRow row) {
		appendRow(row.getInputRowValues(), row.getObservableRowValues());
	}

	/**
	 * Appends a set of rows to the new DataSet. The rows must contain the same
	 * parameters as all rows added to the DataSet before.
	 * 
	 * @param rowList
	 *            Rows to be added.
	 */
	public void appendRows(List<DataSetRow> rowList) {
		for (DataSetRow row : rowList) {
			appendRow(row);
		}
	}

	public ParameterValue getCurrentInputValue(ParameterDefinition parameterDefinition) {
		for (ParameterValue pv : nextRowInput) {
			if (pv.getParameter().equals(parameterDefinition)) {
				return pv;
			}
		}
		return null;
	}

	public ParameterValueList getCurrentObservationValues(ParameterDefinition parameterDefinition) {
		for (ParameterValueList pvl : nextRowObservation) {
			if (pvl.getParameter().equals(parameterDefinition)) {
				return pvl;
			}
		}
		return null;
	}

	public void addAllInputParameterValues(ParameterCollection<ParameterValue<?>> inputPVs) {
		for (ParameterValue<?> parameterValue : inputPVs) {
			addInputParameterValue(parameterValue.getParameter(), parameterValue.getValue());
		}
	}

	public void addAllObservationParameterValues(Collection<ParameterValueList<?>> observations) {
		for (ParameterValueList<?> pvl : observations) {
			addObservationParameterValues(pvl);
		}

	}

}

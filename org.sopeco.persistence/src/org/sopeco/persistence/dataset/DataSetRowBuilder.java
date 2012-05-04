package org.sopeco.persistence.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;

/**
 * Builder for DataSets using rows.
 * 
 * @author Jens Happe
 * 
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class DataSetRowBuilder extends AbstractDataSetRowBuilder {

	/**
	 * Row under construction using methods startRow, addParameterValue, and
	 * finishRow.
	 */
	private List<ParameterValue> nextRowInput;

	/**
	 * Row under construction using methods startRow, addParameterValue, and
	 * finishRow.
	 */
	private List<ParameterValueList> nextRowObservation;

	/**
	 * Create a new row. New ParameterValues can be added to the row calling
	 * addParameterValue. finishRow finished the current row and adds it the the
	 * new DataSet.
	 */
	public void startRow() {
		nextRowInput = new ArrayList<ParameterValue>();
		nextRowObservation = new ArrayList<ParameterValueList>();
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
			throw new IllegalArgumentException(
					"Parameter must be an input parameter!");
		}
		ParameterValue parameterValue = ParameterValueFactory
				.createParameterValue(parameter, value);

		nextRowInput.add(parameterValue);
	}

	public List<ParameterValue> getConfigurationValues() {
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
	public void addObservationParameterValues(ParameterDefinition parameter,
			List values) {
		if (nextRowObservation == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		if (!parameter.getRole().equals(ParameterRole.OBSERVATION)) {
			throw new IllegalArgumentException(
					"Parameter must be an observation parameter!");
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
			throw new IllegalArgumentException(
					"Parameter must be an observation parameter!");
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
	public void addObservationParameterValue(ParameterDefinition parameter,
			Object value) {
		if (nextRowObservation == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		if (!parameter.getRole().equals(ParameterRole.OBSERVATION)) {
			throw new IllegalArgumentException(
					"Parameter must be an observation parameter!");
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
	public void appendRow(Collection<ParameterValue> inputValues,
			List<ParameterValueList> observationValues) {

		if (inputColumnMap.isEmpty()) {
			for (ParameterValue value : inputValues) {
				DataSetInputColumn colum = new DataSetInputColumn(
						value.getParameter(), new ArrayList());
				inputColumnMap.put(value.getParameter(), colum);
			}
		}

		checkInputParameters(inputValues);
		for (ParameterValue value : inputValues) {
			DataSetInputColumn column = inputColumnMap
					.get(value.getParameter());
			column.getValueList().add(value.getValue());
		}

		if (observationColumnMap.isEmpty()) {
			for (ParameterValueList pvl : observationValues) {
				DataSetObservationColumn colum = new DataSetObservationColumn(
						pvl.getParameter(), new ArrayList());
				observationColumnMap.put(pvl.getParameter(), colum);
			}
		}

		checkOutputParameters(observationValues);
		for (ParameterValueList pvl : observationValues) {
			DataSetObservationColumn column = observationColumnMap.get(pvl
					.getParameter());
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

	public ParameterValue getCurrentInputValue(ParameterDefinition ParameterDefinition) {
		for (ParameterValue pv : nextRowInput) {
			if (pv.getParameter().equals(ParameterDefinition)) {
				return pv;
			}
		}
		return null;
	}

	public ParameterValueList getCurrentObservationValues(
			ParameterDefinition ParameterDefinition) {
		for (ParameterValueList pvl : nextRowObservation) {
			if (pvl.getParameter().equals(ParameterDefinition)) {
				return pvl;
			}
		}
		return null;
	}

//	/**
//	 * Adds a new parameterValue to the row under construction.
//	 * 
//	 * @param parameter
//	 *            Parameter for which a value is added.
//	 * @param value
//	 *            Value for the parameter in the new row.
//	 */
//	public void addTimeSeriesValues(ParameterDefinition parameter, List values,
//			List<Double> timestamps) {
//		if (nextRowObservation == null) {
//			throw new IllegalStateException("The row must be started first.");
//		}
//		if (!parameter.getRole().equals(ParameterRole.OBSERVABLE_TIME_SERIES)) {
//			throw new IllegalArgumentException(
//					"Parameter must be an observable time series parameter!");
//		}
//		ParameterValueList pvl = getPVL(parameter);
//		if (pvl == null) {
//			pvl = new TimeSeries(parameter, values, timestamps);
//			nextRowObservation.add(pvl);
//		} else {
//			((TimeSeries) pvl).addTimeValuePairs(timestamps, values);
//		}
//
//	}

//	public void addTimeSeriesValues(TimeSeries values) {
//		if (nextRowObservation == null) {
//			throw new IllegalStateException("The row must be started first.");
//		}
//		if (!values.getParameter().getRole()
//				.equals(ParameterRole.OBSERVABLE_TIME_SERIES)) {
//			throw new IllegalArgumentException(
//					"Parameter must be an observable time series parameter!");
//		}
//		ParameterValueList pvl = getPVL(values.getParameter());
//		if (pvl == null) {
//			nextRowObservation.add(values);
//		} else {
//			throw new IllegalStateException("Values already set for this row!");
//		}
//
//	}

//	/**
//	 * Adds a new parameterValue to the row under construction.
//	 * 
//	 * @param parameter
//	 *            Parameter for which a value is added.
//	 * @param value
//	 *            Value for the parameter in the new row.
//	 */
//	public void addTimeSeriesValue(ParameterDefinition parameter, Object value,
//			Double timestamp) {
//		if (nextRowObservation == null) {
//			throw new IllegalStateException("The row must be started first.");
//		}
//		if (!parameter.getRole().equals(ParameterRole.OBSERVABLE_TIME_SERIES)) {
//			throw new IllegalArgumentException(
//					"Parameter must be an observable time series parameter!");
//		}
//		ParameterValueList pvl = getPVL(parameter);
//		if (pvl == null) {
//			pvl = new TimeSeries(parameter, new ArrayList(), new ArrayList());
//			((TimeSeries) pvl).addTimeValuePair(timestamp, value);
//			nextRowObservation.add(pvl);
//		} else {
//			pvl.addValue(value);
//		}
//	}
}

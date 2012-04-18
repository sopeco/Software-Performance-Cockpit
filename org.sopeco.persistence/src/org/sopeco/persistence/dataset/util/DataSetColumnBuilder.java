package org.sopeco.persistence.dataset.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.sopeco.configuration.parameter.ParameterUsage;
import org.sopeco.persistence.dataset.AbstractDataSetColumn;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetInputColumn;
import org.sopeco.persistence.dataset.DataSetObservationColumn;
import org.sopeco.persistence.dataset.ParameterValueList;

/**
 * Builder for DataSets from the column perspective.
 * 
 * @author Jens Happe
 * 
 */
@SuppressWarnings("unchecked")
public class DataSetColumnBuilder {

	/**
	 * Columns of the new DataSet.
	 */
	private Map<ParameterUsage, DataSetInputColumn> inputColumnMap = new HashMap<ParameterUsage, DataSetInputColumn>();
	/**
	 * Columns of the new DataSet.
	 */
	private Map<ParameterUsage, DataSetObservationColumn> observationColumnMap = new HashMap<ParameterUsage, DataSetObservationColumn>();
	// /**
	// * Indexes of the columns represented by the according parameter
	// */
	// private Map<Integer, ParameterUsage> parameterIndexes = new
	// HashMap<Integer, ParameterUsage>();

	/**
	 * Column under construction using methods startColumn, addValue,
	 * finishColumn.
	 */
	private AbstractDataSetColumn nextColumn;

	/**
	 * Number of elements for all columns in the dataset.
	 */
	private int size = 0;

	/**
	 * Inserts a new column into the new DataSet.
	 * 
	 * The column must have the same number of rows as all columns added before.
	 * Furthermore, its parameter must be unique within the new DataSet.
	 * 
	 * @param column
	 *            Column to be added.
	 */
	public void addColumn(AbstractDataSetColumn column) {
		if (column instanceof DataSetInputColumn) {
			addInputColumn((DataSetInputColumn) column);
		} else if (column instanceof DataSetObservationColumn) {
			addObservationColumn((DataSetObservationColumn) column);
		}
	}

	private void addInputColumn(DataSetInputColumn column) {
		if (inputColumnMap.isEmpty()) {
			size = column.getValueList().size();
		} else if (column.size() != size) {
			throw new IllegalArgumentException("Column length does not match ("
					+ column.getParameter() + ").");
		}
		if (inputColumnMap.containsKey(column.getParameter())) {
			throw new IllegalArgumentException("Column already exists ("
					+ column.getParameter() + ").");
		}
		inputColumnMap.put(column.getParameter(), column);
		// parameterIndexes.put(parameterIndexes.size(), column.getParameter());
	}

	private void addObservationColumn(DataSetObservationColumn column) {
		if (observationColumnMap.isEmpty()) {
			size = column.getValueLists().size();
		} else if (column.size() != size) {
			throw new IllegalArgumentException("Column length does not match ("
					+ column.getParameter() + ").");
		}
		if (observationColumnMap.containsKey(column.getParameter())) {
			throw new IllegalArgumentException("Column already exists ("
					+ column.getParameter() + ").");
		}
		observationColumnMap.put(column.getParameter(), column);
		// parameterIndexes.put(parameterIndexes.size(), column.getParameter());
	}

	/**
	 * Inserts a set of new columns into the new DataSet.
	 * 
	 * The columns must have the same number of rows as all columns added
	 * before. Furthermore, its parameters must be unique within the new
	 * DataSet.
	 * 
	 * @param columnList
	 *            Columns to be added.
	 */
	public void addColumns(List<AbstractDataSetColumn> columnList) {
		for (AbstractDataSetColumn column : columnList) {
			addColumn(column);
		}
	}

	/**
	 * @return All columns held by the dataset.
	 */
	private Collection<AbstractDataSetColumn> getColumns() {
		List<AbstractDataSetColumn> result = new ArrayList<AbstractDataSetColumn>();
		result.addAll(inputColumnMap.values());
		result.addAll(observationColumnMap.values());
		return result;
	}

	/**
	 * @return All columns held by the dataset.
	 */
	private Collection<DataSetInputColumn> getInputColumns() {
		List<DataSetInputColumn> result = new ArrayList<DataSetInputColumn>();
		result.addAll(inputColumnMap.values());
		return result;
	}

	/**
	 * @return All columns held by the dataset.
	 */
	private Collection<DataSetObservationColumn> getObservationColumns() {
		List<DataSetObservationColumn> result = new ArrayList<DataSetObservationColumn>();
		result.addAll(observationColumnMap.values());
		return result;
	}

	/**
	 * Generates a new DataSet based on the data provided to the builder.
	 * 
	 * @return New DataSet.
	 */
	public DataSetAggregated createDataSet() {
		return new DataSetAggregated(new ArrayList<DataSetInputColumn>(
				getInputColumns()), new ArrayList<DataSetObservationColumn>(
				getObservationColumns()), size, UUID.randomUUID().toString());
	}

	/**
	 * Generates a new DataSet based on the data provided to the builder.
	 * 
	 * @param id
	 *            Unique id of the DataSet.
	 * 
	 * @return New DataSet.
	 */
	public DataSetAggregated createDataSet(String id) {
		return new DataSetAggregated(new ArrayList<DataSetInputColumn>(
				getInputColumns()), new ArrayList<DataSetObservationColumn>(
				getObservationColumns()), size, id);
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
	public void startInputColumn(ParameterUsage parameter) {
		nextColumn = new DataSetInputColumn(parameter, new ArrayList());

	}

	public void startObservationColumn(ParameterUsage parameter) {
		nextColumn = new DataSetObservationColumn(parameter, new ArrayList());

	}

	/**
	 * Adds a new value to the column under construction (created by
	 * startColumn).
	 * 
	 * @param value
	 *            Value to be added.
	 */
	public void addInputValue(Object value) {
		if (nextColumn == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		if (!(nextColumn instanceof DataSetInputColumn)) {
			throw new IllegalStateException(
					"The started column must be an input column!");
		}
		((DataSetInputColumn) nextColumn).getValueList().add(value);
	}

	/**
	 * Adds a new value to the column under construction (created by
	 * startColumn).
	 * 
	 * @param value
	 *            Value to be added.
	 */
	public void addObservationValues(List<Object> values) {
		if (nextColumn == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		if (!(nextColumn instanceof DataSetObservationColumn)) {
			throw new IllegalStateException(
					"The started column must be an input column!");
		}
		for (Object val : values) {
			((DataSetObservationColumn) nextColumn).addValue(values);
		}

	}

	/**
	 * Adds a new value to the column under construction (created by
	 * startColumn).
	 * 
	 * @param value
	 *            Value to be added.
	 */
	public void addObservationTimeSeries(List<Object> values,
			List<Double> timestamps) {
		if (values.size() != timestamps.size()) {
			throw new IllegalArgumentException(
					"Value list and timestamp list must have the same size!");
		}
		if (nextColumn == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		if (!(nextColumn instanceof DataSetObservationColumn)) {
			throw new IllegalStateException(
					"The started column must be an input column!");
		}
		for (int i = 0; i < values.size(); i++) {
			((DataSetObservationColumn) nextColumn).addTimeSeriesValue(timestamps.get(i), values.get(i));
		}
	}

	/**
	 * Adds a set of new values to the column under construction (created by
	 * startColumn).
	 * 
	 * @param valueList
	 *            Values to be added.
	 */
	public void addInputValueList(List valueList) {
		if (nextColumn == null) {
			throw new IllegalStateException("The column must be started first.");
		}
		if (!(nextColumn instanceof DataSetInputColumn)) {
			throw new IllegalStateException(
					"The started column must be an input column!");
		}
		((DataSetInputColumn) nextColumn).getValueList().addAll(valueList);
	}

	/**
	 * Adds a set of new values to the column under construction (created by
	 * startColumn).
	 * 
	 * @param valueList
	 *            Values to be added.
	 */
	public void addObservationValueLists(List<ParameterValueList> valueLists) {
		if (nextColumn == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		if (!(nextColumn instanceof DataSetInputColumn)) {
			throw new IllegalStateException(
					"The started column must be an input column!");
		}
		((DataSetObservationColumn) nextColumn).getValueLists().addAll(
				valueLists);
	}

	/**
	 * Finish the column under construction and add it to the newly created
	 * DataSet.
	 */
	public void finishColumn() {
		if (nextColumn == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		addColumn(nextColumn);
		nextColumn = null;
	}

}

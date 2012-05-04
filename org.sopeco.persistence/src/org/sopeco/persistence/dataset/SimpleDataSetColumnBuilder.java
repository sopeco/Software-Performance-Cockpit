package org.sopeco.persistence.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.sopeco.persistence.entities.definition.ParameterDefinition;


/**
 * Builder for DataSets from the column perspective.
 * 
 * @author Jens Happe
 * 
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class SimpleDataSetColumnBuilder {

	/**
	 * Columns of the new DataSet.
	 */
	private Map<ParameterDefinition, SimpleDataSetColumn> columnMap = new HashMap<ParameterDefinition, SimpleDataSetColumn>();

	/**
	 * Indexes of the columns represented by the according parameter
	 */
	private Map<Integer, ParameterDefinition> parameterIndexes = new HashMap<Integer, ParameterDefinition>();

	/**
	 * Column under construction using methods startColumn, addValue,
	 * finishColumn.
	 */
	private SimpleDataSetColumn nextColumn;

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
	public void addColumn(SimpleDataSetColumn column) {
		if (columnMap.isEmpty()) {
			size = column.getValueList().size();
		} else if (column.size() != size) {
			throw new IllegalArgumentException("Column length does not match ("
					+ column.getParameter() + ").");
		}
		if (columnMap.containsKey(column.getParameter())) {
			throw new IllegalArgumentException("Column already exists ("
					+ column.getParameter() + ").");
		}
		columnMap.put(column.getParameter(), column);
		parameterIndexes.put(parameterIndexes.size(), column.getParameter());
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
	public void addColumns(List<SimpleDataSetColumn> columnList) {
		for (SimpleDataSetColumn column : columnList) {
			addColumn(column);
		}
	}

	/**
	 * @return All columns held by the dataset.
	 */
	private Collection<SimpleDataSetColumn> getColumns() {
		List<SimpleDataSetColumn> result = new ArrayList<SimpleDataSetColumn>();
		for (int i = 0; i < columnMap.size(); i++) {
			result.add(columnMap.get(parameterIndexes.get(i)));
		}
		return result;
	}

	/**
	 * Generates a new DataSet based on the data provided to the builder.
	 * 
	 * @return New DataSet.
	 */
	public SimpleDataSet createDataSet() {
		return new SimpleDataSet(new ArrayList<SimpleDataSetColumn>(
				getColumns()), size, UUID.randomUUID().toString());
	}

	/**
	 * Generates a new DataSet based on the data provided to the builder.
	 * 
	 * @param id
	 *            Unique id of the DataSet.
	 * 
	 * @return New DataSet.
	 */
	public SimpleDataSet createDataSet(String id) {
		return new SimpleDataSet(new ArrayList<SimpleDataSetColumn>(
				getColumns()), size, id);
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
	public void startColumn(ParameterDefinition parameter) {
		nextColumn = new SimpleDataSetColumn(parameter, new ArrayList());

	}

	/**
	 * Adds a new value to the column under construction (created by
	 * startColumn).
	 * 
	 * @param value
	 *            Value to be added.
	 */
	public void addValue(Object value) {
		if (nextColumn == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		nextColumn.getValueList().add(value);
	}

	/**
	 * Adds a set of new values to the column under construction (created by
	 * startColumn).
	 * 
	 * @param valueList
	 *            Values to be added.
	 */
	public void addValueList(List valueList) {
		if (nextColumn == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		nextColumn.getValueList().addAll(valueList);
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

package org.sopeco.persistence.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sopeco.configuration.parameter.ParameterRole;
import org.sopeco.configuration.parameter.ParameterUsage;
import org.sopeco.configuration.persistence.impl.IDataSetImpl;
import org.sopeco.persistence.advanced.DataSetAggregated;
import org.sopeco.persistence.advanced.DataSetAppender;
import org.sopeco.persistence.advanced.DataSetRowBuilder;
import org.sopeco.persistence.dataset.util.SimpleDataSetColumnBuilder;
import org.sopeco.persistence.dataset.util.SimpleDataSetRowBuilder;

/**
 * A SimpleDataSet is a column-based storage for data. It contains the values
 * (configured or observed) for each parameter. A SimpleDataSet supports a row
 * and column view on the data. A SimpleDataSet is read-only and can be
 * constructed only by a DataSetColumnBuilder or DataSetRowBuilder.
 * 
 * @author Jens Happe
 * 
 */
@SuppressWarnings("unchecked")
public class SimpleDataSet extends IDataSetImpl implements
		Iterable<SimpleDataSetRow>, Serializable {

	/**
	 * Data in form of columns associated to a parameter.
	 */
	private Map<ParameterUsage, SimpleDataSetColumn> columnMap;

	/**
	 * Indexes of the columns represented by the according parameter
	 */
	private Map<Integer, ParameterUsage> parameterIndexes;

	/**
	 * Size (number of rows) in the SimpleDataSet.
	 */
	private int size;

	/**
	 * Unique identifier of the SimpleDataSet
	 */
	private String id;

	/**
	 * Constructor. Only to be used by the builders.
	 * 
	 * @param columnList
	 */
	protected SimpleDataSet(List<SimpleDataSetColumn> columnList, int size,
			String id) {
		super();
		this.id = id;
		this.columnMap = new HashMap<ParameterUsage, SimpleDataSetColumn>();
		this.parameterIndexes = new HashMap<Integer, ParameterUsage>();
		this.size = size;
		int index = 0;
		for (SimpleDataSetColumn column : columnList) {
			columnMap.put(column.getParameter(), column);
			parameterIndexes.put(index, column.getParameter());
			index++;
		}
	}

	/**
	 * @param parameter
	 *            Parameter whose column / data is of interest.
	 * @return The column containing the data for the given parameter.
	 */
	public SimpleDataSetColumn getColumn(ParameterUsage parameter) {
		if (columnMap.containsKey(parameter)) {
			return columnMap.get(parameter);
		}
		throw new IllegalArgumentException("Unknown Parameter: " + parameter);
	}

	/**
	 * @return All columns held by the dataset.
	 */
	public Collection<SimpleDataSetColumn> getColumns() {
		List<SimpleDataSetColumn> result = new ArrayList<SimpleDataSetColumn>();
		for (int i = 0; i < columnMap.size(); i++) {
			result.add(columnMap.get(parameterIndexes.get(i)));
		}
		return result;
	}

	/**
	 * Get a particular value in the SimpleDataSet defined by the parameter and
	 * row.
	 * 
	 * @param parameter
	 *            parameter of interest.
	 * @param row
	 *            row of interest.
	 * @return The ParameterValue at (parameter, row).
	 */
	public ParameterValue get(ParameterUsage parameter, int row) {
		return getColumn(parameter).getParameterValue(row);
	}

	/**
	 * @param num
	 *            Number of the row of interest.
	 * @return The row at position num.
	 */
	public SimpleDataSetRow getRow(int num) {
		if (!(num < size())) {
			throw new IndexOutOfBoundsException();
		}

		List<ParameterValue> valueList = new ArrayList<ParameterValue>();
		for (SimpleDataSetColumn column : getColumns()) {
			valueList.add(column.getParameterValue(num));
		}

		return new SimpleDataSetRow(valueList);
	}

	/**
	 * @return The number of rows of the SimpleDataSet.
	 */
	public int size() {
		return size;
	}

	/**
	 * @param parameter
	 *            Parameter of interest.
	 * @return True, if the SimpleDataSet contains a column for that Parameter,
	 *         false otherwise.
	 */
	public boolean contains(ParameterUsage parameter) {
		return columnMap.containsKey(parameter);
	}

	/**
	 * @return The unique ID of the SimpleDataSet.
	 */
	public String getID() {
		return id;
	}

	/**
	 * @return Parameters used in this dataset.
	 */
	public Collection<ParameterUsage> getParameters() {
		return new ArrayList<ParameterUsage>(columnMap.keySet());
	}

	/**
	 * Returns a row iterator of the SimpleDataSet.
	 */
	@Override
	public Iterator<SimpleDataSetRow> iterator() {
		return new Iterator<SimpleDataSetRow>() {

			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < size();
			}

			@Override
			public SimpleDataSetRow next() {
				return getRow(i++);
			}

			@Override
			public void remove() {
				throw new IllegalStateException(
						"SimpleDataSet cannot be edited.");
			}
		};
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof SimpleDataSet) {
			SimpleDataSet other = (SimpleDataSet) object;
			if ((this.columnMap.size() == other.columnMap.size())
					&& this.size() == other.size()) {
				for (SimpleDataSetColumn column : this.getColumns()) {
					if (other.contains(column.getParameter())) {
						SimpleDataSetColumn otherColumn = other
								.getColumn(column.getParameter());
						if (!column.equals(otherColumn)) {
							return false;
						}

					} else {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	public List<SimpleDataSetRow> getRowList() {
		List<SimpleDataSetRow> resultList = new ArrayList<SimpleDataSetRow>();
		for (SimpleDataSetRow row : this) {
			resultList.add(row);
		}
		return resultList;
	}

	SimpleDataSet getSubSet(ParameterUsage xParameter, ParameterUsage yParameter) {
		SimpleDataSetColumnBuilder builder = new SimpleDataSetColumnBuilder();
		builder.addColumn(getColumn(xParameter));
		builder.addColumn(getColumn(yParameter));
		return builder.createDataSet();
	}

	public SimpleDataSet getSubSet(Collection<ParameterUsage> parameterList) {
		SimpleDataSetColumnBuilder builder = new SimpleDataSetColumnBuilder();
		for (ParameterUsage p : parameterList) {
			builder.addColumn(getColumn(p));
		}
		return builder.createDataSet();
	}

	public List<ParameterUsage> getVariedParameters() {
		List<ParameterUsage> parameterList = new ArrayList<ParameterUsage>();
		for (SimpleDataSetColumn<?> column : getColumns()) {
			if (column.isVaried()) {
				parameterList.add(column.getParameter());
			}
		}
		return parameterList;
	}

	public SimpleDataSet getVariedSubSet() {
		return getSubSet(getVariedParameters());
	}

	public SimpleDataSet select(Map<ParameterUsage, Object> selectionMap) {
		SimpleDataSetRowBuilder builder = new SimpleDataSetRowBuilder();

		for (SimpleDataSetRow row : this) {
			boolean equals = true;
			for (Entry<ParameterUsage, Object> entry : selectionMap.entrySet()) {
				ParameterValue<?> value = row.getParameterValue(entry.getKey());
				if (!entry.getValue().equals(value.getValue())) {
					equals = false;
					break;
				}
			}
			if (equals) {
				builder.appendRow(row);
			}
		}

		return builder.createDataSet();
	}

	// public boolean equalsStructure(SimpleDataSet other) {
	// if (this.getColumns().size() != other.getColumns().size()) {
	// return false;
	// }
	// for (SimpleDataSetColumn<?> c : other.getColumns()) {
	// if (!containsParameter(c.getParameter().getID())) {
	// return false;
	// }
	// }
	// return true;
	// }

	private boolean containsParameter(String parameterId) {
		for (SimpleDataSetColumn<?> c : this.getColumns()) {
			if (c.getParameter().getID().equals(parameterId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		if (size <= 0) {
			return "EMPTY_DATASET" + super.toString();
		}
		boolean firstIteration = true;
		String result = "";
		String space = "\t";
		String newLine = "\n";
		for (SimpleDataSetRow row : this.getRowList()) {
			if (firstIteration) {
				for (ParameterValue<?> val : row.getRowValues()) {
					result += val.getParameter().getName() + space;
				}
				result += newLine;
				firstIteration = false;
			}

			for (ParameterValue<?> val : row.getRowValues()) {
				result += val.getValue() + space;
			}
			result += newLine;

		}
		return result;
	}

	protected void addColumn(SimpleDataSetColumn<?> col) {
		columnMap.put(col.getParameter(), col);
		parameterIndexes.put(parameterIndexes.size(), col.getParameter());
	}

	protected void setSize(int size) {
		this.size = size;
	}
	
	public DataSetAggregated convertToAggregatedDataSet(){
		// TODO: implement right
		DataSetAppender appender = new DataSetAppender();
		for(SimpleDataSetRow row : getRowList()){
			DataSetRowBuilder builder = new DataSetRowBuilder();
			builder.startRow();
			for(ParameterValue pv : row.getRowValues()){
				if(pv.getParameter().getRole().equals(ParameterRole.INPUT)){
					builder.addInputParameterValue(pv.getParameter(), pv.getValue());
				}else if(pv.getParameter().getRole().equals(ParameterRole.OBSERVATION)){
					builder.addObservationParameterValue(pv.getParameter(), pv.getValue());
				}
				else if(pv.getParameter().getRole().equals(ParameterRole.OBSERVABLE_TIME_SERIES)){
					builder.addTimeSeriesValue(pv.getParameter(), pv.getValue(), 0.0);
				}
			}
			builder.finishRow();
			appender.append(builder.createDataSet());
		}
		return appender.createDataSet();
	}
}
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.util.Tools;
import org.sopeco.util.Tools.SupportedTypes;

/**
 * A SimpleDataSet is a column-based storage for data. It contains the values
 * (configured or observed) for each parameter. A SimpleDataSet supports a row
 * and column view on the data. A SimpleDataSet is read-only and can be
 * constructed only by a DataSetColumnBuilder or DataSetRowBuilder.
 * 
 * @author Jens Happe
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class SimpleDataSet implements Iterable<SimpleDataSetRow>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Data in form of columns associated to a parameter.
	 */
	private Map<ParameterDefinition, SimpleDataSetColumn> columnMap;

	/**
	 * Indexes of the columns represented by the according parameter
	 */
	private Map<Integer, ParameterDefinition> parameterIndexes;

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
	protected SimpleDataSet(List<SimpleDataSetColumn> columnList, int numOfRows, String datasetId) {
		super();
		this.id = datasetId;
		this.columnMap = new HashMap<ParameterDefinition, SimpleDataSetColumn>();
		this.parameterIndexes = new HashMap<Integer, ParameterDefinition>();
		this.size = numOfRows;
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
	public SimpleDataSetColumn getColumn(ParameterDefinition parameter) {
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
	public ParameterValue get(ParameterDefinition parameter, int row) {
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
	public boolean contains(ParameterDefinition parameter) {
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
	public Collection<ParameterDefinition> getParameters() {
		return new ArrayList<ParameterDefinition>(columnMap.keySet());
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
				throw new IllegalStateException("SimpleDataSet cannot be edited.");
			}
		};
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof SimpleDataSet) {
			SimpleDataSet other = (SimpleDataSet) object;
			if ((this.columnMap.size() == other.columnMap.size()) && this.size() == other.size()) {
				for (SimpleDataSetColumn column : this.getColumns()) {
					if (other.contains(column.getParameter())) {
						SimpleDataSetColumn otherColumn = other.getColumn(column.getParameter());
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columnMap == null) ? 0 : columnMap.hashCode());
		result = prime * result + size;
		return result;
	}

	public List<SimpleDataSetRow> getRowList() {
		List<SimpleDataSetRow> resultList = new ArrayList<SimpleDataSetRow>();
		for (SimpleDataSetRow row : this) {
			resultList.add(row);
		}
		return resultList;
	}

	SimpleDataSet getSubSet(ParameterDefinition xParameter, ParameterDefinition yParameter) {
		SimpleDataSetColumnBuilder builder = new SimpleDataSetColumnBuilder();
		builder.addColumn(getColumn(xParameter));
		builder.addColumn(getColumn(yParameter));
		return builder.createDataSet();
	}

	public SimpleDataSet getSubSet(Collection<ParameterDefinition> parameterList) {
		SimpleDataSetColumnBuilder builder = new SimpleDataSetColumnBuilder();
		for (ParameterDefinition p : parameterList) {
			builder.addColumn(getColumn(p));
		}
		return builder.createDataSet();
	}

	public List<ParameterDefinition> getVariedParameters() {
		List<ParameterDefinition> parameterList = new ArrayList<ParameterDefinition>();
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

	public SimpleDataSet select(Map<ParameterDefinition, Object> selectionMap) {
		SimpleDataSetRowBuilder builder = new SimpleDataSetRowBuilder();

		for (SimpleDataSetRow row : this) {
			boolean equals = true;
			for (Entry<ParameterDefinition, Object> entry : selectionMap.entrySet()) {
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

	// private boolean containsParameter(String parameterId) {
	// for (SimpleDataSetColumn<?> c : this.getColumns()) {
	// if (c.getParameter().getFullName().equals(parameterId)) {
	// return true;
	// }
	// }
	// return false;
	// }

	@Override
	public String toString() {
		List<String> lines = convertToCSV("\t");

		StringBuffer output = new StringBuffer();
		for (String line: lines) {
			output.append(line + Tools.getEOL());
		}
		
		return output.toString();
	}

	/**
	 * Creates a CSV style representation of the content of this dataset with the tab character as the separator.
	 * 
	 * @return a list of String values, each representing one line. The first line is the header.
	 * @deprecated use {@link #convertToCSV(boolean)} instead.
	 */
	@Deprecated
	public List<String> convertToCSV() {
		return convertToCSV("\t", false);
	}
	
	/**
	 * Creates a CSV style representation of the content of this dataset.
	 * 
	 * @param separator the separator to use between values
	 * @return a list of String values, each representing one line. The first line is the header.
	 * 
	 * @deprecated use {@link #convertToCSV(String, boolean)} instead.
	 */
	@Deprecated
	public List<String> convertToCSV(String separator) {
		return convertToCSV(separator, false);
	}

	/**
	 * Creates a CSV style representation of the content of this dataset with the tab character as the separator.
	 * If <code>quotations</code> is set, String values are put in quote.
	 *
	 * @param quotations if set, String values are put in quote
	 * @return a list of String values, each representing one line. The first line is the header.
	 * @see #convertToCSV(String)
	 */
	public List<String> convertToCSV(boolean quotations) {
		return convertToCSV("\t", quotations);
	}
	
	/**
	 * Creates a CSV style representation of the content of this dataset.
	 * If <code>quotations</code> is set, String values are put in quote.
	 *
	 * @param separator the separator to use between values
	 * @param quotations if set, String values are put in quote
	 * @return a list of String values, each representing one line. The first line is the header.
	 */
	public List<String> convertToCSV(String separator, boolean quotations) {
		List<String> result = new ArrayList<String>();

		if (size <= 0) {
			result.add("EMPTY_DATASET" + super.toString());
		} else {
			boolean firstIteration = true;
			for (SimpleDataSetRow row : this.getRowList()) {
				String line = "";
				if (firstIteration) {
					for (ParameterValue<?> val : row.getRowValues()) {
						line += val.getParameter().getName() + separator;
					}
					result.add(line);
					firstIteration = false;
					line = "";
				}

				for (ParameterValue<?> val : row.getRowValues()) {
					String cellValue = val.getValueAsString();
					if (quotations) {
						final String type = val.getParameter().getType();
						if (type != null && type.equalsIgnoreCase(String.class.getSimpleName())) {
							cellValue = "\"" + cellValue + "\"";
						}
					}
					line += cellValue + separator;
				}
				
				result.add(line);
			}
		}

		return result;
	}

	protected void addColumn(SimpleDataSetColumn<?> col) {
		columnMap.put(col.getParameter(), col);
		parameterIndexes.put(parameterIndexes.size(), col.getParameter());
	}

	protected void setSize(int numOfRows) {
		this.size = numOfRows;
	}

	public DataSetAggregated convertToAggregatedDataSet() {
		// TODO: implement right
		DataSetAppender appender = new DataSetAppender();
		for (SimpleDataSetRow row : getRowList()) {
			DataSetRowBuilder builder = new DataSetRowBuilder();
			builder.startRow();
			for (ParameterValue pv : row.getRowValues()) {
				if (pv.getParameter().getRole().equals(ParameterRole.INPUT)) {
					builder.addInputParameterValue(pv.getParameter(), pv.getValue());
				} else if (pv.getParameter().getRole().equals(ParameterRole.OBSERVATION)) {
					builder.addObservationParameterValue(pv.getParameter(), pv.getValue());
				}
			}
			builder.finishRow();
			appender.append(builder.createDataSet());
		}
		return appender.createDataSet();
	}
}
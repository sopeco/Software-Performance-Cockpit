package org.sopeco.persistence.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.persistence.util.ParameterCollection;

/**
 * A DataSet is a column-based storage for data. It contains the values
 * (configured or observed) for each parameter. A DataSet supports a row and
 * column view on the data. A DataSet is read-only and can be constructed only
 * by a DataSetColumnBuilder or DataSetRowBuilder.
 * 
 * @author Jens Happe, Dennis Westermann
 * 
 */
@SuppressWarnings({"rawtypes"})
@Entity
public class DataSetAggregated implements
		Iterable<DataSetRow>, Serializable {

 	private final static Logger logger = LoggerFactory.getLogger(DataSetAggregated.class);

	private static final long serialVersionUID = 1L;

	/**
	 * Data in form of columns associated to a input parameter.
	 */

	@Lob
	@Column(name = "inputColumns")
	private List<DataSetInputColumn> inputColumns;

	/**
	 * Data in form of columns associated to a observation parameter.
	 */
	@Lob
	@Column(name = "observationColumns")
	private List<DataSetObservationColumn> observationColumns;

	// /**
	// * Indexes of the columns represented by the according parameter
	// */
	// private Map<Integer, ParameterDefinition> parameterIndexes;

	/**
	 * Size (number of rows) in the DataSet.
	 */
	private int size;

	/**
	 * Unique identifier of the DataSet
	 */
	@Id
	private String id;

	public DataSetAggregated(){
		
	}
	

//	public void reconstructMaps() {
//		for (Entry<ParameterDefinition, DataSetInputColumn> entry : inputColumnMap
//				.entrySet()) {
//			entry.getValue().parameter = entry.getKey();
//		}
//		for (Entry<ParameterDefinition, DataSetObservationColumn> entry : observationColumnMap
//				.entrySet()) {
//			entry.getValue().parameter = entry.getKey();
//		}
//	}

	/**
	 * Constructor. Only to be used by the builders.
	 * 
	 * @param columnList
	 */
	protected DataSetAggregated(List<DataSetInputColumn> inputColumns,
			List<DataSetObservationColumn> observationColumns, int size,
			String id) {
		super();
		this.id = id;
		this.inputColumns = inputColumns;
		this.observationColumns = observationColumns;
		// this.parameterIndexes = new HashMap<Integer, ParameterDefinition>();
		this.size = size;
		// int index = 0;
//		for (DataSetInputColumn column : inputColumns) {
//			inputColumnMap.put(column.getParameter(), column);
//			// parameterIndexes.put(index, column.getParameter());
//			// index++;
//		}
//		for (DataSetObservationColumn column : observationColumns) {
//			observationColumnMap.put(column.getParameter(), column);
//			// parameterIndexes.put(index, column.getParameter());
//			// index++;
//		}
	}

	/**
	 * @param parameter
	 *            Parameter whose column / data is of interest.
	 * @return The column containing the data for the given parameter.
	 */
	public AbstractDataSetColumn getColumn(ParameterDefinition parameter) {
		
		for (AbstractDataSetColumn col : inputColumns){
			
			if(col.getParameter().equals(parameter)){
				return col;
			}
		}
		for (AbstractDataSetColumn col : observationColumns){
			if(col.getParameter().equals(parameter)){
				return col;
			}
		}
//		if (inputColumnMap.containsKey(parameter)) {
//			return inputColumnMap.get(parameter);
//		}
//		if (observationColumnMap.containsKey(parameter)) {
//			return observationColumnMap.get(parameter);
//		}
		throw new IllegalArgumentException("Unknown Parameter: " + parameter);
	}

	/**
	 * @return All columns held by the dataset.
	 */
	public Collection<AbstractDataSetColumn> getColumns() {
		List<AbstractDataSetColumn> result = new ArrayList<AbstractDataSetColumn>();
		result.addAll(getInputColumns());
		result.addAll(getObservationColumns());
		return result;
	}

	/**
	 * @return All columns held by the dataset.
	 */
	public Collection<DataSetInputColumn> getInputColumns() {
		
		return inputColumns;
//		Collection<DataSetInputColumn> col = inputColumnMap.values();
//		List<DataSetInputColumn> list = new ArrayList<DataSetInputColumn>(col);
//		return list;
	}

	/**
	 * @return All columns held by the dataset.
	 */
	public Collection<DataSetObservationColumn> getObservationColumns() {
//		return new ArrayList<DataSetObservationColumn>(
//				observationColumnMap.values());
		return observationColumns;
	}

	/**
	 * @return All columns held by the dataset.
	 */
	public DataSetInputColumn getInputColumn(ParameterDefinition parameter) {
		return (DataSetInputColumn) getColumn(parameter);
	}

	/**
	 * @return All columns held by the dataset.
	 */
	public DataSetObservationColumn getObservationColumn(ParameterDefinition parameter) {
//		return observationColumnMap.get(parameter);
		return (DataSetObservationColumn) getColumn(parameter);
	}

	/**
	 * Get a particular value in the DataSet defined by the parameter and row.
	 * 
	 * @param parameter
	 *            parameter of interest.
	 * @param row
	 *            row of interest.
	 * @return The ParameterValue at (parameter, row).
	 */
	public ParameterValue getInputValue(ParameterDefinition parameter, int row) {
		if (!parameter.getRole().equals(ParameterRole.INPUT)
				|| !(getColumn(parameter) instanceof DataSetInputColumn)) {
			throw new IllegalArgumentException(
					"Parameter must be an input parameter!");
		}
		return ((DataSetInputColumn) getColumn(parameter))
				.getParameterValue(row);
	}

	/**
	 * Get a particular value in the DataSet defined by the parameter and row.
	 * 
	 * @param parameter
	 *            parameter of interest.
	 * @param row
	 *            row of interest.
	 * @return The ParameterValue at (parameter, row).
	 */
	public ParameterValueList getObservationValues(ParameterDefinition parameter,
			int row) {
		if ((!parameter.getRole().equals(ParameterRole.OBSERVATION))
				|| !(getColumn(parameter) instanceof DataSetObservationColumn)) {
			throw new IllegalArgumentException(
					"Parameter must be an observation parameter!");
		}
		return ((DataSetObservationColumn) getColumn(parameter))
				.getParameterValues(row);
	}

	/**
	 * @param num
	 *            Number of the row of interest.
	 * @return The row at position num.
	 */
	public DataSetRow getRow(int num) {
		if (!(num < size())) {
			throw new IndexOutOfBoundsException();
		}

		List<ParameterValue<?>> inputValueList = new ArrayList<ParameterValue<?>>();
		List<ParameterValueList<?>> observationValueList = new ArrayList<ParameterValueList<?>>();
		for (AbstractDataSetColumn column : getColumns()) {
			if (column instanceof DataSetInputColumn) {

				inputValueList.add(((DataSetInputColumn) column)
						.getParameterValue(num));
			} else if (column instanceof DataSetObservationColumn) {
				observationValueList.add(((DataSetObservationColumn) column)
						.getParameterValues(num));
			}

		}

		return new DataSetRow(inputValueList, observationValueList);
	}

	/**
	 * @return The number of rows of the DataSet.
	 */
	public int size() {
		return size;
	}

	/**
	 * @param parameter
	 *            Parameter of interest.
	 * @return True, if the DataSet contains a column for that Parameter, false
	 *         otherwise.
	 */
	public boolean contains(ParameterDefinition parameter) {
//		return inputColumnMap.containsKey(parameter)
//				|| observationColumnMap.containsKey(parameter);
		for (AbstractDataSetColumn col : getColumns()){
			if(parameter.equals(parameter)){
				return true;
			}
		}
		return false;
	}

	/**
	 * @return The unique ID of the DataSet.
	 */
	public String getID() {
		return id;
	}

	/**
	 * @return Parameters used in this dataset.
	 */
	public Collection<ParameterDefinition> getParameterDefinitions() {
		ArrayList<ParameterDefinition> result = new ArrayList<ParameterDefinition>();
		for(DataSetInputColumn dic : inputColumns){
			result.add(dic.getParameter());
		}
		
		for(DataSetObservationColumn doc: observationColumns){
			result.add(doc.getParameter());
		}
		
		return result;
	}

	/**
	 * Returns a row iterator of the DataSet.
	 */
	@Override
	public Iterator<DataSetRow> iterator() {
		return new Iterator<DataSetRow>() {

			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < size();
			}

			@Override
			public DataSetRow next() {
				return getRow(i++);
			}

			@Override
			public void remove() {
				throw new IllegalStateException("DataSet cannot be edited.");
			}
		};
	}

	@Override
	// TODO: observation parameters are not compared
	public boolean equals(Object object) {
		if (object instanceof DataSetAggregated) {
			DataSetAggregated other = (DataSetAggregated) object;
			if ((this.inputColumns.size() == other.inputColumns.size())
					&& this.size() == other.size()
					&& (this.observationColumns.size() == other.observationColumns
							.size())) {
				for (AbstractDataSetColumn column : this.getColumns()) {
					if (other.contains(column.getParameter())) {
						AbstractDataSetColumn otherColumn = other
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

	public List<DataSetRow> getRowList() {
		List<DataSetRow> resultList = new ArrayList<DataSetRow>();
		for (DataSetRow row : this) {
			resultList.add(row);
		}
		return resultList;
	}

	DataSetAggregated getSubSet(ParameterDefinition xParameter,
			ParameterDefinition yParameter) {
		DataSetColumnBuilder builder = new DataSetColumnBuilder();
		builder.addColumn(getColumn(xParameter));
		builder.addColumn(getColumn(yParameter));
		return builder.createDataSet();
	}

	public DataSetAggregated getSubSet(Collection<ParameterDefinition> parameterList) {
		DataSetColumnBuilder builder = new DataSetColumnBuilder();
		for (ParameterDefinition p : parameterList) {
			builder.addColumn(getColumn(p));
		}
		return builder.createDataSet();
	}

	public List<ParameterDefinition> getVariedInputParameters() {
		List<ParameterDefinition> parameterList = new ArrayList<ParameterDefinition>();
		for (DataSetInputColumn<?> column : getInputColumns()) {
			if (column.isVaried()) {
				parameterList.add(column.getParameter());
			}
		}
		return parameterList;
	}

	public DataSetAggregated getVariedSubSet() {
		return getSubSet(getVariedInputParameters());
	}

	public DataSetAggregated select(Map<ParameterDefinition, Object> selectionMap) {
		DataSetRowBuilder builder = new DataSetRowBuilder();

		for (DataSetRow row : this) {
			boolean equals = true;
			for (Entry<ParameterDefinition, Object> entry : selectionMap.entrySet()) {
				if (!entry.getKey().getRole().equals(ParameterRole.INPUT)) {
					throw new IllegalArgumentException(
							"Parameter must be an input parameter!");
				}
				ParameterValue<?> value = row.getInputParameterValue(entry
						.getKey());
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

	// public boolean equalsStructure(AggregatedDataSet other) {
	// if (this.getColumns().size() != other.getColumns().size()) {
	// return false;
	// }
	// for (DataSetInputColumn<?> c : other.getColumns()) {
	// if (!containsParameter(c.getParameter().getId())) {
	// return false;
	// }
	// }
	// return true;
	// }

//	private boolean containsParameter(String parameterId) {
//		for (AbstractDataSetColumn<?> c : this.getColumns()) {
//			if (c.getParameter().getFullName().equals(parameterId)) {
//				return true;
//			}
//		}
//
//		return false;
//	}

	@Override
	public String toString() {
		return "";
//		if (size <= 0) {
//			return "EMPTY_DATASET" + super.toString();
//		}
//		boolean firstIteration = true;
//		String result = "";
//		String space = "\t";
//		String newLine = "\n";
//		for (DataSetRow row : this.getRowList()) {
//			if (firstIteration) {
//				for (ParameterValue<?> val : row.getInputRowValues()) {
//					result += val.getParameter().getName() + space;
//				}
//				for (ParameterValueList<?> pvl : row.getObservableRowValues()) {
//					result += pvl.getParameter().getName() + space;
//				}
//				result += newLine;
//				firstIteration = false;
//			}
//
//			for (ParameterValue<?> val : row.getInputRowValues()) {
//				result += val.getValue() + space;
//			}
//
//			for (ParameterValueList<?> pvl : row.getObservableRowValues()) {
//				String s = "{";
//				boolean first = true;
//				
//				for (int i = 0; i < pvl.getSize(); i++){
//					Object value = pvl.getValues().get(i);
//					String valueString = "";
//					if(pvl instanceof TimeSeries){
//						Double timestamp = (Double)((TimeSeries)pvl).getTimeStamps().get(i);
//						valueString = "("+timestamp+" | "+value.toString()+")";
//					}else{
//						valueString = value.toString();
//					}
//					if (first) {
//						s += valueString;
//						first = false;
//					} else {
//						s += ";" + valueString;
//					}
//				}
//
//				s += "}";
//				result += s + space;
//			}
//			result += newLine;
//
//		}
//		return result;
	}

	protected void addColumn(AbstractDataSetColumn<?> col) {
		if (inputColumns.isEmpty() && observationColumns.isEmpty()) {
			setSize(col.size());
		}
		if (col instanceof DataSetInputColumn) {
			inputColumns.add((DataSetInputColumn) col);
		} else if (col instanceof DataSetObservationColumn) {
			observationColumns.add((DataSetObservationColumn) col);
		}

		// parameterIndexes.put(parameterIndexes.size(), col.getParameter());
	}

	protected void setSize(int size) {
		this.size = size;
	}

	public SimpleDataSet convertToSimpleDataSet() {
		SimpleDataSetRowBuilder builder = new SimpleDataSetRowBuilder();
		for (DataSetRow row : getRowList()) {
			if (row.getObservableRowValues().isEmpty()) {
				builder.startRow();
				for (ParameterValue pv : row.getInputRowValues()) {
					builder.addParameterValue(pv.getParameter(), pv.getValue());
				}
				builder.finishRow();
			} else {
				final int size = sameSizeOfAllVPLs(row.getObservableRowValues()); 
				if (size == -1) {
 					logger.error("Observed value lists are not of the same size.");
					throw new IllegalStateException(
							"Cannot convert  DataSetAggregated to SimpleDataSet"
									+ " as observationColumns do not have the same size! ");
				}

				RollOutRows(builder, row, size);
			}
		}
		return builder.createDataSet();
	}

	/**
	 * Rolls out the row into many rows by expanding the cells with many values.
	 * 
	 * @param builder
	 * @param row
	 * @param pvlSize
	 */
	private void RollOutRows(SimpleDataSetRowBuilder builder, DataSetRow row,
			int pvlSize) {
		for (int i = 0; i < pvlSize; i++) {
			builder.startRow();
			for (ParameterValue pv : row.getInputRowValues()) {
				builder.addParameterValue(pv.getParameter(), pv.getValue());
			}
			for (ParameterValueList pvl : row.getObservableRowValues()) {
				if (pvl.getSize() == 1)
					builder.addParameterValue(pvl.getParameter(), pvl.getValues().get(0));
				else
					builder.addParameterValue(pvl.getParameter(), pvl.getValues().get(i));
			}
			builder.finishRow();
		}
	}

	/**
	 * Returns the size of observation values if they are consistent (i.e., they are either 1 or the same size).
	 * Otherwise, returns -1.
	 * 
	 * @param collection
	 * @return -1, if data sizes are inconsistent, otherwise the size of observation parameter value lists.
	 */
	private int sameSizeOfAllVPLs(List<ParameterValueList<?>> collection) {
		if (collection.size() == 1) {
			return collection.get(0).getValues().size();
		}
		
		// get Max value size
		int maxSize = -1;
		for (ParameterValueList pvl: collection) {
			if (pvl.getValues().size() > maxSize)
				maxSize = pvl.getValues().size();
		}
		
		// check if all sizes larger than 1 are equal
		for (ParameterValueList pvl: collection) {
			final int size = pvl.getValues().size();
			if (size > 1 && size != maxSize)
				return -1;
		}
		
		return maxSize;
	}
	
	public boolean containsRowWithInputValues(ParameterCollection<ParameterValue<?>> inputParameterValues){
	 for (DataSetRow dataSetRow : this) {
		ArrayList<ParameterValue<?>> paramValueList = new ArrayList<ParameterValue<?>>();
		paramValueList.addAll(inputParameterValues);
		if(dataSetRow.equalIndependentParameterValues((new DataSetRow(paramValueList, Collections.EMPTY_LIST)))){
			return true;
		}
	 }
	 
	 return false;
	}

	public Collection<ParameterValueList<?>> getObservationParameterValues(ParameterCollection<ParameterValue<?>> inputParameterValues) throws DataNotFoundException{
		 for (DataSetRow dataSetRow : this) {
			ArrayList<ParameterValue<?>> paramValueList = new ArrayList<ParameterValue<?>>();
			paramValueList.addAll(inputParameterValues);
			if (dataSetRow.equalIndependentParameterValues((new DataSetRow(paramValueList, Collections.EMPTY_LIST)))){
				return dataSetRow.getObservableRowValues();
			}
				
		 }
		 
		 throw new DataNotFoundException("DataSet does not contain a row with the given input parameter values.");
	}

}
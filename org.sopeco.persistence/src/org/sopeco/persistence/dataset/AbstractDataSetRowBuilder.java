package org.sopeco.persistence.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Abstract class for row based DataSet builder. Current implementations are
 * DataSetRowBuilder and DataSetAppender.
 * 
 * @author Jens Happe
 * 
 */
@SuppressWarnings({ "rawtypes" })
public abstract class AbstractDataSetRowBuilder {

	/**
	 * Columns of the new DataSet.
	 */
	private Map<ParameterDefinition, DataSetInputColumn> inputColumnMap = new HashMap<ParameterDefinition, DataSetInputColumn>();
	
	/**
	 * Columns of the new DataSet.
	 */
	private Map<ParameterDefinition, DataSetObservationColumn> observationColumnMap = new HashMap<ParameterDefinition, DataSetObservationColumn>();

	/**
	 * Constructor.
	 */
	protected AbstractDataSetRowBuilder() {
		super();
	}

	/**
	 * Checks if the row contains exactly the parameters required by the
	 * DataSet.
	 * 
	 * @param inputValueList
	 *            input values of the row to be checked.
	 */
	protected void checkInputParameters(Collection<ParameterValue<?>> inputValueList) {
		boolean isValid = (inputValueList.size() == getInputColumnMap().size());

		for (ParameterValue value : inputValueList) {
			if (!getInputColumnMap().containsKey(value.getParameter())) {
				isValid = false;
				break;
			}
		}

		if (!isValid) {
			throw new IllegalArgumentException(
					"Parameters of inserted row do not match the parameters of the data set.");
		}
	}

	/**
	 * Checks if the row contains exactly the parameters required by the
	 * DataSet.
	 * 
	 * @param observationValueList
	 *            Row to be checked.
	 */
	protected void checkOutputParameters(Collection<ParameterValueList<?>> observationValueList) {
		boolean isValid = (getObservationColumnMap().size() == observationValueList.size());

		for (ParameterValueList pvl : observationValueList) {
			if (!getObservationColumnMap().containsKey(pvl.getParameter())) {
				isValid = false;
				break;
			}
		}

		if (!isValid) {
			throw new IllegalArgumentException(
					"Parameters of inserted row do not match the parameters of the data set.");
		}
	}

	/**
	 * Generates a new DataSet based on the data provided to the builder. Sets
	 * the Id of the dataset to the current nano timestamp.
	 * 
	 * @return New DataSet.
	 */
	public DataSetAggregated createDataSet() {
		return new DataSetAggregated(new ArrayList<DataSetInputColumn>(getInputColumnMap().values()),
				new ArrayList<DataSetObservationColumn>(getObservationColumnMap().values()), size(),
				Long.toString(System.nanoTime()));
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
		return new DataSetAggregated(new ArrayList<DataSetInputColumn>(getInputColumnMap().values()),
				new ArrayList<DataSetObservationColumn>(getObservationColumnMap().values()), size(), id);
	}

	/**
	 * @return Number of rows in the DataSet.
	 */
	protected int size() {
		for (DataSetInputColumn column : getInputColumnMap().values()) {
			if (column.getValueList() != null) {
				return column.getValueList().size();
			} else {
				return 0;
			}
		}
		for (DataSetObservationColumn column : getObservationColumnMap().values()) {
			if (column.getValueLists() != null) {
				return column.getValueLists().size();
			} else {
				return 0;
			}
		}
		return 0;
	}

	protected Map<ParameterDefinition, DataSetInputColumn> getInputColumnMap() {
		return inputColumnMap;
	}

	protected void setInputColumnMap(Map<ParameterDefinition, DataSetInputColumn> inputColumns) {
		this.inputColumnMap = inputColumns;
	}

	protected Map<ParameterDefinition, DataSetObservationColumn> getObservationColumnMap() {
		return observationColumnMap;
	}

	protected void setObservationColumnMap(Map<ParameterDefinition, DataSetObservationColumn> observationColumns) {
		this.observationColumnMap = observationColumns;
	}

}
package org.sopeco.persistence.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Abstract class for row based DataSet builder. Current implementations are
 * DataSetRowBuilder and DataSetAppender.
 * 
 * @author Jens Happe
 * 
 */
@SuppressWarnings({"rawtypes"})
public abstract class AbstractDataSetRowBuilder {

	/**
	 * Columns of the new DataSet.
	 */
	protected Map<ParameterDefinition, DataSetInputColumn> inputColumnMap = new HashMap<ParameterDefinition, DataSetInputColumn>();
	/**
	 * Columns of the new DataSet.
	 */
	protected Map<ParameterDefinition, DataSetObservationColumn> observationColumnMap = new HashMap<ParameterDefinition, DataSetObservationColumn>();

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
	 *           input values of the row to be checked.
	 */
	protected void checkInputParameters(Collection<ParameterValue> inputValueList) {
		boolean isValid = (inputValueList.size() == inputColumnMap.size());
		
		for (ParameterValue value : inputValueList) {
			if (!inputColumnMap.containsKey(value.getParameter())) {
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
	protected void checkOutputParameters(Collection<ParameterValueList> observationValueList) {
		boolean isValid = (observationColumnMap.size() == observationValueList.size());

		for (ParameterValueList pvl : observationValueList) {
			if (!observationColumnMap.containsKey(pvl.getParameter())) {
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
	 * Generates a new DataSet based on the data provided to the builder.
	 * 
	 * @return New DataSet.
	 */
	public DataSetAggregated createDataSet() {
		return new DataSetAggregated(new ArrayList<DataSetInputColumn>(
				inputColumnMap.values()),
				new ArrayList<DataSetObservationColumn>(observationColumnMap
						.values()), size(), UUID.randomUUID().toString());
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
				inputColumnMap.values()),
				new ArrayList<DataSetObservationColumn>(observationColumnMap
						.values()), size(), id);
	}

	/**
	 * @return Number of rows in the DataSet.
	 */
	protected int size() {
		for (DataSetInputColumn column : inputColumnMap.values()) {
			if (column.getValueList() != null) {
				return column.getValueList().size();
			} else {
				return 0;
			}
		}
		for (DataSetObservationColumn column : observationColumnMap.values()) {
			if (column.getValueLists() != null) {
				return column.getValueLists().size();
			} else {
				return 0;
			}
		}
		return 0;
	}

}
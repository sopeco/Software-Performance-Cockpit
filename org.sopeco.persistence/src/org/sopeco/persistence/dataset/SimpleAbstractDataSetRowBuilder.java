package org.sopeco.persistence.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Abstract class for row based SimpleDataSet builder. Current implementations
 * are DataSetRowBuilder and DataSetAppender.
 * 
 * @author Jens Happe
 * 
 */
@SuppressWarnings({"rawtypes"})
public abstract class SimpleAbstractDataSetRowBuilder {

	/**
	 * Data (in form of columns) of the SimpleDataSet under construction.
	 */
	protected Map<String, SimpleDataSetColumn> columnMap = new HashMap<String, SimpleDataSetColumn>();

	/**
	 * Constructor.
	 */
	protected SimpleAbstractDataSetRowBuilder() {
		super();
	}

	/**
	 * Checks if the row contains exactly the parameters required by the
	 * SimpleDataSet.
	 * 
	 * @param row
	 *            Row to be checked.
	 */
	protected void checkParameters(Collection<ParameterValue> row) {
		boolean isValid = row.size() == columnMap.size();

		for (ParameterValue value : row) {
			if (!columnMap.containsKey(value.getParameter().getFullName())) {
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
	 * Generates a new SimpleDataSet based on the data provided to the builder.
	 * 
	 * @return New SimpleDataSet.
	 */
	public SimpleDataSet createDataSet() {
		return new SimpleDataSet(new ArrayList<SimpleDataSetColumn>(
				columnMap.values()), size(), UUID.randomUUID().toString());
	}

	/**
	 * Generates a new SimpleDataSet based on the data provided to the builder.
	 * 
	 * @param id
	 *            Unique id of the SimpleDataSet.
	 * 
	 * @return New SimpleDataSet.
	 */
	public SimpleDataSet createDataSet(String id) {
		return new SimpleDataSet(new ArrayList<SimpleDataSetColumn>(
				columnMap.values()), size(), id);
	}

	/**
	 * @return Number of rows in the SimpleDataSet.
	 */
	protected int size() {
		for (SimpleDataSetColumn column : columnMap.values()) {
			if (column.getValueList() != null) {
				return column.getValueList().size();
			} else {
				return 0;
			}
		}
		return 0;
	}

}
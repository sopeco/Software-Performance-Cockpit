package org.sopeco.persistence.dataset.util;

import java.util.ArrayList;

import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetInputColumn;
import org.sopeco.persistence.dataset.DataSetObservationColumn;
import org.sopeco.persistence.dataset.DataSetRow;

/**
 * Builder to merge multiple DataSets into a single one.
 * 
 * @author Jens Happe
 * 
 */
public class DataSetAppender extends AbstractDataSetRowBuilder {

	public DataSetAppender() {
		super();
	}

	/**
	 * Append a dataset to the newly constructed one. The dataset must contain
	 * the same parameters as already added ones.
	 * 
	 * @param dataset
	 *            To be added.
	 */
	@SuppressWarnings("unchecked")
	public void append(DataSetAggregated dataset) {

		if (dataset.size() == 0) {
			return;
		}

		if (inputColumnMap.isEmpty()) {
			for (DataSetInputColumn column : dataset.getInputColumns()) {
				DataSetInputColumn col = new DataSetInputColumn(
						column.getParameter(), new ArrayList());
				inputColumnMap.put(column.getParameter(), col);
			}
		}

		if (observationColumnMap.isEmpty()) {
			for (DataSetObservationColumn column : dataset
					.getObservationColumns()) {
				DataSetObservationColumn col = new DataSetObservationColumn(
						column.getParameter(), new ArrayList());
				observationColumnMap.put(column.getParameter(), col);
			}
		}

		boolean first = true;
		for (int i = 0; i < dataset.size(); i++) {
			DataSetRow row = dataset.getRow(i);
			if (first) {
				checkInputParameters(row.getInputRowValues());
				checkOutputParameters(row.getObservableRowValues());
				first = false;
			}

			int index = getRowIndexForConfig(row);
			if (index < 0) {
				appendNewValues(dataset, i);
			} else {
				addValuesToExisting(dataset, i, index);
			}
		}

	}

	private void appendNewValues(DataSetAggregated dataset, int sourceIndex) {
		for (DataSetInputColumn appendColumn : dataset.getInputColumns()) {
			DataSetInputColumn column = inputColumnMap.get(appendColumn
					.getParameter());
			column.getValueList().add(
					appendColumn.getParameterValue(sourceIndex).getValue());
		}
		for (DataSetObservationColumn appendColumn : dataset
				.getObservationColumns()) {
			DataSetObservationColumn column = observationColumnMap
					.get(appendColumn.getParameter());
			column.getValueLists().add(
					appendColumn.getParameterValues(sourceIndex));
		}
	}

	private void addValuesToExisting(DataSetAggregated dataset,
			int sourceIndex, int targetIndex) {
		for (DataSetObservationColumn appendColumn : dataset
				.getObservationColumns()) {
			DataSetObservationColumn column = observationColumnMap
					.get(appendColumn.getParameter());
			column.getParameterValues(targetIndex).addValues(
					appendColumn.getParameterValues(sourceIndex).getValues());
		}
	}

	private int getRowIndexForConfig(DataSetRow row) {

		for (int i = 0; i < size(); i++) {
			boolean found = true;
			for (DataSetInputColumn col : inputColumnMap.values()) {
				if (!col.getParameterValue(i).getValue()
						.equals(row.getInputParameterValue(col.getParameter()))) {
					found = false;
					break;
				}
			}
			if (found) {
				return i;
			}
		}
		return -1;
	}

}

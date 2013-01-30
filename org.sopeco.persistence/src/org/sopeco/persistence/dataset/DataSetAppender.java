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

import java.util.ArrayList;

/**
 * Builder to merge multiple DataSets into a single one.
 * 
 * @author Jens Happe
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
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
	public void append(DataSetAggregated dataset) {

		if (dataset.size() == 0) {
			return;
		}

		if (getInputColumnMap().isEmpty()) {
			for (DataSetInputColumn column : dataset.getInputColumns()) {
				DataSetInputColumn col = new DataSetInputColumn(column.getParameter(), new ArrayList());
				getInputColumnMap().put(column.getParameter(), col);
			}
		}

		if (getObservationColumnMap().isEmpty()) {
			for (DataSetObservationColumn column : dataset.getObservationColumns()) {
				DataSetObservationColumn col = new DataSetObservationColumn(column.getParameter(), new ArrayList());
				getObservationColumnMap().put(column.getParameter(), col);
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
			DataSetInputColumn column = getInputColumnMap().get(appendColumn.getParameter());
			column.getValueList().add(appendColumn.getParameterValue(sourceIndex).getValue());
		}
		for (DataSetObservationColumn appendColumn : dataset.getObservationColumns()) {
			DataSetObservationColumn column = getObservationColumnMap().get(appendColumn.getParameter());
			column.getValueLists().add(appendColumn.getParameterValues(sourceIndex));
		}
	}

	private void addValuesToExisting(DataSetAggregated dataset, int sourceIndex, int targetIndex) {
		for (DataSetObservationColumn appendColumn : dataset.getObservationColumns()) {
			DataSetObservationColumn column = getObservationColumnMap().get(appendColumn.getParameter());
			column.getParameterValues(targetIndex).addValues(appendColumn.getParameterValues(sourceIndex).getValues());
		}
	}

	private int getRowIndexForConfig(DataSetRow row) {

		for (int i = 0; i < size(); i++) {
			boolean found = true;
			for (DataSetInputColumn col : getInputColumnMap().values()) {
				if (!col.getParameterValue(i).getValue().equals(row.getInputParameterValue(col.getParameter()))) {
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

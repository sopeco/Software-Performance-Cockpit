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
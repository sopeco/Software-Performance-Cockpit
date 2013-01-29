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
import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;


/**
 * Builder for input columns.
 * @author Alexander Wert
 *
 * @param <T> Type of the values contained in that column
 */
public class InputColumnBuilder<T> {

	private DataSetInputColumn<T> column;

	public InputColumnBuilder() {
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
	public void setParameter(ParameterDefinition parameter) {
		column = new DataSetInputColumn<T>(parameter, new ArrayList<T>());

	}

	/**
	 * Adds a new value to the column under construction (created by
	 * startColumn).
	 * 
	 * @param value
	 *            Value to be added.
	 */
	public void addValue(T value) {
		if (column == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		column.getValueList().add(value);
	}

	/**
	 * Adds a set of new values to the column under construction (created by
	 * startColumn).
	 * 
	 * @param valueList
	 *            Values to be added.
	 */
	public void addValueList(List<T> valueList) {
		if (column == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		column.getValueList().addAll(valueList);
	}

	/**
	 * Finish the column under construction and add it to the newly created
	 * DataSet.
	 */
	public DataSetInputColumn<T> createColumn() {
		if (column == null) {
			throw new IllegalStateException("The row must be started first.");
		}
		return column;
	}
}

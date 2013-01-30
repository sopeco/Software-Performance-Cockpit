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
import java.util.UUID;

/**
 * Abstract class for row based SimpleDataSet builder. Current implementations
 * are DataSetRowBuilder and DataSetAppender.
 * 
 * @author Jens Happe
 * 
 */
@SuppressWarnings({ "rawtypes" })
public abstract class SimpleAbstractDataSetRowBuilder {

	/**
	 * Data (in form of columns) of the SimpleDataSet under construction.
	 */
	private Map<String, SimpleDataSetColumn> columnMap = new HashMap<String, SimpleDataSetColumn>();

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
		boolean isValid = row.size() == getColumnMap().size();

		for (ParameterValue value : row) {
			if (!getColumnMap().containsKey(value.getParameter().getFullName())) {
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
		return new SimpleDataSet(new ArrayList<SimpleDataSetColumn>(getColumnMap().values()), size(), UUID.randomUUID()
				.toString());
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
		return new SimpleDataSet(new ArrayList<SimpleDataSetColumn>(getColumnMap().values()), size(), id);
	}

	/**
	 * @return Number of rows in the SimpleDataSet.
	 */
	public int size() {
		for (SimpleDataSetColumn column : getColumnMap().values()) {
			if (column.getValueList() != null) {
				return column.getValueList().size();
			} else {
				return 0;
			}
		}
		return 0;
	}

	protected Map<String, SimpleDataSetColumn> getColumnMap() {
		return columnMap;
	}

	protected void setColumnMap(Map<String, SimpleDataSetColumn> columns) {
		this.columnMap = columns;
	}

}
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
package org.sopeco.analysis.wrapper.common;

import org.sopeco.analysis.wrapper.AnalysisWrapper;
import org.sopeco.persistence.dataset.SimpleDataSet;
import org.sopeco.persistence.dataset.SimpleDataSetColumn;
import org.sopeco.persistence.entities.definition.ParameterDefinition;


/**
 * Representation of a DataSet in R. This class provides the functionality to
 * load and use a DataSet in R.
 * 
 * @author Jens Happe, Dennis Westermann
 * 
 */
public class RDataSet extends RIdentifiableObject {

	/**
	 * DataSet under consideration.
	 */
	private SimpleDataSet dataset;

	/**
	 * 
	 */
	private boolean dataLoaded;

	/**
	 * Create a new RDataSet.
	 * 
	 * @param dataset
	 *            DataSet that is to be passed to R.
	 */
	public RDataSet(SimpleDataSet dataset) {
		this.dataset = dataset;
		dataLoaded = false;
	}

	/**
	 * Transfers this DataSet to R and makes it available under a newly
	 * generated ID.
	 */
	public void loadDataSetInR(AnalysisWrapper analysisWrapper) {

		String parameterIdString = "";
		String parameterIdStringInQuotes = "";

		int i = 0;
		for (SimpleDataSetColumn<?> column : dataset.getColumns()) {
			String columnId = column.getParameter().getFullName("_");
			analysisWrapper.executeCommandString(columnId + " <- c(" + new CSVStringGenerator().generateValueString(column.getParameterValues()) + ")");
			if (i == 0) {
				parameterIdString = columnId;
				parameterIdStringInQuotes = "\"" + columnId + "\"";
			} else {
				parameterIdString += ", " + columnId;
				parameterIdStringInQuotes += ", \"" + columnId + "\"";
			}
			i++;
		}

		analysisWrapper.executeCommandString(getId() + " <- data.frame(" + parameterIdString + ");");
		analysisWrapper.executeCommandString("colnames(" + getId() + ") <- c(" + parameterIdStringInQuotes + ")");
		
		dataLoaded = true;

	}

	/**
	 * Jitters the values of the given parameter in the RDataSet. Example:
	 * vector [10, 10, 10] is transfered to [10.0001, 9.9998, 1.0003].
	 * 
	 * Note: Precondition is that the data set has already been loaded in R and
	 * that it contains the given parameter.
	 * 
	 * @param parameter for which the values should be jittered
	 */
	public void jitter(AnalysisWrapper analysisWrapper, ParameterDefinition parameter) {

		
		StringBuilder cmdBuilder = new StringBuilder();
		cmdBuilder.append(getId());
		cmdBuilder.append("$");
		cmdBuilder.append(parameter.getFullName("_")); 
		cmdBuilder.append(" <- jitter(");
		cmdBuilder.append(getId());
		cmdBuilder.append("$");
		cmdBuilder.append(parameter.getFullName("_")); 
		cmdBuilder.append(", 0.0000001)");
		analysisWrapper.executeCommandString(cmdBuilder.toString());
	}

	/**
	 * Removes this DataSet from R to free the used memory.
	 */
	public void removeDataSetInR(AnalysisWrapper analysisWrapper) {
		if (dataLoaded) {
			// Delete all columns
			for (SimpleDataSetColumn<?> column : dataset.getColumns()) {
				String columnId = column.getParameter().getFullName("_");
				analysisWrapper.executeCommandString("rm(" + columnId + ");");
			}
			// Delete data table
			analysisWrapper.executeCommandString("rm(" + getId() + ");");
			dataLoaded = false;
		}
	}
}

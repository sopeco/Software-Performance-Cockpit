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
package org.sopeco.engine.validation;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.persistence.dataset.AbstractDataSetColumn;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetInputColumn;
import org.sopeco.persistence.dataset.DataSetObservationColumn;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.dataset.util.ParameterUtil;

/**
 * This class converts the DataSet, where the data is stored column-wise in a
 * set of rows which are used for the validation. One ValidationRow consists of
 * many independent parameters (inputs) and one dependent parameters (output).
 * 
 * @author Dennis Westermann
 * 
 */
public final class DataSetToValidationRows {

	/**
	 * Utility class, only static methods, thus private constructor.
	 */
	private DataSetToValidationRows() {

	}

	/**
	 * This method extracts the correct parameter values (as described in the
	 * analysis configuration) from the data set and returns a list of
	 * TrainingTuples.
	 * 
	 * @param nameOfDependentParameter
	 *            name of the dependent parameter
	 * @param namesOfIndependentParameters
	 *            list of names of the independent parameters
	 * @param data
	 *            dataset to be converted
	 * 
	 * @return a list of validation rows
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList<ValidationRow> convert(DataSetAggregated data, String nameOfDependentParameter,
			List<String> namesOfIndependentParameters) {
		ArrayList<ValidationRow> extractedRows = new ArrayList<ValidationRow>();

		validate(data);

		int size = data.size();

		for (int i = 0; i < size; i++) {
			ValidationRow validationRow = new ValidationRow();

			for (AbstractDataSetColumn<?> column : data.getColumns()) {
				if (column.getParameter().getFullName().equals(nameOfDependentParameter)) {
					validationRow.setDependentParameterValue(((DataSetObservationColumn) column).getParameterValues(i)
							.getMeanAsParameterValue());
					validationRow.setDependentParameterName(nameOfDependentParameter);
				} else if (namesOfIndependentParameters.contains(column.getParameter().getFullName())) {
					validationRow.addIndependentParameterValue(((DataSetInputColumn) column).getParameterValue(i));
				}
			}
			extractedRows.add(validationRow);
		}

		return extractedRows;
	}

	/**
	 * This method validates the ParameterValueLists for the dependent and
	 * independent parameter. They are valid, if they are of the type Integer or
	 * Double and all lists have the same size.
	 * 
	 */
	private static void validate(DataSetAggregated dataSet) {

		validateTypes(dataSet);

		validateLengths(dataSet);
	}

	/**
	 * This method checks, if all Parameters have valid types (Integer or
	 * Double) Otherwise
	 */
	private static void validateTypes(DataSetAggregated dataSet) {

		for (AbstractDataSetColumn<?> column : dataSet.getColumns()) {
			// if (!valideType(column))
			// throw new IllegalStateException (
			// "The type ("+ column.getParameter().getType()
			// +") of the independent parameter "
			// + column.getParameter().getFullName()
			// + " is not valid");
		}
	}

	/**
	 * This method checks if the Parameter lists are of the type Integer or
	 * Double. Other types are not supported.
	 * 
	 * @param pvl
	 * @return
	 */
	private static boolean valideType(AbstractDataSetColumn<?> column) {
		if (ParameterUtil.getTypeEnumeration(column.getParameter().getType()).equals(ParameterType.INTEGER)
				|| ParameterUtil.getTypeEnumeration(column.getParameter().getType()).equals(ParameterType.DOUBLE)) {
			return true;
		}
		return false;
	}

	/**
	 * This method checks if all relevant ParameterValueLists have the same
	 * lengths. If not, this converter cannot build correct TrainingTuples
	 * because the mapping between the entries of the different parameter value
	 * lists is not possible.
	 * 
	 */
	private static void validateLengths(DataSetAggregated dataSet) {

		int expectedLength = dataSet.size();

		for (AbstractDataSetColumn<?> column : dataSet.getColumns()) {
			int actualLength = column.size();
			if (expectedLength != actualLength) {
				throw new IllegalStateException("Length of ParameterValueLists differs");
			}
		}

	}

}

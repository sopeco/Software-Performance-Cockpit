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
public class DataSetToValidationRows {
	/**
	 * This method extracts the correct parameter values (as described in the
	 * analysis configuration) from the data set and returns a list of
	 * TrainingTuples.
	 * 
	 * @param manyToOneDependency
	 * @param data
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList<ValidationRow> convert(DataSetAggregated data,
			String nameOfDependentParameter,
			List<String> namesOfIndependentParameters) {
		ArrayList<ValidationRow> extractedRows = new ArrayList<ValidationRow>();

		validate(data);

		int size = data.size();

		for (int i = 0; i < size; i++) {
			ValidationRow validationRow = new ValidationRow();

			for (AbstractDataSetColumn<?> column : data.getColumns()) {
				if (column.getParameter().getFullName()
						.equals(nameOfDependentParameter)) {
					validationRow
							.setDependentParameterValue(((DataSetObservationColumn) column)
									.getParameterValues(i)
									.getMeanAsParameterValue());
					validationRow
							.setDependentParameterName(nameOfDependentParameter);
				} else if (namesOfIndependentParameters.contains(column
						.getParameter().getFullName())) {
					validationRow
							.addIndependentParameterValue(((DataSetInputColumn) column)
									.getParameterValue(i));
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
			if (!valideType(column))
				throw new IllegalStateException (
						"The type of the independent parameter "
								+ column.getParameter().getFullName()
								+ " is not valid");
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
				|| ParameterUtil.getTypeEnumeration(column.getParameter().getType()).equals(ParameterType.DOUBLE))
			return true;
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
			if (expectedLength != actualLength)
				throw new IllegalStateException(
						"Length of ParameterValueLists differs");
		}

	}

}

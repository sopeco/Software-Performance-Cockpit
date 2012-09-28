package org.sopeco.engine.validation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.sopeco.persistence.dataset.DataSetAggregated;

/**
 * Holds the measured data against which different prediction functions can be
 * validated.
 * 
 * @author Dennis Westermann
 * 
 */
public class ValidationObject implements Iterable<ValidationRow> {
	private DataSetAggregated validationDataSet;
	private String nameOfDependentParameter;
	private List<String> namesOfIndependentParameters;
	private int size;

	private ArrayList<ValidationRow> validationRows;

	/**
	 * @param validationDataSet
	 *            the measured data against which prediction functions should be
	 *            validated
	 * @param nameOfDependentParameter
	 *            the name of the dependent parameter in the dataset
	 * @param namesOfIndependentParameters
	 *            the names of the independent parameters in the dataset
	 */
	public ValidationObject(DataSetAggregated validationDataSet, String nameOfDependentParameter,
			List<String> namesOfIndependentParameters) {
		super();
		this.validationDataSet = validationDataSet;
		this.size = validationDataSet.size();
		this.nameOfDependentParameter = nameOfDependentParameter;
		this.namesOfIndependentParameters = namesOfIndependentParameters;
		this.validationRows = DataSetToValidationRows.convert(this.validationDataSet, this.nameOfDependentParameter,
				this.namesOfIndependentParameters);
	}

	/**
	 * Returns the validation row for the given index.
	 * 
	 * @param index
	 *            index for which the validation row should be returned
	 * @return the validation row for the given index
	 */
	public ValidationRow getValidationRow(int index) {
		return validationRows.get(index);
	}

	/**
	 * Returns the size of the validation object.
	 * 
	 * @return size
	 */
	public int getSize() {
		return this.size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sopeco.validation.IValidationData#iterator()
	 */

	@Override
	public Iterator<ValidationRow> iterator() {
		return new Iterator<ValidationRow>() {

			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < size;
			}

			@Override
			public ValidationRow next() {
				return getValidationRow(i++);
			}

			@Override
			public void remove() {
				throw new IllegalStateException("ValidationObject cannot be edited.");
			}
		};
	}

	protected void setValidationDataSet(DataSetAggregated validationDataSet) {
		this.validationDataSet = validationDataSet;
		this.size = validationDataSet.size();
		this.validationRows = DataSetToValidationRows.convert(this.validationDataSet, this.nameOfDependentParameter,
				this.namesOfIndependentParameters);
	}

	/**
	 * Returns the validation dataset.
	 * 
	 * @return the validation dataset.
	 */
	public DataSetAggregated getValidationDataSet() {
		return validationDataSet;
	}

	protected void setNameOfDependentParameter(String nameOfDependentParameter) {
		this.nameOfDependentParameter = nameOfDependentParameter;
	}

	/**
	 * Returns the name of the dependent parameter.
	 * 
	 * @return the name of the dependent parameter
	 */
	public String getNameOfDependentParameter() {
		return nameOfDependentParameter;
	}

}

package org.sopeco.persistence.dataset;

import java.io.Serializable;
import java.util.List;

import org.sopeco.configuration.parameter.ParameterRole;
import org.sopeco.configuration.parameter.ParameterUsage;
import org.sopeco.persistence.dataset.ParameterValue;

/**
 * Row of a DataSet.
 * 
 * @author Jens Happe
 * 
 */
@SuppressWarnings("unchecked")
public class DataSetRow implements Serializable {

	/**
	 * ParameterValues of the row.
	 */
	private List<ParameterValue> inputParameterValues;

	/**
	 * ParameterValues of the row.
	 */
	private List<ParameterValueList> observationParameterValues;

	/**
	 * Constructor. Only to be used in builders & factories.
	 * 
	 * @param parameterValues
	 */
	protected DataSetRow(List<ParameterValue> inputParameterValues,
			List<ParameterValueList> observationParameterValues) {
		super();
		this.inputParameterValues = inputParameterValues;
		this.observationParameterValues = observationParameterValues;
	}

	/**
	 * @return The ParameterValues of this row.
	 */
	public List<ParameterValue> getInputRowValues() {
		return inputParameterValues;
	}

	/**
	 * @return The ParameterValues of this row.
	 */
	public List<ParameterValueList> getObservableRowValues() {
		return observationParameterValues;
	}

	/**
	 * @param parameter
	 *            Parameter of interest.
	 * @return The value of a particular parameter in this row.
	 */
	public ParameterValue getInputParameterValue(ParameterUsage parameter) {
		if (!parameter.getRole().equals(ParameterRole.INPUT)) {
			throw new IllegalArgumentException(
					"Parameter must be an input parameter");
		}
		for (ParameterValue pv : inputParameterValues) {
			if (pv.getParameter().equals(parameter)) {
				return pv;
			}
		}
		throw new IllegalArgumentException("Unknown Parameter: " + parameter);
	}

	/**
	 * @param parameter
	 *            Parameter of interest.
	 * @return The value of a particular parameter in this row.
	 */
	public ParameterValueList getObservableParameterValues(
			ParameterUsage parameter) {
		if (!parameter.getRole().equals(ParameterRole.OBSERVATION) && !parameter.getRole().equals(ParameterRole.OBSERVABLE_TIME_SERIES)) {
			throw new IllegalArgumentException(
					"Parameter must be an input parameter");
		}
		for (ParameterValueList pvl : observationParameterValues) {
			if (pvl.getParameter().equals(parameter)) {
				return pvl;
			}
		}
		throw new IllegalArgumentException("Unknown Parameter: " + parameter);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DataSetRow)) {
			return false;
		}
		DataSetRow other = (DataSetRow) obj;
		if ((this.getInputRowValues().size() != other.getInputRowValues()
				.size())
				|| (this.getObservableRowValues().size() != other
						.getObservableRowValues().size())) {
			return false;
		}

		if (!equalIndependentParameterValues(other)) {
			return false;
		}

		// TODO: compare observation values
		return true;
	}

	@Override
	public int hashCode() {
		int code = 0;
		for (ParameterValue<?> pv : this.getInputRowValues()) {
			code += pv.getParameter().hashCode() + pv.getValue().hashCode();
		}
		for (ParameterValueList pvl : this.getObservableRowValues()) {

			for (Object value : pvl.getValues()) {
				code += pvl.getParameter().hashCode() + value.hashCode();
			}

		}
		return code;
	}

	public boolean equalIndependentParameterValues(DataSetRow other) {
		for (ParameterValue<?> pv : this.getInputRowValues()) {
			try {
				ParameterValue<?> otherValue = other.getInputParameterValue(pv
						.getParameter());
				if (!otherValue.getValue().equals(pv.getValue())) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}

		return true;
	}
	


}

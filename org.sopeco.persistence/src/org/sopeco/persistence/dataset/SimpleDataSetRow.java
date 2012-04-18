package org.sopeco.persistence.dataset;

import java.io.Serializable;
import java.util.List;

import org.sopeco.model.configuration.environment.ParameterDefinition;

/**
 * Row of a DataSet.
 * 
 * @author Jens Happe
 * 
 */
public class SimpleDataSetRow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ParameterValues of the row.
	 */
	@SuppressWarnings({ "rawtypes" })
	private List<ParameterValue> parameterValues;

	/**
	 * Constructor. Only to be used in builders & factories.
	 * 
	 * @param parameterValues
	 */
	@SuppressWarnings("rawtypes")
	protected SimpleDataSetRow(List<ParameterValue> parameterValues) {
		super();
		this.parameterValues = parameterValues;
	}

	/**
	 * @return The ParameterValues of this row.
	 */
	@SuppressWarnings("rawtypes")
	public List<ParameterValue> getRowValues() {
		return parameterValues;
	}

	/**
	 * @param parameter
	 *            Parameter of interest.
	 * @return The value of a particular parameter in this row.
	 */
	@SuppressWarnings("rawtypes")
	public ParameterValue getParameterValue(ParameterDefinition parameter) {
		for (ParameterValue pv : parameterValues) {
			if (pv.getParameter().equals(parameter)) {
				return pv;
			}
		}
		throw new IllegalArgumentException("Unknown Parameter: " + parameter);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SimpleDataSetRow)) {
			return false;
		}
		SimpleDataSetRow other = (SimpleDataSetRow) obj;
		if (this.getRowValues().size() != other.getRowValues().size()) {
			return false;
		}
		for (ParameterValue<?> pv : this.getRowValues()) {
			try {
				ParameterValue<?> otherValue = other.getParameterValue(pv
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

	@Override
	public int hashCode() {
		int code = 0;
		for (ParameterValue<?> pv : this.getRowValues()) {
			code += pv.getParameter().hashCode() + pv.getValue().hashCode();
		}
		return code;
	}

	public boolean equalIndependentParameterValues(SimpleDataSetRow other,
			ParameterDefinition dependentParameter) {
		for (ParameterValue<?> pv : this.getRowValues()) {
			try {
				ParameterValue<?> otherValue = other.getParameterValue(pv
						.getParameter());
				if (otherValue.getParameter().getFullName()
						.equals(dependentParameter.getFullName())) {
					continue;
				}
				if (!otherValue.getValue().equals(pv.getValue())) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}

		return true;
	}

	public boolean equalIndependentParameterValues(SimpleDataSetRow other,
			List<ParameterDefinition> dependentParameters) {
		for (ParameterValue<?> pv : this.getRowValues()) {
			try {
				ParameterValue<?> otherValue = other.getParameterValue(pv
						.getParameter());
				if (dependentParameters.contains(otherValue.getParameter())) {
					continue;
				}
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

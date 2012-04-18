package org.sopeco.persistence.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.sopeco.configuration.parameter.ParameterRole;
import org.sopeco.configuration.parameter.ParameterUsage;

/**
 * Row of a DataSet.
 * 
 * @author Jens Happe
 * 
 */
@SuppressWarnings("unchecked")
public class SimpleDataSetRow implements Serializable {

	/**
	 * ParameterValues of the row.
	 */
	private List<ParameterValue> parameterValues;

	/**
	 * Constructor. Only to be used in builders & factories.
	 * 
	 * @param parameterValues
	 */
	protected SimpleDataSetRow(List<ParameterValue> parameterValues) {
		super();
		this.parameterValues = parameterValues;
	}

	/**
	 * @return The ParameterValues of this row.
	 */
	public List<ParameterValue> getRowValues() {
		return parameterValues;
	}

	/**
	 * @param parameter
	 *            Parameter of interest.
	 * @return The value of a particular parameter in this row.
	 */
	public ParameterValue getParameterValue(ParameterUsage parameter) {
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
			ParameterUsage dependentParameter) {
		for (ParameterValue<?> pv : this.getRowValues()) {
			try {
				ParameterValue<?> otherValue = other.getParameterValue(pv
						.getParameter());
				if (otherValue.getParameter().getID()
						.equals(dependentParameter.getID())) {
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
			List<ParameterUsage> dependentParameters) {
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

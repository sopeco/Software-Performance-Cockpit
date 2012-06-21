package org.sopeco.persistence.entities.definition;

import java.io.Serializable;

/**
 * @author Dennis Westermann
 * 
 */
public abstract class ParameterValueAssignment implements Serializable {

	private static final long serialVersionUID = 1L;

	protected ParameterDefinition parameter;

	protected ParameterValueAssignment() {
		super();
	}

	public ParameterDefinition getParameter() {
		return parameter;
	}

	public void setParameter(ParameterDefinition newParameter) {
		parameter = newParameter;
	}

	/**
	 * Creates a new {@link ParameterValueAssignment} instance with the same
	 * attributes as this instance.
	 * 
	 * @return a new {@link ParameterValueAssignment} instance with the same
	 *         attributes as the this instance.
	 */
	@Override
	public abstract ParameterValueAssignment clone();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParameterValueAssignment other = (ParameterValueAssignment) obj;
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter))
			return false;
		return true;
	}
	
}
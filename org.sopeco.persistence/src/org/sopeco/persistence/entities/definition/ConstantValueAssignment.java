
package org.sopeco.persistence.entities.definition;


/**
 * 
 * 
 * @author Dennis Westermann
 *
 */
public class ConstantValueAssignment extends ParameterValueAssignment {

	private static final long serialVersionUID = 1L;

	private String value = null;

	public ConstantValueAssignment() {
		super();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String newValue) {
		value = newValue;
	}

	/* (non-Javadoc)
	 * @see org.sopeco.persistence.entities.definition.ParameterValueAssignment#clone()
	 */
	@Override
	public ConstantValueAssignment clone() {
		ConstantValueAssignment target = new ConstantValueAssignment();
		target.setParameter(this.getParameter());
		target.setValue(this.getValue());
		return target;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		ConstantValueAssignment other = (ConstantValueAssignment) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter))
			return false;
		return true;
	}
	
} 
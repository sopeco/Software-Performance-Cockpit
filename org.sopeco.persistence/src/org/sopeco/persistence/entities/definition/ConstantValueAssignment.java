
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

	
} 
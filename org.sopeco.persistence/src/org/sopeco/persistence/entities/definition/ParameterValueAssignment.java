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

} 
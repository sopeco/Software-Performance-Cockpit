package org.sopeco.persistence.entities.definition;

import java.io.Serializable;

public class MeasurementEnvironmentDefinition implements Serializable {
	
	private static final long serialVersionUID = 1L;

	protected ParameterNamespace root;

	public MeasurementEnvironmentDefinition() {
		super();
	}

	public ParameterNamespace getRoot() {
		return root;
	}

	public void setRoot(ParameterNamespace newRoot) {
		root = newRoot;
	}

} 
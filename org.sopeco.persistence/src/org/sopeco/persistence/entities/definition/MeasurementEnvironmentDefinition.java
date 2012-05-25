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

	/**
	 * Looks for a namespace with the given name (Example: "org.sopeco").
	 * 
	 * @param name
	 * @return the namespace with the given name (Example: "org.sopeco").
	 *         <code>null</code> if the {@link MeasurementEnvironmentDefinition}
	 *         does not contain a namespace with the given name.
	 */
	public ParameterNamespace getNamespace(String name) {
		return searchNamespace(root, name);
	}

	private ParameterNamespace searchNamespace(ParameterNamespace parent, String name) {
		if (parent.getFullName().equals(name)) {
			return parent;
		} else {
			for (ParameterNamespace child : parent.getChildren()) {
				ParameterNamespace childSearchResult = searchNamespace(child, name);
				if (childSearchResult != null) {
					return childSearchResult;
				}
			}
		}

		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null) 
			return false;
		
		MeasurementEnvironmentDefinition other = (MeasurementEnvironmentDefinition) obj;
		return this.getRoot().equals(other.getRoot());
	}

	@Override
	public int hashCode() {
		return getRoot() != null ? getRoot().hashCode() : 0;
	}

}
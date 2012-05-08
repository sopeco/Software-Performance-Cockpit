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
		if (parent.getName().equals(name)) {
			return parent;
		} else {
			for (ParameterNamespace child : parent.getChildren()) {
				if (searchNamespace(child, name) != null) {
					return child;
				}
			}
		}

		return null;
	}

	private String createFullNamespaceString(String fullNamespace, ParameterNamespace namespace, String namespaceDelimitter) {

		if (namespace != null && !namespace.getName().isEmpty()) {
			fullNamespace = namespace.getName() + namespaceDelimitter + fullNamespace;
			return createFullNamespaceString(fullNamespace, namespace.getParent(), namespaceDelimitter);
		}

		return fullNamespace;
	}

}
package org.sopeco.persistence.entities.definition;

import java.io.Serializable;

import org.sopeco.util.Tools;

/**
 * @author Dennis Westermann
 * 
 */
public class ParameterDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_NAMESPACE_DELIMITER = ".";

	protected String name = null;

	protected String type = null;

	protected ParameterRole role;

	protected ParameterNamespace namespace;

	public ParameterDefinition() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}

	public String getType() {

		return type;
	}

	public void setType(String newType) {
		type = newType.toUpperCase();
	}

	public ParameterRole getRole() {
		return role;
	}

	public void setRole(ParameterRole newRole) {
		role = newRole;
	}

	/**
	 * Returns the full name of a parameter. The full name is a concatenation of
	 * the namespace hierarchy and the name of the parameter. The different
	 * hierarchy levels are delimited by the default delimiter '.'. Example:
	 * "org.sopeco.myParam".
	 * 
	 * @return the full name (namespace + name) of the parameter where the
	 *         namespaces are delimited by the default delimitter '.'
	 */
	public String getFullName() {
		ParameterNamespace namespace = this.getNamespace();
		String result = createFullNamespaceString("", namespace, DEFAULT_NAMESPACE_DELIMITER) + getName();
		return result;
	}

	/**
	 * Returns the full name of a parameter. The full name is a concatenation of
	 * the namespace hierarchy and the name of the parameter. The different
	 * hierarchy levels are delimited by the given string. Example:
	 * "org_sopeco_myParam" for the delimiter '_'
	 * 
	 * @param namespaceDelimiter
	 *            - the string that should be used for delimiting namespaces in
	 *            a namespace hierarchy
	 * @return the full name (namespace + name) of the parameter where the
	 *         namespaces are delimited by the given string
	 */
	public String getFullName(String namespaceDelimiter) {
		ParameterNamespace namespace = this.getNamespace();
		String result = createFullNamespaceString("", namespace, namespaceDelimiter) + getName();
		return result;
	}

	private String createFullNamespaceString(String fullNamespace, ParameterNamespace namespace,
			String namespaceDelimitter) {

		if (namespace != null && namespace.getName() != null && !namespace.getName().isEmpty()) {
			fullNamespace = namespace.getName() + namespaceDelimitter + fullNamespace;
			return createFullNamespaceString(fullNamespace, namespace.getParent(), namespaceDelimitter);
		}

		return fullNamespace;
	}

	public ParameterNamespace getNamespace() {
		return namespace;
	}

	public void setNamespace(ParameterNamespace newNamespace) {
		namespace = newNamespace;
	}

	/**
	 * @return <code>true</code> if parameter type is Integer or Double,
	 *         otherwise <code>false</code>.
	 */
	public boolean isNumeric() {
		if (this.getType().trim().equalsIgnoreCase("Integer") || this.getType().trim().equalsIgnoreCase("Double")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		ParameterDefinition obj = (ParameterDefinition) o;
		if (getFullName() == null || obj.getFullName() == null)
			return false;
		if (!this.getFullName().equals(obj.getFullName())) {
			return false;
		}
		if (!this.getType().equals(obj.getType())) {
			return false;
		}
		if (!this.getRole().equals(obj.getRole())) {
			return false;
		}

		return true;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getFullName() == null) ? 0 : getFullName().hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public String toString() {

		return "ParameterDefinition{" + "name='" + this.getName() + "\' " + "type='" + this.getType() + "\' "
				+ "role='" + this.getRole() + "\' " + '}';
	}

}
/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.persistence.entities.definition;

import java.io.Serializable;

import org.sopeco.util.Tools.SupportedTypes;

/**
 * @author Dennis Westermann
 * @author Roozbeh Farahbod
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
		return type.toUpperCase();
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
	public String toString() {

		return "ParameterDefinition{" + "name='" + this.getName() + "\' " + "type='" + this.getType() + "\' "
				+ "role='" + this.getRole() + "\' " + '}';
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		ParameterDefinition other = (ParameterDefinition) obj;
		
		
		
		
		if (getFullName() == null) {
			if (other.getFullName() != null)
				return false;
		} else if (!getFullName().equals(other.getFullName()))
			return false;
		return true;
	}

}
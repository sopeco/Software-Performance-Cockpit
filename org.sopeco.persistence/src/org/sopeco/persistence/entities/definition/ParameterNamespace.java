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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dennis Westermann
 * 
 */
public class ParameterNamespace implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_NAMESPACE_DELIMITER = ".";

	protected List<ParameterNamespace> children;

	protected String name = null;

	protected List<ParameterDefinition> parameters;

	protected ParameterNamespace parent;

	public ParameterNamespace() {
		super();
	}

	public List<ParameterNamespace> getChildren() {
		if (children == null) {
			children = new LinkedList<ParameterNamespace>();
		}
		return children;
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}

	public List<ParameterDefinition> getParameters() {
		if (parameters == null) {
			parameters = new LinkedList<ParameterDefinition>();
		}
		return parameters;
	}

	public ParameterNamespace getParent() {
		return parent;
	}

	public void setParent(ParameterNamespace newParent) {
		parent = newParent;
	}

	/**
	 * Returns the full name of a parameter namespace. The full name is a
	 * concatenation of the namespace hierarchy. The different hierarchy levels
	 * are delimited by the default delimiter '.'. Example: "org.sopeco".
	 * 
	 * @return the full name (namespace + all parent namespaces) of the
	 *         namespace where the namespaces are delimited by the default
	 *         delimitter '.'
	 */
	public String getFullName() {
		return createFullNamespaceString("", this, DEFAULT_NAMESPACE_DELIMITER);
	}

	private String createFullNamespaceString(String fullNamespace, ParameterNamespace namespace, String namespaceDelimitter) {

		if (namespace != null && !namespace.getName().isEmpty()) {
			fullNamespace = namespace.getName() + namespaceDelimitter + fullNamespace;
			return createFullNamespaceString(fullNamespace, namespace.getParent(), namespaceDelimitter);
		}

		if (fullNamespace.isEmpty()) {
			return fullNamespace;
		} else {
			return fullNamespace.substring(0, fullNamespace.lastIndexOf(DEFAULT_NAMESPACE_DELIMITER));
		}
	}

	/*
	 * Utility functions
	 */

	/**
	 * Looks in the current namespace (but not in its children) for a parameter
	 * with the given name (not full name).
	 * 
	 * @param name
	 *            the name of the parameter of this namespace (not the fullname)
	 * @return the {@link ParameterDefinition} with the given name located in
	 *         this namespace; <code>null</code> if this namespace does not
	 *         contain a parameter with the given name.
	 */
	public ParameterDefinition getParameter(String name) {

		for (ParameterDefinition pd : this.getParameters()) {
			if (pd.getName().equals(name)) {
				return pd;
			}
		}

		return null;
	}

	/**
	 * Collects all observation parameters of this namespace (including all
	 * child namespaces).
	 * 
	 * @return the set of parameter definitions
	 */
	public List<ParameterDefinition> getObservationParameters() {
		List<ParameterDefinition> result = new LinkedList<ParameterDefinition>();
		collectObservationParameters(this, result);
		return result;
	}

	/**
	 * Collects all observation parameters of the namespace (including all child
	 * namespaces).
	 * 
	 * @param namespace
	 * @param observationParameterList
	 *            - the collection in which the observation parameters should be
	 *            stored (must not be null)
	 */
	private static void collectObservationParameters(ParameterNamespace namespace, Collection<ParameterDefinition> observationParameterList) {

		for (ParameterDefinition parameter : namespace.getParameters()) {
			if (parameter.getRole().equals(ParameterRole.OBSERVATION)) {
				observationParameterList.add(parameter);
			}
		}

		for (ParameterNamespace child : namespace.getChildren()) {
			collectObservationParameters(child, observationParameterList);
		}

	}

	/**
	 * Collects all parameters (input and observation) of this namespace
	 * (including all child namespaces).
	 * 
	 * @return the set of parameter definitions
	 */
	public List<ParameterDefinition> getAllParameters() {
		List<ParameterDefinition> result = new LinkedList<ParameterDefinition>();
		collectAllParameters(this, result);
		return result;
	}

	/**
	 * Collects all parameters of the given namespace (including all child
	 * namespaces).
	 * 
	 * @param namespace
	 * @param parameterList
	 *            - the list in which the parameters should be stored (must not
	 *            be null)
	 */
	private static void collectAllParameters(ParameterNamespace namespace, Collection<ParameterDefinition> parameterList) {

		for (ParameterDefinition parameter : namespace.getParameters()) {
			parameterList.add(parameter);
		}

		for (ParameterNamespace child : namespace.getChildren()) {
			collectAllParameters(child, parameterList);
		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parameters == null|| parameters.isEmpty()) ? 0 : parameters.hashCode());
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
		ParameterNamespace other = (ParameterNamespace) obj;
		if (children == null || children.isEmpty()) {
			if (other.children != null && !other.children.isEmpty())
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parameters == null || parameters.isEmpty()) {
			if (other.parameters != null && !other.parameters.isEmpty())
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		return true;
	}
	

}
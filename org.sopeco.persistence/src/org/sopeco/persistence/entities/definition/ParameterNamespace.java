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

	private static final String DEFAULT_NAMESPACE_DELIMITER = ".";
	
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
	 * Returns the full name of a parameter namespace. The full name is a concatenation of the namespace hierarchy.
	 * The different hierarchy levels are delimited by the default delimiter '.'.
	 * Example: "org.sopeco".
	 * 
	 * @return the full name (namespace + all parent namespaces) of the namespace where the namespaces are delimited by the default delimitter '.'
	 */
	public String getFullName(){
		return createFullNamespaceString("", this, DEFAULT_NAMESPACE_DELIMITER);
	}
	
	private String createFullNamespaceString(String fullNamespace, ParameterNamespace namespace, String namespaceDelimitter) {

		if (namespace != null && !namespace.getName().isEmpty()) {
			fullNamespace = namespace.getName() + namespaceDelimitter + fullNamespace;
			return createFullNamespaceString(fullNamespace, namespace.getParent(), namespaceDelimitter);
		}

		if(fullNamespace.isEmpty()) {
		 return fullNamespace;
		} else {
			return fullNamespace.substring(0, fullNamespace.lastIndexOf(DEFAULT_NAMESPACE_DELIMITER));
		}
	}

	
	/*
	 * Utility functions
	 */
	
	/**
	 * Collects all observation parameters of this namespace (including all child namespaces). 
	 * 
	 * @return the set of parameter definitions
	 */
	public List<ParameterDefinition> getObservationParameters() {
		List<ParameterDefinition> result = new LinkedList<ParameterDefinition>();
		collectObservationParameters(this, result);
		return result;
	}
	
	
	/**
	 * Collects all observation parameters of the namespace (including all child namespaces). 
	 * 
	 * @param namespace
	 * @param observationParameterList - the collection in which the observation parameters should be stored (must not be null)
	 */
	private static void collectObservationParameters(ParameterNamespace namespace, Collection<ParameterDefinition> observationParameterList) {
		
		for(ParameterDefinition parameter : namespace.getParameters()){
			if(parameter.getRole().equals(ParameterRole.OBSERVATION)){
				observationParameterList.add(parameter);
			}
		}
		
		for(ParameterNamespace child : namespace.getChildren()){
			collectObservationParameters(child, observationParameterList);
		}
		
	}
	
	/**
	 * Collects all parameters (input and observation) of this namespace (including all child namespaces). 
	 * 
	 * @return the set of parameter definitions
	 */
	public List<ParameterDefinition> getAllParameters() {
		List<ParameterDefinition> result = new LinkedList<ParameterDefinition>();
		collectAllParameters(this, result);
		return result;
	}
	
	/**
	 * Collects all parameters of the given namespace (including all child namespaces). 
	 * 
	 * @param namespace
	 * @param parameterList - the list in which the parameters should be stored (must not be null)
	 */
	private static void collectAllParameters(ParameterNamespace namespace, Collection<ParameterDefinition> parameterList) {
		
		for(ParameterDefinition parameter : namespace.getParameters()){
			parameterList.add(parameter);
		}
		
		for(ParameterNamespace child : namespace.getChildren()){
			collectAllParameters(child, parameterList);
		}
		
	}

}
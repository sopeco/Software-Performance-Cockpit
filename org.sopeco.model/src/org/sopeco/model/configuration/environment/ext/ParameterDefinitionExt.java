package org.sopeco.model.configuration.environment.ext;

import org.sopeco.model.configuration.environment.ParameterNamespace;
import org.sopeco.model.configuration.environment.impl.ParameterDefinitionImpl;


/**
 * This class extends the generated class {@link ParameterDefinitionImpl} in order to implement not generated methods
 * as well as equals(), hashCode(), and toString().
 * 
 * @author Dennis Westermann
 *
 */
public class ParameterDefinitionExt extends ParameterDefinitionImpl {
	
	private static final long serialVersionUID = 1L;

	
	private static final String NAMESPACE_DELIMITER = ".";
	
	/**
	 * Returns the full name of a parameter. The full name is a concatenation of the namespace hierarchy and the name of the parameter.
	 * The different hierarchy levels are delimited by the default delimiter '.'.
	 * Example: "org.sopeco.myParam".
	 * 
	 * @param nameSpaceDelimitter - the string that should be used for delimiting namespaces in a namespace hierarchy 
	 * @return the full name (namespace + name) of the parameter where the namespaces are delimited by the given string
	 */
	@Override
	public String getFullName() {
		ParameterNamespace namespace =(ParameterNamespace) this.eContainer();
		String result = createFullNamespaceString("", namespace, NAMESPACE_DELIMITER) + getName();
		return result;
	}

	@Override
	public boolean isSetFullName() {
		return true;
	}

	
	/**
	 * Returns the full name of a parameter. The full name is a concatenation of the namespace hierarchy and the name of the parameter.
	 * The different hierarchy levels are delimited by the given string.
	 * Example: "org_sopeco_myParam" for the delimiter '_'
	 * 
	 * @param namespaceDelimiter - the string that should be used for delimiting namespaces in a namespace hierarchy 
	 * @return the full name (namespace + name) of the parameter where the namespaces are delimited by the given string
	 */
	@Override
	public String getFullName(String namespaceDelimiter){
		ParameterNamespace namespace = (ParameterNamespace)this.eContainer();
		String result = createFullNamespaceString("", namespace, namespaceDelimiter) + getName();
		return result;
	}
	
	private String createFullNamespaceString(String fullNamespace, ParameterNamespace namespace, String namespaceDelimitter){

		if(namespace!=null && namespace.getName() != null && !namespace.getName().isEmpty()){
			
			fullNamespace = namespace.getName() + namespaceDelimitter + fullNamespace;
			
			if(namespace.eContainer() instanceof ParameterNamespace) {
				ParameterNamespace parent = (ParameterNamespace) namespace.eContainer();
				return createFullNamespaceString(fullNamespace, parent, namespaceDelimitter);
			}
			
		}
		
		return fullNamespace;
	}

	@Override
	public boolean equals(Object o) {

		 if (this == o) return true;
		 if (o == null || getClass() != o.getClass()) return false;

		 ParameterDefinitionExt obj = (ParameterDefinitionExt) o;
		 if(getFullName() == null || obj.getFullName() == null) return false;
		 return this.getFullName().equals(obj.getFullName());

	}

	@Override
	public int hashCode() {
		if(this.getFullName()!=null){
			return getFullName().hashCode();
		} else {
			return 0;
		}
	}

	@Override
    public String toString() {

       return "ParameterDefinition{" +
	                 "name='" + this.getName() + "\' " +
	                 "type='" + this.getType() + "\' " +
	                 "role='" + this.getRole() + "\' " +'}';
    }

} 

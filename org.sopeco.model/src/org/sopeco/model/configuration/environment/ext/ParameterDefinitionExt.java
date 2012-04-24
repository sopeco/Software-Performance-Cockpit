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

	
	private static final String NAMESPACE_DELIMITTER = ".";
	
	@Override
	public String getFullName() {
		ParameterNamespace namespace = this.getNamespace();
		String result = createFullNamespaceString("", namespace) + getName();
		return result;
	}

	@Override
	public boolean isSetFullName() {
		return true;
	}

	
	private String createFullNamespaceString(String fullNamespace, ParameterNamespace namespace){

		if(namespace!=null && !namespace.getName().isEmpty()){
			fullNamespace = namespace.getName() + NAMESPACE_DELIMITTER + fullNamespace;
			return createFullNamespaceString(fullNamespace, namespace.getParent());
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

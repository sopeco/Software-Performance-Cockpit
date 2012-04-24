package org.sopeco.model.configuration.environment.ext;

import org.sopeco.model.configuration.environment.ParameterNamespace;
import org.sopeco.model.configuration.environment.impl.ParameterDefinitionImpl;


public class ParameterDefinitionExt extends ParameterDefinitionImpl {
	
	private static final long serialVersionUID = 1L;

	
	private static final String NAMESPACE_DELIMITTER = ".";
	
	@Override
	public String getFullName() {
		ParameterNamespace namespace = this.getNamespace();
		String result = createFullNamespaceString("", namespace) + getName();
		System.out.println(result);
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
	
	

} 

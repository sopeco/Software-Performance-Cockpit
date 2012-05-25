package org.sopeco.model.configuration.environment.provider.ext;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.environment.provider.ParameterDefinitionItemProvider;

public class ParameterDefinitionItemProviderExt extends
		ParameterDefinitionItemProvider {

	public ParameterDefinitionItemProviderExt(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}
	
	@Override
	public String getText(Object object) {
		String label = ((ParameterDefinition)object).getFullName();
		return label == null || label.length() == 0 ?
			getString("_UI_ParameterDefinition_type") :
			getString("_UI_ParameterDefinition_type") + " " + label;
	}

}

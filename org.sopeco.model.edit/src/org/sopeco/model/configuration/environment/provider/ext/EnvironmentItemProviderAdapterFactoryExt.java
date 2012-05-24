package org.sopeco.model.configuration.environment.provider.ext;

import org.eclipse.emf.common.notify.Adapter;
import org.sopeco.model.configuration.SoPeCoModelFactoryHandler;
import org.sopeco.model.configuration.environment.provider.EnvironmentItemProviderAdapterFactory;
import org.sopeco.model.configuration.environment.provider.ParameterDefinitionItemProvider;

public class EnvironmentItemProviderAdapterFactoryExt extends
		EnvironmentItemProviderAdapterFactory {
	
	public EnvironmentItemProviderAdapterFactoryExt() {
		SoPeCoModelFactoryHandler.initFactories();
	}
	
//	/**
//	 * This creates an adapter for a {@link org.sopeco.model.configuration.environment.ParameterNamespace}.
//	 * <!-- begin-user-doc -->
//	 * <!-- end-user-doc -->
//	 */
//	@Override
//	public Adapter createParameterNamespaceAdapter() {
//		if (parameterNamespaceItemProvider == null) {
//			parameterNamespaceItemProvider = new ParameterNamespaceItemProviderExt(this);
//		}
//
//		return parameterNamespaceItemProvider;
//	}
	
	/**
	 * This creates an adapter for a {@link org.sopeco.model.configuration.environment.ParameterDefinition}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public Adapter createParameterDefinitionAdapter() {
		if (parameterDefinitionItemProvider == null) {
			parameterDefinitionItemProvider = new ParameterDefinitionItemProviderExt(this);
		}

		return parameterDefinitionItemProvider;
	}

}

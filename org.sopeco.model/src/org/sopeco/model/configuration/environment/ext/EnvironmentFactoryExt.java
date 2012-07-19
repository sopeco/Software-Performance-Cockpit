package org.sopeco.model.configuration.environment.ext;

import org.sopeco.model.configuration.environment.EnvironmentFactory;
import org.sopeco.model.configuration.environment.EnvironmentPackage;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.environment.impl.EnvironmentFactoryImpl;

/**
 * Wrapper class for a generated factory in order to make sure that the extended implementations are used.
 * 
 * 
 * @author Dennis Westermann
 *
 */
public class EnvironmentFactoryExt extends EnvironmentFactoryImpl{

	
	private static EnvironmentFactory factory = null;

	public static synchronized EnvironmentFactory getFactory() {
		if (factory == null) {
			factory = new EnvironmentFactoryExt();
			EnvironmentPackage.eINSTANCE
					.setEFactoryInstance(factory);
		}
		return factory;
	}

	
	@Override
	public ParameterDefinition createParameterDefinition() {
		
		return new ParameterDefinitionExt();
	}


}

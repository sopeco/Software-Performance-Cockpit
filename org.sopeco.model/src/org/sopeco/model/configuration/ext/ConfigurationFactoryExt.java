package org.sopeco.model.configuration.ext;

import org.sopeco.model.configuration.ConfigurationFactory;
import org.sopeco.model.configuration.ConfigurationPackage;
import org.sopeco.model.configuration.ScenarioDefinition;
import org.sopeco.model.configuration.impl.ConfigurationFactoryImpl;

/**
 * Wrapper class for a generated factory in order to make sure that the extended implementations are used.
 * 
 * 
 * @author Dennis Westermann
 *
 */
public class ConfigurationFactoryExt extends ConfigurationFactoryImpl{

	
	private static ConfigurationFactory factory = null;

	public static synchronized ConfigurationFactory getFactory() {
		if (factory == null) {
			factory = new ConfigurationFactoryExt();
			ConfigurationPackage.eINSTANCE
					.setEFactoryInstance(factory);
		}
		return factory;
	}

	
	@Override
	public ScenarioDefinition createScenarioDefinition() {
		
		return new ScenarioDefinitionExt();
	}


}

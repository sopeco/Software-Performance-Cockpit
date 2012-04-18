package org.sopeco.persistence.dataset.util;

import org.sopeco.configuration.parameter.ParameterFactory;
import org.sopeco.configuration.parameter.ParameterPackage;

public class ParameterCreator {
	private ParameterCreator() {

	}

	private static ParameterFactory factory = null;

	public static synchronized ParameterFactoryExtended getFactory() {
		if (factory == null) {
			factory = new ParameterFactoryExtended();
			ParameterPackage.eINSTANCE.setEFactoryInstance(factory);
		}
		return (ParameterFactoryExtended) factory;
	}
}

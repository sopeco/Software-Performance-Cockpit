package org.sopeco.model.configuration.measurements.provider.ext;

import org.eclipse.emf.common.notify.Adapter;
import org.sopeco.model.configuration.measurements.provider.MeasurementsItemProviderAdapterFactory;

public class MeasurementsItemProviderAdapterFactoryExt extends
		MeasurementsItemProviderAdapterFactory {
	
	@Override
	public Adapter createDynamicValueAssignmentAdapter() {
		if (dynamicValueAssignmentItemProvider == null) {
			dynamicValueAssignmentItemProvider = new DynamicValueAssignmentItemProviderExt(this);
		}

		return dynamicValueAssignmentItemProvider;
	}

}

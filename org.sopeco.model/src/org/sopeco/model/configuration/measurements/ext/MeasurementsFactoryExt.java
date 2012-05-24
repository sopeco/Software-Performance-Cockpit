package org.sopeco.model.configuration.measurements.ext;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.sopeco.engine.experimentseries.IParameterVariationExtension;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.engine.registry.Extensions;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.model.configuration.measurements.DynamicValueAssignment;
import org.sopeco.model.configuration.measurements.MeasurementsPackage;
import org.sopeco.model.configuration.measurements.impl.MeasurementsFactoryImpl;

public class MeasurementsFactoryExt extends MeasurementsFactoryImpl {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	@Override
	public DynamicValueAssignment createDynamicValueAssignment() {
		final DynamicValueAssignment dynamicValueAssignment = super
				.createDynamicValueAssignment();

		dynamicValueAssignment.eAdapters().add(new AdapterImpl() {
			@Override
			public void notifyChanged(Notification notification) {
				// Listen for changes to features.
				switch (notification.getFeatureID(DynamicValueAssignment.class)) {
				case MeasurementsPackage.DYNAMIC_VALUE_ASSIGNMENT__NAME:
					if (notification.getEventType() == Notification.SET) {
						dynamicValueAssignment.getConfiguration().clear();

						IExtensionRegistry registry = ExtensionRegistry
								.getSingleton();
						Extensions<IParameterVariationExtension> ext = registry
								.getExtensions(IParameterVariationExtension.class);

						for (IParameterVariationExtension parameterVariation : ext
								.getList()) {

							if (dynamicValueAssignment.getName() == parameterVariation
									.getName()) {
								dynamicValueAssignment.getConfiguration()
										.putAll(parameterVariation
												.getConfigParameters());
								break;
							}
						}

					}
				}
			}
		});

		// ToDo: Get Childs to Add from Configuration
		// dynamicValueAssignment.getConfiguration().put("", "");
		return dynamicValueAssignment;
	}

}

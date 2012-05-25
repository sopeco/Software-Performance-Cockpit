package org.sopeco.model.configuration.measurements.provider.ext;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.sopeco.engine.experimentseries.IParameterVariationExtension;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.engine.registry.Extensions;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.model.configuration.measurements.DynamicValueAssignment;
import org.sopeco.model.configuration.measurements.MeasurementsPackage;
import org.sopeco.model.configuration.measurements.provider.DynamicValueAssignmentItemProvider;

public class DynamicValueAssignmentItemProviderExt extends
		DynamicValueAssignmentItemProvider {

	public DynamicValueAssignmentItemProviderExt(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}
	
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
		
		final DynamicValueAssignment dynamicValueAssignment = (DynamicValueAssignment)object;
		
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
	}
	
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(DynamicValueAssignment.class)) {
			case MeasurementsPackage.DYNAMIC_VALUE_ASSIGNMENT__NAME:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, true));
				return;
		}
		super.notifyChanged(notification);
	}


}

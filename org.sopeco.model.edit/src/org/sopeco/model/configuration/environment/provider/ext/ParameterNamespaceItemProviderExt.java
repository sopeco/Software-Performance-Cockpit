package org.sopeco.model.configuration.environment.provider.ext;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.sopeco.model.configuration.environment.EnvironmentFactory;
import org.sopeco.model.configuration.environment.EnvironmentPackage;
import org.sopeco.model.configuration.environment.ParameterNamespace;
import org.sopeco.model.configuration.environment.provider.ParameterNamespaceItemProvider;

public class ParameterNamespaceItemProviderExt extends
		ParameterNamespaceItemProvider {

	public ParameterNamespaceItemProviderExt(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to
	 * update any cached children and by creating a viewer notification, which
	 * it passes to {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(ParameterNamespace.class)) {
		case EnvironmentPackage.PARAMETER_NAMESPACE__NAME:
			fireNotifyChanged(new ViewerNotification(notification,
					notification.getNotifier(), true, true));
			return;
		case EnvironmentPackage.PARAMETER_NAMESPACE__CHILDREN:
		case EnvironmentPackage.PARAMETER_NAMESPACE__PARAMETERS:
			fireNotifyChanged(new ViewerNotification(notification,
					notification.getNotifier(), true, false));
			return;
		}
		super.notifyChanged(notification);
	}

	@Override
	protected void collectNewChildDescriptors(
			Collection<Object> newChildDescriptors, Object object) {

		EnvironmentFactory factory = (EnvironmentFactory) EnvironmentPackage.eINSTANCE
				.getEFactoryInstance();

		newChildDescriptors.add(createChildParameter(
				EnvironmentPackage.Literals.PARAMETER_NAMESPACE__CHILDREN,
				factory.createParameterNamespace()));

		newChildDescriptors.add(createChildParameter(
				EnvironmentPackage.Literals.PARAMETER_NAMESPACE__PARAMETERS,
				factory.createParameterDefinition()));
	}

}

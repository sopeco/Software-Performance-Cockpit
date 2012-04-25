/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.measurements.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.model.configuration.measurements.MeasurementsFactory;
import org.sopeco.model.configuration.measurements.MeasurementsPackage;

import org.sopeco.model.configuration.provider.SoPeCoConfigurationEditPlugin;

/**
 * This is the item provider adapter for a {@link org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ExperimentSeriesDefinitionItemProvider
	extends ItemProviderAdapter
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExperimentSeriesDefinitionItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addNamePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ExperimentSeriesDefinition_name_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ExperimentSeriesDefinition_name_feature", "_UI_ExperimentSeriesDefinition_type"),
				 MeasurementsPackage.Literals.EXPERIMENT_SERIES_DEFINITION__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(MeasurementsPackage.Literals.EXPERIMENT_SERIES_DEFINITION__EXPLORATION_STRATEGY);
			childrenFeatures.add(MeasurementsPackage.Literals.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_ASSIGNMENTS);
			childrenFeatures.add(MeasurementsPackage.Literals.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_TERMINATION_CONDITION);
			childrenFeatures.add(MeasurementsPackage.Literals.EXPERIMENT_SERIES_DEFINITION__PREPERATION_ASSIGNMENTS);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns ExperimentSeriesDefinition.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ExperimentSeriesDefinition"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((ExperimentSeriesDefinition)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_ExperimentSeriesDefinition_type") :
			getString("_UI_ExperimentSeriesDefinition_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(ExperimentSeriesDefinition.class)) {
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__NAME:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPLORATION_STRATEGY:
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_ASSIGNMENTS:
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_TERMINATION_CONDITION:
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__PREPERATION_ASSIGNMENTS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(MeasurementsPackage.Literals.EXPERIMENT_SERIES_DEFINITION__EXPLORATION_STRATEGY,
				 MeasurementsFactory.eINSTANCE.createExplorationStrategy()));

		newChildDescriptors.add
			(createChildParameter
				(MeasurementsPackage.Literals.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_ASSIGNMENTS,
				 MeasurementsFactory.eINSTANCE.createConstantValueAssignment()));

		newChildDescriptors.add
			(createChildParameter
				(MeasurementsPackage.Literals.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_ASSIGNMENTS,
				 MeasurementsFactory.eINSTANCE.createDynamicValueAssignment()));

		newChildDescriptors.add
			(createChildParameter
				(MeasurementsPackage.Literals.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_TERMINATION_CONDITION,
				 MeasurementsFactory.eINSTANCE.createNumberOfRepetitions()));

		newChildDescriptors.add
			(createChildParameter
				(MeasurementsPackage.Literals.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_TERMINATION_CONDITION,
				 MeasurementsFactory.eINSTANCE.createTimeOut()));

		newChildDescriptors.add
			(createChildParameter
				(MeasurementsPackage.Literals.EXPERIMENT_SERIES_DEFINITION__PREPERATION_ASSIGNMENTS,
				 MeasurementsFactory.eINSTANCE.createConstantValueAssignment()));
	}

	/**
	 * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
		Object childFeature = feature;
		Object childObject = child;

		boolean qualify =
			childFeature == MeasurementsPackage.Literals.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_ASSIGNMENTS ||
			childFeature == MeasurementsPackage.Literals.EXPERIMENT_SERIES_DEFINITION__PREPERATION_ASSIGNMENTS;

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return SoPeCoConfigurationEditPlugin.INSTANCE;
	}

}

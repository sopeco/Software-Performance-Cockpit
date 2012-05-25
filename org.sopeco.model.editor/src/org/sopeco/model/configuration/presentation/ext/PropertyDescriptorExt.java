package org.sopeco.model.configuration.presentation.ext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.ui.celleditor.ExtendedComboBoxCellEditor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.sopeco.engine.experimentseries.IExplorationStrategyExtension;
import org.sopeco.engine.experimentseries.IParameterVariationExtension;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.engine.registry.Extensions;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.environment.ParameterRole;
import org.sopeco.model.configuration.measurements.ConstantValueAssignment;
import org.sopeco.model.configuration.measurements.DynamicValueAssignment;
import org.sopeco.model.configuration.measurements.ExplorationStrategy;
import org.sopeco.util.Tools;

/**
 * 
 * @author C5170538
 * 
 */
public class PropertyDescriptorExt extends PropertyDescriptor {

	public PropertyDescriptorExt(Object object,
			IItemPropertyDescriptor itemPropertyDescriptor) {
		super(object, itemPropertyDescriptor);
	}

	@Override
	public CellEditor createPropertyEditor(Composite composite) {

		if (object instanceof ExplorationStrategy) {
			return createExplorationStrategyEditor(composite);
		}

		if (object instanceof ParameterDefinition) {

			return createParameterDefinitionEditor(composite);
		}
		if (object instanceof DynamicValueAssignment) {

			return createDynamicValueAssignmentEditor(composite);
		}
		if (object instanceof ConstantValueAssignment) {

			return createConstantValueAssignmentEditor(composite);
		}

		return super.createPropertyEditor(composite);
	}

	private CellEditor createConstantValueAssignmentEditor(Composite composite) {
		EStructuralFeature feature = (EStructuralFeature) itemPropertyDescriptor
				.getFeature(this.object);

		if (feature.getName() == "parameter") {
			return createParameterFeatureEditor(composite, feature);
		}
		return super.createPropertyEditor(composite);
	}

	/**
	 * Overrides default property editor and sets choice of values only to
	 * registered '
	 * {@link org.sopeco.engine.experimentseries.IParameterVariation
	 * <em>ParameterVariations</em>}'.
	 * 
	 * @param composite
	 * @return
	 */
	private CellEditor createDynamicValueAssignmentEditor(Composite composite) {
		EStructuralFeature feature = (EStructuralFeature) itemPropertyDescriptor
				.getFeature(this.object);

		if (feature.getName() == "name") {
			IExtensionRegistry registry = ExtensionRegistry.getSingleton();

			List<String> parameterVariations = new ArrayList<String>();

			Extensions<IParameterVariationExtension> ext = registry
					.getExtensions(IParameterVariationExtension.class);

			for (IParameterVariationExtension parameterVariation : ext
					.getList()) {
				parameterVariations.add(parameterVariation.getName());
			}

			ExtendedComboBoxCellEditor result = new ExtendedComboBoxCellEditor(
					composite, parameterVariations, getEditLabelProvider(),
					true);

			return result;
		}

		if (feature.getName() == "parameter") {
			return createParameterFeatureEditor(composite, feature);
		}
		return super.createPropertyEditor(composite);
	}

	/**
	 * Allows only parameters with '
	 * {@link org.sopeco.model.configuration.environment.ParameterDefinition#getRole
	 * <em>Role</em>}' INPUT as choice of value.
	 * 
	 * @param composite
	 * @param feature
	 * @return
	 */
	private CellEditor createParameterFeatureEditor(Composite composite,
			EStructuralFeature feature) {
		EClassifier eType = feature.getEType();
		Collection<?> choiceOfValues = itemPropertyDescriptor
				.getChoiceOfValues(object);
		ArrayList<ParameterDefinition> validChoices = new ArrayList<ParameterDefinition>();

		for (Object choice : choiceOfValues) {
			if (!eType.isInstance(choice)) {
				continue;
			}
			if (choice instanceof ParameterDefinition) {
				ParameterDefinition parameterDefinition = (ParameterDefinition) choice;
				if (parameterDefinition.getRole() == ParameterRole.INPUT) {
					validChoices.add(parameterDefinition);
				}
			}
		}
		CellEditor result = new ExtendedComboBoxCellEditor(composite,
				validChoices, getEditLabelProvider(),
				itemPropertyDescriptor.isSortChoices(object));
		return result;
	}

	/**
	 * Overrides default property editor and sets choice of values only to '
	 * {@link org.sopeco.utils.SupportedTypes <em>SupportedTypes</em>}'.
	 * 
	 * @param composite
	 * @return
	 */
	private CellEditor createParameterDefinitionEditor(Composite composite) {
		EAttribute feature = (EAttribute) itemPropertyDescriptor
				.getFeature(this.object);

		if (feature.getName() == "type") {
			CellEditor result = new ExtendedComboBoxCellEditor(composite,
					Tools.SupportedTypes.asList(), getEditLabelProvider(), true);
			return result;
		}
		return super.createPropertyEditor(composite);
	}

	/**
	 * Overrides default property editor and sets choice of values only to
	 * registered '
	 * {@link org.sopeco.model.configuration.measurements.ExplorationStrategy
	 * <em>ExplorationStrategies</em>}'.
	 * 
	 * @param composite
	 * @return
	 */
	private CellEditor createExplorationStrategyEditor(Composite composite) {
		IExtensionRegistry registry = ExtensionRegistry.getSingleton();
		Extensions<IExplorationStrategyExtension> ext = registry
				.getExtensions(IExplorationStrategyExtension.class);

		List<String> explorationStrategies = new ArrayList<String>();

		for (IExplorationStrategyExtension explorationStrategy : ext.getList()) {
			explorationStrategies.add(explorationStrategy.getName());
		}

		ExtendedComboBoxCellEditor result = new ExtendedComboBoxCellEditor(
				composite, explorationStrategies, getEditLabelProvider(), true);

		return result;
	}

}

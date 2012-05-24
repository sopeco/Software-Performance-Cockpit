package org.sopeco.model.configuration.presentation.ext;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.ui.celleditor.ExtendedComboBoxCellEditor;
import org.eclipse.emf.ecore.EAttribute;
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
import org.sopeco.model.configuration.measurements.DynamicValueAssignment;
import org.sopeco.model.configuration.measurements.ExplorationStrategy;
import org.sopeco.util.Tools;

public class PropertyDescriptorExt extends PropertyDescriptor {

	public PropertyDescriptorExt(Object object,
			IItemPropertyDescriptor itemPropertyDescriptor) {
		super(object, itemPropertyDescriptor);
	}

	@Override
	public CellEditor createPropertyEditor(Composite composite) {

		if (object instanceof ExplorationStrategy) {
			IExtensionRegistry registry = ExtensionRegistry.getSingleton();
			Extensions<IExplorationStrategyExtension> ext = registry
					.getExtensions(IExplorationStrategyExtension.class);

			List<String> explorationStrategies = new ArrayList<String>();

			for (IExplorationStrategyExtension explorationStrategy : ext
					.getList()) {
				explorationStrategies.add(explorationStrategy.getName());
			}

			ExtendedComboBoxCellEditor result = new ExtendedComboBoxCellEditor(
					composite, explorationStrategies, getEditLabelProvider(),
					true);

			return result;
		}

		if (object instanceof ParameterDefinition) {

			EAttribute feature = (EAttribute) itemPropertyDescriptor
					.getFeature(this.object);

			if (feature.getName() == "type") {
				CellEditor result = new ExtendedComboBoxCellEditor(composite,
						Tools.SupportedTypes.asList(), getEditLabelProvider(),
						true);
				return result;
			}
		}
		if (object instanceof DynamicValueAssignment) {
			
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
		}
		
		

		return super.createPropertyEditor(composite);
	}
}

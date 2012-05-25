package org.sopeco.visualisation.eclipse.dialogs;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Label provider for parameter selection. This provider is needed, in order to
 * acertain that in parameter selection lists only the parameter name is shown
 * but not the whole toString text.
 * 
 * @author D054009
 * 
 */
public class ParameterLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
		// Not needed

	}

	@Override
	public void dispose() {
		// Not needed

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// Not needed
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// Not needed

	}

	@Override
	public Image getImage(Object element) {
		// Not needed
		return null;
	}

	/**
	 * returns the parameter name
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof ParameterDefinition) {
			ParameterDefinition parameter = (ParameterDefinition) element;

			return parameter.getName() + " (" + parameter.getNamespace().getFullName() + ")";
		}
		return element.toString();
	}

}

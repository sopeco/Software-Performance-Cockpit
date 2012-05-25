package org.sopeco.visualisation.eclipse.dialogs;

import java.util.Collection;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

public class ParameterSelection {
	public static ParameterDefinition selectParameter(String promptText, Collection<ParameterDefinition> parameters) {
		if (parameters.size() <= 0) {
			throw new RuntimeException("Cannot select from zero parameters!");
		}
		if (parameters.size() == 1) {
			return (ParameterDefinition) parameters.toArray()[0];
		}
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		ElementListSelectionDialog selectionDialog = new ElementListSelectionDialog(shell, new ParameterLabelProvider());
		return selectParameter(promptText, parameters, selectionDialog);
	}
	
	public static ParameterDefinition selectParameterWithIgnoreOption(String promptText, Collection<ParameterDefinition> parameters) {
		if (parameters.size() <= 0) {
			throw new RuntimeException("Cannot select from zero parameters!");
		}
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		ElementSelectionWithIgnoreDialog selectionDialog = new ElementSelectionWithIgnoreDialog(shell, new ParameterLabelProvider());
		return selectParameter(promptText, parameters, selectionDialog);
	}
	
	private static ParameterDefinition selectParameter(String promptText, Collection<ParameterDefinition> parameters, ElementListSelectionDialog selectionDialog){
		
		selectionDialog.setMultipleSelection(false);
		selectionDialog.setMessage(promptText);
		selectionDialog.setElements(parameters.toArray());
		int status = selectionDialog.open();
		if (status == ElementListSelectionDialog.OK) {
			return (ParameterDefinition) selectionDialog.getResult()[0];
		}

		return null;
	}
}

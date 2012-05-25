package org.sopeco.visualisation.eclipse.dialogs;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

public class ElementSelectionWithIgnoreDialog extends ElementListSelectionDialog {
	public static int IGNORE = 2;
	public static int IGNORE_ALL = 3;

	public ElementSelectionWithIgnoreDialog(Shell parent, ILabelProvider renderer) {
		super(parent, renderer);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);

		createButton(parent, IGNORE, "Ignore", false);
		createButton(parent, IGNORE_ALL, "Ignore All", false);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		if (buttonId == IGNORE || buttonId == IGNORE_ALL) {
			setReturnCode(buttonId);
			close();
		}
	}
}

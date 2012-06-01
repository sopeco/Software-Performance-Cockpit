package org.sopeco.visualisation.eclipse.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

public class ValueAssignmentsDialog extends Dialog {

	public class IgnoreValue {
		@Override
		public String toString() {

			return "Ignore";
		}
	}

	private String message;
	private boolean returnStatusOk;
	private Map<ParameterDefinition, Collection<Object>> parameterValueOptions;
	private Map<ParameterDefinition, Object> result;

	public ValueAssignmentsDialog(Shell parent, String title, String message, Map<ParameterDefinition, Collection<Object>> parameterValueOptions) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

		setText(title);
		setMessage(message);
		this.parameterValueOptions = parameterValueOptions;
		initResult();
	}

	private void initResult() {
		result = new HashMap<ParameterDefinition, Object>();
	}

	/**
	 * Gets the message
	 * 
	 * @return String
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message
	 * 
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Opens the dialog and returns the input
	 * 
	 * @return String
	 */
	public boolean open() {
		// Create the dialog window
		Shell shell = new Shell(getParent(), getStyle());
		shell.setText(getText());
		createContents(shell);
		shell.pack();
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		// Return the entered value, or null
		return returnStatusOk;
	}

	/**
	 * Creates the dialog's contents
	 * 
	 * @param shell
	 *            the dialog window
	 */
	private void createContents(final Shell shell) {
		shell.setLayout(new GridLayout(2, false));

		showMessage(shell);

		for (final ParameterDefinition parameter : parameterValueOptions.keySet()) {
			Label label = new Label(shell, SWT.NONE);
			label.setText(parameter.getFullName());

			List<Object> values = new ArrayList<Object>(parameterValueOptions.get(parameter));
			IgnoreValue ignoreObject = new IgnoreValue();
			values.add(ignoreObject);
			ComboViewer comboViewer = new ComboViewer(shell);
			comboViewer.add(values.toArray());
			comboViewer.setSelection(new StructuredSelection(ignoreObject));
			comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					StructuredSelection selection = (StructuredSelection) event.getSelection();
					if (selection.getFirstElement() instanceof IgnoreValue) {
						result.remove(parameter);
					} else {
						result.put(parameter, selection.getFirstElement());
					}

				}
			});
		}

		Button ok = createOkButton(shell);
		createCancelButton(shell);
		shell.setDefaultButton(ok);
	}

	private void createCancelButton(final Shell shell) {
		Button cancel = new Button(shell, SWT.PUSH);
		cancel.setText("Cancel");
		GridData data = new GridData(GridData.CENTER);
		cancel.setLayoutData(data);
		cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				returnStatusOk = false;
				shell.close();
			}
		});
	}

	private Button createOkButton(final Shell shell) {
		Button ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		GridData data = new GridData(GridData.CENTER);
		ok.setLayoutData(data);
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				returnStatusOk = true;
				shell.close();
			}
		});
		return ok;
	}

	private void showMessage(final Shell shell) {
		// Show the message
		Label label = new Label(shell, SWT.NONE);
		label.setText(message);
		GridData data = new GridData();
		data.horizontalSpan = 2;
		label.setLayoutData(data);
	}

	public Map<ParameterDefinition, Object> getResult() {
		return result;
	}

}

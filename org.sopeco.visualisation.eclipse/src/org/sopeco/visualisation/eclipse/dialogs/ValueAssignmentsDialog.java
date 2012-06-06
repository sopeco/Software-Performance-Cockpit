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

	public enum FurtherOptions {
		Ignore, Compare
	}

	private String message;
	private boolean returnStatusOk;
	private Map<ParameterDefinition, Collection<Object>> parameterValueOptions;
	private Map<ParameterDefinition, Object> result;
	private List<ComboViewer> comboBoxes;
	private FurtherOptions ignoreValue = FurtherOptions.Ignore;
	private FurtherOptions compareValue = FurtherOptions.Compare;
	private ParameterDefinition compareParameter = null;

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
		comboBoxes = new ArrayList<ComboViewer>();
		for (final ParameterDefinition parameter : parameterValueOptions.keySet()) {
			Label label = new Label(shell, SWT.NONE);
			label.setText(parameter.getFullName());

			List<Object> values = new ArrayList<Object>(parameterValueOptions.get(parameter));

			values.add(ignoreValue);
			values.add(compareValue);
			final ComboViewer comboViewer = new ComboViewer(shell);
			comboViewer.add(values.toArray());
			comboViewer.setSelection(new StructuredSelection(ignoreValue));
			comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				StructuredSelection prevSelection = null;

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					StructuredSelection selection = (StructuredSelection) event.getSelection();

					if (selection.getFirstElement().equals(compareValue)) {
						compareParameter = parameter;
						removeCompareOptions(comboViewer);
					} else if (selection.getFirstElement().equals(ignoreValue)) {
						result.remove(parameter);
					} else {
						result.put(parameter, selection.getFirstElement());
					}
					if (prevSelection != null && prevSelection.getFirstElement().equals(compareValue)) {
						compareParameter = null;
						addCompareOptions(comboViewer);
					}

					prevSelection = selection;

				}
			});
			comboBoxes.add(comboViewer);
		}

		Button ok = createOkButton(shell);
		createCancelButton(shell);
		shell.setDefaultButton(ok);
	}

	private void removeCompareOptions(ComboViewer exceptFor) {
		for (ComboViewer combo : comboBoxes) {
			if (combo != exceptFor) {
				combo.remove(compareValue);
			}
		}
	}

	private void addCompareOptions(ComboViewer exceptFor) {
		for (ComboViewer combo : comboBoxes) {
			if (combo != exceptFor) {
				combo.add(compareValue);
			}
		}
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

	public Map<ParameterDefinition, Object> getValueAssignments() {
		return result;
	}
	
	public ParameterDefinition getComparisonParameter(){
		return compareParameter;
	}

}

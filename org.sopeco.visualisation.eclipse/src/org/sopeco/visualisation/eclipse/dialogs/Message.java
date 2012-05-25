package org.sopeco.visualisation.eclipse.dialogs;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.sopeco.visualisation.eclipse.VisualisationPlugin;

/**
 * This class is used to show warning messages.
 * 
 * @author Alexander Wert
 * 
 */
public class Message {

	/**
	 * show warning message.
	 * 
	 * @param title
	 *            title of the dialog
	 * @param message
	 *            text of the warning message
	 */
	public static void show(String title, String message) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		MessageDialog warningDialog = new MessageDialog(shell, title, null, message, MessageDialog.WARNING, new String[] { "OK" }, 0);

		warningDialog.open();
	}

	/**
	 * show warning message.
	 * 
	 * @param title
	 *            title of the dialog
	 * @param message
	 *            text of the warning message
	 */
	public static boolean question(String title, String message) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		return MessageDialog.openQuestion(shell, title, message);
	}

	/**
	 * show error message with details
	 * 
	 * @param title
	 *            title of the dialog
	 * @param message
	 *            text of the warning message
	 */
	public static void showError(String title, String message, Exception exception) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		String PluginID = VisualisationPlugin.PLUGIN_ID;
		String reason = exception.toString();
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		exception.printStackTrace(out);
		String details = writer.toString();
		MultiStatus status = new MultiStatus(PluginID, 1, reason, null);
		for (String line : details.split(System.getProperty("line.separator"))) {
			if (line.startsWith("\t")) {
				line = "     " + line;
			}
			status.add(new Status(IStatus.ERROR, PluginID, line));
		}

		ErrorDialog.openError(shell, title, message, status);
	}
}

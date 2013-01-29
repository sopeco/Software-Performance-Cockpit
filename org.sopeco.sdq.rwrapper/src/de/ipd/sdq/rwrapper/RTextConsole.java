package de.ipd.sdq.rwrapper;

import java.awt.FileDialog;
import java.awt.Frame;

import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

/**
 * Text console adapter for R engines. This text console adapter is used to log
 * inputs, outputs and the history for a corresponding R engine.
 * 
 * @author groenda
 */
public class RTextConsole implements RMainLoopCallbacks {

	/** The current output on the console. */
	private String consoleOutput;
	/** The history of the output on the text console. */
	private String consoleOutputHistory;
	/** Content of the last message issued to the R engine. */
	private String lastMessage;

	/**
	 * Construct a new console.
	 */
	public RTextConsole() {
		consoleOutput = "";
		consoleOutputHistory = "";
		lastMessage = "";
	}

	/**
	 * Used if text is written to the console.
	 * 
	 * @param re
	 *            Connected R engine.
	 * @param text
	 *            Console output.
	 */
	public void rWriteConsole(final Rengine re, final String text) {
		consoleOutput += text + "\n";
	}

	/**
	 * {@inheritDoc}
	 */
	public void rBusy(final Rengine re, final int which) {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	public String rReadConsole(final Rengine re, final String prompt, final int addToHistory) {
		// Nothing to do
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void rShowMessage(final Rengine re, final String message) {
		// Nothing to do
		consoleOutput += message + "\n";
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	public String rChooseFile(final Rengine re, final int newFile) {
		FileDialog fd = new FileDialog(new Frame(), (newFile == 0) ? "Select a file"
				: "Select a new file", (newFile == 0) ? FileDialog.LOAD : FileDialog.SAVE);
		fd.show();
		String res = null;
		if (fd.getDirectory() != null) {
			res = fd.getDirectory();
		}
		if (fd.getFile() != null) {
			res = (res == null) ? fd.getFile() : (res + fd.getFile());
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public void rFlushConsole(final Rengine re) {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	public void rLoadHistory(final Rengine re, final String filename) {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	public void rSaveHistory(final Rengine re, final String filename) {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	public void rWriteConsole(final Rengine arg0, final String message, final int arg2) {
		lastMessage = message;
		consoleOutputHistory += lastMessage;
	}

	/**
	 * Returns the last message displayed on the R console.
	 * 
	 * @return the last message on the R console.
	 */
	public String getLastMessage() {
		return lastMessage;
	}

	public String getConsoleOutputHistory() {
		return this.consoleOutputHistory;
	}
}

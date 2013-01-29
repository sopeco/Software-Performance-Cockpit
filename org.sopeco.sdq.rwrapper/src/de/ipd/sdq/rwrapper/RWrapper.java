package de.ipd.sdq.rwrapper;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Usage description: See test cases
 * 
 * @author kelsaka
 * 
 */
public class RWrapper {

	/** The logger used by this class. */
	private static Logger logger = LoggerFactory.getLogger(RWrapper.class.getName());

	private REXP rExpression;

	private Rengine rEngine;

	/** The text console of the connected R engine. */
	private static RTextConsole rConsole = new RTextConsole();

	/**
	 * Initialize Variables
	 * 
	 * @param varPrefix
	 *            prefix of each variable; e.g. 'X'
	 * @param variablesValue
	 *            The value of the variable
	 */
	public void initVariables(String varPrefix, double[] variablesValue) {
		if (init()) {
			for (int i = 0; i < variablesValue.length; i++) {
				rExpression = rEngine.eval(varPrefix + i + " = " + variablesValue[i]);
				logger.debug(rExpression.toString());
			}
		} else {
			throw new RuntimeException("R not running");
		}
	}

	public String executeRCommandString(String rCommand) {
//		logger.debug("Executing RCommand '{}'.", rCommand);
		System.out.println(rCommand);
		if (init()) {
			String rResults = "";

			// execute the R command itself:
			REXP rExpression = rEngine.eval(rCommand);

			if (rExpression != null) {
				switch (rExpression.getType()) {
				case 34: // String
					rResults = "String: " + rExpression.asString() + ", SymbolName: "
							+ rExpression.asSymbolName();
					break;
				case 33: // Real
					rResults = "Real: " + rExpression.asDouble() + ", SymbolName: "
							+ rExpression.asSymbolName();
					break;
				case 16: // Vector
					/*
					 * RVector rVector = rExpression.asVector();
					 * 
					 * for (int i = 0; i<rVector.at(0).asDoubleArray().length;
					 * i++) { rResults += rVector.at(0).asDoubleArray()[i]; if
					 * (i!=rVector.at(0).asDoubleArray().length-1) { rResults +=
					 * ", "; } }
					 */
					// use the following for boxplots etc...
					rResults = "String: " + rExpression.asString() + ", SymbolName: "
							+ rExpression.asSymbolName();
					break;

				case 0:
					// no type:
					rResults = rConsole.getLastMessage();
					break;
				default:
					// unknown type:
					rResults = "RWrapper says: " + rExpression.toString() + ", SymbolName: "
							+ rExpression.asSymbolName() + ", Unknown Type: "
							+ rExpression.getType() + ", Console: " + rConsole.getLastMessage();
					logger.warn(rExpression.toString());
				}
			} else {
				rResults = "R returned NO result expression (was NULL).\n" + "rCommand: "
						+ rCommand + "\n" + "Console Output:\n" + rConsole.getLastMessage();
				String consoleHistory = "Console History:\n" + rConsole.getConsoleOutputHistory();
				logger.error(rResults + consoleHistory);
			}

			return rResults;
		}
		throw new RuntimeException("R not running");
	}

	/**
	 * For R commands with double/real return value.
	 * 
	 * @param rCommand
	 * @return
	 */
	public double executeRCommandDouble(String rCommand) {
		logger.debug("Executing RCommand '{}'.", rCommand);

		if (init()) {
			rExpression = rEngine.eval(rCommand);
			logger.debug(rExpression.toString());
			if (rExpression == null) {
				String rResults = "R returned NO result expression (was NULL).\n" + "rCommand: "
						+ rCommand + "\n" + "Console Output:\n" + rConsole.getLastMessage();
				String consoleHistory = "Console History:\n" + rConsole.getConsoleOutputHistory();
				logger.error(rResults + consoleHistory);
				throw new RuntimeException("R eval failed.");
			} else if (rExpression.getType() != 33) {
				logger.error("R Expression Type was not REAL/DOUBLE (33); Type: "
						+ rExpression.getType());
				throw new RuntimeException("R result type not expected here.");
			} else {
				return rExpression.asDouble();
			}
		}
		throw new RuntimeException("R not running");
	}

	/**
	 * For R commands with double/real array return value.
	 * 
	 * @param rCommand
	 * @return
	 */
	public double[] executeRCommandDoubleArray(String rCommand) {
		if (init()) {
			rExpression = rEngine.eval(rCommand);
			logger.debug(rExpression.toString());
			if (rExpression == null) {
				String rResults = "R returned NO result expression (was NULL).\n" + "rCommand: "
						+ rCommand + "\n" + "Console Output:\n" + rConsole.getLastMessage();
				String consoleHistory = "Console History:\n" + rConsole.getConsoleOutputHistory();
				logger.error(rResults + consoleHistory);
				throw new RuntimeException("R eval failed.");
			} else if (rExpression.getType() != 33) {
				logger.error("R Expression Type was not REAL/DOUBLE (33); Type: "
						+ rExpression.getType());
				throw new RuntimeException("R result type not expected here.");
			} else {
				return rExpression.asDoubleArray();
			}
		}
		throw new RuntimeException("R not running");
	}
	
	/**
	 * For R commands with String array return value.
	 * 
	 * @param rCommand
	 * @return a string array
	 */
	public String[] executeRCommandStringArray(String rCommand) {
		if (init()) {
			rExpression = rEngine.eval(rCommand);
			logger.debug(rExpression.toString());
			if (rExpression == null) {
				String rResults = "R returned NO result expression (was NULL).\n" + "rCommand: "
						+ rCommand + "\n" + "Console Output:\n" + rConsole.getLastMessage();
				String consoleHistory = "Console History:\n" + rConsole.getConsoleOutputHistory();
				logger.error(rResults + consoleHistory);
				throw new RuntimeException("R eval failed.");
			} else if (rExpression.getType() != 34) {
				logger.error("R Expression Type was not STRING (33); Type: "
						+ rExpression.getType());
				throw new RuntimeException("R result type not expected here.");
			} else {
				return rExpression.asStringArray();
			}
		}
		throw new RuntimeException("R not running");
	}
	
	/**
	 * Call after using R engine to shutdown.
	 */
	public void shutdown() {
		if (this.rEngine != null)
			this.rEngine.end();
		this.rEngine = null;
		logger.debug("R end");
	}

	public RTextConsole getConsole() {
		return rConsole;
	}

	private boolean init() {
		if (rEngine == null) {
			// just making sure we have the right version of everything
			if (!Rengine.versionCheck()) {
				logger.error("** Version mismatch - Java files don't match library version.");
				logger.error("use R 2.8.1");
				return false;
			}

			logger.info("Creating Rengine");
			this.rEngine = new Rengine(new String[] {}, false, rConsole);
			logger.info("Rengine created, waiting for R");
			// the engine creates R is a new thread, so we should wait until
			// it's ready
			if (!rEngine.waitForR()) {
				logger.error("Cannot load R");
				return false;
			}
		}
		return true;
	}

	/**
	 * Removes all objects from R's memory.
	 */
	public void deleteAllObjects() {
		executeRCommandString("rm(list = ls())");
	}
	
}

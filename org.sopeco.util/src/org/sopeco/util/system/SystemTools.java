/**
 * Project: SoPeCo
 * 
 * (c) 2011 Roozbeh Farahbod, roozbeh.farahbod@sap.com
 * 
 */
package org.sopeco.util.system;

import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OS-related functionalities. 
 * 
 * @author Roozbeh Farahbod
 *
 */
public class SystemTools {

	private static final String KILLSERVICE_CMD_WINDOWS = "net stop "; 

	private static Logger logger = LoggerFactory.getLogger(SystemTools.class);
	
	/**
	 * @return  the thread instance of Sigar controller
	 */
	public static SigarController getSigarController() {
		return SigarController.getThreadInstance();
	}
	
	/**
	 * @return the name of the operating system
	 */
	public static String getOSName() {
		return System.getProperty("os.name");
	}
	
	/**
	 * @return true if the OS is Mac.
	 */
	public static boolean isMacOS() {
		return System.getProperty("os.name").toLowerCase().startsWith("mac");
	}
	
	/**
	 * @return trus if the OS is Windows.
	 */
	public static boolean isWindowsOS() {
		return System.getProperty("os.name").toLowerCase().startsWith("windows");
	}

	/**
	 * @return trus if the OS is Unix or Linux.
	 */
	public static boolean isUnix() {
		final String name = System.getProperty("os.name").toLowerCase();
		return name.contains("nix") || name.contains("nux");
	}

	/**
	 * Returns true if there is a process with the exact given name running.
	 * 
	 * @param processName process name
	 * @return true if there is a process running with the given name.
	 */
	public static boolean isProcessRunning(String processName) throws ProcessInfoException {
		try {
			getSigarController().getProcessPId(processName);
			return true;
		} catch (ProcessNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * Returns a set of process ids for the given process name.
	 * 
	 * @param processName process name
	 * @return the set of process ids; empty if there is no such process.
	 * @throws ProcessInfoException if there is a problem with looking up the process name.
	 */
	public static Set<Long> getProcessPIds(String processName) throws ProcessInfoException {
		return getSigarController().getProcessPIds(processName);
	}

	/**
	 * Kills the given process with a KILL (9) signal. 
	 * @param pid process id
	 * @throws SystemToolsException if it fails.
	 */
	public static void killProcess(long pid) throws SystemToolsException {
		getSigarController().killProcess(pid, 9);
	}

	/**
	 * Shuts down the process with a QUIT (3) signal.
	 * 
	 * @param pid process id
	 * @throws SystemToolsException if it fails.
	 */
	public static void shutdownProcess(long pid) throws SystemToolsException {
		getSigarController().killProcess(pid, 3);
	}

	/**
	 * Shutdowns the service identified with the given name.
	 * 
	 * @param serviceName the name of the service
	 * @throws SystemToolsException if shutdown fails.
	 */
	public static void shutdownService(String serviceName) throws SystemToolsException {
		if (SystemTools.isWindowsOS()) {
			try {
				Runtime.getRuntime().exec(KILLSERVICE_CMD_WINDOWS + serviceName);
			} catch (IOException e) {
				throw new SystemToolsException("Could not shutdown " + serviceName + ".", e, logger);
			}    
		} else
			logger.warn("Process is running on a non-Windows OS. Shutting down services is not implemented for this OS.");
			// TODO Implement shutdown for other OSs
	}

}

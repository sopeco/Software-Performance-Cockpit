/**
 * Project: SoPeCo
 * 
 * (c) 2011 Roozbeh Farahbod, roozbeh.farahbod@sap.com
 * 
 */
package org.sopeco.util.system;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.util.Tools;

/**
 * OS-related functionalities. 
 * 
 * @author Roozbeh Farahbod
 *
 */
public class SystemTools {

	private static final String JAVA_LIBRARY_PATH = "java.library.path";

	private static final String NATIVE_LIBS_LIST_FILENAME = "native/nativeLibs.list";

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

	public static void loadNativeLibraries() {
		final String tempDir = System.getProperty("java.io.tmpdir");

		System.out.println(tempDir);
		
		final String tempLibDir = tempDir + "sopecoLib" + File.separator;
		
		// create a temp lib directory
		File tempLibDirFile = new File(tempLibDir);
		tempLibDirFile.mkdir();

		logger.debug("Copying native libraries to {}.", tempLibDir);
		
		try {
			Set<String> nativeFiles = new HashSet<String>();
			Enumeration<URL> urls = ClassLoader.getSystemResources(NATIVE_LIBS_LIST_FILENAME);
			while (urls.hasMoreElements()) {
				final URL url = urls.nextElement();
				logger.debug("Loading native library file names from {}...", url);
				nativeFiles.addAll(Tools.readLines(url));
			}
			
			for (String fileName: nativeFiles) {
				final String fileNameTrimmed = fileName.trim();
				logger.debug("Copying native library {}...", fileNameTrimmed);
				if (fileNameTrimmed.length() > 0) 
					copyLibToTemp(fileName, tempLibDir);
				
			}

			setLibraryPath(tempLibDir);
		
			logger.debug("The value of '{}' is {}.", JAVA_LIBRARY_PATH, System.getProperty(JAVA_LIBRARY_PATH));
			
		} catch (Exception e) {
			e.printStackTrace();
		}  

	}

	/**
	* Copies the given native library file from the classpath to a temp library directory.
	* 
	* @param fileName library file name
	* @param libDir the destination folder
	* @return full path of the copied file
	* @throws IOException thrown in case of any IO Exception (obviously!)
	*/
	private static String copyLibToTemp(String fileName, String libDir) throws IOException {
		InputStream in = new BufferedInputStream(ClassLoader.getSystemResourceAsStream("native/" + fileName));
		final String targetFile = libDir + fileName;
		FileUtils.copyInputStreamToFile(in, new File(targetFile));
		return targetFile;
	}

	/**
	* Resets the Java library path to a new value. 
	* 
	* @param path the new path
	* @throws Exception
	*/
	public static void setLibraryPath(String path) throws Exception {
	   System.setProperty(JAVA_LIBRARY_PATH, path);
	 
	   //set sys_paths to null
	   final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
	   sysPathsField.setAccessible(true);
	   sysPathsField.set(null, null);
	}

}


/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * Project: SoPeCo
 * 
 * (c) 2011 Roozbeh Farahbod, roozbeh.farahbod@sap.com
 * 
 */
package org.sopeco.util.system;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

	private static final String NATIVE_SUBFOLDER = "native";

	private static final String JAVA_LIBRARY_PATH = "java.library.path";

	private static final String KILLSERVICE_CMD_WINDOWS = "net stop "; 

	private static Logger logger = LoggerFactory.getLogger(SystemTools.class);
	
	private static boolean nativeLibrariesLoaded = false;

	private static String systemTempDir = null;
	
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

	/**
	 * Unpacks all native libraries in the JAR files into a temp folder and explicitly appends them to the Java native library path.
	 * 
	 * The native library files are assumed to be under {@value SystemTools#NATIVE_SUBFOLDER} directories in the classpath.
	 * If this is done once, it will not do it again. Also, see {@link #appendLibraryPath(String)}.   
	 *  
	 */
	public static void loadNativeLibraries() {
		if (nativeLibrariesLoaded) {
			logger.warn("Native libraries are already loaded. Nothing done.");
			return;
		}

		try {
			String tempLibDir = extractFilesFromClasspath(NATIVE_SUBFOLDER, "sopecoLibs", "native libraries", SystemTools.class.getClassLoader());
			
			appendLibraryPath(tempLibDir);
			
			nativeLibrariesLoaded = true;
			
			logger.debug("The value of '{}' is {}.", JAVA_LIBRARY_PATH, System.getProperty(JAVA_LIBRARY_PATH));
			
		} catch (Exception e) {
			e.printStackTrace();
		}  

	}

	/**
	 * Returns the system temp directory.
	 * 
	 * @return the system temp directory
	 */
	public static String getSystemTempDir() {
		if (systemTempDir == null) {
			systemTempDir = FileUtils.getTempDirectoryPath();
			logger.debug("Temp direcotry is located at {}.", systemTempDir);
		}
		return systemTempDir;
	}

	
	/**
	 * Extracts files from a directory in the classpath to a temp directory (with a time stamp) and returns the File instance of the destination directory. 
	 *
	 * @param srcDirName a directory name in the classpath
	 * @param destName the name of the destination folder in the temp folder
	 * @param fileType a string describing the file types, if a log message is needed
	 * 
	 * @return the name of the target directory
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 * 
	 * @see #extractFilesFromClasspath(String, String, String, boolean)
	 */
	public static String extractFilesFromClasspath(String srcDirName, String destName, String fileType, ClassLoader classLoader) throws IOException, URISyntaxException {
		return extractFilesFromClasspath(srcDirName, destName, fileType, classLoader, true);
	}

	/**
	 * Extracts files from a directory in the classpath to a temp directory and returns the File instance of the destination directory. 
	 *
	 * @param srcDirName a directory name in the classpath
	 * @param destName the name of the destination folder in the temp folder
	 * @param fileType a string describing the file types, if a log message is needed
	 * @param timeStamp if <code>true</code>, it will add a time stamp to the name of the target directory (recommended)
	 * 
	 * @return the name of the target directory
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String extractFilesFromClasspath(String srcDirName, String destName, String fileType, ClassLoader classloader, boolean timeStamp) throws IOException, URISyntaxException {
		
		if (timeStamp) {
			// remove dots and colons from timestamp as they are not allowed for windows directory names
			String clearedTimeStamp = (Tools.getTimeStamp() + "-" + Thread.currentThread().getId()).replace('.', '_');
//			String clearedTimeStamp = (Tools.getTimeStamp()).replace('.', '_');
			clearedTimeStamp = clearedTimeStamp.replace(':', '_');
			clearedTimeStamp = clearedTimeStamp.replace(' ', '_');

			destName = destName + "_" + clearedTimeStamp;
		}
		final String targetDirName = Tools.concatFileName(getSystemTempDir(), destName);
		
		// create a temp lib directory
		File targetDirFile = new File(targetDirName);
		if (!targetDirFile.exists())
			targetDirFile.mkdir();

		logger.debug("Copying {} to {}.", fileType, targetDirName);

		Enumeration<URL> urls = classloader.getResources(srcDirName); // getSystemResources(srcDirName);

		if (urls.hasMoreElements())
			logger.debug("There are some urls for resource '{}' provided by the classloader.", srcDirName);
		else
			logger.debug("There are no urls for resource '{}' provided by the classloader.", srcDirName);
		

		while (urls.hasMoreElements()) {
			final URL url = urls.nextElement();
			if (fileType != null && fileType.trim().length() > 0)
				logger.debug("Loading {} from {}...", fileType, url);

			Iterator<File> libs = null;
			try {
				File nativeLibDir = new File(url.toURI());
				libs = FileUtils.iterateFiles(nativeLibDir, null, false);
			} catch (IllegalArgumentException e) {
				// most likely because it is within a JAR file
				final String unpackedJarDir = Tools.concatFileName(targetDirName, "temp");
				extractJARtoTemp(url, srcDirName, unpackedJarDir);
				final String unpackedNativeDir = Tools.concatFileName(unpackedJarDir, srcDirName);
				final File unpackedNativeDirFile = new File(unpackedNativeDir);
				libs = FileUtils.iterateFiles(unpackedNativeDirFile, null, false);
			}
			
			while (libs.hasNext()) {
				final File libFile = libs.next();
				logger.debug("Copying resouce file {}...", libFile.getName());
				FileUtils.copyFileToDirectory(libFile, targetDirFile);
			}
		}
		
		return targetDirName;
	}

	/**
	 * Extracts a file/folder identified by the URL that resides in the classpath, into the destiation folder. 
	 * 
	 * @param url URL of the JAR file
	 * @param dirOfInterest the name of the directory of interest
	 * @param dest destination folder
	 * 
	 * @throws IOException 
	 * @throws URISyntaxException
	 */
	public static void extractJARtoTemp(URL url, String dirOfInterest, String dest) throws IOException, URISyntaxException {
		if (url.toString().indexOf("jar:") != 0)
			throw new IllegalArgumentException("Cannot locate the JAR file.");
		
		// create a temp lib directory
		File tempJarDirFile = new File(dest);
		if (!tempJarDirFile.exists())
			tempJarDirFile.mkdir();
		else
			FileUtils.cleanDirectory(tempJarDirFile);

		String urlStr = url.getFile();
		final int endIndex = urlStr.indexOf("!");
		urlStr = urlStr.substring(0, endIndex);

		URI uri = new URI(urlStr);
		
		final File jarFile = new File(uri);

		logger.debug("Unpacking jar file {}...", jarFile.getAbsolutePath());
		
		java.util.jar.JarFile jar = new JarFile(jarFile);
		java.util.Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
		    java.util.jar.JarEntry file = (JarEntry) entries.nextElement();
		    
		    String destFileName = dest + File.separator + file.getName();
		    if (destFileName.indexOf(dirOfInterest) < 0)
		    	continue;
		    
		    logger.debug("unpacking {}...", file.getName());
		    
		    java.io.File f = new java.io.File(destFileName);
		    if (file.isDirectory()) { // if its a directory, create it
		        f.mkdir();
		        continue;
		    }
		    InputStream is = new BufferedInputStream(jar.getInputStream(file));
		    OutputStream fos = new BufferedOutputStream(new FileOutputStream(f));
		    while (is.available() > 0) {  
		        fos.write(is.read());
		    }
		    fos.close();
		    is.close();
		}	

		logger.debug("Unpacking jar file done.");

	}

	/**
	 * Appends a directory to the Java library path. 
	 * 
	 * @param path the new path
	 * @throws Exception
	 * @see {@link #setLibraryPath(String)}
	 */
	public static void appendLibraryPath(String path) throws Exception {
		setLibraryPath(System.getProperty(JAVA_LIBRARY_PATH) + File.pathSeparator + path);
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

	public static void main(String[] args) {
		loadNativeLibraries();
	}

}


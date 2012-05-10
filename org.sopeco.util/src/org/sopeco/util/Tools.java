/**
 * 
 */
package org.sopeco.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.tools.ant.DirectoryScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A collection of auxiliary helper methods.
 * 
 * Some of the methods are based on utility classes of the CoreASM project (www.coreasm.org).
 * 
 * @author Roozbeh Farahbod
 *
 */
public class Tools {

	private final static Logger logger = LoggerFactory.getLogger(Tools.class);
	
	public enum SupportedTypes {
		Double, Integer, String, Boolean;
	
		/**
		 * If the given class is supported, returns the 
		 * supported type for that class. Otherwise, returns null.
		 * 
		 * @see #valueOf(String)
		 */
		public static SupportedTypes get(Class<?> c) {
			return get(c.getSimpleName());
		}

		/**
		 * If the given class name is supported, returns the 
		 * supported type for that class. Otherwise, returns null.
		 */
		public static SupportedTypes get(String name) {
			if (Tools.strEqualName(name, Double.class.getSimpleName()))
				return Double;
			if (Tools.strEqualName(name, Integer.class.getSimpleName()))
				return Integer;
			if (Tools.strEqualName(name, String.class.getSimpleName()))
				return String;
			if (Tools.strEqualName(name, Boolean.class.getSimpleName()))
				return Boolean;

			return null;
		}

	};
	
	private static String eol;

	/**
	 * Returns the next word in the src string after the beginWith portion
	 */
	public static String nextWordAfter(String src, String beginWith) {
		final int index = src.indexOf(beginWith);
		if (index < 0)
			return "";
		
		final String rest = src.substring(index).replace(beginWith, "");
		StringTokenizer tk = new StringTokenizer(rest, " ,");
		if (tk.hasMoreTokens())
			return tk.nextToken();
		else
			return "";
	}

	/**
	 * Reads the lines of the given file.
	 * 
	 */
	public static List<String> readLines(String fileName) throws IOException {
		List<String> result = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		
		String line = "";
		
		do {
			line = reader.readLine();
			if (line != null)
				result.add(line);
		} while (line != null);
		
		return result;
	}

	/**
	 * Reads the lines of the given URL.
	 */
	public static List<String> readLines(URL url) throws IOException {
		List<String> result = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		
		String line = "";
		
		do {
			line = reader.readLine();
			if (line != null)
				result.add(line);
		} while (line != null);
		
		return result;
	}

//	/**
//	 * Returns a list of files that their names match the given pattern.
//	 * 
//	 * @param pattern filename pattern
//	 * @return array of file names
//	 */
//	public static String[] getFileNames(String baseDir, String pattern) {
//		DirectoryScanner scanner = new DirectoryScanner();
//		scanner.setIncludes(new String[]{pattern});
//		scanner.setBasedir(baseDir);
//		scanner.setCaseSensitive(false);
//		scanner.scan();
//		final String[] result = scanner.getIncludedFiles();
//		if (result == null)
//			return new String[] {};
//		else
//			return result;
//	}

	/**
	 * Writes the given content to the file.
	 */
	public static void printToFile(String content, String fileName) throws IOException  {
		final PrintWriter pw = new PrintWriter(new FileWriter(fileName));
		pw.println(content);
		pw.close();
	}

	/**
	 * Reads the content of the given stream as a string
	 * @return
	 */
	public static String readFromInputStream(InputStream is) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			
			StringBuffer content = new StringBuffer();
			String line;
			
			while ((line = reader.readLine()) != null)
				content.append(line + " ");
			
			return content.toString();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Formats a <code>double</code> value into a <code>String</code>
	 * with <i>d</i> digits after decimal point.
	 */
	public static String dFormat(double v, int d) {
		double p = Math.pow(10, d);
		return String.valueOf( Math.round(v * p) / p );
	}
	
	/**
	 * @return a system independent EOL string.
	 */
	public static String getEOL() {
		if (eol == null) {
			eol = System.getProperty("line.separator");
			if (eol == null)
				eol = "\n";
		}
		return eol;
	}

	/**
	 * Checks the equivalency of two strings ignoring lower/upper cases.
	 * 
	 * @return <code>true</code> if the two strings are equal.
	 */
	public static boolean strEqualCaseInsensitive(String a, String b) {
		return a.toLowerCase().equals(b.toLowerCase());
	}
	
	/**
	 * Checks the equivalency of two names ignoring lower/upper cases.
	 * It trims the strings before calling {@link #strEqualCaseInsensitive(String, String)}. 
	 * 
	 * @return <code>true</code> if the two names are equal.
	 */
	public static boolean strEqualName(String a, String b) {
		return strEqualCaseInsensitive(a.trim(), b.trim());
	}
	
	/**
	 * Checks if the given name is the simple name of the given class.
	 * The test is case insensitive. 
	 */
	public static boolean isClassName(String name, Class<?> c) {
		return strEqualName(name, c.getSimpleName());
	}

	/**
	 * Returns a list of files that their names match the given pattern.
	 * 
	 * @param pattern filename pattern
	 * @return array of file names
	 */
	public static String[] getFileNames(String baseDir, String pattern) {
		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setIncludes(new String[]{pattern});
		scanner.setBasedir(baseDir);
		scanner.setCaseSensitive(false);
		scanner.scan();
		final String[] result = scanner.getIncludedFiles();
		if (result == null)
			return new String[] {};
		else
			return result;
	}

	/**
	 * Returns the index of the given object in the given array if it exsists.
	 * It uses the {@link Object#equals(Object)} method.
	 * 
	 * @param obj object to look for
	 * @param array array of objects
	 * @return the index of the object or -1 if the object doesn't exist
	 */
	public static int exists(Object obj, Object[] array) {
		for (int i=0; i < array.length; i++)
			if (array[i].equals(obj))
				return i;
		return -1; 
	}
	
	/**
	 * Detects and returns the root folder of the running application.
	 */
	public static String getRootFolder() {
		return getRootFolder(null);
	}

	/**
	 * Detects and returns the root folder of the running application.
	 */
	public static String getRootFolder(Class<?> mainClass) {
		if (mainClass == null)
			mainClass = Tools.class;
		
		final String classFile = mainClass.getName().replaceAll("\\.", "/") + ".class";
		String fullPath = ClassLoader.getSystemResource(classFile).toString();
		
		try {
			fullPath = URLDecoder.decode(fullPath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.warn("Cannot find root folder. UTF-8 encoding is not supported.");
		}
		
		if (fullPath.indexOf("file:") > -1) {
			fullPath = fullPath.replaceFirst("file:", "").replaceFirst(classFile, "");
			fullPath = fullPath.substring(0, fullPath.lastIndexOf('/'));
		} 
		if (fullPath.indexOf("jar:") > -1) {
			fullPath = fullPath.replaceFirst("jar:", "").replaceFirst("!" + classFile, "");
			fullPath = fullPath.substring(0, fullPath.lastIndexOf('/'));
		}
		
		// replace the java separator with the 
		fullPath = fullPath.replace('/', File.separatorChar);
		
		// remove leading backslash
		if (fullPath.startsWith("\\")){
			fullPath = fullPath.substring(1);
		}
		
		// remove the final 'bin'
		final int binIndex = fullPath.indexOf(File.separator + "bin");
		if (binIndex == fullPath.length() - 4)
			fullPath = fullPath.substring(0, binIndex);
		
		logger.debug("Root folder is detected at {}.", fullPath);
		
		return fullPath;
	}

}

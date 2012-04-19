/**
 * 
 */
package org.sopeco.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * A collection of auxiliary helper methods.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class Tools {

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
	 * Reads and returns the last line of the given file.
	 * 
	 */
	public static String readLastLine(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			
			String line = "";
			String output = null;
			
			do {
				output = line;
				line = reader.readLine();
			} while (line != null);
			
			return output;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
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
	
	
}
/**
 * 
 */
package org.sopeco.config;

import java.util.Collection;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

/**
 * This interface has to be implemented by SoPeCo extensions that provide new
 * command line arguments.
 * 
 * @author Roozbeh Farahbod
 * 
 */
public interface ICommandLineArgumentsExtension {

	/**
	 * @return Returns a collection of command line options provided by the
	 *         extension.
	 */
	Collection<Option> getCommandLineOptions();

	/**
	 * Allows the extension to process command line options.
	 * 
	 * @param line
	 *            the command line to be processed
	 */
	void processOptions(CommandLine line);
}

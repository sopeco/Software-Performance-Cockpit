/**
 * 
 */
package org.sopeco.config;

import java.util.Collection;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

/**
 * This interface has to be implemented by SoPeCo extensions that provide new command line arguments.
 * 
 * @author Roozbeh Farahbod
 *
 */
public interface ICommandLineArgumentsExtension {

	/**
	 * Returns a collection of command line options provided by the extension.
	 */
	public Collection<Option> getCommandLineOptions();

	/**
	 * Allows the extension to process command line options. 
	 */
	public void processOptions(CommandLine line);
}

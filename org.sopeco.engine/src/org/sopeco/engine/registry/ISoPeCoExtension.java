package org.sopeco.engine.registry;

/**
 * This is root interface of all SoPeCo extensions.
 * 
 * @author Roozbeh Farahbod
 *
 */
public interface ISoPeCoExtension {
	
	/**
	 * Returns the name of the extension which is expected to be unique in the framework.
	 * 
	 * The name is expected to be specific to the extension that is 
	 * provided, for example 'MARS' or 'GP'. 
	 */
	public String getName();

}

/**
 * 
 */
package org.sopeco.engine.registry;

/**
 * The interface to extension artifacts provided by SoPeCo extensions.
 * 
 * @author Roozbeh Farahbod
 *
 */
public interface ISoPeCoExtensionArtifact {

	/**
	 * Returns the provider of this extension artifact.
	 */
	public ISoPeCoExtension getProvider();

}

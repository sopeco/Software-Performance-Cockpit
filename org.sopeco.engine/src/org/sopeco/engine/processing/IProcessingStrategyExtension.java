/**
 * 
 */
package org.sopeco.engine.processing;

import org.sopeco.engine.registry.ISoPeCoExtension;

/**
 * The interface of data processing strategy extensions.
 * 
 * @author Roozbeh Farahbod
 *
 */
public interface IProcessingStrategyExtension extends ISoPeCoExtension<IProcessingStrategy> {
	
	/**
	 * Creates a new data processing strategy provided by the extension.
	 */
	public IProcessingStrategy createExtensionArtifact();

}

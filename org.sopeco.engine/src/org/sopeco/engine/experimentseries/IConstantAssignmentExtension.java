/**
 * 
 */
package org.sopeco.engine.experimentseries;

import org.sopeco.engine.registry.ISoPeCoExtension;

/**
 * The interface of constant value assignments.
 * 
 * @author Roozbeh Farahbod
 * 
 */
public interface IConstantAssignmentExtension extends ISoPeCoExtension<IConstantAssignment> {

	/**
	 * Creates a new constant value assignment provided by the extension.
	 * 
	 * @return a new constant assignment
	 */
	 IConstantAssignment createExtensionArtifact();

}

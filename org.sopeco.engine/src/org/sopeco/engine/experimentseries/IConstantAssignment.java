/**
 * 
 */
package org.sopeco.engine.experimentseries;

import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;

/**
 * The interface for constant assignment extension artifacts.
 * 
 * @author Roozbeh Farahbod
 * 
 */
public interface IConstantAssignment extends ISoPeCoExtensionArtifact {

	/**
	 * Returns <code>true</code> if it can provide the assignment for the given
	 * configuration.
	 * 
	 * @param valueAssignment
	 *            the constant value assignment configuration
	 * 
	 * @return true, if a value van be assigned, otherwise false
	 */
	boolean canAssign(ConstantValueAssignment valueAssignment);

	/**
	 * Creates a parameter value based on the given value assignemtn.
	 * 
	 * @param valueAssignment
	 *            the constant value assignment configuration
	 * @return a new parameter value
	 */
	ParameterValue<?> createParameterValue(ConstantValueAssignment valueAssignment);
}

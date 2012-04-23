/**
 * 
 */
package org.sopeco.engine.experimentseries;

import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;
import org.sopeco.model.configuration.measurements.ConstantValueAssignment;
import org.sopeco.persistence.dataset.ParameterValue;

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
	 * @param valueAssignment the constant value assignment configuration
	 */
	public boolean canAssign(ConstantValueAssignment valueAssignment);
	
	/**
	 * Creates a parameter value based on the given value assignemtn.
	 * 
	 * @param valueAssignment the constant value assignment configuration
	 */
	public ParameterValue<?> createParameterValue(ConstantValueAssignment valueAssignment);
}

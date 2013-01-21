/**
 * 
 */
package org.sopeco.engine.util;

import java.util.Collection;

import org.sopeco.engine.experimentseries.IConstantAssignment;
import org.sopeco.engine.experimentseries.IConstantAssignmentExtension;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.persistence.util.ParameterCollectionFactory;

/**
 * A collection of utility methods needed in the context of SoPeCo experiments.
 * 
 * @author Roozbeh Farahbod
 * 
 */
public final class EngineTools {

	/**
	 * Utility class. Only static methods, thus private constructor.
	 */
	private EngineTools() {

	}

	/**
	 * Returns a collection of parameter value assignments based on the given
	 * constant value assignments.
	 * 
	 * @param constantValueAssignments
	 *            constant value assignments where to read the parameter values
	 *            from
	 * @return returns a collection of {@link ParameterValue}; never returns
	 *         null.
	 */
	public static ParameterCollection<ParameterValue<?>> getConstantParameterValues(
			Collection<ConstantValueAssignment> constantValueAssignments) {
		ParameterCollection<ParameterValue<?>> result = ParameterCollectionFactory.createParameterValueCollection();

		for (ConstantValueAssignment cva : constantValueAssignments) {
			ParameterValue<?> pv = getConstantParameterValue(cva);
			if (pv != null) {
				result.add(pv);
			} else {
				throw new RuntimeException("Parameter value is null!");
			}
		}
		return result;
	}

	/**
	 * Returns a single parameter value assignment based on the given constant
	 * value assignment.
	 * 
	 * @param constantValueAssignment
	 *            constant value assignments where to read the parameter value
	 *            from
	 * @return returns a {@link ParameterValue}. If the constant value is not
	 *         supported, returns <code>null</code>.
	 */
	public static ParameterValue<?> getConstantParameterValue(ConstantValueAssignment constantValueAssignment) {
		IConstantAssignment ca = ExtensionRegistry.getSingleton().getExtensionArtifact(
				IConstantAssignmentExtension.class, constantValueAssignment.getParameter().getType());
		if (ca.canAssign(constantValueAssignment)) {
			return ca.createParameterValue(constantValueAssignment);
		} else {
			return null;
		}
	}

}

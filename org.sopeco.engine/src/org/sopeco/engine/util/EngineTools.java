/**
 * 
 */
package org.sopeco.engine.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sopeco.engine.experimentseries.IConstantAssignment;
import org.sopeco.engine.experimentseries.IConstantAssignmentExtension;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.model.configuration.measurements.ConstantValueAssignment;
import org.sopeco.persistence.dataset.ParameterValue;

/**
 * A collection of utility methods needed in the context of SoPeCo experiments. 
 * 
 * @author Roozbeh Farahbod
 *
 */
public class EngineTools {

	/**
	 * Returns a list of parameter value assignments based on the given constant value assignments.
	 * 
	 * @param constantValueAssignments
	 * @return returns a list of {@link ParameterValue}; never returns null.
	 */
	public static List<ParameterValue<?>> getConstantParameterValues(Collection<ConstantValueAssignment> constantValueAssignments) {
		ArrayList<ParameterValue<?>> result = new ArrayList<ParameterValue<?>>();
		
		for (ConstantValueAssignment cva: constantValueAssignments) {
			ParameterValue<?> pv = getConstantParameterValue(cva);
			if (pv != null)
				result.add(pv);
			else {
				// TODO throw runtime error
			}
		}
		return result;
	}

	/**
	 * Returns a single parameter value assignment based on the given constant value assignment.
	 * 
	 * @param constantValueAssignment
	 * @return returns a {@link ParameterValue}. If the constant value is not supported, returns <code>null</code>.
	 */
	public static ParameterValue<?> getConstantParameterValue(ConstantValueAssignment constantValueAssignment) {
		IConstantAssignment ca = ExtensionRegistry.getSingleton().getExtensionArtifact(IConstantAssignmentExtension.class, constantValueAssignment.getParameter().getType());
		if (ca.canAssign(constantValueAssignment))
			return ca.createParameterValue(constantValueAssignment);
		else {
			return null;
		}
	}

}

/**
 * 
 */
package org.sopeco.plugin.std.constantassignment;

import org.sopeco.engine.experimentseries.IConstantAssignment;
import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueFactory;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.util.Tools;
import org.sopeco.util.Tools.SupportedTypes;

/**
 * Default provider of constant value assignments for basic types.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class BasicConstantValueAssignment extends AbstractSoPeCoExtensionArtifact implements IConstantAssignment {

	public BasicConstantValueAssignment(ISoPeCoExtension<?> provider) {
		super(provider);
	}

	@Override
	public boolean canAssign(ConstantValueAssignment valueAssignment) {
		String typeName = valueAssignment.getParameter().getType();
		
		return Tools.SupportedTypes.get(typeName) != null;
	}

	@Override
	public ParameterValue<?> createParameterValue(ConstantValueAssignment valueAssignment) {
		SupportedTypes type = SupportedTypes.get(valueAssignment.getParameter().getType());

		switch (type) {
		case Integer:
			return ParameterValueFactory.createParameterValue(valueAssignment.getParameter(), Integer.valueOf(valueAssignment.getValue()));
			
		case Double:
			return ParameterValueFactory.createParameterValue(valueAssignment.getParameter(), Double.valueOf(valueAssignment.getValue()));
			
		case String:
			return ParameterValueFactory.createParameterValue(valueAssignment.getParameter(), valueAssignment.getValue());
			
		case Boolean:
			return ParameterValueFactory.createParameterValue(valueAssignment.getParameter(), Boolean.valueOf(valueAssignment.getValue()));
		}
		
		return null;
	}

}

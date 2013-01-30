/**
 * 
 */
package org.sopeco.plugin.test.dummyconstantassignment;

import org.sopeco.engine.experimentseries.IConstantAssignment;
import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;

/**
 * A dummy constant value assignment strategy for testing.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class DummyConstantValueAssignment extends AbstractSoPeCoExtensionArtifact implements IConstantAssignment {

	public DummyConstantValueAssignment(ISoPeCoExtension<?> provider) {
		super(provider);
	}

	@Override
	public boolean canAssign(ConstantValueAssignment valueAssignment) {
		return false;
	}

	@Override
	public ParameterValue<?> createParameterValue(ConstantValueAssignment valueAssignment) {
		return null;
	}

}

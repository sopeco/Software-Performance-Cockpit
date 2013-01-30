/**
 * 
 */
package org.sopeco.plugin.test.dummyconstantassignment;

import java.util.Collections;
import java.util.Map;

import org.sopeco.engine.experimentseries.IConstantAssignment;
import org.sopeco.engine.experimentseries.IConstantAssignmentExtension;

/**
 * A dummy constant value assignment extension for testing.  
 * 
 * @author Roozbeh Farahbod
 *
 */
public class DummyConstantValueAssignmentExtension implements IConstantAssignmentExtension {

	protected final String name = "Dummy Constant Value Assignment";
	
	public DummyConstantValueAssignmentExtension() {}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public IConstantAssignment createExtensionArtifact() {
		return new DummyConstantValueAssignment(this);
	}

	@Override
	public Map<String, String> getConfigParameters() {
		return Collections.emptyMap();
	}

}

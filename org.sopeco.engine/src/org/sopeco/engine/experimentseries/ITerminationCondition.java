package org.sopeco.engine.experimentseries;

import java.util.Map;

import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;

/**
 * A termination condition defines when an experiment should be terminated.
 * 
 * @author Alexander Wert
 * 
 */
public interface ITerminationCondition extends ISoPeCoExtensionArtifact {
	/**
	 * Initializes the termination condition with the given configuration.
	 * 
	 * @param configuration
	 */
	void initialize(Map<String, String> configuration);
}

package org.sopeco.engine.experimentseries;

import org.sopeco.engine.registry.ISoPeCoExtension;

/**
 * Extension interface for termination condition extensions.
 * @author Alexander Wert
 *
 */
public interface ITerminationConditionExtension extends ISoPeCoExtension<ITerminationCondition> {
	/** The name of the special constant variation extension. */
	String TERMINATION_CONDITION_EXTENSION_NAME = "Termination Condition";

	/**
	 * Creates a new termination condition strategy provided by the extension.
	 * 
	 * @return returns the created termination conditionstrategy
	 */
	ITerminationCondition createExtensionArtifact();
}

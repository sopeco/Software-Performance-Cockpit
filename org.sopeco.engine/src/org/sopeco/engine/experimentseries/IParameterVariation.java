package org.sopeco.engine.experimentseries;

import org.sopeco.core.model.configuration.environment.ParameterDefinition;
import org.sopeco.core.model.configuration.measurements.ParameterValueAssignment;
import org.sopeco.engine.registry.ISoPeCoExtension;

/**
 * Plugin interface that determines the values of a parameter based on its configuration.
 * 
 * @author D053711
 *
 */
public interface IParameterVariation extends ISoPeCoExtension, Iterable<ParameterValue<?>> {
	
	public void initialize(ParameterValueAssignment configuration);
	
	public ParameterValue<?> get(int pos);
	
	public int size();
	
	public ParameterDefinition getParameter();

}

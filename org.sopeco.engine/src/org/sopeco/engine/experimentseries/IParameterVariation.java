package org.sopeco.engine.experimentseries;

import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.measurements.ParameterValueAssignment;

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

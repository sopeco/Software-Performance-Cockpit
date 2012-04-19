package org.sopeco.engine.experimentseries;

import java.util.Iterator;

import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.measurements.ParameterValueAssignment;
import org.sopeco.persistence.dataset.ParameterValue;

/**
 * Plugin interface that determines the values of a parameter based on its configuration.
 * 
 * @author D053711
 *
 */
public interface IParameterVariation extends ISoPeCoExtensionArtifact, Iterable<ParameterValue<?>> {
	
	public void initialize(ParameterValueAssignment configuration);
	
	public ParameterValue<?> get(int pos);
	
	public int size();
	
	public ParameterDefinition getParameter();

	/**
	 * Returns the iterator of this parameter variations. Different calls to 
	 * this method should return the same iterator, unless the {@link #reset()}
	 * method is called.
	 */
	public Iterator<ParameterValue<?>> iterator();
	
	/**
	 * Resets the iterator on this instance. 
	 */
	public void reset();
}

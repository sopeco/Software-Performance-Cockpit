package org.sopeco.engine.experimentseries;

import java.util.Iterator;

import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.measurements.ParameterValueAssignment;
import org.sopeco.persistence.dataset.ParameterValue;

/**
 * Extension artifact interface that determines the values of a parameter based on its configuration.
 * 
 * @author Jens Happe, Roozbeh Farahbod
 *
 */
public interface IParameterVariation extends ISoPeCoExtensionArtifact, Iterable<ParameterValue<?>> {

	/**
	 * Initializes this parameter variation with the given parameter value assignment.
	 * This method has to be called before calling any other methods of this class.
	 * 
	 * @param configuration parameter value assignment configuration
	 */
	public void initialize(ParameterValueAssignment configuration);
	
	/**
	 * Returns the value at position <code>pos</code> in the value variation series.
	 * 
	 * @param pos the position of the value in the variation
	 * @return a parameter value
	 */
	public ParameterValue<?> get(int pos);
	
	/**
	 * Returns the number of values provided by this variation.
	 */
	public int size();

	/**
	 * Returns the parameter this variation is associated to.
	 */
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
	
	/**
	 * Checks if this parameter variation can be applied to the given value assignment.
	 * This method is recommended to be called before initializing this parameter variation. 
	 * 
	 * @param configuration a parameter value assignment
	 * @return </code>true</code> if this variation suits the given assignment.
	 */
	public boolean canVary(ParameterValueAssignment configuration);
}

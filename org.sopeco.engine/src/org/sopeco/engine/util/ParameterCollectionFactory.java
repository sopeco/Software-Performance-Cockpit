/**
 * 
 */
package org.sopeco.engine.util;

import java.util.Collection;

import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Creates instances of {@link ParameterCollection}.
 * 
 * @author Roozbeh Farahbod
 */
public class ParameterCollectionFactory {

	/**
	 * Creates an empty parameter value collection.
	 */
	public static ParameterCollection<ParameterValue<?>> createParameterValueCollection() {
		return new ParameterCollection<ParameterValue<?>>();
	}

	/**
	 * Creates a parameter value collection based on the given values.
	 * 
	 * @param parameterValueCollection a collection of parameter values
	 * @return a parameter value collection that holds the given parameter values
	 */
	public static ParameterCollection<ParameterValue<?>> createParameterValueCollection(Collection<ParameterValue<?>> parameterValueCollection) {
		
		if (parameterValueCollection == null){
			return null;
		}
		final ParameterCollection<ParameterValue<?>> result = new ParameterCollection<ParameterValue<?>>();
		result.addAll(parameterValueCollection);
		return result;
	}
	
	/**
	 * Creates an empty parameter definition collection.
	 */
	public static ParameterCollection<ParameterDefinition> createParameterDefinitionCollection() {
		return new ParameterCollection<ParameterDefinition>();
	}

	/**
	 * Creates a parameter definition collection based on the given parameter definitions.
	 * 
	 * @param parameterDefCollection a collection of parameter definition
	 * @return a parameter definition collection that holds the given parameter definition
	 */
	public static ParameterCollection<ParameterDefinition> createParameterDefinitionCollection(Collection<ParameterDefinition> parameterDefCollection) {
		final ParameterCollection<ParameterDefinition> result = new ParameterCollection<ParameterDefinition>();
		result.addAll(parameterDefCollection);
		return result;
	}
}

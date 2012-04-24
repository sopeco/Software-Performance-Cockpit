/**
 * 
 */
package org.sopeco.plugin.std.parametervariation.bool;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.measurements.DynamicValueAssignment;
import org.sopeco.model.configuration.measurements.ParameterValueAssignment;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueFactory;
import org.sopeco.util.Tools.SupportedTypes;

/**
 * Provides a Boolean value variation which just switches between the two boolean values. 
 * 
 * @author Roozbeh Farahbod
 *
 */
public class BooleanVariation extends AbstractSoPeCoExtensionArtifact implements IParameterVariation {

//	private static final Logger logger = LoggerFactory.getLogger(BooleanVariation.class);
	
	private Iterator<ParameterValue<?>> iterator = null;

	/** Holds the dynamic value assignment configuration for this variation. */
	protected DynamicValueAssignment dynamicValueAssignment = null;
	
	public BooleanVariation(ISoPeCoExtension<?> provider) {
		super(provider);
	}

	@Override
	public void initialize(ParameterValueAssignment configuration) {
		dynamicValueAssignment = (DynamicValueAssignment) configuration;
	}

	@Override
	public ParameterValue<?> get(int pos) {
		// the list of values is [false, true]
		if (pos < 2) {
			return ParameterValueFactory.createParameterValue(dynamicValueAssignment.getParameter(), (pos == 1));
		} else
			throw new IndexOutOfBoundsException("Parameter value index " + pos + " cannot be greater than 1 for a Boolean value variation.");
	}

	@Override
	public int size() {
		return 2;
	}

	@Override
	public ParameterDefinition getParameter() {
		return dynamicValueAssignment.getParameter();
	}

	@Override
	public Iterator<ParameterValue<?>> iterator() {
		if (iterator == null) {
			iterator = new BooleanValueIterator();
		}
		return iterator;
	}

	@Override
	public void reset() {
		iterator = null;
	}

	/**
	 * Returns true if the given configuration is a dynamic value assignment on a Boolean parameter.
	 */
	@Override
	public boolean canVary(ParameterValueAssignment configuration) {
		final SupportedTypes paramType = SupportedTypes.get(configuration.getParameter().getType());
		final boolean typeCheck = (paramType != null) && (paramType == SupportedTypes.Boolean);
		return (configuration instanceof DynamicValueAssignment) && typeCheck;
	}

	/**
	 * Implements the Boolean variation iterator.
	 *  
	 * @author Roozbeh Farahbod
	 *
	 */
	protected class BooleanValueIterator implements Iterator<ParameterValue<?>> {

		private int pos = 0;
		
		@Override
		public boolean hasNext() {
			return pos < size();
		}

		@Override
		public ParameterValue<?> next() {
			if (hasNext()) {
				return get(pos++);
			} else {
				throw new NoSuchElementException("Boolean value iterator has no more elements.");
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}

}

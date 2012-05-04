/**
 * 
 */
package org.sopeco.plugin.std.parametervariation.constant;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.engine.util.EngineTools;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

/**
 * Provides a constant value variation, which is basically returning the constant value as 
 * the only possible option for the value of the parameter. 
 * 
 * @author Roozbeh Farahbod
 *
 */
public class ConstantVariation extends AbstractSoPeCoExtensionArtifact implements IParameterVariation {

	private static final Logger logger = LoggerFactory.getLogger(ConstantVariation.class);
	
	private ParameterValue<?> constantValue = null;
	private Iterator<ParameterValue<?>> iterator = null;
	private ConstantValueAssignment parameterValueAssignment = null;
	
	public ConstantVariation(ISoPeCoExtension<?> provider) {
		super(provider);
	}

	@Override
	public void initialize(ParameterValueAssignment configuration) {
		parameterValueAssignment = (ConstantValueAssignment) configuration;
		ParameterValue<?> value = EngineTools.getConstantParameterValue(parameterValueAssignment);
		if (value != null) {
			constantValue = value;
		} else {
			// TODO throw runtime error
		}
	}

	@Override
	public ParameterValue<?> get(int pos) {
		if (pos == 0)
			return constantValue;
		else
			throw new IndexOutOfBoundsException("Parameter value index " + pos + " cannot be greater than zero for a constant value variation.");
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public ParameterDefinition getParameter() {
		return parameterValueAssignment.getParameter();
	}

	@Override
	public Iterator<ParameterValue<?>> iterator() {
		if (iterator == null) {
			iterator = new ConstantValueIterator();
		}
		return iterator;
	}

	@Override
	public void reset() {
		iterator = null;
	}

	/**
	 * Returns true if the given configuration is a constant value assignment.
	 */
	@Override
	public boolean canVary(ParameterValueAssignment configuration) {
		return configuration instanceof ConstantValueAssignment;
	}

	/**
	 * Implements the constant variation iterator.
	 *  
	 * @author Roozbeh Farahbod
	 *
	 */
	protected class ConstantValueIterator implements Iterator<ParameterValue<?>> {

		private boolean read = false;
		
		@Override
		public boolean hasNext() {
			return !read;
		}

		@Override
		public ParameterValue<?> next() {
			if (hasNext()) {
				read = true;
				return get(0);
			} else {
				throw new NoSuchElementException("Constant value iterator has no more elements.");
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}

}

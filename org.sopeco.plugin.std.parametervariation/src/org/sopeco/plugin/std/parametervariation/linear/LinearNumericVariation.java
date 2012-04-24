/**
 * 
 */
package org.sopeco.plugin.std.parametervariation.linear;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.emf.common.util.EMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.measurements.DynamicValueAssignment;
import org.sopeco.model.configuration.measurements.ParameterValueAssignment;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueFactory;
import org.sopeco.util.Tools;
import org.sopeco.util.Tools.SupportedTypes;

/**
 * Provides a linear numeric parameter value variation.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class LinearNumericVariation extends AbstractSoPeCoExtensionArtifact implements IParameterVariation {

	private static Logger logger = LoggerFactory.getLogger(LinearNumericVariation.class);
	
	/** The name of the 'min' configuration parameter. */
	public static final String PARAM_MIN = "min";

	/** The name of the 'max' configuration parameter. */
	public static final String PARAM_MAX = "max";

	/** The name of the 'step' configuration parameter. */
	public static final String PARAM_STEP = "step";
	
	/** Holds the state of this instance. */
	protected LMVState state = null;
	
	/** Holds the dynamic value assignment configuration for this variation. */
	protected DynamicValueAssignment dynamicValueAssignment = null;

	/** Holds the type of the parameter this variation is assigned to. */
	protected Tools.SupportedTypes parameterType;

	/** Holds the current iterator of this variation. */
	protected LinearNumericIterator iterator;
	
	public LinearNumericVariation(LinearNumericVariationExtension provider) {
		super(provider);
	}
	
	@Override
	public void initialize(ParameterValueAssignment configuration) {
		final String paramType = configuration.getParameter().getType();
		final DynamicValueAssignment dva = (DynamicValueAssignment) configuration;
		final EMap<String, String> params = dva.getConfiguration();

		parameterType = SupportedTypes.get(paramType);
		if (parameterType == null) {
			throw new IllegalArgumentException("The given parameter value assignment is not supported by " + this.getClass().getSimpleName() + ".");
		}
		
		final LMVState dState = new LMVState();
		dState.min = Double.valueOf(params.get(PARAM_MIN));
		dState.max = Double.valueOf(params.get(PARAM_MAX));
		dState.step = Double.valueOf(params.get(PARAM_STEP));
		state = dState;
		
		if (state.step == 0) {
			state.size = 1;
		} else {
			state.size = (int) (1 + Math.floor((state.max - state.min) / state.step)); 
		}
		
		dynamicValueAssignment = dva;
	}

	@Override
	public ParameterValue<?> get(int pos) {
		if (pos < state.size) {
			final double value = state.min + pos * state.step;
			ParameterValue<?> result = null;
			
			switch (parameterType) {
			case Integer:
				result = ParameterValueFactory.createParameterValue(dynamicValueAssignment.getParameter(), (int) Math.round(value));
				break;
			case Double:
				result = ParameterValueFactory.createParameterValue(dynamicValueAssignment.getParameter(), value);
			default:
				break;
			}
			
			return result;
		} else {
			throw new IndexOutOfBoundsException("Parameter value index " + pos + " is out of bound [0.." + state.size + "].");
		}
	}

	@Override
	public int size() {
		return state.size;
	}

	@Override
	public ParameterDefinition getParameter() {
		return dynamicValueAssignment.getParameter();
	}

	@Override
	public Iterator<ParameterValue<?>> iterator() {
		if (iterator == null)
			iterator = new LinearNumericIterator();
		return iterator;
	}

	@Override
	public void reset() {
		iterator = null;
	}

	@Override
	public boolean canVary(ParameterValueAssignment configuration) {
		final SupportedTypes paramType = SupportedTypes.get(configuration.getParameter().getType());
		final boolean typeCheck = (paramType != null) && (paramType == SupportedTypes.Double || paramType == SupportedTypes.Integer);
		return (configuration instanceof DynamicValueAssignment) && typeCheck;
	}

	public String toString() {
		return "[min: " + state.min + ", step: " + state.step + ", max: " + state.max + "]";
	}
	
	/**
	 * Represents the current state of the numeric variation.
	 * 
	 * @author Roozbeh Farahbod
	 */
	private static class LMVState {
		private double min;
		private double max;
		private double step;
		private int size;
	}

	/**
	 * Implements the linear numeric variation iterator.
	 *  
	 * @author Roozbeh Farahbod
	 *
	 */
	protected class LinearNumericIterator implements Iterator<ParameterValue<?>> {

		private int pos = 0;
		
		@Override
		public boolean hasNext() {
			return pos < state.size;
		}

		@Override
		public ParameterValue<?> next() {
			if (hasNext()) {
				return get(pos++);
			} else {
				logger.debug("Next item will be position " + pos + "in lineare numeric variation " + LinearNumericVariation.this.toString() + ".");
				throw new NoSuchElementException("Linear numeric iterator has no more elements.");
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
}

/**
 * 
 */
package org.sopeco.plugin.std.parametervariation.csv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.experimentseries.IParameterVariationExtension;
import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueFactory;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;
import org.sopeco.util.Tools;
import org.sopeco.util.Tools.SupportedTypes;

/**
 * Provides a comma-separated value variation strategy.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class CSVVariation extends AbstractSoPeCoExtensionArtifact implements IParameterVariation {

	private static Logger logger = LoggerFactory.getLogger(CSVVariation.class);
	
	/** The name of the 'values' configuration parameter. */
	public static final String PARAM_VALUES = "values";
	
	/** The name of the delimiter configuration parameter. */
	public static final String PARAM_DELIMITER = "delim";
	
	private static final String DEFAULT_DELIMITER = ",";

	private static final String RANGE_OPERATOR = "...";

	/** Holds the dynamic value assignment configuration for this variation. */
	protected DynamicValueAssignment dynamicValueAssignment = null;

	/** Holds the type of the parameter this variation is assigned to. */
	protected Tools.SupportedTypes parameterType;

	/** Holds the current iterator of this variation. */
	protected Iterator<ParameterValue<?>> iterator;

	/** Holds the value expression of this variation. */
	protected String valuesExpression = null;
	
	/** Holds the list of values. */
	protected List<ParameterValue<?>> values = new ArrayList<ParameterValue<?>>();
	
	public CSVVariation(IParameterVariationExtension provider) {
		super(provider);
	}
	
	@Override
	public void initialize(ParameterValueAssignment configuration) {
		final String paramType = configuration.getParameter().getType();
		final DynamicValueAssignment dva = (DynamicValueAssignment) configuration;
		final Map<String, String> params = dva.getConfiguration();

		parameterType = SupportedTypes.get(paramType);
		if (parameterType == null) {
			throw new IllegalArgumentException("The given parameter value assignment is not supported by " + this.getClass().getSimpleName() + ".");
		}

		dynamicValueAssignment = dva;
		
		final String delim = params.get(PARAM_DELIMITER);
		valuesExpression = params.get(PARAM_VALUES);
		extractValues(valuesExpression, delim);
		
	}

	private void extractValues(String expression, String delim) {
		StringTokenizer tokenizer = new StringTokenizer(expression, delim == null ? DEFAULT_DELIMITER : delim);
		List<Object> eValues = new ArrayList<Object>();
		while (tokenizer.hasMoreTokens()) {
			final String token = tokenizer.nextToken().trim();
			
			switch (parameterType) {
			case Boolean:
				final Boolean bv = Boolean.valueOf(token);
				if (!Tools.strEqualName(bv.toString(), token)) {
					logger.warn("The value '{}' specified in {} is not a Boolean. It is assumed as false.", token, this.getClass().getSimpleName());
				}
				eValues.add(bv);
				break;
				
			case Integer:
				if (isRangeValue(token)) {
					List<Integer> ivs =  expandIntegerToken(token);
					for (Integer i: ivs) {
						eValues.add(i);
					}
				} else {
					final Integer i = strToInteger(token);
					if (i != null) {
						eValues.add(i);
					}
				}
				break;
				
			case Double:
				try {
					final Double dv = Double.valueOf(token);
					eValues.add(dv);
				} catch (NumberFormatException e) {
					logger.error("The value '{}' specified in {} is not a Double value. This value is ignored.", token, this.getClass().getSimpleName());
				}
				break;
				
			case String:
				eValues.add(token);
				break;
				
			default:
				// at the time of coding these lines, this never happens
				break;
			}
		}

		values.clear();
		for (Object obj: eValues) {
			values.add(ParameterValueFactory.createParameterValue(dynamicValueAssignment.getParameter(), obj));
		}
	}
	
	/**
	 * Expands a range value (see {@link #isRangeValue(String)}) 
	 * into a list of values.
	 */
	private List<Integer> expandIntegerToken(String token) {
		List<Integer> result = new ArrayList<Integer>();
		String left = token.substring(0, token.indexOf(RANGE_OPERATOR)).trim();
		String right = token.substring(token.indexOf(RANGE_OPERATOR) + RANGE_OPERATOR.length()).trim();
		
		if (left.length() > 0 && right.length() > 0) {
			final Integer li = strToInteger(left);
			final Integer ri = strToInteger(right);
			if (ri != null && li != null) {
				for (int i = li; i <= ri; i++) {
					result.add(i);
				}
			}
		} else { 
			logger.error("Invalid integer range value '{}'.", token);
		}
		
		return result;
	}

	/**
	 * Returns true if the given value has the range operator in it.
	 */
	private boolean isRangeValue(String value) {
		return value.indexOf(RANGE_OPERATOR) > -1;
	}
	
	/**
	 * If the value represents an integer, returns the integer value. Otherwise,
	 * logs an error message and returns null.
	 */
	private Integer strToInteger(String value) {
		try {
			final Integer i = Integer.valueOf(value);
			return i;
		} catch (NumberFormatException e) {
			logger.error("The value '{}' specified in {} is not an Integer value. This value is ignored.", value, this.getClass().getSimpleName());
			return null;
		}
	}

	@Override
	public ParameterValue<?> get(int pos) {
		if (pos < values.size()) {
			return ParameterValueFactory.createParameterValue(dynamicValueAssignment.getParameter(), values.get(pos).getValue());
		} else {
			throw new IndexOutOfBoundsException("Parameter value index " + pos + " is out of bound [0.." + values.size() + "].");
		}
	}

	@Override
	public int size() {
		return values.size();
	}

	@Override
	public ParameterDefinition getParameter() {
		return dynamicValueAssignment.getParameter();
	}

	@Override
	public Iterator<ParameterValue<?>> iterator() {
		if (iterator == null)
			iterator = values.iterator();
		return iterator;
	}

	@Override
	public void reset() {
		iterator = null;
	}

	@Override
	public boolean canVary(ParameterValueAssignment configuration) {
		final SupportedTypes paramType = SupportedTypes.get(configuration.getParameter().getType());
		return (configuration instanceof DynamicValueAssignment) && (paramType != null);
	}
	
	public String toString() {
		return "[" + valuesExpression + "]";
	}
	
}

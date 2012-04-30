package org.sopeco.plugin.std.exploration.breakdown.environment;

import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.util.ParameterUtil;

/**
 * Factory class do create AbstractEnvironmentValues.
 * 
 * @author Rouven Krebs
 */
public final class EnvironmentValueFactory {
	/**
	 * Singleton.
	 */
	private static final  EnvironmentValueFactory INSTANCE = new EnvironmentValueFactory();

	private EnvironmentValueFactory() {	
	}

	public AbstractEnvironmentValue caseIntegerValue(
			ParameterValue<Integer> object) {
		return new IntegerEnvironmentValue(object);
	}

	public AbstractEnvironmentValue caseDoubleValue(
			ParameterValue<Double> object) {
		return new DoubleEnvironmentValue(object);
	}

	/**
	 * Creates a new Environment Value based on the class of the value.
	 * 
	 * @param runInfo
	 *            corresponding to the measurement of this value.
	 * @param value
	 *            to be encapsulated.
	 * @return the environmental value encapsulating the value itself.
	 */
	/*
	 * expected to be faster this way instead of creating a new Object every
	 * time.
	 */
	@SuppressWarnings("unchecked")
	public static synchronized AbstractEnvironmentValue createAbstractEnvironmentValue(ParameterValue<?> value) {
		

		AbstractEnvironmentValue environmentValue = null;
		switch (ParameterUtil.getTypeEnumeration(value.getParameter().getType())) {
		case DOUBLE:
			environmentValue = INSTANCE
					.caseDoubleValue((ParameterValue<Double>) value);
			break;
		case INTEGER:
			environmentValue = INSTANCE
					.caseIntegerValue((ParameterValue<Integer>) value);
			break;
		default:
			throw new IllegalArgumentException("Exploration supports only Integer and Double values.");
		}

		return environmentValue;
	}

}

package org.sopeco.plugin.std.exploration.breakdown.environment;

import org.sopeco.persistence.dataset.ParameterValue;

/**
 * The double environment value.
 * 
 * @author Rouven Krebs
 */
public class DoubleEnvironmentValue extends AbstractEnvironmentValue {
	DoubleEnvironmentValue(ParameterValue<Double> value) {
		super(value);
	}

	@Override
	public double calculateDifferenceInPercent(
			AbstractEnvironmentValue referenceValue) {

		if (referenceValue instanceof DoubleEnvironmentValue) {
			return this.calculateDoubleDifference(referenceValue);
		} else if (referenceValue instanceof IntegerEnvironmentValue) {
			return this.calculateIntegerDifference(referenceValue);
		}
		
		throw new RuntimeException(
				"AbstractEnvironmentValue is an unsupported type");

	}

	/**
	 * calculate the absolute relative error for a
	 * {@link DoubleEnvironmentValue}.
	 * 
	 * @param referenceValue
	 * @return the difference
	 */
	private double calculateDoubleDifference(
			AbstractEnvironmentValue referenceValue) {
		double referencedDoubleValue = ((DoubleEnvironmentValue) referenceValue)
				.getValue().getValueAsDouble();
		double thisValue = this.getValue().getValueAsDouble();
		double relativeError;
		if (thisValue < RELATIVE_ERROR_THRESHOLD
				&& thisValue > -RELATIVE_ERROR_THRESHOLD) {
			relativeError = Math.abs(referencedDoubleValue - thisValue);
		} else {
			relativeError = Math.abs((referencedDoubleValue - thisValue)
					/ thisValue);
		}
		return relativeError;

	}

	/**
	 * calculate the absolute relative error for a
	 * {@link IntegerEnvironmentValue}.
	 * 
	 * @param referenceValue
	 * @return the difference
	 */
	private double calculateIntegerDifference(
			AbstractEnvironmentValue referenceValue) {
		double referencedDoubleValue = ((IntegerEnvironmentValue) referenceValue)
				.getValue().getValueAsDouble();
		double thisValue = this.getValue().getValueAsDouble();
		double relativeError;
		if (thisValue < RELATIVE_ERROR_THRESHOLD
				&& thisValue > -RELATIVE_ERROR_THRESHOLD) {
			relativeError = Math.abs(referencedDoubleValue - thisValue);
		} else {
			relativeError = Math.abs((referencedDoubleValue - thisValue)
					/ thisValue);
		}
		return relativeError;
	}

	@Override
	public boolean isLower(AbstractEnvironmentValue value) {

		if (value instanceof DoubleEnvironmentValue) {
			return this.getValue().getValueAsDouble() < ((DoubleEnvironmentValue) value)
					.getValue().getValueAsDouble();
		} else if (value instanceof IntegerEnvironmentValue) {
			return this.getValue().getValueAsDouble() < ((IntegerEnvironmentValue) value)
					.getValue().getValueAsDouble();
		}

		throw new RuntimeException(
				"AbstractEnvironmentValue is an unsupported type");

	}

	@Override
	public double calculateDifferenceToAverage(AbstractEnvironmentValue value) {
		double secondValue = 0d;

		if (value instanceof DoubleEnvironmentValue) {
			secondValue = ((DoubleEnvironmentValue) value).getValue()
					.getValueAsDouble();
		} else if (value instanceof IntegerEnvironmentValue) {
			secondValue = ((IntegerEnvironmentValue) value).getValue()
					.getValueAsDouble();
		} else {
			throw new RuntimeException(
					"AbstractEnvironmentValue is an unsupported type");
		}
		
		return Math.abs(secondValue - this.getValue().getValueAsDouble())
				/ ((Math.abs(secondValue) + Math.abs(this.getValue()
						.getValueAsDouble())) / 2);

	}

}

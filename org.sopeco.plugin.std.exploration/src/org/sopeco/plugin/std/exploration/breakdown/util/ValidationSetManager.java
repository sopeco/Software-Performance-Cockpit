package org.sopeco.plugin.std.exploration.breakdown.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.exploration.breakdown.environment.AbstractEnvironmentValue;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess.MeasurementCacheResult;
import org.sopeco.plugin.std.exploration.breakdown.space.RelativePosition;

/**
 * This class allows to create validation sets and perform model validation
 * based on this set.
 * 
 * @author Dennis Westermann
 * 
 */
public class ValidationSetManager {

	private LinkedList<RelativePosition> mainValidationPoints = new LinkedList<RelativePosition>();
	private EnvironmentCachedAccess cachedEnvironmentAccess;
	private List<ParameterDefinition> allParameters;

	public ValidationSetManager(EnvironmentCachedAccess cachedEnvironmentAccess, List<ParameterDefinition> allParameters) {
		super();
		this.cachedEnvironmentAccess = cachedEnvironmentAccess;
		this.allParameters = allParameters;
	}

	public void createRandomValidationSet(double size) {

		for (int i = 0; i < size; i++) {
			mainValidationPoints.add(createRandomPoint());
		}
	}

	protected RelativePosition createRandomPoint() {
		Random random = new Random();
		RelativePosition result = new RelativePosition();
		for (ParameterDefinition dimension : allParameters) {
			result.put(dimension.getFullName(), random.nextDouble());
		}
		return result;
	}

	public double getAvgRelativePredictionError() {

		double sumOfRelativePredictionErrors = 0;

		for (RelativePosition validationPoint : mainValidationPoints) {
			AbstractEnvironmentValue predictedValue = cachedEnvironmentAccess.predictValue(validationPoint);
			MeasurementCacheResult measuredValue = cachedEnvironmentAccess.measure(validationPoint);

			sumOfRelativePredictionErrors += predictedValue.calculateDifferenceInPercent(measuredValue.value);
		}

		return sumOfRelativePredictionErrors / mainValidationPoints.size();
	}

}

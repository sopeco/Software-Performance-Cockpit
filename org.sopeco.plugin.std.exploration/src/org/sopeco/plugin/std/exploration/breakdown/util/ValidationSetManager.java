/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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

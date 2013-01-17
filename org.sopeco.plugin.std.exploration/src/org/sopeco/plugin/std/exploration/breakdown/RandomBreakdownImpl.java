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
package org.sopeco.plugin.std.exploration.breakdown;

import java.util.List;

import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess.MeasurementCacheResult;
import org.sopeco.plugin.std.exploration.breakdown.space.RelativePosition;
import org.sopeco.plugin.std.exploration.breakdown.stop.AbstractStopController;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration;

/**
 * This class implements the RandomBrakdown strategy. This strategy randomly
 * selects additional experiments from the parameter space until a stop criteria
 * has been reached.
 * 
 * @author Dennis Westermann
 * 
 */
public class RandomBreakdownImpl extends AbstractBreakdownExploration implements IBreakdownExploration {

	

	public RandomBreakdownImpl(ExplorationStrategy strategyConfig, 
			EnvironmentCachedAccess cachedEnvironmentAccess, 
			List<ParameterDefinition> allParameters,
			AbstractStopController stopController, 
			ISoPeCoExtension<?> provider) {
		super(allParameters, cachedEnvironmentAccess, strategyConfig, stopController, provider);
	}

	@Override
	public void doIteration() {
		long numPointsInModelBeforeIteration = cachedEnvironmentAccess.getNumberOfPointsInModel();

		for (int j = 0; j < BreakdownConfiguration.getNumberOfExperimentsPerIteration(strategyConfig); j++) {
			RelativePosition point = createRandomPoint();
			MeasurementCacheResult measuredValue = cachedEnvironmentAccess.measure(point);
			cachedEnvironmentAccess.addToModel(point, measuredValue.value);
		}

		// check if new points have been added to the model
		hasNewModelPoints = numPointsInModelBeforeIteration < cachedEnvironmentAccess.getNumberOfPointsInModel();

	}

	@Override
	public void finishWork() {
		// not used

	}

	@Override
	public void initialise() {
		for (int j = 0; j < BreakdownConfiguration.getNumberOfInitialExperiments(strategyConfig); j++) {
			RelativePosition point = createRandomPoint();
			MeasurementCacheResult measuredValue = cachedEnvironmentAccess.measure(point);
			cachedEnvironmentAccess.addToModel(point, measuredValue.value);
		}

	}

}

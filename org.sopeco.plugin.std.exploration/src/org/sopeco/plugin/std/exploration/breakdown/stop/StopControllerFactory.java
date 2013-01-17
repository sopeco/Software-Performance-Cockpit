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
package org.sopeco.plugin.std.exploration.breakdown.stop;

import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.plugin.std.exploration.breakdown.environment.AlgorithmsEnvironment;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration;
import org.sopeco.plugin.std.exploration.breakdown.util.IMeasurementSeriesAwareable;

/**
 * This class provides the opportunity to create a Stop Controller which can be
 * used to stop an exploration before it reaches it self defined termination.
 * 
 * @author Rouven Krebs, Dennis Westermann
 */
public abstract class StopControllerFactory {
	/**
	 * Create a stop controller deciding if the exploration should be stopped.
	 * The creation is based on the information from the configuration model.
	 * 
	 * @param expSeriesConfig
	 *            the configuration defining the stop conditions
	 * @param info
	 *            where information about the current exploration can get from.
	 * @return the controller deciding if the exploration should be stopped.
	 */
	public static AbstractStopController createStopController(ExplorationStrategy strategyConfig, IMeasurementSeriesAwareable info,
			AlgorithmsEnvironment environment) {
		OneStopController ofsc = new OneStopController();

		
		if (BreakdownConfiguration.getMaxExplorationTimeInMin(strategyConfig) != null) {
			TimeStopCondition tsc = new TimeStopCondition(BreakdownConfiguration.getMaxExplorationTimeInMin(strategyConfig) * 60 * 1000); 
			ofsc.addCondition(tsc);
		}																																

		if(BreakdownConfiguration.getMaxNumberOfExperiments(strategyConfig) != null) {
			NumberOfMeasurementsStopCondition nsc = new NumberOfMeasurementsStopCondition(BreakdownConfiguration.getMaxNumberOfExperiments(strategyConfig), info);
			ofsc.addCondition(nsc);
		}

		return ofsc;
	}

}

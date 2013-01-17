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
package org.sopeco.plugin.std.exploration.breakdown.extensions;

import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration.AccuracyDetermination;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration.DynamicSectorAccuracyScope;

/**
 * The extension that provides the adaptive equidistant breakdown exploration
 * strategy.
 * 
 * @author Dennis Westermann
 * 
 */
public class AdaptiveEquidistantBreakdownExtension extends AbstractBreakdownExplorationExtension {

	public AdaptiveEquidistantBreakdownExtension() {
		super();
	}

	/**
	 * The name of the provided extension artifact.
	 */
	public static final String NAME = BreakdownConfiguration.ADAPTIVE_EQUIDISTANT_BREAKDOWN;

	@Override
	protected void prepareConfigurationParameterMap() {

		/* Stop Criteria */
		configParams.put(BreakdownConfiguration.DESIRED_MODEL_ACCURACY, "0.3");
		configParams.put(BreakdownConfiguration.MAX_EXPLORATION_TIME_IN_MIN, "");
		configParams.put(BreakdownConfiguration.MAX_NUMBER_OF_EXPERIMENTS, "");

		/* Algorithm tuning */
		configParams.put(BreakdownConfiguration.BORDER_MEASUREMENT_DEPTH, "1");
		configParams.put(BreakdownConfiguration.DIMINUTION_OF_VALIDATIONS, "1.04");

		/* Accuracy Determination */
		configParams.put(BreakdownConfiguration.ACCURACY_DETERMINATION_METHOD,
				AccuracyDetermination.DynamicSector.name());

		// only for DynamicSector validation
		configParams.put(BreakdownConfiguration.DYNAMIC_SECTOR_ACCURACY_SCOPE, DynamicSectorAccuracyScope.Local.name());

		// only for RandomValidationSet validation
		configParams.put(BreakdownConfiguration.SIZE_OF_VALIDATION_SET, "");

	}

	@Override
	public String getName() {

		return NAME;
	}
}
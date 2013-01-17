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
package org.sopeco.plugin.std.exploration.screening.adapter;

import java.util.List;

import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.plugin.std.exploration.screening.container.ExpDesign;

/**
 * Interface describing the public methods of a ScreeningAdapter used to
 * generate a screening design.
 * 
 * @author Pascal Meier
 * @author Dennis Westermann
 * 
 */
public interface IScreeningAdapter {

	/**
	 * Sets the low, high and center point values of all the screening
	 * parameters according to the specified ParameterVariationIterators.
	 * 
	 * @param variationImplementations
	 *            ParameterVariationIterators used to get the parameter
	 *            screening values.
	 */
	void setParameterVariations(
			List<IParameterVariation> variationImplementations);

	/**
	 * Sets the configuration of the Screening Exploration Strategy.
	 * 
	 * @param expConf
	 *            Screening configuration
	 * @throws FrameworkException
	 */
	void setExplorationConf(ExplorationStrategy expSeriesConfig);

	/**
	 * Generates a screening design with the adapter.
	 * 
	 * @throws FrameworkException
	 */
	void generateDesign();

	/**
	 * Returns the generated ExpDesign.
	 * 
	 * @return
	 */
	ExpDesign getExpDesign();

	/**
	 * Returns a list of ParameterValues for parameters that won't be explored
	 * by the design but are necessary for executing measurements because the
	 * system requires them.
	 * 
	 * @return
	 */
	List<ParameterValue<?>> getConstantParameterValues();
}

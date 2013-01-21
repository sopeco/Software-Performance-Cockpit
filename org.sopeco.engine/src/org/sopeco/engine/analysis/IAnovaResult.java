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
package org.sopeco.engine.analysis;

import java.util.List;

/**
 * Contains the result of an ANOVA test.
 * 
 * @author Dennis Westermann
 */
public interface IAnovaResult extends IParameterInfluenceResult {

	/**
	 * Returns a list of all ParameterEffect-Objects describing the F-value of
	 * the independent parameter.
	 * 
	 * @return list of all main effects
	 */
	List<AnovaCalculatedEffect> getAllMainEffects();

	/**
	 * Returns a list of ParameterEffect-Objects where the effect is significant
	 * with the given p-value.
	 * 
	 * @param significanceLevel
	 *            a value between 0 and 1 describing the targeted significance
	 *            level
	 * 
	 * @return list of main effects with the given significance level
	 */
	List<AnovaCalculatedEffect> getMainEffects(double significanceLevel);

	/**
	 * Returns a list of all ParameterEffect-Objects describing the F-value of
	 * the independent parameter.
	 * 
	 * @return list of all interaction effects
	 */
	List<AnovaCalculatedEffect> getAllInteractionEffects();

	/**
	 * Returns a list of all InteractionEffects with the given depth.
	 * 
	 * @param depth
	 *            the depth of interaction effects (i.e. the number of
	 *            parameters that are part of the interaction. Thus, the minimum
	 *            value is 2 and the maximum value is the number of independent
	 *            parameters in the analysis)
	 * 
	 * @return list of interaction effects with the given depth
	 */
	List<AnovaCalculatedEffect> getInteractionEffects(int depth);

	/**
	 * Returns a list of ParameterEffect-Objects where the effect is significant
	 * with the given p-value.
	 * 
	 * @param significanceLevel
	 *            a value between 0 and 1 describing the targeted significance
	 *            level
	 * 
	 * @return list of interaction effects with the given significance level
	 */
	List<AnovaCalculatedEffect> getInteractionEffects(double significanceLevel);

	/**
	 * Returns a list of all InteractionEffects with the given depth and the
	 * given significance level.
	 * 
	 * @param depth
	 *            the depth of interaction effects
	 * @param significanceLevel
	 *            a value between 0 and 1 describing the targeted significance
	 *            level
	 * @return list of interaction effects with the given depth and significance
	 *         level
	 */
	List<AnovaCalculatedEffect> getInteractionEffects(int depth, double significanceLevel);

}

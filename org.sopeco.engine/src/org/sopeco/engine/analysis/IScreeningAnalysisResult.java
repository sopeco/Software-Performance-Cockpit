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

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Contains a result of a screening analysis which determines the effect of one
 * parameter or of interacting parameters on a dependent parameter.
 * 
 * @author Pascal Meier
 * @author Dennis Westermann
 */
public interface IScreeningAnalysisResult extends IParameterInfluenceResult {

	/**
	 * Returns a list of all Main- and Interaction-ParameterEffects of all
	 * screened parameters.
	 * 
	 * @return list of ParameterEffects
	 */
	List<ParameterEffect> getAllParameterEffects();

	/**
	 * Returns a list of all MainEffects of all parameters.
	 * 
	 * @return list of ParameterEffects
	 */
	List<ParameterEffect> getAllMainEffects();

	/**
	 * Returns a list of all InteractionEffects of all parameters.
	 * 
	 * @return list of ParameterEffects
	 */
	List<ParameterEffect> getAllInteractionEffects();

	/**
	 * Returns the object describing the Main-ParameterEffect of the specified
	 * parameter.
	 * 
	 * @param parameter
	 *            parameter for which the main effect should be retrieved
	 * @return a ParameterEffect describing the main effect of the specified
	 *         parameter
	 */
	ParameterEffect getMainEffectByParam(ParameterDefinition parameter);

	/**
	 * Returns all InteractionEffects of the specified parameter with other
	 * parameters if any are existent.
	 * 
	 * @param parameter
	 *            parameter to get interaction effects for
	 * @return list of ParameterEffects of the interactions, can be empty
	 */
	List<ParameterEffect> getInteractionEffectsOfSingleParam(ParameterDefinition parameter);

	/**
	 * Returns a ParameterEffect-object which represents the single interaction
	 * effect of all the specified parameters.
	 * 
	 * @param parameters
	 *            the parameters of the desired Interaction-ParameterEffect
	 * @return value representing the interaction effect of the parameters, or
	 *         null if not existent
	 */
	ParameterEffect getInteractionEffectByParameters(List<ParameterDefinition> parameters);

	/**
	 * Returns all ParameterEffect-objects which represent all interaction
	 * effects which describe all of the specified parameters (and other
	 * parameters!).
	 * 
	 * @param parameters
	 *            the parameters of the desired Interaction-ParameterEffect
	 * @return value representing the interaction effect of the parameters, or
	 *         null if not existent
	 */
	List<ParameterEffect> getAllInteractionEffectsByParameters(List<ParameterDefinition> parameters);

}

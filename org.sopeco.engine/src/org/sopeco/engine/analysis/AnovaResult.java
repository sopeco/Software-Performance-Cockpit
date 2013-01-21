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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Class represents the result of an ANOVA analysis.
 * 
 * @author Dennis Westermann
 * 
 */
public class AnovaResult implements IAnovaResult {

	private static final long serialVersionUID = 1L;

	/** An id that uniquely identifies this result instance **/
	private String id;

	/** List to store parameter effect value */
	private List<AnovaCalculatedEffect> mainEffects;

	/** List to store parameter effect value */
	private List<AnovaCalculatedEffect> interactionEffects;

	/**
	 * Configuration used to derive this result object.
	 */
	private AnalysisConfiguration configuration;

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            configuration of the ANOVA analysis
	 */
	public AnovaResult(AnalysisConfiguration configuration) {
		this.configuration = configuration;
		mainEffects = new ArrayList<AnovaCalculatedEffect>();
		interactionEffects = new ArrayList<AnovaCalculatedEffect>();
	}

	/**
	 * Adds a main effect to this result instance.
	 * 
	 * @param effect
	 *            the main effect to be added
	 */
	public void addMainEffect(AnovaCalculatedEffect effect) {
		this.mainEffects.add(effect);
	}

	/**
	 * Adds an interaction effect to this result instance.
	 * 
	 * @param effect
	 *            the interaction effect to be added
	 */
	public void addInteractionEffect(AnovaCalculatedEffect effect) {
		this.interactionEffects.add(effect);
	}

	@Override
	public List<IParameterInfluenceDescriptor> getAllParameterInfluenceDescriptors() {
		List<IParameterInfluenceDescriptor> influenceList = new LinkedList<IParameterInfluenceDescriptor>();
		influenceList.addAll(getAllMainEffects());
		return influenceList;
	}

	@Override
	public IParameterInfluenceDescriptor getParameterInfluenceDescriptorByParam(ParameterDefinition parameter) {

		for (IParameterInfluenceDescriptor infDescr : getAllMainEffects()) {
			if (infDescr.getIndependentParameter().equals(parameter)) {
				return infDescr;
			}
		}
		return null;
	}

	@Override
	public AnalysisConfiguration getAnalysisStrategyConfiguration() {

		return configuration;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public List<AnovaCalculatedEffect> getAllMainEffects() {
		return mainEffects;
	}

	@Override
	public List<AnovaCalculatedEffect> getMainEffects(double significanceLevel) {
		List<AnovaCalculatedEffect> resultList = new ArrayList<AnovaCalculatedEffect>();
		for (AnovaCalculatedEffect effect : mainEffects) {
			if (effect.getpValue() <= significanceLevel) {
				resultList.add(effect);
			}
		}

		return resultList;
	}

	@Override
	public List<AnovaCalculatedEffect> getAllInteractionEffects() {
		return interactionEffects;
	}

	@Override
	public List<AnovaCalculatedEffect> getInteractionEffects(int depth) {
		List<AnovaCalculatedEffect> resultList = new ArrayList<AnovaCalculatedEffect>();
		for (AnovaCalculatedEffect effect : interactionEffects) {
			if (effect.getIndependentParameters().size() == depth) {
				resultList.add(effect);
			}
		}

		return resultList;
	}

	@Override
	public List<AnovaCalculatedEffect> getInteractionEffects(double significanceLevel) {
		List<AnovaCalculatedEffect> resultList = new ArrayList<AnovaCalculatedEffect>();
		for (AnovaCalculatedEffect effect : interactionEffects) {
			if (effect.isSignificant(significanceLevel)) {
				resultList.add(effect);
			}
		}

		return resultList;
	}

	@Override
	public List<AnovaCalculatedEffect> getInteractionEffects(int depth, double significanceLevel) {
		List<AnovaCalculatedEffect> resultList = new ArrayList<AnovaCalculatedEffect>();
		for (AnovaCalculatedEffect effect : interactionEffects) {
			if (effect.getIndependentParameters().size() == depth && effect.isSignificant(significanceLevel)) {
				resultList.add(effect);
			}
		}

		return resultList;
	}

}

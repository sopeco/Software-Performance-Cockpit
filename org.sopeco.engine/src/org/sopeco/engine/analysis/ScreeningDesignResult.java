package org.sopeco.engine.analysis;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Result object describing the effects of parameters derived by a screening
 * analysis.
 * 
 * @author Pascal Meier
 * @author Dennis Westermann
 */
public class ScreeningDesignResult implements IScreeningAnalysisResult {

	private static final long serialVersionUID = 1L;

	/**
	 * Strategy used to create this result.
	 */
	private AnalysisConfiguration strategy;

	/**
	 * List of all derived ParameterEffect-objects.
	 */
	private List<ParameterEffect> parameterEffects;
	
	/**
	 * Unique identifier for this result instance.
	 */
	private String resultId;

	/**
	 * Constructor
	 * 
	 * @param strategy
	 *            the analysis strategy.
	 */
	public ScreeningDesignResult(AnalysisConfiguration strategy) {
		this.strategy = strategy;
		parameterEffects = new ArrayList<ParameterEffect>();
	}

	@Override
	public AnalysisConfiguration getAnalysisStrategyConfiguration() {
		return this.strategy;
	}

	public void addParameterEffect(ParameterEffect effect) {
		if (effect != null) {
			parameterEffects.add(effect);
		}
	}

	@Override
	public List<ParameterEffect> getAllInteractionEffects() {
		List<ParameterEffect> resultEffects = new ArrayList<ParameterEffect>();
		for (ParameterEffect effect : parameterEffects) {
			if (effect.getAnalysedParameters().size() >= 2) {
				resultEffects.add(effect);
			}
		}
		return resultEffects;
	}

	@Override
	public List<ParameterEffect> getAllMainEffects() {
		List<ParameterEffect> resultEffects = new ArrayList<ParameterEffect>();
		for (ParameterEffect effect : parameterEffects) {
			if (effect.getAnalysedParameters().size() == 1) {
				resultEffects.add(effect);
			}
		}
		return resultEffects;
	}

	@Override
	public List<ParameterEffect> getAllParameterEffects() {
		return parameterEffects;
	}

	@Override
	public ParameterEffect getInteractionEffectByParameters(
			List<ParameterDefinition> parameters) {

		for (ParameterEffect effect : parameterEffects) {
			if (parameters.size() == effect.getAnalysedParameters().size()
					&& effect.getAnalysedParameters().contains(parameters)) {
				return effect;
			}
		}
		return null;
	}

	@Override
	public List<ParameterEffect> getAllInteractionEffectsByParameters(
			List<ParameterDefinition> parameters) {

		List<ParameterEffect> resultEffects = new ArrayList<ParameterEffect>();
		for (ParameterEffect effect : parameterEffects) {
			if (effect.getAnalysedParameters().contains(parameters)) {
				resultEffects.add(effect);
			}
		}
		return resultEffects;
	}

	@Override
	public List<ParameterEffect> getInteractionEffectsOfSingleParam(
			ParameterDefinition param) {
		List<ParameterEffect> resultEffects = new ArrayList<ParameterEffect>();
		for (ParameterEffect effect : parameterEffects) {
			if (effect.getAnalysedParameters().size() >= 2) {
				if (effect.getAnalysedParameters().contains(param)) {
					resultEffects.add(effect);
				}
			}
		}
		return resultEffects;
	}

	@Override
	public ParameterEffect getMainEffectByParam(ParameterDefinition param) {
		for (ParameterEffect effect : parameterEffects) {
			if (effect.getAnalysedParameters().size() == 1) {
				if (effect.getAnalysedParameters().get(0).equals(param)) {
					return effect;
				}
			}
		}
		return null;
	}


	@Override
	public String getId() {
		return this.resultId;
	}

	@Override
	public void setId(String id) {
		
		this.resultId = id;
	}
}

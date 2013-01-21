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

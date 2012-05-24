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
public interface IScreeningAnalysisResult extends IAnalysisResult {

	/**
	 * Returns a list of all Main- and Interaction-ParameterEffects of all
	 * screened parameters.
	 * 
	 * @return list of ParameterEffects
	 */
	public List<ParameterEffect> getAllParameterEffects();

	/**
	 * Returns a list of all MainEffects of all parameters.
	 * 
	 * @return list of ParameterEffects
	 */
	public List<ParameterEffect> getAllMainEffects();

	/**
	 * Returns a list of all InteractionEffects of all parameters.
	 * 
	 * @return list of ParameterEffects
	 */
	public List<ParameterEffect> getAllInteractionEffects();

	/**
	 * Returns the object describing the Main-ParameterEffect of the specified
	 * parameter.
	 * 
	 * @return a ParameterEffect describing the main effect of the specified
	 *         parameter
	 */
	public ParameterEffect getMainEffectByParam(ParameterDefinition parameter);

	/**
	 * Returns all InteractionEffects of the specified parameter with other
	 * parameters if any are existent.
	 * 
	 * @param parameter
	 *            parameter to get interaction effects for
	 * @return list of ParameterEffects of the interactions, can be empty
	 */
	public List<ParameterEffect> getInteractionEffectsOfSingleParam(
			ParameterDefinition parameter);

	/**
	 * Returns a ParameterEffect-object which represents the single interaction
	 * effect of all the specified parameters.
	 * 
	 * @param parameters
	 *            the parameters of the desired Interaction-ParameterEffect
	 * @return value representing the interaction effect of the parameters, or
	 *         null if not existent
	 */
	public ParameterEffect getInteractionEffectByParameters(
			List<ParameterDefinition> parameters);

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
	public List<ParameterEffect> getAllInteractionEffectsByParameters(
			List<ParameterDefinition> parameters);

}

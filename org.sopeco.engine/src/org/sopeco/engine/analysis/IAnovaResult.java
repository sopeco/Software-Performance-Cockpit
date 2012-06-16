package org.sopeco.engine.analysis;

import java.util.List;

/**
 * Contains the result of an ANOVA test
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
	public List<AnovaCalculatedEffect> getAllMainEffects();

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
	public List<AnovaCalculatedEffect> getMainEffects(double significanceLevel);

	/**
	 * Returns a list of all ParameterEffect-Objects describing the F-value of
	 * the independent parameter.
	 * 
	 * @return list of all interaction effects
	 */
	public List<AnovaCalculatedEffect> getAllInteractionEffects();

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
	public List<AnovaCalculatedEffect> getInteractionEffects(int depth);

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
	public List<AnovaCalculatedEffect> getInteractionEffects(double significanceLevel);

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
	public List<AnovaCalculatedEffect> getInteractionEffects(int depth, double significanceLevel);

}

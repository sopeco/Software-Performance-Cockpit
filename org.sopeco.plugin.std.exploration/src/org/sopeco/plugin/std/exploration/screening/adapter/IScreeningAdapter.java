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

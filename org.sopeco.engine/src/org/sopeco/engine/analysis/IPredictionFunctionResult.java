package org.sopeco.engine.analysis;

import java.util.List;
import java.util.Map;

import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * An AnalysisResult contains the representation of a function derived by a
 * data-analysis.
 * 
 * @author Chris Heupel, Jens Happe, Dennis Westermann
 * 
 */
public interface IPredictionFunctionResult extends IAnalysisResult {

	/**
	 * Calculates the output parameter's {@link ParameterValue} for the given
	 * set of input parameters.
	 * 
	 * @param inputParameters
	 *            Values of the input parameters used to compute the output
	 *            value.
	 * 
	 * @return Result computed from the input values.
	 * @throws UnknownParameterException
	 * 
	 */
	ParameterValue<?> predictOutputParameter(List<ParameterValue<?>> inputParameters);

	/**
	 * Calculates the output parameter's {@link ParameterValue} for the given
	 * input parameter.
	 * 
	 * @param inputParameter
	 *            Value of the input parameter used to compute the output value.
	 * 
	 * @return Result computed from the input values.
	 * @throws UnknownParameterException
	 */
	ParameterValue<?> predictOutputParameter(ParameterValue<?> inputParameter);

	/**
	 * Returns a String-representation of the contained function.
	 * 
	 * @return The representing String.
	 */
	String getFunctionAsString();

	/**
	 * Returns a map that holds the information how the values of a non-numeric
	 * parameter have been encoded to an Integer representation.
	 * 
	 * @return a map that holds the information how the values of a non-numeric
	 *         parameter have been encoded to an Integer representation
	 */
	Map<ParameterDefinition, Map<Object, Integer>> getNonNumericParameterEncodings();

}

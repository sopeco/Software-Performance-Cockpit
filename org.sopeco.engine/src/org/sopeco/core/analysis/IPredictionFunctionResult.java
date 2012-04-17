package org.sopeco.core.analysis;

import java.util.List;

import org.sopeco.configuration.analysis.AnalysisStrategy;
import org.sopeco.core.exceptions.UnknownParameterException;
import org.sopeco.persistence.dataset.ParameterValue;


/**
 * An AnalysisResult contains the representation of a function derived by a
 * data-analysis.
 * 
 * @author Chris Heupel, Jens Happe
 * 
 */
public interface IPredictionFunctionResult extends IAnalysisResult{

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
	public ParameterValue<?> predictOutputParameter(List<ParameterValue<?>> inputParameters) throws UnknownParameterException;

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
	public ParameterValue<?> predictOutputParameter(ParameterValue<?> inputParameter) throws UnknownParameterException;

	/**
	 * Returns a String-representation of the contained function.
	 * 
	 * @return The representing String.
	 */
	public String getFunctionAsString();

	/**
	 * Returns the configuration that was used to derive this result.
	 * 
	 * @return Configuration used for the analysis.
	 */
	public AnalysisStrategy getAnalysisStrategyConfiguration();

}

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

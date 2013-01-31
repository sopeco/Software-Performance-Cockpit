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
 * Contains the result of a correlation based analysis of the influence of
 * parameters on a dependent parameter.
 * 
 * @author Dennis Westermann, Pascal Meier
 */
public interface ICorrelationResult extends IParameterInfluenceResult {

	/**
	 * Returns a list of all ParameterCorrelation-Objects describing the
	 * correlation of the independent parameters with the dependent parameter.
	 * 
	 * @return list of ParameterCorrelations
	 */
	List<ParameterCorrelation> getAllParameterCorrelations();

	/**
	 * Returns the ParameterCorrelation-Object of the specified parameter(s).
	 * 
	 * @param parameter
	 *            an independent parameter that has been part of the correlation
	 *            analysis.
	 * @return a {@link ParameterCorrelation} that describes the correlation
	 *         between the given independent parameter and the dependent
	 *         parameter used in the correlation analysis, <code>null</code> if
	 *         the given parameter is not in the list of independent parameters
	 *         of the correlation analysis that created the result.
	 */
	ParameterCorrelation getParameterCorrelationByParam(ParameterDefinition parameter);

}

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
package org.sopeco.engine.analysis.util;

import java.util.LinkedList;
import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * This class is a container class that holds the result of a parameter
 * relevance estimation (@see {@link ParameterRelevanceEstimator}).
 * 
 * @author Dennis Westermann
 * 
 */
public class ParameterRelevanceResult {

	ParameterDefinition dependentParameter;

	List<ParameterDefinition> relevantParameters;

	List<ParameterDefinition> irrelevantParameters;

	/**
	 * Adds a parameter to the list of relevant parameters in this result object.
	 * 
	 * @param relevantParameter
	 *            a parameter that is considered as relevant with
	 *            respect to its influence on the dependent parameter of this
	 *            result object
	 */
	public void addRelevantParameter(ParameterDefinition relevantParameter) {
		this.relevantParameters.add(relevantParameter);
	}

	/**
	 * Adds a parameter to the list of irrelevant parameters in this result object.
	 * 
	 * @param irrelevantParameter
	 *            a parameter that is considered as irrelevant with
	 *            respect to its influence on the dependent parameter of this
	 *            result object
	 */
	public void addIrrelevantParameter(ParameterDefinition irrelevantParameter) {
		this.irrelevantParameters.add(irrelevantParameter);
	}
	
	/**
	 * @param dependentParameter
	 *            the dependent parameter for which the independent parameters
	 *            are classified in relevant and irrelevant
	 */
	public ParameterRelevanceResult(ParameterDefinition dependentParameter) {
		relevantParameters = new LinkedList<ParameterDefinition>();
		irrelevantParameters = new LinkedList<ParameterDefinition>();
	}

	/**
	 * @return a list of parameter that are considered as relevant with respect
	 *         to their influence on the dependent parameter
	 */
	public List<ParameterDefinition> getRelevantParameters() {
		return relevantParameters;
	}

	/**
	 * @param relevantParameters
	 *            a list of parameters that are considered as relevant with
	 *            respect to their influence on the dependent parameter of this
	 *            result object
	 */
	public void setRelevantParameters(List<ParameterDefinition> relevantParameters) {
		this.relevantParameters = relevantParameters;
	}

	/**
	 * @return a list of parameter that are considered as irrelevant with respect
	 *         to their influence on the dependent parameter
	 */
	public List<ParameterDefinition> getIrrelevantParameters() {
		return irrelevantParameters;
	}

	/**
	 * @param relevantParameters
	 *            a list of parameters that are considered as irrelevant with
	 *            respect to their influence on the dependent parameter of this
	 *            result object
	 */
	public void setIrrelevantParameters(List<ParameterDefinition> irrelevantParameters) {
		this.irrelevantParameters = irrelevantParameters;
	}

}

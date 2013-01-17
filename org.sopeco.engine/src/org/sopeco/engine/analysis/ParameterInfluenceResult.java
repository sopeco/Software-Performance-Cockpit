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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Class represents the result of a parameter influence analysis.
 * 
 * @author Dennis Westermann
 * 
 */
public class ParameterInfluenceResult implements IParameterInfluenceResult {

	private static final long serialVersionUID = 1L;

	/** An id that uniquely identifies this result instance **/
	private String id;

	/** List to store influence values of the parameters */
	private HashMap<ParameterDefinition, IParameterInfluenceDescriptor> parameterInfluenceDescriptors;

	/**
	 * Configuration used to derive this result object.
	 */
	private AnalysisConfiguration configuration;

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            configuration of the correlation analysis
	 */
	public ParameterInfluenceResult(AnalysisConfiguration configuration) {
		this.configuration = configuration;
		parameterInfluenceDescriptors = new HashMap<ParameterDefinition, IParameterInfluenceDescriptor>();
	}

	/**
	 * Used to store a new ParameterCorrelation-object in the result.
	 * 
	 * @param influenceDescriptor
	 *            {@link IParameterInfluenceDescriptor} that describes the
	 *            correlation of a parameter with the observation parameter.
	 */
	public void addParameterInfluenceDescriptor(IParameterInfluenceDescriptor influenceDescriptor) {
		parameterInfluenceDescriptors.put(influenceDescriptor.getIndependentParameter(), influenceDescriptor);
	}

	@Override
	public AnalysisConfiguration getAnalysisStrategyConfiguration() {
		return configuration;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public List<IParameterInfluenceDescriptor> getAllParameterInfluenceDescriptors() {
		List<IParameterInfluenceDescriptor> resultList = new LinkedList<IParameterInfluenceDescriptor>();
		resultList.addAll(parameterInfluenceDescriptors.values());
		return resultList;
	}

	@Override
	public IParameterInfluenceDescriptor getParameterInfluenceDescriptorByParam(ParameterDefinition parameter) {
		return parameterInfluenceDescriptors.get(parameter);
	}

}
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
/**
 * 
 */
package org.sopeco.persistence.util;

import java.util.Collection;

import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Creates instances of {@link ParameterCollection}.
 * 
 * @author Roozbeh Farahbod
 */
public class ParameterCollectionFactory {

	/**
	 * Creates an empty parameter value collection.
	 */
	public static ParameterCollection<ParameterValue<?>> createParameterValueCollection() {
		return new ParameterCollection<ParameterValue<?>>();
	}

	/**
	 * Creates a parameter value collection based on the given values.
	 * 
	 * @param parameterValueCollection a collection of parameter values
	 * @return a parameter value collection that holds the given parameter values
	 */
	public static ParameterCollection<ParameterValue<?>> createParameterValueCollection(Collection<ParameterValue<?>> parameterValueCollection) {
		
		if (parameterValueCollection == null){
			return null;
		}
		final ParameterCollection<ParameterValue<?>> result = new ParameterCollection<ParameterValue<?>>();
		result.addAll(parameterValueCollection);
		return result;
	}
	
	/**
	 * Creates an empty parameter definition collection.
	 */
	public static ParameterCollection<ParameterDefinition> createParameterDefinitionCollection() {
		return new ParameterCollection<ParameterDefinition>();
	}

	/**
	 * Creates a parameter definition collection based on the given parameter definitions.
	 * 
	 * @param parameterDefCollection a collection of parameter definition
	 * @return a parameter definition collection that holds the given parameter definition
	 */
	public static ParameterCollection<ParameterDefinition> createParameterDefinitionCollection(Collection<ParameterDefinition> parameterDefCollection) {
		final ParameterCollection<ParameterDefinition> result = new ParameterCollection<ParameterDefinition>();
		result.addAll(parameterDefCollection);
		return result;
	}
}

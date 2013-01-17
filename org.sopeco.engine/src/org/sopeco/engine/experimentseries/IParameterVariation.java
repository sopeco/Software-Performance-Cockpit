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
package org.sopeco.engine.experimentseries;

import java.util.Iterator;

import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

/**
 * Extension artifact interface that determines the values of a parameter based
 * on its configuration.
 * 
 * @author Jens Happe, Roozbeh Farahbod
 * 
 */
public interface IParameterVariation extends ISoPeCoExtensionArtifact, Iterable<ParameterValue<?>> {

	/**
	 * Initializes this parameter variation with the given parameter value
	 * assignment. This method has to be called before calling any other methods
	 * of this class.
	 * 
	 * @param configuration
	 *            parameter value assignment configuration
	 */
	void initialize(ParameterValueAssignment configuration);

	/**
	 * Returns the value at position <code>pos</code> in the value variation
	 * series.
	 * 
	 * @param pos
	 *            the position of the value in the variation
	 * @return a parameter value
	 */
	ParameterValue<?> get(int pos);

	/**
	 * Returns the number of values provided by this variation.
	 * @return number of values provided by this variation
	 */
	int size();

	/**
	 * Returns the parameter this variation is associated to.
	 * @return the parameter this variation is associated to
	 */
	ParameterDefinition getParameter();

	/**
	 * Returns the iterator of this parameter variations. Different calls to
	 * this method should return the same iterator, unless the {@link #reset()}
	 * method is called.
	 * @return iterator of this parameter variations
	 */
	Iterator<ParameterValue<?>> iterator();

	/**
	 * Resets the iterator on this instance.
	 */
	void reset();

	/**
	 * Checks if this parameter variation can be applied to the given value
	 * assignment. This method is recommended to be called before initializing
	 * this parameter variation.
	 * 
	 * @param configuration
	 *            a parameter value assignment
	 * @return <code>true</code> if this variation suits the given assignment.
	 */
	boolean canVary(ParameterValueAssignment configuration);
}

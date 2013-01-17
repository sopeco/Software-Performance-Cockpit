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
package org.sopeco.plugin.std.parametervariation.bool;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueFactory;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;
import org.sopeco.util.Tools.SupportedTypes;

/**
 * Provides a Boolean value variation which just switches between the two boolean values. 
 * 
 * @author Roozbeh Farahbod
 *
 */
public class BooleanVariation extends AbstractSoPeCoExtensionArtifact implements IParameterVariation {

//	private static final Logger logger = LoggerFactory.getLogger(BooleanVariation.class);
	
	private Iterator<ParameterValue<?>> iterator = null;

	/** Holds the dynamic value assignment configuration for this variation. */
	protected DynamicValueAssignment dynamicValueAssignment = null;
	
	public BooleanVariation(ISoPeCoExtension<?> provider) {
		super(provider);
	}

	@Override
	public void initialize(ParameterValueAssignment configuration) {
		dynamicValueAssignment = (DynamicValueAssignment) configuration;
	}

	@Override
	public ParameterValue<?> get(int pos) {
		// the list of values is [false, true]
		if (pos < 2) {
			return ParameterValueFactory.createParameterValue(dynamicValueAssignment.getParameter(), (pos == 1));
		} else
			throw new IndexOutOfBoundsException("Parameter value index " + pos + " cannot be greater than 1 for a Boolean value variation.");
	}

	@Override
	public int size() {
		return 2;
	}

	@Override
	public ParameterDefinition getParameter() {
		return dynamicValueAssignment.getParameter();
	}

	@Override
	public Iterator<ParameterValue<?>> iterator() {
		if (iterator == null) {
			iterator = new BooleanValueIterator();
		}
		return iterator;
	}

	@Override
	public void reset() {
		iterator = null;
	}

	/**
	 * Returns true if the given configuration is a dynamic value assignment on a Boolean parameter.
	 */
	@Override
	public boolean canVary(ParameterValueAssignment configuration) {
		final SupportedTypes paramType = SupportedTypes.get(configuration.getParameter().getType());
		final boolean typeCheck = (paramType != null) && (paramType == SupportedTypes.Boolean);
		return (configuration instanceof DynamicValueAssignment) && typeCheck;
	}

	/**
	 * Implements the Boolean variation iterator.
	 *  
	 * @author Roozbeh Farahbod
	 *
	 */
	protected class BooleanValueIterator implements Iterator<ParameterValue<?>> {

		private int pos = 0;
		
		@Override
		public boolean hasNext() {
			return pos < size();
		}

		@Override
		public ParameterValue<?> next() {
			if (hasNext()) {
				return get(pos++);
			} else {
				throw new NoSuchElementException("Boolean value iterator has no more elements.");
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}

}

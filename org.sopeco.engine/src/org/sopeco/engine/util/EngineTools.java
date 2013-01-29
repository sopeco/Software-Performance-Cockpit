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
package org.sopeco.engine.util;

import java.util.Collection;

import org.sopeco.engine.experimentseries.IConstantAssignment;
import org.sopeco.engine.experimentseries.IConstantAssignmentExtension;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.persistence.util.ParameterCollectionFactory;

/**
 * A collection of utility methods needed in the context of SoPeCo experiments.
 * 
 * @author Roozbeh Farahbod
 * 
 */
public final class EngineTools {

	/**
	 * Utility class. Only static methods, thus private constructor.
	 */
	private EngineTools() {

	}

	/**
	 * Returns a collection of parameter value assignments based on the given
	 * constant value assignments.
	 * 
	 * @param constantValueAssignments
	 *            constant value assignments where to read the parameter values
	 *            from
	 * @return returns a collection of {@link ParameterValue}; never returns
	 *         null.
	 */
	public static ParameterCollection<ParameterValue<?>> getConstantParameterValues(
			Collection<ConstantValueAssignment> constantValueAssignments) {
		ParameterCollection<ParameterValue<?>> result = ParameterCollectionFactory.createParameterValueCollection();

		for (ConstantValueAssignment cva : constantValueAssignments) {
			ParameterValue<?> pv = getConstantParameterValue(cva);
			if (pv != null) {
				result.add(pv);
			} else {
				throw new RuntimeException("Parameter value is null!");
			}
		}
		return result;
	}

	/**
	 * Returns a single parameter value assignment based on the given constant
	 * value assignment.
	 * 
	 * @param constantValueAssignment
	 *            constant value assignments where to read the parameter value
	 *            from
	 * @return returns a {@link ParameterValue}. If the constant value is not
	 *         supported, returns <code>null</code>.
	 */
	public static ParameterValue<?> getConstantParameterValue(ConstantValueAssignment constantValueAssignment) {
		IConstantAssignment ca = ExtensionRegistry.getSingleton().getExtensionArtifact(
				IConstantAssignmentExtension.class, constantValueAssignment.getParameter().getType());
		if (ca.canAssign(constantValueAssignment)) {
			return ca.createParameterValue(constantValueAssignment);
		} else {
			return null;
		}
	}

}

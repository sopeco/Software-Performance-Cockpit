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
package org.sopeco.persistence.entities.definition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the experiment definition information provided by the measurement environment.
 * 
 * @author Dennis Westermann
 *
 */
public class MeasurementEnvironmentDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	protected ParameterNamespace root;
	
	protected List<ExperimentTerminationCondition> supportedTerminationConditions = new ArrayList<ExperimentTerminationCondition>();


	public MeasurementEnvironmentDefinition() {
		super();
	}
	
	public List<ExperimentTerminationCondition> getSupportedTerminationConditions() {
		return supportedTerminationConditions;
	}
	
	public void setSupportedTerminationConditions(List<ExperimentTerminationCondition> supportedTerminationConditions){
		this.supportedTerminationConditions = supportedTerminationConditions;
	}

	public ParameterNamespace getRoot() {
		return root;
	}

	public void setRoot(ParameterNamespace newRoot) {
		root = newRoot;
	}

	/**
	 * Looks for a namespace with the given name (Example: "org.sopeco").
	 * 
	 * @param name
	 * @return the namespace with the given name (Example: "org.sopeco").
	 *         <code>null</code> if the {@link MeasurementEnvironmentDefinition}
	 *         does not contain a namespace with the given name.
	 */
	public ParameterNamespace getNamespace(String name) {
		return searchNamespace(root, name);
	}

	private ParameterNamespace searchNamespace(ParameterNamespace parent, String name) {
		if (parent.getFullName().equals(name)) {
			return parent;
		} else {
			for (ParameterNamespace child : parent.getChildren()) {
				ParameterNamespace childSearchResult = searchNamespace(child, name);
				if (childSearchResult != null) {
					return childSearchResult;
				}
			}
		}

		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null) 
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeasurementEnvironmentDefinition other = (MeasurementEnvironmentDefinition) obj;
		return this.getRoot().equals(other.getRoot());
	}

	@Override
	public int hashCode() {
		return getRoot() != null ? getRoot().hashCode() : 0;
	}

}
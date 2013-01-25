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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Dennis Westermann
 * 
 */
public class ExperimentSeriesDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	protected ExplorationStrategy explorationStrategy;

	protected List<ParameterValueAssignment> experimentAssignments=new ArrayList<ParameterValueAssignment>();;

	protected List<ConstantValueAssignment> preperationAssignments=new ArrayList<ConstantValueAssignment>();;

	protected Set<ExperimentTerminationCondition> terminationConditions = new HashSet<ExperimentTerminationCondition>();

	protected String name = null;



	public ExperimentSeriesDefinition() {
		super();
	}

	public Set<ExperimentTerminationCondition> getTerminationConditions() {
		return terminationConditions;
	}

	public ExplorationStrategy getExplorationStrategy() {
		return explorationStrategy;
	}

	public void setExplorationStrategy(ExplorationStrategy newExplorationStrategy) {
		explorationStrategy = newExplorationStrategy;
	}

	public List<ParameterValueAssignment> getExperimentAssignments() {
		if (experimentAssignments == null) {
			experimentAssignments = new ArrayList<ParameterValueAssignment>();
		}
		return experimentAssignments;
	}

	public List<ConstantValueAssignment> getPreperationAssignments() {
		if (preperationAssignments == null) {
			preperationAssignments = new ArrayList<ConstantValueAssignment>();
		}
		return preperationAssignments;
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}



	public void addTerminationCondition(ExperimentTerminationCondition tc) {
		terminationConditions.add(tc);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((experimentAssignments == null) ? 0 : experimentAssignments.hashCode());
		result = prime * result + ((explorationStrategy == null) ? 0 : explorationStrategy.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((preperationAssignments == null) ? 0 : preperationAssignments.hashCode());
		result = prime * result + ((terminationConditions == null) ? 0 : terminationConditions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExperimentSeriesDefinition other = (ExperimentSeriesDefinition) obj;
		if (experimentAssignments == null) {
			if (other.experimentAssignments != null)
				return false;
		} else if (!experimentAssignments.equals(other.experimentAssignments))
			return false;
		if (explorationStrategy == null) {
			if (other.explorationStrategy != null)
				return false;
		} else if (!explorationStrategy.equals(other.explorationStrategy))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (preperationAssignments == null) {
			if (other.preperationAssignments != null)
				return false;
		} else if (!preperationAssignments.equals(other.preperationAssignments))
			return false;
		if (terminationConditions == null) {
			if (other.terminationConditions != null)
				return false;
		} else if (!terminationConditions.equals(other.terminationConditions))
			return false;
		return true;
	}



}
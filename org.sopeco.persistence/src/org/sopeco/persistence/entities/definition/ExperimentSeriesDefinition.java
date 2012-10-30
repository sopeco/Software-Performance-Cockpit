/**
 * <copyright>
 * </copyright>
 *
 * $Id$
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

	protected List<ParameterValueAssignment> experimentAssignments;

	protected List<ConstantValueAssignment> preperationAssignments;

	protected Set<ExperimentTerminationCondition> terminationConditions = new HashSet<ExperimentTerminationCondition>();

	protected String name = null;

	private long version = 0;

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

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
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
		if (getExperimentAssignments() == null) {
			if (other.getExperimentAssignments() != null)
				return false;
		} else if (!getExperimentAssignments().equals(other.getExperimentAssignments()))
			return false;
		if (getExplorationStrategy() == null) {
			if (other.getExplorationStrategy() != null)
				return false;
		} else if (!getExplorationStrategy().equals(other.getExplorationStrategy()))
			return false;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		if (getPreperationAssignments() == null) {
			if (other.getPreperationAssignments() != null)
				return false;
		} else if (!getPreperationAssignments().equals(other.getPreperationAssignments()))
			return false;
		if (getTerminationConditions() == null) {
			if (other.getTerminationConditions() != null)
				return false;
		} else if (!getTerminationConditions().equals(other.getTerminationConditions()))
			return false;
		return true;
	}

}
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.persistence.entities.definition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Dennis Westermann
 *
 */
public class ExperimentSeriesDefinition implements Serializable {
	
	private static final long serialVersionUID = 1L;

	protected ExplorationStrategy explorationStrategy;

	protected List<ParameterValueAssignment> experimentAssignments;

	protected ExperimentTerminationCondition experimentTerminationCondition;

	protected List<ConstantValueAssignment> preperationAssignments;

	protected String name = null;
	
	private long version = 0;

	public ExperimentSeriesDefinition() {
		super();
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

	public ExperimentTerminationCondition getExperimentTerminationCondition() {
		return experimentTerminationCondition;
	}

	public void setExperimentTerminationCondition(ExperimentTerminationCondition newExperimentTerminationCondition) {
		experimentTerminationCondition = newExperimentTerminationCondition;
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

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((experimentAssignments == null|| experimentAssignments.isEmpty()) ? 0 : experimentAssignments.hashCode());
		result = prime * result + ((experimentTerminationCondition == null) ? 0 : experimentTerminationCondition.hashCode());
		result = prime * result + ((explorationStrategy == null) ? 0 : explorationStrategy.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((preperationAssignments == null|| preperationAssignments.isEmpty()) ? 0 : preperationAssignments.hashCode());
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (experimentAssignments == null || experimentAssignments.isEmpty()) {
			if (other.experimentAssignments != null && !other.experimentAssignments.isEmpty())
				return false;
		} else if (!experimentAssignments.equals(other.experimentAssignments))
			return false;
		if (experimentTerminationCondition == null) {
			if (other.experimentTerminationCondition != null)
				return false;
		} else if (!experimentTerminationCondition.equals(other.experimentTerminationCondition))
			return false;
		if (explorationStrategy == null) {
			if (other.explorationStrategy != null)
				return false;
		} else if (!explorationStrategy.equals(other.explorationStrategy))
			return false;
		
		if (preperationAssignments == null || preperationAssignments.isEmpty()) {
			if (other.preperationAssignments != null && !other.preperationAssignments.isEmpty())
				return false;
		} else if (!preperationAssignments.equals(other.preperationAssignments))
			return false;
		return true;
	}
	
	

	public long getVersion() {
		return version;
	}





	public void setVersion(long version) {
		this.version = version;
	}

	
	
	
} 
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
	
} 
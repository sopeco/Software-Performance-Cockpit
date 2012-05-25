package org.sopeco.persistence.entities.definition;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dennis Westermann
 * 
 */
public class MeasurementSpecification implements Serializable {

	private static final long serialVersionUID = 1L;

	protected List<ExperimentSeriesDefinition> experimentSeriesDefinitions;

	protected List<ConstantValueAssignment> initializationAssignemts;

	public MeasurementSpecification() {
		super();
	}

	public List<ExperimentSeriesDefinition> getExperimentSeriesDefinitions() {
		if (experimentSeriesDefinitions == null) {
			experimentSeriesDefinitions = new LinkedList<ExperimentSeriesDefinition>();
		}
		return experimentSeriesDefinitions;
	}

	public List<ConstantValueAssignment> getInitializationAssignemts() {
		if (initializationAssignemts == null) {
			initializationAssignemts = new LinkedList<ConstantValueAssignment>();
		}
		return initializationAssignemts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((experimentSeriesDefinitions == null) ? 0 : experimentSeriesDefinitions.hashCode());
		result = prime * result + ((initializationAssignemts == null) ? 0 : initializationAssignemts.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		MeasurementSpecification other = (MeasurementSpecification) obj;
		if (experimentSeriesDefinitions == null) {
			if (other.experimentSeriesDefinitions != null)
				return false;
		} else if (!experimentSeriesDefinitions.equals(other.experimentSeriesDefinitions))
			return false;
		if (initializationAssignemts == null) {
			if (other.initializationAssignemts != null)
				return false;
		} else if (!initializationAssignemts.equals(other.initializationAssignemts))
			return false;
		return true;
	}

	
}
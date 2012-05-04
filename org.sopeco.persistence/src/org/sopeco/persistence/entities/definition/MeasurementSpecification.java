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

}
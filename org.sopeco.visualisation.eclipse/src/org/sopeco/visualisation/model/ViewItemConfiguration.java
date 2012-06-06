package org.sopeco.visualisation.model;

import java.util.Map;

import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

public class ViewItemConfiguration {
	private ExperimentSeriesRun experimentSeriesRun;
	private ParameterDefinition xParameter;
	private ParameterDefinition yParameter;
	private Map<ParameterDefinition, Object> valueAssignments;
	private ParameterDefinition comparisonParameter;

	public ParameterDefinition getxParameter() {
		return xParameter;
	}

	public void setxParameter(ParameterDefinition xParameter) {
		this.xParameter = xParameter;
	}

	public ParameterDefinition getyParameter() {
		return yParameter;
	}

	public void setyParameter(ParameterDefinition yParameter) {
		this.yParameter = yParameter;
	}

	public Map<ParameterDefinition, Object> getValueAssignments() {
		return valueAssignments;
	}

	public void setValueAssignments(Map<ParameterDefinition, Object> valueAssignments) {
		this.valueAssignments = valueAssignments;
	}

	public ExperimentSeriesRun getExperimentSeriesRun() {
		return experimentSeriesRun;
	}

	public void setExperimentSeriesRun(ExperimentSeriesRun experimentSeriesRun) {
		this.experimentSeriesRun = experimentSeriesRun;
	}

	public ParameterDefinition getComparisonParameter() {
		return comparisonParameter;
	}

	public void setComparisonParameter(ParameterDefinition comparisonParameter) {
		this.comparisonParameter = comparisonParameter;
	}
}

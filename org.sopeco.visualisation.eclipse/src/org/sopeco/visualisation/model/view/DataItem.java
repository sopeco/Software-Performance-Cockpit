package org.sopeco.visualisation.model.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

public class DataItem {
	private ExperimentSeriesRun data;

	private ParameterDefinition xParameter;
	private ParameterDefinition yParameter;
	private Map<ParameterDefinition, Object> valueAssignments;

	private List<AbstractPreprocessingStep> preprocessingSteps;

	public DataItem() {
		valueAssignments = new HashMap<ParameterDefinition, Object>();
		preprocessingSteps = new ArrayList<AbstractPreprocessingStep>();
	}

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

	public List<AbstractPreprocessingStep> getPreprocessingSteps() {
		return preprocessingSteps;
	}

	public void setPreprocessingSteps(List<AbstractPreprocessingStep> preprocessingSteps) {
		this.preprocessingSteps = preprocessingSteps;
	}

	public ExperimentSeriesRun getData() {
		return data;
	}

	public void setData(ExperimentSeriesRun data) {
		this.data = data;
	}

	public Map<ParameterDefinition, Object> getValueAssignments() {
		return valueAssignments;
	}

	public void setValueAssignments(Map<ParameterDefinition, Object> valueAssignments) {
		this.valueAssignments = valueAssignments;
	}

	public void addValueAssignment(ParameterDefinition parameter, Object value) {
		if (valueAssignments != null) {
			valueAssignments.put(parameter, value);
		}
	}

	public boolean xParameterUsed() {
		return xParameter != null;
	}
	
	

}

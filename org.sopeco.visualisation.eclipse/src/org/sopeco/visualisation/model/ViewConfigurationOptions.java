package org.sopeco.visualisation.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.analysis.common.AbstractAnalysisStrategy;
import org.sopeco.util.Tools;

public class ViewConfigurationOptions {
	private Map<ParameterDefinition, Collection<Object>> inputParameterAssignmentOptions;
	private List<ParameterDefinition> outputParameters;
	private List<AbstractAnalysisStrategy> analysisStrategies;

	public ViewConfigurationOptions() {
		inputParameterAssignmentOptions = new HashMap<ParameterDefinition, Collection<Object>>();
	}

	public List<ParameterDefinition> getNumericInputParameters() {

		List<ParameterDefinition> numericInputParameter = new ArrayList<ParameterDefinition>();
		for (ParameterDefinition parameter : inputParameterAssignmentOptions.keySet()) {
			if (isNumericParameter(parameter)) {
				numericInputParameter.add(parameter);
			}
		}
		return numericInputParameter;
	}

	public List<ParameterDefinition> getInputParameters() {
		return new ArrayList<ParameterDefinition>(inputParameterAssignmentOptions.keySet());
	}

	public void setInputParameterAssignmentOptions(ParameterDefinition inputParameter, Collection<Object> values) {
		getInputParameterAssignmentOptions().put(inputParameter, values);
	}

	public List<ParameterDefinition> getOutputParameters() {
		return outputParameters;
	}

	public void setOutputParameters(List<ParameterDefinition> outputParameters) {
		this.outputParameters = outputParameters;
	}

	public List<AbstractAnalysisStrategy> getAnalysisStrategies() {
		return analysisStrategies;
	}

	public void setAnalysisStrategies(List<AbstractAnalysisStrategy> analysisStrategies) {
		this.analysisStrategies = analysisStrategies;
	}

	public Map<ParameterDefinition, Collection<Object>> getInputParameterAssignmentOptions() {
		return inputParameterAssignmentOptions;
	}

	protected boolean isNumericParameter(ParameterDefinition pDef) {
		if (Tools.SupportedTypes.valueOf(pDef.getType()).equals(Tools.SupportedTypes.Integer)) {
			return true;
		}
		if (Tools.SupportedTypes.valueOf(pDef.getType()).equals(Tools.SupportedTypes.Double)) {
			return true;
		}
		return false;
	}

}

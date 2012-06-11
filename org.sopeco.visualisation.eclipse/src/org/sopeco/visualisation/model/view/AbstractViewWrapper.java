package org.sopeco.visualisation.model.view;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;
import org.sopeco.util.Tools;
import org.sopeco.visualisation.model.ErrorStatus;
import org.sopeco.visualisation.model.ErrorType;
import org.sopeco.visualisation.model.IViewModel;
import org.sopeco.visualisation.model.ViewConfigurationOptions;

public abstract class AbstractViewWrapper implements IViewModel {

	
	public ViewConfigurationOptions getConfigurationAlternatives(ExperimentSeriesRun experimentSeriesRun, ErrorStatus errorStatus) {
		resetErrorStatus(errorStatus);

		ViewConfigurationOptions configAlternatives = new ViewConfigurationOptions();

		
		if(experimentSeriesRun.getSuccessfulResultDataSet() == null || experimentSeriesRun.getSuccessfulResultDataSet().getRowList().isEmpty()){
			if (errorStatus != null) {
				errorStatus.setErrorType(ErrorType.EmptyDataset);
			}
			return configAlternatives;
		}
		
		for (ParameterDefinition pDef : getVariedInputParameters(experimentSeriesRun)) {
			configAlternatives.setInputParameterAssignmentOptions(pDef, experimentSeriesRun.getSuccessfulResultDataSet().getInputColumn(pDef).getValueSet());
		}

		if (getVariedInputParameters(experimentSeriesRun).size() < 1) {
			if (errorStatus != null) {
				errorStatus.setErrorType(ErrorType.NoInputParameter);
			}
		}

		configAlternatives.setOutputParameters(getNumericObservationParameters(experimentSeriesRun));
		if (configAlternatives.getOutputParameters().size() < 1) {
			if (errorStatus != null) {
				errorStatus.setErrorType(ErrorType.NoObservationParameter);
			}
		}

		// TODO: analysis

		return configAlternatives;
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
	
	protected List<ParameterDefinition> getNumericObservationParameters(ExperimentSeriesRun experimentSeriesRun) {
		List<ParameterDefinition> numericObservationParameter = new ArrayList<ParameterDefinition>();
		for (ParameterDefinition pDef : experimentSeriesRun.getSuccessfulResultDataSet().getParameterDefinitions()) {
			if (pDef.getRole().equals(ParameterRole.OBSERVATION) && isNumericParameter(pDef)) {
				numericObservationParameter.add(pDef);
			}
		}
		return numericObservationParameter;
	}
	
	protected List<ParameterDefinition> getNumericParameters(ExperimentSeriesRun experimentSeriesRun) {
		List<ParameterDefinition> numericParameter = new ArrayList<ParameterDefinition>();
		for (ParameterDefinition pDef : experimentSeriesRun.getSuccessfulResultDataSet().getParameterDefinitions()) {
			if (isNumericParameter(pDef)) {
				numericParameter.add(pDef);
			}
		}
		return numericParameter;
	}

	protected List<ParameterDefinition> getNumericVariedInputParameters(ExperimentSeriesRun experimentSeriesRun) {
		List<ParameterDefinition> variedInputParameter = new ArrayList<ParameterDefinition>();
		for (ParameterValueAssignment pva : experimentSeriesRun.getExperimentSeries().getExperimentSeriesDefinition().getExperimentAssignments()) {
			if (pva instanceof DynamicValueAssignment && isNumericParameter(pva.getParameter())) {
				variedInputParameter.add(pva.getParameter());
			}
		}
		return variedInputParameter;
	}
	
	protected List<ParameterDefinition> getVariedInputParameters(ExperimentSeriesRun experimentSeriesRun) {
		List<ParameterDefinition> variedInputParameter = new ArrayList<ParameterDefinition>();
		for (ParameterValueAssignment pva : experimentSeriesRun.getExperimentSeries().getExperimentSeriesDefinition().getExperimentAssignments()) {
			if (pva instanceof DynamicValueAssignment) {
				variedInputParameter.add(pva.getParameter());
			}
		}
		return variedInputParameter;
	}

	protected void resetErrorStatus(ErrorStatus errorStatus) {
		if (errorStatus != null) {
			errorStatus.setErrorType(ErrorType.None);
		}
	}
	

	
}

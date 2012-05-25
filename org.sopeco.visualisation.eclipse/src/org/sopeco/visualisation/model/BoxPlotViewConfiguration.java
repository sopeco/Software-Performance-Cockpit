package org.sopeco.visualisation.model;

import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Alternatives for BoxPlot view configuration
 * @author Alexander Wert
 *
 */
public class BoxPlotViewConfiguration {
	
	
	private List<ParameterDefinition> inputParameters;
	private List<ParameterDefinition> outputParameters;

	/**
	 * returns all possible observation parameters
	 * @return
	 */
	public List<ParameterDefinition> getOutputParameters() {
		return outputParameters;
	}

	public void setOutputParameters(List<ParameterDefinition> outputParameters) {
		this.outputParameters = outputParameters;
	}

	/**
	 * returns all possible input parameters
	 * @return
	 */
	public List<ParameterDefinition> getInputParameters() {
		return inputParameters;
	}

	public void setInputParameters(List<ParameterDefinition> inputParameters) {
		this.inputParameters = inputParameters;
	}
}

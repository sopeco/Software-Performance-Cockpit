package org.sopeco.visualisation.model;

import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

public class BoxPlotData {
	
	private int id;
	private String label;
	private List<Object> data;
	private ParameterDefinition observationParameter;

	/**
	 * Returns id of the data item
	 * @return
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns label of the data item
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Returns a list of values to be plotted by in a boxplot
	 * @return
	 */
	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}

	/**
	 * Returns the observation parameter
	 * @return
	 */
	public ParameterDefinition getObservationParameter() {
		return observationParameter;
	}

	public void setObservationParameter(ParameterDefinition observationParameter) {
		this.observationParameter = observationParameter;
	}
}

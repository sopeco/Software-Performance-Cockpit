package org.sopeco.visualisation.model;

import org.sopeco.persistence.dataset.SimpleDataSet;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Data, representing a curve in a function view
 * 
 * @author Alexander Wert
 * 
 */
public class FunctionData {

	private int id;
	private String label;
	private SimpleDataSet data;
	private ParameterDefinition inputParameter;
	private ParameterDefinition observationParameter;

	/**
	 * returns the id of the curve
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * sets the id of the curve
	 * 
	 * @return
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * returns the two-dimensional dataset of the curve
	 * 
	 * @return
	 */
	public SimpleDataSet getData() {
		return data;
	}

	/**
	 * sets the two-dimensional dataset of the curve
	 * 
	 * @return
	 */
	public void setData(SimpleDataSet data) {
		this.data = data;
	}

	/**
	 * returns the label of the curve
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * sets the label of the curve
	 * 
	 * @return
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * returns the x parameter of the curve
	 * 
	 * @return
	 */
	public ParameterDefinition getInputParameter() {
		return inputParameter;
	}

	/**
	 * sets the x parameter of the curve
	 * 
	 * @return
	 */
	public void setXParameter(ParameterDefinition inputParameter) {
		this.inputParameter = inputParameter;
	}

	/**
	 * returns the y parameter of the curve
	 * 
	 * @return
	 */
	public ParameterDefinition getObservationParameter() {
		return observationParameter;
	}

	/**
	 * sets the y parameter of the curve
	 * 
	 * @return
	 */
	public void setYParameter(ParameterDefinition observationParameter) {
		this.observationParameter = observationParameter;
	}
}

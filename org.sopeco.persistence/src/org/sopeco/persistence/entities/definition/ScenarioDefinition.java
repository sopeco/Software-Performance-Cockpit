package org.sopeco.persistence.entities.definition;

import java.io.Serializable;
import java.util.List;

/**
 * @author Dennis Westermann, Jens Happe
 * 
 */
public class ScenarioDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String scenarioName = null;

	protected String definitionId;

	protected MeasurementEnvironmentDefinition measurementEnvironmentDefinition;

	protected MeasurementSpecification measurementSpecification;

	public ScenarioDefinition() {
		super();
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String newName) {
		this.scenarioName = newName;
	}

	public String getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	public MeasurementEnvironmentDefinition getMeasurementEnvironmentDefinition() {
		return measurementEnvironmentDefinition;
	}

	public void setMeasurementEnvironmentDefinition(MeasurementEnvironmentDefinition newMeasurementEnvironmentDefinition) {
		measurementEnvironmentDefinition = newMeasurementEnvironmentDefinition;
	}

	public MeasurementSpecification getMeasurementSpecification() {
		return measurementSpecification;
	}

	public void setMeasurementSpecification(MeasurementSpecification newMeasurementSpecification) {
		measurementSpecification = newMeasurementSpecification;
	}

	/*
	 * Utility functions
	 */

	/**
	 * Looks for the experiment series definition with the given name.
	 * 
	 * @param name
	 *            - the name of the requested experiment series definition
	 * @return the {@link ExperimentSeriesDefinition} instance with the given
	 *         name; <code>null</code> if no {@link ExperimentSeriesDefinition}
	 *         could be found
	 */
	public ExperimentSeriesDefinition getExperimentSeriesDefinition(String name) {
		for (ExperimentSeriesDefinition esd : this.getMeasurementSpecification().getExperimentSeriesDefinitions()) {
			if (esd.getName().equals(name))
				return esd;
		}
		return null;
	}

	/**
	 * Looks for the parameter definition with the given full name (namespace +
	 * name).
	 * 
	 * @param fullName
	 *            - the full name (namespace + name) of the requested
	 *            {@link ParameterDefinition}
	 * @return the {@link ParameterDefinition} instance with the given full
	 *         name; <code>null</code> if no {@link ParameterDefinition} could
	 *         be found
	 */
	public ParameterDefinition getParameterDefinition(String fullName) {

		List<ParameterDefinition> listOfAllParameters = this.getMeasurementEnvironmentDefinition().getRoot()
				.getAllParameters();

		for (ParameterDefinition parameterDefinition : listOfAllParameters) {
			if (parameterDefinition.getFullName().equals(fullName)) {
				return parameterDefinition;
			}
		}

		throw new IllegalArgumentException("Unknown Parameter: " + fullName);
	}

	/*
	 * Overrides
	 */

	@Override
	public String toString() {

		return "ScenarioDefinition{" + "scenarioName='" + scenarioName + '\'' 
				+ ", definitionId='" + definitionId + '\'' + '}';
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((measurementEnvironmentDefinition == null) ? 0 : measurementEnvironmentDefinition.hashCode());
		result = prime * result + ((measurementSpecification == null) ? 0 : measurementSpecification.hashCode());
		result = prime * result + ((scenarioName == null) ? 0 : scenarioName.hashCode());
		result = prime * result + ((definitionId == null) ? 0 : definitionId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		ScenarioDefinition other = (ScenarioDefinition) obj;
		if (measurementEnvironmentDefinition == null) {
			if (other.measurementEnvironmentDefinition != null)
				return false;
		} else if (!measurementEnvironmentDefinition.equals(other.measurementEnvironmentDefinition))
			return false;
		if (measurementSpecification == null) {
			if (other.measurementSpecification != null)
				return false;
		} else if (!measurementSpecification.equals(other.measurementSpecification))
			return false;
		if (scenarioName == null) {
			if (other.scenarioName != null)
				return false;
		} else if (!scenarioName.equals(other.scenarioName))
			return false;
		if (definitionId == null) {
			if (other.definitionId != null)
				return false;
		} else if (!definitionId.equals(other.definitionId))
			return false;
		return true;
	}

}
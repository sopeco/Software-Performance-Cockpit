package org.sopeco.persistence.entities.definition;

import java.io.Serializable;
import java.util.List;

/**
 * @author Dennis Westermann, Jens Happe
 *
 */
public class ScenarioDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String name = null;

	protected MeasurementEnvironmentDefinition measurementEnvironmentDefinition;

	protected MeasurementSpecification measurementSpecification;

	public ScenarioDefinition() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		name = newName;
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

		List<ParameterDefinition> listOfAllParameters = this.getMeasurementEnvironmentDefinition().getRoot().getAllParameters();

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

		return "ScenarioDefinition{" + "name='" + name + '\'' + '}';
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((measurementEnvironmentDefinition == null) ? 0 : measurementEnvironmentDefinition.hashCode());
		result = prime * result + ((measurementSpecification == null) ? 0 : measurementSpecification.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
package org.sopeco.persistence.entities.definition;

import java.io.Serializable;
import java.util.List;

/**
 * @author Dennis Westermann
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

		return null;
	}

	/*
	 * Overrides
	 */

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (o == null || !getClass().equals(o.getClass()))
			return false;

		ScenarioDefinition obj = (ScenarioDefinition) o;

		if (name != null ? !name.equals(obj.name) : obj.name != null)
			return false;

		return true;

	}

	@Override
	public int hashCode() {
		return (name != null ? name.hashCode() : 0);
	}

	@Override
	public String toString() {

		return "ScenarioDefinition{" + "name='" + name + '\'' + '}';
	}

}
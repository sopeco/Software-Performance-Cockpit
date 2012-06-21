package org.sopeco.persistence.entities.definition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Westermann, Jens Happe
 * 
 */
public class ScenarioDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String scenarioName = null;

	protected MeasurementEnvironmentDefinition measurementEnvironmentDefinition;

	protected List<MeasurementSpecification> measurementSpecifications = new ArrayList<MeasurementSpecification>();

	public ScenarioDefinition() {
		super();
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String newName) {
		this.scenarioName = newName;
	}

	public MeasurementEnvironmentDefinition getMeasurementEnvironmentDefinition() {
		return measurementEnvironmentDefinition;
	}

	public void setMeasurementEnvironmentDefinition(MeasurementEnvironmentDefinition newMeasurementEnvironmentDefinition) {
		measurementEnvironmentDefinition = newMeasurementEnvironmentDefinition;
	}

	public List<MeasurementSpecification> getMeasurementSpecifications() {
		return measurementSpecifications;
	}

	/*
	 * Utility functions
	 */

	/**
	 * Looks for the experiment series definition with the given name.
	 * 
	 * @param name
	 *            - the name of the requested experiment series definition
	 * @return the first found {@link ExperimentSeriesDefinition} instance with
	 *         the given name; <code>null</code> if no
	 *         {@link ExperimentSeriesDefinition} could be found
	 */
	public ExperimentSeriesDefinition getExperimentSeriesDefinition(String name) {
		for (MeasurementSpecification measSpec : this.measurementSpecifications) {
			for (ExperimentSeriesDefinition esd : measSpec.getExperimentSeriesDefinitions()) {
				if (esd.getName().equals(name))
					return esd;
			}
		}
		return null;
	}

	/**
	 * Looks for the experiment series definition with the given name in the
	 * given measurement specification
	 * 
	 * @param name
	 *            - the name of the requested experiment series definition
	 * @param measurementSpecificationName
	 *            - the name of the measurement specification where to look for
	 *            the series definition
	 * @return the {@link ExperimentSeriesDefinition} instance with the given
	 *         name specified in the given measurement specification;
	 *         <code>null</code> if no {@link ExperimentSeriesDefinition} could
	 *         be found
	 */
	public ExperimentSeriesDefinition getExperimentSeriesDefinition(String name, String measurementSpecificationName) {
		MeasurementSpecification measSpec = this.getMeasurementSpecification(measurementSpecificationName);

		for (ExperimentSeriesDefinition esd : measSpec.getExperimentSeriesDefinitions()) {
			if (esd.getName().equals(name))
				return esd;
		}

		return null;
	}

	/**
	 * Looks for all experiment series definitions with the given name.
	 * 
	 * @param name
	 *            - the name of the requested experiment series definition
	 * @return all {@link ExperimentSeriesDefinition} instances with the given
	 *         name; <code>null</code> if no {@link ExperimentSeriesDefinition}
	 *         could be found
	 */
	public List<ExperimentSeriesDefinition> getAllExperimentSeriesDefinitions(String name) {
		List<ExperimentSeriesDefinition> experimentSeriesDefinitions = new ArrayList<ExperimentSeriesDefinition>();

		for (MeasurementSpecification measSpec : this.measurementSpecifications) {
			for (ExperimentSeriesDefinition esd : measSpec.getExperimentSeriesDefinitions()) {
				if (esd.getName().equals(name))
					experimentSeriesDefinitions.add(esd);
			}
		}
		return experimentSeriesDefinitions;
	}

	/**
	 * Looks for the measurement specification with the given name.
	 * 
	 * @param name
	 *            - the name of the requested measurement specification
	 * @return the {@link MeasurementSpecification} instance with the given
	 *         name; <code>null</code> if no {@link MeasurementSpecification}
	 *         could be found
	 */
	public MeasurementSpecification getMeasurementSpecification(String name) {
		for (MeasurementSpecification measSpec : this.measurementSpecifications) {
			if (measSpec.getName().equals(name)) {
				return measSpec;
			}
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

		return "ScenarioDefinition{" + "scenarioName='" + scenarioName + "'}";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((measurementEnvironmentDefinition == null) ? 0 : measurementEnvironmentDefinition.hashCode());
		result = prime * result + ((measurementSpecifications == null || measurementSpecifications.isEmpty()) ? 0 : measurementSpecifications.hashCode());
		result = prime * result + ((scenarioName == null) ? 0 : scenarioName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScenarioDefinition other = (ScenarioDefinition) obj;
		if (measurementEnvironmentDefinition == null) {
			if (other.measurementEnvironmentDefinition != null)
				return false;
		} else if (!measurementEnvironmentDefinition.equals(other.measurementEnvironmentDefinition))
			return false;
		if (measurementSpecifications == null || measurementSpecifications.isEmpty()) {
			if (other.measurementSpecifications != null && !other.measurementSpecifications.isEmpty())
				return false;
		} else if (!measurementSpecifications.equals(other.measurementSpecifications))
			return false;
		if (scenarioName == null) {
			if (other.scenarioName != null)
				return false;
		} else if (!scenarioName.equals(other.scenarioName))
			return false;
		return true;
	}

	/**
	 * Checks whether this instnace of scenarioDefinition contains all elements
	 * the passed scenarioDefinition comprises
	 * 
	 * @param other
	 * @return
	 */
	public boolean containsAllElementsOf(ScenarioDefinition other) {
		for (MeasurementSpecification measSpecOther : other.getMeasurementSpecifications()) {
			boolean found = false;
			for (MeasurementSpecification measSpecThis : this.getMeasurementSpecifications()) {
				if (measSpecThis.getName().equals(measSpecOther.getName())) {
					found = true;
					if (!measSpecThis.containsAllElementsOf(measSpecOther)) {
						return false;
					}
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Extends this scenario definition by all additional experiment series in
	 * the given scenario definition
	 * 
	 * @param other
	 * @return
	 */
	public List<ExperimentSeriesDefinition> extendBy(ScenarioDefinition other) {
		List<ExperimentSeriesDefinition> addedESDList = new ArrayList<ExperimentSeriesDefinition>();
		for (MeasurementSpecification measSpecOther : other.getMeasurementSpecifications()) {
			boolean found = false;
			for (MeasurementSpecification measSpecThis : this.getMeasurementSpecifications()) {
				if (measSpecThis.getName().equals(measSpecOther.getName())) {
					found = true;
					addedESDList.addAll(measSpecThis.extendBy(measSpecOther));
					break;
				}
			}
			if (!found) {
				this.measurementSpecifications.add(measSpecOther);
				addedESDList.addAll(measSpecOther.getExperimentSeriesDefinitions());
			}
		}
		return addedESDList;
	}

}
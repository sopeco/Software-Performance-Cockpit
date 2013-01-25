/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.persistence.entities.definition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * @author Dennis Westermann, Jens Happe
 * 
 */
@NamedQueries({ @NamedQuery(name = "findAllScenarioDefinitions", query = "SELECT o FROM ScenarioDefinition o") })
@Entity
public class ScenarioDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "name")
	protected String scenarioName = null;

	@Lob
	@Column(name = "measurementEnvironmentDefinition")
	protected MeasurementEnvironmentDefinition measurementEnvironmentDefinition;

	@Lob
	@Column(name = "measurementSpecification")
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
				if (esd.getName().equals(name)) {
					return esd;
				}

			}
		}
		return null;
	}

	/**
	 * Returns all {@link ExperimentSeriesDefinition} objects belonging to that
	 * scenarioDefinition.
	 * 
	 * 
	 * @return all {@link ExperimentSeriesDefinition} instances
	 */
	public List<ExperimentSeriesDefinition> getAllExperimentSeriesDefinitions() {
		List<ExperimentSeriesDefinition> expSeries = new ArrayList<ExperimentSeriesDefinition>();

		for (MeasurementSpecification measSpec : this.measurementSpecifications) {
			for (ExperimentSeriesDefinition esd : measSpec.getExperimentSeriesDefinitions()) {
				expSeries.add(esd);
			}
		}
		return expSeries;
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

		return "ScenarioDefinition{" + "scenarioName='" + scenarioName + "'}";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((measurementEnvironmentDefinition == null) ? 0 : measurementEnvironmentDefinition.hashCode());
		result = prime * result + ((measurementSpecifications == null) ? 0 : measurementSpecifications.hashCode());
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
		if (measurementSpecifications == null) {
			if (other.measurementSpecifications != null)
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

}
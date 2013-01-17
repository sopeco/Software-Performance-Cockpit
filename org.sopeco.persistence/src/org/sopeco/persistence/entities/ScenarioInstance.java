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
package org.sopeco.persistence.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.entities.keys.ScenarioInstancePK;

/**
 * 
 * 
 * @author Dennis Westermann
 */
@NamedQueries({
		@NamedQuery(name = "findAllScenarioInstances", query = "SELECT o FROM ScenarioInstance o"),
		@NamedQuery(name = "findScenarioInstancesByName", query = "SELECT o FROM ScenarioInstance o WHERE o.primaryKey.name = :name") })
@Entity
public class ScenarioInstance implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Entity Attributes
	 */
	@Column(name = "description")
	private String description;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "scenarioInstance", orphanRemoval = true, fetch = FetchType.EAGER)
	private List<ExperimentSeries> experimentSeries = new ArrayList<ExperimentSeries>();

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	private ScenarioDefinition scenarioDefinition;

	/*
	 * Foreign Key Attributes
	 */
	@EmbeddedId
	private ScenarioInstancePK primaryKey = new ScenarioInstancePK();



	/*
	 * Getters and Setters
	 */

	public String getName() {
		return primaryKey.getName();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMeasurementEnvironmentUrl() {
		return primaryKey.getMeasurementEnvironmentUrl();
	}

	public void setMeasurementEnvironmentUrl(String measurementEnvironmentUrl) {
		this.primaryKey.setMeasurementEnvironmentUrl(measurementEnvironmentUrl);
	}

	public List<ExperimentSeries> getExperimentSeriesList() {
		return experimentSeries;
	}

	public ScenarioInstancePK getPrimaryKey() {
		return primaryKey;
	}

	public void setScenarioDefinition(ScenarioDefinition scenarioDefinition) {
		this.primaryKey.setName(scenarioDefinition.getScenarioName());
		this.scenarioDefinition = scenarioDefinition;
	}

	public ScenarioDefinition getScenarioDefinition() {
		return this.scenarioDefinition;
	}

	/*
	 * Utility Functions
	 */
	/**
	 * @param name
	 *            the name of the experiment series that is included in this
	 *            scenario instance
	 * @return the experiment series instance with the given name (in its latest
	 *         version); <code>null</code> if no series with that name exists
	 */
	public ExperimentSeries getExperimentSeries(String name) {
		ExperimentSeries resultSeries = null;
		for (ExperimentSeries series : getExperimentSeriesList()) {
			if (series.getName().equals(name)) {
				if (resultSeries == null) {
					resultSeries = series;
				} else if (resultSeries.getVersion() < series.getVersion()) {
					resultSeries = series;
				}
			}
		}

		return resultSeries;
	}

	/**
	 * @param name
	 *            the name of the experiment series that is included in this
	 *            scenario instance
	 * @return the list of experiment series with the given name (i.e. the
	 *         different versions of the experiment series); <code>null</code>
	 *         if no series with that name exists
	 */
	public List<ExperimentSeries> getAllExperimentSeriesVersions(String name) {
		List<ExperimentSeries> resultList = new ArrayList<ExperimentSeries>();
		for (ExperimentSeries series : getExperimentSeriesList()) {
			if (series.getName().equals(name)) {
				resultList.add(series);
			}
			return resultList;
		}

		return null;
	}

	/**
	 * @param name
	 *            the name of the experiment series that is included in this
	 *            scenario instance
	 * @return the experiment series instance with the given name;
	 *         <code>null</code> if no series with that name exists
	 */
	public ExperimentSeries getExperimentSeries(String name, Long version) {
		for (ExperimentSeries series : getExperimentSeriesList()) {
			if (series.getName().equals(name) && series.getVersion().equals(version)) {
				return series;
			}
		}

		return null;
	}



	public void extendScenarioInstance(ScenarioDefinition otherSD) {
		List<ExperimentSeriesDefinition> esdList = this.getScenarioDefinition().extendBy(otherSD);
		// Why? Introduced a bug! TODO: Discuss
		// for (ExperimentSeriesDefinition esd : esdList) {
		// ExperimentSeries expSeries =
		// EntityFactory.createExperimentSeries(esd);
		// expSeries.setScenarioInstance(this);
		// this.getExperimentSeriesList().add(expSeries);
		//
		// }
	}

	/*
	 * Overrides
	 */

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ScenarioInstance obj = (ScenarioInstance) o;
		if (primaryKey == null || obj.getPrimaryKey() == null) {
			return false;
		}
		return this.primaryKey.equals(obj.getPrimaryKey());

	}

	@Override
	public int hashCode() {
		if (this.primaryKey != null) {
			return primaryKey.hashCode();
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {

		return "ScenarioInstance{" + "name='" + this.primaryKey.getName() + "\' " + "measurementEnvironmentUrl='"
				+ this.primaryKey.getMeasurementEnvironmentUrl() + '\'' + '}';
	}



}

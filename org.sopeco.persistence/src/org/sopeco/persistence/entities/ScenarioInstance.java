package org.sopeco.persistence.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

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

	@Lob
	@Column(name = "scenarioDefinitions")
	private Collection<ScenarioDefinition> scenarioDefinitions = new HashSet<ScenarioDefinition>();

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

	public void setName(String name) {
		this.primaryKey.setName(name);
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

	public Collection<ScenarioDefinition> getScenarioDefinitions() {
		return this.scenarioDefinitions;
	}

	/*
	 * Utility Functions
	 */
	/**
	 * @param name
	 *            the name of the experiment series that is included in this
	 *            scenario instance
	 * @return the experiment series instance with the given name;
	 *         <code>null</code> if no series with that name exists
	 */
	public ExperimentSeries getExperimentSeries(String name) {
		for (ExperimentSeries series : getExperimentSeriesList()) {
			if (series.getName().equals(name)) {
				return series;
			}
		}

		return null;
	}

	/**
	 * @param definitionId
	 *            the id of the scenario definition that is included in this
	 *            scenario instance
	 * @return the scenario definition with the given name; <code>null</code> if
	 *         no scenario definition with the given id exists
	 */
	public ScenarioDefinition getScenarioDefinition(String definitionId) {
		for (ScenarioDefinition sd : this.scenarioDefinitions) {
			if (sd.getDefinitionId().equals(definitionId)) {
				return sd;
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
		if (o == null || getClass() != o.getClass())
			return false;

		ScenarioInstance obj = (ScenarioInstance) o;
		if (primaryKey == null || obj.getPrimaryKey() == null)
			return false;
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

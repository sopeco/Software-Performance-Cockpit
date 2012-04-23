package org.sopeco.persistence.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import org.sopeco.persistence.entities.keys.ScenarioInstancePK;

/**
 * 
 * 
 * @author Dennis Westermann
 */
@NamedQuery(
    name="findScenarioInstancesByName",
    query="SELECT o FROM ScenarioInstance o WHERE o.primaryKey.name = :name"
)
@Entity
public class ScenarioInstance implements Serializable {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	@Version private long version;
	/*
	 * Entity Attributes
	 */
	@Column(name = "description")
	private String description;
	
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy = "scenarioInstance", orphanRemoval=true)
	private List<ExperimentSeries> experimentSeries = new ArrayList<ExperimentSeries>();
	

	/*
	 * Foreign Key Attributes
	 */
	@EmbeddedId
	private
	ScenarioInstancePK primaryKey = new ScenarioInstancePK();
	
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

	public List<ExperimentSeries> getExperimentSeries() {
		return experimentSeries;
	}
	
	public ScenarioInstancePK getPrimaryKey() {
		return primaryKey;
	}
	
	/*
	 * Overrides
	 */
	
	@Override
	public boolean equals(Object o) {

		 if (this == o) return true;
		 if (o == null || getClass() != o.getClass()) return false;

		 ScenarioInstance obj = (ScenarioInstance) o;
		 if(primaryKey == null || obj.getPrimaryKey() == null) return false;
		 return this.primaryKey.equals(obj.getPrimaryKey());

	}

	@Override
	public int hashCode() {
		if(this.primaryKey!=null){
			return primaryKey.hashCode();
		} else {
			return 0;
		}
	}

	@Override
    public String toString() {

       return "ScenarioInstance{" +
	                 "name='" + this.primaryKey.getName() + "\' " +
	                 "measurementEnvironmentUrl='" + this.primaryKey.getMeasurementEnvironmentUrl() + '\'' +'}';
    }
	

	
}

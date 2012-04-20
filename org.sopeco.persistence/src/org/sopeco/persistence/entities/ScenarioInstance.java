package org.sopeco.persistence.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.sopeco.persistence.entities.keys.ScenarioInstancePK;

/**
 * 
 * 
 * @author Dennis Westermann
 */
@Entity
public class ScenarioInstance implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Entity Attributes
	 */
//	@Column(name = "name", nullable = false, insertable=false, updatable=false)
//	private String name;
	
	private String description;
	
//	@Column(name = "measurementEnvironmentUrl", nullable = false, insertable=false, updatable=false)
//	private String measurementEnvironmentUrl;
	
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy = "scenarioInstance")
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
//		return name;
		return primaryKey.getName();
	}
	public void setName(String name) {
//		this.name = name;
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
//		return measurementEnvironmentUrl;
	}
	public void setMeasurementEnvironmentUrl(String measurementEnvironmentUrl) {
//		this.measurementEnvironmentUrl = measurementEnvironmentUrl;
		this.primaryKey.setMeasurementEnvironmentUrl(measurementEnvironmentUrl);
	}

	public List<ExperimentSeries> getExperimentSeries() {
		return experimentSeries;
	}
	
	
	/*
	 * Overrides
	 */
	
//	@Override
//	public boolean equals(Object o) {
//
//		 if (this == o) return true;
//		 if (o == null || getClass() != o.getClass()) return false;
//
//		 ScenarioInstance obj = (ScenarioInstance) o;
//		 if (name == null || measurementEnvironmentUrl == null || obj.name == null || obj.measurementEnvironmentUrl == null) return false;
//		 if(!name.equals(obj.name) || !measurementEnvironmentUrl.equals(obj.measurementEnvironmentUrl)) return false;
//
//		 return true;
//
//	}
//
//	@Override
//	public int hashCode() {
//		if(name!=null && measurementEnvironmentUrl!=null){
//			String hashString = name + measurementEnvironmentUrl;
//			return hashString.hashCode();
//		} else {
//			return 0;
//		}
//	}
//
//	@Override
//    public String toString() {
//
//       return "ScenarioInstance{" +
//	                 "name='" + name + '\'' +
//	                 "measurementEnvironmentUrl='" + measurementEnvironmentUrl + '\'' +'}';
//    }
	
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
	                 "name='" + this.primaryKey.getName() + '\'' +
	                 "measurementEnvironmentUrl='" + this.primaryKey.getMeasurementEnvironmentUrl() + '\'' +'}';
    }
	
	public ScenarioInstancePK getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(ScenarioInstancePK primaryKey) {
		this.primaryKey = primaryKey;
	}
	
}

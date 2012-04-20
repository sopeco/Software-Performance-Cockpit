package org.sopeco.persistence.entities;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.model.util.EMFUtil;
import org.sopeco.persistence.entities.keys.ExperimentSeriesPK;

//@IdClass(ExperimentSeriesPK.class)
@Entity
public class ExperimentSeries implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/*
	 * Entity Attributes
	 */
	
//	private String name;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumns({
	    @JoinColumn(name="scenarioInstanceName", referencedColumnName = "NAME"),
	    @JoinColumn(name="measurementEnvironmentUrl", referencedColumnName = "MEASUREMENTENVIRONMENTURL")
	})
	private ScenarioInstance scenarioInstance;
	
	@Lob
	private String experimentSeriesDefinition;
	
//	@Transient
	@OneToMany(cascade=CascadeType.ALL, mappedBy = "experimentSeries")
	private List<ExperimentSeriesRun> experimentSeriesRuns = new ArrayList<ExperimentSeriesRun>();
	
	
	
	/*
	 * ForeignKey Attributes
	 */
//	@Id
//	private String scenarioInstanceName;
//	
//	@Id
//	private String measurementEnvironmentUrl;
	
	@EmbeddedId
	private
	ExperimentSeriesPK primaryKey = new ExperimentSeriesPK();
	
	/*
	 * Getters and Setters
	 */
	public String getName() {
//		return name;
		return this.primaryKey.getName();
	}


	public void setName(String name) {
//		this.name = name;
		this.primaryKey.setName(name);
	}


	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}


	public void setScenarioInstance(ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
//		this.setScenarioInstanceName(scenarioInstance.getName());
//		this.setMeasurementEnvironmentUrl(scenarioInstance.getMeasurementEnvironmentUrl());
	}


	public ExperimentSeriesDefinition getExperimentSeriesDefinition() {
		
		ExperimentSeriesDefinition returnValue;
		try {
			returnValue = (ExperimentSeriesDefinition) EMFUtil.loadFromSting(this.experimentSeriesDefinition);
		} catch (IOException e) {
			throw new RuntimeException("Cannot serialize object.", e);
		}
		return returnValue;
	}


	public void setExperimentSeriesDefinition(
			ExperimentSeriesDefinition experimentSeriesDefinition) {
		try {
			this.experimentSeriesDefinition = EMFUtil.saveToString(experimentSeriesDefinition);
		} catch (IOException e) {
			throw new RuntimeException("Cannot serialize object.", e);
		}
		
	}
	
	public List<ExperimentSeriesRun> getExperimentSeriesRuns(){
		return this.experimentSeriesRuns;
	}
	
	
	/*
	 * Overrides
	 */
	
//	public String getScenarioInstanceName() {
//		return scenarioInstanceName;
//	}


//	public void setScenarioInstanceName(String scenarioInstanceName) {
//		this.scenarioInstanceName = scenarioInstanceName;
//	}


//	public String getMeasurementEnvironmentUrl() {
//		return measurementEnvironmentUrl;
//	}


//	public void setMeasurementEnvironmentUrl(String measurementEnvironmentUrl) {
//		this.measurementEnvironmentUrl = measurementEnvironmentUrl;
//	}


//	@Override
//	public boolean equals(Object o) {
//
//		 if (this == o) return true;
//		 if (o == null || getClass() != o.getClass()) return false;
//
//		 ExperimentSeries obj = (ExperimentSeries) o;
//		 if (name == null || scenarioInstance == null || obj.name == null || obj.scenarioInstance == null) return false;
//		 if(!name.equals(obj.name) || !scenarioInstance.equals(obj.scenarioInstance)) return false;
//
//		 return true;
//
//	}
//
//	@Override
//	public int hashCode() {
//		if(name!=null && scenarioInstance!=null){
//			String hashString = name + scenarioInstance.hashCode();
//			return hashString.hashCode();
//		} else {
//			return 0;
//		}
//	}
//
//	@Override
//    public String toString() {
//
//       return "ExperimentSeries{" +
//	                 "name='" + name + '\'' +
//	                 "scenarioInstance='" + scenarioInstance.toString() + '\'' +'}';
//    }
//
//
	
	@Override
	public boolean equals(Object o) {

		 if (this == o) return true;
		 if (o == null || getClass() != o.getClass()) return false;

		 ExperimentSeries obj = (ExperimentSeries) o;
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

       return "ExperimentSeries{" +
	                 "name='" + this.primaryKey.getName() + '\'' +
	                 "scenarioInstance='" + scenarioInstance.toString() + '\'' +'}';
    }
	
	public ExperimentSeriesPK getPrimaryKey() {
		return primaryKey;
	}


	public void setPrimaryKey(ExperimentSeriesPK primaryKey) {
		this.primaryKey = primaryKey;
	}

}

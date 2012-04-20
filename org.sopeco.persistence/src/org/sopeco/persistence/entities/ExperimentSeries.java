package org.sopeco.persistence.entities;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

@Entity
public class ExperimentSeries implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/*
	 * Entity Attributes
	 */
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumns({
	    @JoinColumn(name="scenarioInstanceName", referencedColumnName = "name"),
	    @JoinColumn(name="measurementEnvironmentUrl", referencedColumnName = "measurementEnvironmentUrl")
	})
	private ScenarioInstance scenarioInstance;
	
	@Lob
	@Column(name = "experimentSeriesDefinition")
	private String experimentSeriesDefinition;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy = "experimentSeries", orphanRemoval=true)
	private List<ExperimentSeriesRun> experimentSeriesRuns = new ArrayList<ExperimentSeriesRun>();
		
	@EmbeddedId
	private
	ExperimentSeriesPK primaryKey = new ExperimentSeriesPK();
	
	/*
	 * Getters and Setters
	 */
	public String getName() {
		return this.primaryKey.getName();
	}


	public void setName(String name) {
		this.primaryKey.setName(name);
	}


	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}


	public void setScenarioInstance(ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
		this.primaryKey.setScenarioInstanceName(scenarioInstance.getName());
		this.primaryKey.setMeasurementEnvironmentUrl(scenarioInstance.getMeasurementEnvironmentUrl());
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
	
	public ExperimentSeriesPK getPrimaryKey() {
		return primaryKey;
	}

	
	
	/*
	 * Overrides
	 */
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
	                 "name='" + this.primaryKey.getName() + "\' " +
	                 "scenarioInstance='" + scenarioInstance.toString() + '\'' +'}';
    }

}

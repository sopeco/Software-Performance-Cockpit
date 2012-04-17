package org.sopeco.core.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ScenarioInstance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String description;
	
	private String measurementEnvironmentUrl;
	
	@OneToMany(mappedBy = "ScenarioInstance")
	private List<ExperimentSeriesRun> experimentSeries = new ArrayList<ExperimentSeriesRun>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMeasurementEnvironmentUrl() {
		return measurementEnvironmentUrl;
	}
	public void setMeasurementEnvironmentUrl(String measurementEnvironmentUrl) {
		this.measurementEnvironmentUrl = measurementEnvironmentUrl;
	}
	
	public Long getId() {
		return id;
	}
	public List<ExperimentSeriesRun> getExperimentSeries() {
		return experimentSeries;
	}
	
	
	
}

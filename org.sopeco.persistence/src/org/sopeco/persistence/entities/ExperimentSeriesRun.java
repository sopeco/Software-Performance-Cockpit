package org.sopeco.persistence.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.sopeco.persistence.dataset.DataSetAggregated;

@Entity
public class ExperimentSeriesRun implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/*
	 * Entity Attributes
	 */
	
	@Lob
	private DataSetAggregated resultDataSet;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumns({
	    @JoinColumn(name="scenarioInstanceName", referencedColumnName = "scenarioInstanceName"),
	    @JoinColumn(name="measurementEnvironmentUrl", referencedColumnName = "measurementEnvironmentUrl"),
	    @JoinColumn(name="experimentSeriesName", referencedColumnName = "NAME")
	})
	private ExperimentSeries experimentSeries;
	
	/*
	 * ForeignKeyAttributes
	 */
//	private String scenarioInstanceName;
//	private String measurementEnvironmentUrl;
//	private String experimentSeriesName;

	/*
	 * Getter and Setter
	 */
	
	public Long getId() {
		return id;
	}
	
	public ExperimentSeries getExperimentSeries() {
		return experimentSeries;
	}

	public void setExperimentSeries(ExperimentSeries experimentSeries) {
//		this.experimentSeriesName = experimentSeries.getName();
//		if(experimentSeries.getScenarioInstance() != null){
//			this.scenarioInstanceName = experimentSeries.getScenarioInstance().getName();
//			this.measurementEnvironmentUrl = experimentSeries.getScenarioInstance().getMeasurementEnvironmentUrl();
//		}
		this.experimentSeries = experimentSeries;
	}

	public DataSetAggregated getResultDataSet() {
		return resultDataSet;
	}
	
	public void setResultDataSet(DataSetAggregated resultDataSet){
		this.resultDataSet = resultDataSet; 
	}

	
	/*
	 * Overrides
	 */
	
	@Override
	public boolean equals(Object o) {

		 if (this == o) return true;
		 if (o == null || getClass() != o.getClass()) return false;

		 ExperimentSeriesRun obj = (ExperimentSeriesRun) o;
		 if (id == null || obj.id == null) return false;
		 if(id != obj.id) return false;

		 return true;

	}

	@Override
	public int hashCode() {
		if(id!=null){
			return id.hashCode();
		} else {
			return 0;
		}
	}

	@Override
    public String toString() {

       return "ExperimentSeriesRun{" +
	                 "id='" + id + '\'' +'}';
    }
}

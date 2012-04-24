package org.sopeco.persistence.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetAppender;

@NamedQuery(
	    name="findAllExperimentSeriesRuns",
	    query="SELECT o FROM ExperimentSeriesRun o"
	)
@Entity
public class ExperimentSeriesRun implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/*
	 * Entity Attributes
	 */
	
	@Id
	@Column(name = "timestamp")
	private Long timestamp;
	
	@Lob
	@Column(name = "restultDataSet")
	private DataSetAggregated resultDataSet;
	
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumns({
	    @JoinColumn(name="scenarioInstanceName", referencedColumnName = "scenarioInstanceName"),
	    @JoinColumn(name="measurementEnvironmentUrl", referencedColumnName = "measurementEnvironmentUrl"),
	    @JoinColumn(name="experimentSeriesName", referencedColumnName = "name")
	})
	private ExperimentSeries experimentSeries;
	

	/*
	 * Getter and Setter
	 */
	
	public Long getPrimaryKey() {
		return this.timestamp;
	}
	
	public ExperimentSeries getExperimentSeries() {
		return experimentSeries;
	}

	public void setExperimentSeries(ExperimentSeries experimentSeries) {
		this.experimentSeries = experimentSeries;
	}

	public DataSetAggregated getResultDataSet() {
		return resultDataSet;
	}
	
	public void setResultDataSet(DataSetAggregated resultDataSet){
		this.resultDataSet = resultDataSet; 
	}


	public Long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Long timestamp){
		this.timestamp = timestamp; 
	}
	
	/*
	 * Utility functions
	 */
	
	/**
	 * Adds the given experiment run results to the result data set of this {@link ExperimentSeriesRun}.
	 * 
	 * @param experimentRunResults - a dataSet containing the parameter values of one or many experiment runs
	 */
	public void append(DataSetAggregated experimentRunResults){
		
		DataSetAppender appender = new DataSetAppender();
		if(resultDataSet!=null) appender.append(resultDataSet);
		appender.append(experimentRunResults);
		this.resultDataSet = appender.createDataSet();
		
	}


	/*
	 * Overrides
	 */
	
	@Override
	public boolean equals(Object o) {

		 if (this == o) return true;
		 if (o == null || getClass() != o.getClass()) return false;

		 ExperimentSeriesRun obj = (ExperimentSeriesRun) o;
		 if (timestamp == null || obj.timestamp == null) return false;
		 if(!timestamp.equals(obj.timestamp)) return false;

		 return true;

	}

	@Override
	public int hashCode() {
		if(timestamp!=null){
			return timestamp.hashCode();
		} else {
			return 0;
		}
	}

	@Override
    public String toString() {

       return "ExperimentSeriesRun{" +
	                 "timestamp='" + timestamp + '\'' +'}';
    }

}

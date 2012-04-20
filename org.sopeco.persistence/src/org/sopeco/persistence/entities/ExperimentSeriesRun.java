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

@NamedQuery(
	    name="findAllExperimentSeriesRuns",
	    query="SELECT o FROM ExperimentSeriesRun o"
	)
@Entity
public class ExperimentSeriesRun implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	public ExperimentSeriesRun(){
		super();
	}
	
	public ExperimentSeriesRun(Long timestamp){
		super();
		this.timestamp = timestamp;
	}
	
	/*
	 * Entity Attributes
	 */
	
	@Id
	@Column(name = "timestamp")
	private Long timestamp;
	
	@Lob
	@Column(name = "restultDataSet")
	private DataSetAggregated resultDataSet;
	
	@ManyToOne(cascade=CascadeType.ALL, optional=false)
//	@JoinColumns({
//	    @JoinColumn(name="scenarioInstanceName", referencedColumnName = "scenarioInstanceName"),
//	    @JoinColumn(name="measurementEnvironmentUrl", referencedColumnName = "measurementEnvironmentUrl"),
//	    @JoinColumn(name="experimentSeriesName", referencedColumnName = "name")
//	})
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

	/*
	 * Overrides
	 */
	
	@Override
	public boolean equals(Object o) {

		 if (this == o) return true;
		 if (o == null || getClass() != o.getClass()) return false;

		 ExperimentSeriesRun obj = (ExperimentSeriesRun) o;
		 if (timestamp == null || obj.timestamp == null) return false;
		 if(timestamp != obj.timestamp) return false;

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

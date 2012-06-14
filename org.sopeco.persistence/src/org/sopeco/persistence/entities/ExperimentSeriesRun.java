package org.sopeco.persistence.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.PersistenceProviderFactory;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetAppender;
import org.sopeco.persistence.entities.exceptions.ExperimentFailedException;
import org.sopeco.persistence.exceptions.DataNotFoundException;

/**
 * @author Dennis Westermann
 * 
 */
@NamedQuery(name = "findAllExperimentSeriesRuns", query = "SELECT o FROM ExperimentSeriesRun o")
@Entity
public class ExperimentSeriesRun implements Serializable, Comparable<ExperimentSeriesRun> {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(ExperimentSeriesRun.class);

	/*
	 * Entity Attributes
	 */

	@Id
	@Column(name = "timestamp")
	private Long timestamp;

	@Transient
	private DataSetAggregated successfulResultDataSet;

	@Column(name = "successfulResultDataSetId")
	private String successfulResultDataSetId;

	@Lob
	@Column(name = "experimentFailedExceptions")
	private List<ExperimentFailedException> experimentFailedExceptions = new ArrayList<ExperimentFailedException>();

	@Lob
	@Column(name = "processedDataSets")
	private List<DataSetAggregated> processedDataSets = new ArrayList<DataSetAggregated>();
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumns({ @JoinColumn(name = "scenarioInstanceName", referencedColumnName = "scenarioInstanceName"),
			@JoinColumn(name = "measurementEnvironmentUrl", referencedColumnName = "measurementEnvironmentUrl"),
			@JoinColumn(name = "experimentSeriesName", referencedColumnName = "name"),
			@JoinColumn(name = "experimentSeriesVersion", referencedColumnName = "version") })
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

	public DataSetAggregated getSuccessfulResultDataSet() {

		if (successfulResultDataSet == null && successfulResultDataSetId != null) {
			try {
				successfulResultDataSet = PersistenceProviderFactory.getPersistenceProvider().loadDataSet(
						successfulResultDataSetId);
			} catch (DataNotFoundException e) {
				logger.warn(e.getMessage());
			}
		}
		return successfulResultDataSet;
	}

	public List<DataSetAggregated> getProcessedDataSets() {
		return processedDataSets;
	}
	
	public void addProcessedDataSet(DataSetAggregated dsa) {
		processedDataSets.add(dsa);
	}
	
	public List<ExperimentFailedException> getExperimentFailedExceptions() {
		return experimentFailedExceptions;
	}
	
	public void setSuccessfulResultDataSet(DataSetAggregated resultDataSet) {

		this.successfulResultDataSet = resultDataSet;
		this.successfulResultDataSetId = resultDataSet.getID();

	}

	public void addExperimentFailedException(ExperimentFailedException exception) {
		this.experimentFailedExceptions.add(exception);
	}
	
	public void setExperimentFailedExceptions(List<ExperimentFailedException> exceptions) {
		this.experimentFailedExceptions = exceptions;
	}
	
	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	/*
	 * Utility functions
	 */
	/**
	 * Stores the successful result data set in the database.
	 */
	public void storeDataSets() {
		if(this.getSuccessfulResultDataSet()!=null) {
			PersistenceProviderFactory.getPersistenceProvider().store(this.getSuccessfulResultDataSet());
		}
	}
	/**
	 * Removes the successful result data set from the database.
	 */
	public void removeDataSets() {
		if(this.successfulResultDataSetId!=null) {
			try {
				PersistenceProviderFactory.getPersistenceProvider().remove(this.getSuccessfulResultDataSet());
			} catch (DataNotFoundException e) {
				logger.warn(e.getMessage());
			}
		}
	}

	/**
	 * Adds the given successful experiment run results to the result data set
	 * of this {@link ExperimentSeriesRun}.
	 * 
	 * @param experimentRunResults
	 *            - a dataSet containing the parameter values of one or many
	 *            experiment runs
	 */
	public void appendSuccessfulResults(DataSetAggregated experimentRunResults) {

		DataSetAppender appender = new DataSetAppender();
		if (this.getSuccessfulResultDataSet() != null)
			appender.append(successfulResultDataSet);
		appender.append(experimentRunResults);
		this.setSuccessfulResultDataSet(appender.createDataSet());

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

		ExperimentSeriesRun obj = (ExperimentSeriesRun) o;
		if (timestamp == null || obj.timestamp == null)
			return false;
		if (!timestamp.equals(obj.timestamp))
			return false;

		return true;

	}

	@Override
	public int hashCode() {
		if (timestamp != null) {
			return timestamp.hashCode();
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "ExperimentSeriesRun{" + "timestamp='" + timestamp + '\'' + '}';
	}

	/**
	 * Compares the given instance with this instance. The value used for
	 * comparison is the timestamp on which the experiment series run has been
	 * created.
	 * 
	 * @param compareRun
	 *            - the instance that should be compared to this instance
	 * 
	 * @return result of compareRun.getTimestamp() - this.getTimestamp()
	 */
	@Override
	public int compareTo(ExperimentSeriesRun compareRun) {
		return (int) (compareRun.getTimestamp() - this.getTimestamp());
	}

}

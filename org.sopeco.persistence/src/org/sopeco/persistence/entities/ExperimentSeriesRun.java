/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
import org.sopeco.persistence.IPersistenceProvider;
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

	@Column(name = "label")
	private String label;

	@Transient
	private DataSetAggregated successfulResultDataSet;

	@Column(name = "successfulResultDataSetId")
	private String successfulResultDataSetId;

	@Lob
	@Column(name = "experimentFailedExceptions")
	private List<ExperimentFailedException> experimentFailedExceptions = new ArrayList<ExperimentFailedException>();

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumns({ @JoinColumn(name = "scenarioInstanceName", referencedColumnName = "scenarioInstanceName"),
			@JoinColumn(name = "measurementEnvironmentUrl", referencedColumnName = "measurementEnvironmentUrl"),
			@JoinColumn(name = "experimentSeriesName", referencedColumnName = "name")})
	private ExperimentSeries experimentSeries;

	
	/**
	 * The persistence provider instance which loaded this object.
	 */
	@Transient
	private IPersistenceProvider persistenceProvider;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public DataSetAggregated getSuccessfulResultDataSet() {

		if (successfulResultDataSet == null && successfulResultDataSetId != null) {
			try {
				if(getPersistenceProvider() == null){
					logger.error("The persistence provider for this entity has not been set!");
					throw new RuntimeException("The persistence provider for this entity has not been set!");
				}
				successfulResultDataSet = getPersistenceProvider().loadDataSet(
						successfulResultDataSetId);
			} catch (DataNotFoundException e) {
				logger.warn(e.getMessage());
			}
		}
		return successfulResultDataSet;
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
	public void storeDataSets(IPersistenceProvider provider) {
		if (this.getSuccessfulResultDataSet() != null) {
			provider.store(this.getSuccessfulResultDataSet());
		}
	}

	/**
	 * Removes the successful result data set from the database.
	 */
	public void removeDataSets(IPersistenceProvider provider) {
		if (this.successfulResultDataSetId != null) {
			try {
				provider.remove(this.getSuccessfulResultDataSet());
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
		if (this.getSuccessfulResultDataSet() != null) {
			appender.append(successfulResultDataSet);
		}
		appender.append(experimentRunResults);
		this.setSuccessfulResultDataSet(appender.createDataSet());

	}

	/*
	 * Overrides
	 */

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ExperimentSeriesRun obj = (ExperimentSeriesRun) o;
		if (timestamp == null || obj.timestamp == null) {
			return false;
		}
		if (!timestamp.equals(obj.timestamp)) {
			return false;
		}

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
	
	public IPersistenceProvider getPersistenceProvider() {
		return persistenceProvider;
	}

	public void setPersistenceProvider(IPersistenceProvider persistenceProvider) {
		this.persistenceProvider = persistenceProvider;
	}
	
	public String getDatasetId(){
		return successfulResultDataSetId;
	}

}

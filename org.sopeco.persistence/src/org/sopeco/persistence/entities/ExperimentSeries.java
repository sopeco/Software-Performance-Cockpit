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
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetAppender;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.exceptions.ExperimentFailedException;
import org.sopeco.persistence.entities.keys.ExperimentSeriesPK;

/**
 * @author Dennis Westermann
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "findExperimentSeriesByName", query = "SELECT o FROM ExperimentSeries o WHERE o.primaryKey.name = :name "
		+ "AND o.primaryKey.scenarioInstanceName = :scenarioInstanceName "
		+ "AND o.primaryKey.measurementEnvironmentUrl = :measurementEnvironmentUrl") })
public class ExperimentSeries implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Entity Attributes
	 */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumns({ @JoinColumn(name = "scenarioInstanceName", referencedColumnName = "name"),
			@JoinColumn(name = "measurementEnvironmentUrl", referencedColumnName = "measurementEnvironmentUrl") })
	private ScenarioInstance scenarioInstance;

	@Lob
	@Column(name = "experimentSeriesDefinition")
	private ExperimentSeriesDefinition experimentSeriesDefinition;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "experimentSeries", orphanRemoval = true, fetch = FetchType.EAGER)
	private List<ExperimentSeriesRun> experimentSeriesRuns = new ArrayList<ExperimentSeriesRun>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "experimentSeries", orphanRemoval = true, fetch = FetchType.EAGER)
	private List<ProcessedDataSet> processedDataSets = new ArrayList<ProcessedDataSet>();

	@EmbeddedId
	private ExperimentSeriesPK primaryKey = new ExperimentSeriesPK();


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

		return this.experimentSeriesDefinition;
	}

	public void setExperimentSeriesDefinition(ExperimentSeriesDefinition experimentSeriesDefinition) {

		this.experimentSeriesDefinition = experimentSeriesDefinition;
	}

	public List<ExperimentSeriesRun> getExperimentSeriesRuns() {
		return this.experimentSeriesRuns;
	}

	public List<ProcessedDataSet> getProcessedDataSets() {
		return processedDataSets;
	}

	public void addProcessedDataSet(ProcessedDataSet pds) {
		processedDataSets.add(pds);
		pds.setExperimentSeries(this);
	}

	public ExperimentSeriesPK getPrimaryKey() {
		return primaryKey;
	}

	/*
	 * Utility Functions
	 */

	/**
	 * 
	 * @return all data measured during the successful experiment series runs of
	 *         this experiment series
	 */
	public DataSetAggregated getAllExperimentSeriesRunSuccessfulResultsInOneDataSet() {
		DataSetAppender appender = new DataSetAppender();
		for (ExperimentSeriesRun seriesRun : getExperimentSeriesRuns()) {
			if (seriesRun.getSuccessfulResultDataSet() != null) {
				appender.append(seriesRun.getSuccessfulResultDataSet());
			}
		}

		return appender.createDataSet();
	}

	/**
	 * 
	 * @return all data measured during the failed experiment series runs of
	 *         this experiment series
	 */
	public List<ExperimentFailedException> getAllExperimentFailedExceptions() {
		List<ExperimentFailedException> result = new ArrayList<ExperimentFailedException>();

		for (ExperimentSeriesRun seriesRun : getExperimentSeriesRuns()) {
			final List<ExperimentFailedException> seriesExceptions = seriesRun.getExperimentFailedExceptions();
			if (seriesExceptions != null) {
				result.addAll(seriesExceptions);
			}
		}

		return result;
	}



	/**
	 * 
	 * @return the latest experiment series run that has been executed, or null 
	 * if there is no such run.
	 */
	public ExperimentSeriesRun getLatestExperimentSeriesRun() {
		if (this.getExperimentSeriesRuns().size() > 0) {
			// sorts the list of experiment runs descending based on their
			// timestamps
			Collections.sort(this.getExperimentSeriesRuns());
	
			return this.getExperimentSeriesRuns().get(0);
		} else {
			return null;
		}
	}

	/*
	 * Overrides
	 */
	@Override
	public boolean equals(Object o) {

		// TODO cleanup
		if (this == o) {
			return true;
		}
		if (o == null || !getClass().equals(o.getClass())) {
			return false;
		}

		ExperimentSeries obj = (ExperimentSeries) o;
		if (primaryKey == null || obj.getPrimaryKey() == null) {
			return false;
		}
		return this.primaryKey.equals(obj.getPrimaryKey());

	}

	@Override
	public int hashCode() {
		if (this.primaryKey != null) {
			return primaryKey.hashCode();
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {

		return "ExperimentSeries{" + "name='" + this.primaryKey.getName() + "\' " + "scenarioInstance='"
				+ scenarioInstance.toString() + '\'' + '}';
	}

	

}

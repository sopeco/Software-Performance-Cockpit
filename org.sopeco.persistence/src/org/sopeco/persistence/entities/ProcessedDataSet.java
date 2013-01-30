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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.exceptions.DataNotFoundException;

/**
 * Processed dataset is a wrapper around a data set that is the result of some
 * data processing over experiment results of an experiment series.
 * 
 * @author Roozbeh Farahbod
 * 
 */
@SuppressWarnings({ "rawtypes" })
@NamedQuery(name = "findAllProcessedDataSets", query = "SELECT o FROM ProcessedDataSet o")
@Entity
public class ProcessedDataSet implements Serializable {
	private static Logger logger = LoggerFactory.getLogger(ProcessedDataSet.class);
	private static final long serialVersionUID = 1L;

	/**
	 * Unique identifier of the DataSet
	 */
	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "label")
	private String label;

	@Transient
	private DataSetAggregated dataSet;

	@Column(name = "successfulResultDataSetId")
	private String dataSetId;

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
	
	public ProcessedDataSet() {
	}

	public ProcessedDataSet(String id, ExperimentSeries es, DataSetAggregated dsa) {
		setId(id);
		setExperimentSeries(es);
		setDataSet(dsa);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public DataSetAggregated getDataSet() {

		if (dataSet == null && dataSetId != null) {
			try {
				dataSet = getPersistenceProvider().loadDataSet(dataSetId);
			} catch (DataNotFoundException e) {
				logger.warn(e.getMessage());
			}
		}

		return dataSet;
	}

	public void setDataSet(DataSetAggregated dataSet) {
		this.dataSet = dataSet;
		this.dataSetId = dataSet.getID();
	}

	public ExperimentSeries getExperimentSeries() {
		return experimentSeries;
	}

	public void setExperimentSeries(ExperimentSeries experimentSeries) {
		this.experimentSeries = experimentSeries;
	}

	public void storeDataSets(IPersistenceProvider provider) {
		if (this.getDataSet() != null) {
			provider.store(this.getDataSet());
		}

	}

	public void removeDataSets(IPersistenceProvider provider) {
		if (this.dataSetId != null) {
			try {
				provider.remove(this.getDataSet());
			} catch (DataNotFoundException e) {
				logger.warn(e.getMessage());
			}
		}
	}
	
	public IPersistenceProvider getPersistenceProvider() {
		return persistenceProvider;
	}

	public void setPersistenceProvider(IPersistenceProvider persistenceProvider) {
		this.persistenceProvider = persistenceProvider;
	}

}
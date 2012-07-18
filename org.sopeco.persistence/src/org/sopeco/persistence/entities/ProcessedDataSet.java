package org.sopeco.persistence.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.persistence.util.ParameterCollection;

/**
 * Processed dataset is a wrapper around a data set that is the result 
 * of some data processing over experiment results of an experiment series. 
 * 
 * @author Roozbeh Farahbod
 * 
 */
@SuppressWarnings({"rawtypes"})
@NamedQuery(name = "findAllProcessedDataSets", query = "SELECT o FROM ProcessedDataSet o")
@Entity
public class ProcessedDataSet implements Serializable {
	private static Logger logger = LoggerFactory.getLogger(ProcessedDataSet.class);
	private static final long serialVersionUID = 1L;

	/**
	 * Unique identifier of the DataSet
	 */
	@Id
	@Column(name= "id")
	private String id;

	@Column(name= "label")
	private String label;
	
	@Transient
	private DataSetAggregated dataSet;
	
	@Column(name = "successfulResultDataSetId")
	private String dataSetId;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumns({ @JoinColumn(name = "scenarioInstanceName", referencedColumnName = "scenarioInstanceName"),
			@JoinColumn(name = "measurementEnvironmentUrl", referencedColumnName = "measurementEnvironmentUrl"),
			@JoinColumn(name = "experimentSeriesName", referencedColumnName = "name"),
			@JoinColumn(name = "experimentSeriesVersion", referencedColumnName = "version") })
	private ExperimentSeries experimentSeries;

	public ProcessedDataSet() {}
	
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
				dataSet = PersistenceProviderFactory.getPersistenceProvider().loadDataSet(
						dataSetId);
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

	public void storeDataSets() {
		if(this.getDataSet()!=null) {
			PersistenceProviderFactory.getPersistenceProvider().store(this.getDataSet());
		}
		
	}

	public void removeDataSets() {
		if(this.dataSetId!=null) {
			try {
				PersistenceProviderFactory.getPersistenceProvider().remove(this.getDataSet());
			} catch (DataNotFoundException e) {
				logger.warn(e.getMessage());
			}
		}	
	}


	
}
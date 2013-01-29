package org.sopeco.persistence.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.exceptions.DataNotFoundException;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "findAllArchiveEntries", query = "SELECT o FROM ArchiveEntry o"), 
	@NamedQuery(name = "findArchiveEntriesByScenarioName", query = "SELECT o FROM ArchiveEntry o " +
			"WHERE o.scenarioName = :scenarioName"),
    @NamedQuery(name = "findArchiveEntriesByScenarioInstance", query = "SELECT o FROM ArchiveEntry o " +
		"WHERE o.scenarioName = :scenarioName "
		+ "AND o.meControllerUrl = :meControllerUrl "),
	@NamedQuery(name = "findArchiveEntriesByExpSeries", query = "SELECT o FROM ArchiveEntry o " +
			"WHERE o.scenarioName = :scenarioName "
		+ "AND o.meControllerUrl = :meControllerUrl "
		+ "AND o.experimentSeriesName = :experimentSeriesName "),
	@NamedQuery(name = "findArchiveEntriesByLabel", query = "SELECT o FROM ArchiveEntry o " +
		"WHERE o.label = :label")
	})

public class ArchiveEntry implements Serializable {
	private static Logger logger = LoggerFactory.getLogger(ArchiveEntry.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -5873188790653386360L;

	@Id
	@Column(name = "timestamp")
	private Long timestamp;

	@Column(name = "scenarioName")
	private String scenarioName;
	@Column(name = "meControllerUrl")
	private String meControllerUrl;
	@Column(name = "experimentSeriesName")
	private String experimentSeriesName;
	@Column(name = "label")
	private String label;
	@Column(name = "datasetId")
	private String datasetId;
	

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "id", referencedColumnName = "id")
	private ArchivedScenarioDefinition archivedScenarioDefinition;
	
	/**
	 * The persistence provider instance which loaded this object.
	 */
	@Transient
	private IPersistenceProvider persistenceProvider;

	public ArchiveEntry() {
		// standard constructor required for JPA
	}

	public ArchiveEntry(IPersistenceProvider persistenceProvider,Long timestamp, String scenarioName, String meControllerUrl, String experimentSeriesName,
			String label, String scenarioDefinitionXML, String datasetId) {
		this.timestamp = timestamp;
		this.scenarioName = scenarioName;
		this.meControllerUrl = meControllerUrl;
		this.experimentSeriesName = experimentSeriesName;
		this.label = label;
		this.datasetId = datasetId;
		this.archivedScenarioDefinition = new ArchivedScenarioDefinition(scenarioDefinitionXML);
		this.persistenceProvider=persistenceProvider;
	}

	public Long getTimeStamp() {
		return timestamp;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public String getMEControllerUrl() {
		return meControllerUrl;
	}

	public String getExperimentSeriesName() {
		return experimentSeriesName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public DataSetAggregated gettDataSet() {

		if (datasetId != null) {
			try {
				if(getPersistenceProvider() == null){
					logger.error("The persistence provider for this entity has not been set!");
					throw new RuntimeException("The persistence provider for this entity has not been set!");
				}
				return getPersistenceProvider().loadDataSet(
						datasetId);
			} catch (DataNotFoundException e) {
				logger.warn(e.getMessage());
			}
		}
		return null;
	}

	private IPersistenceProvider getPersistenceProvider() {
		return persistenceProvider;
	}
	
	public void setPersistenceProvider(IPersistenceProvider provider) {
		 this.persistenceProvider=provider;
	}
	
	public String getDataSetId(){
		return datasetId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((experimentSeriesName == null) ? 0 : experimentSeriesName.hashCode());
		result = prime * result + ((getLabel() == null) ? 0 : getLabel().hashCode());
		result = prime * result + ((meControllerUrl == null) ? 0 : meControllerUrl.hashCode());
		result = prime * result + ((scenarioName == null) ? 0 : scenarioName.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArchiveEntry other = (ArchiveEntry) obj;
		if (experimentSeriesName == null) {
			if (other.experimentSeriesName != null)
				return false;
		} else if (!experimentSeriesName.equals(other.experimentSeriesName))
			return false;
		if (getLabel() == null) {
			if (other.getLabel() != null)
				return false;
		} else if (!getLabel().equals(other.getLabel()))
			return false;
		if (meControllerUrl == null) {
			if (other.meControllerUrl != null)
				return false;
		} else if (!meControllerUrl.equals(other.meControllerUrl))
			return false;
		if (scenarioName == null) {
			if (other.scenarioName != null)
				return false;
		} else if (!scenarioName.equals(other.scenarioName))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

	public Long getPrimaryKey() {
		return timestamp;
	}

}

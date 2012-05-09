package org.sopeco.persistence.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetAppender;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.keys.ExperimentSeriesPK;

/**
 * @author Dennis Westermann
 * 
 */
@Entity
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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "experimentSeries", orphanRemoval = true)
	private List<ExperimentSeriesRun> experimentSeriesRuns = new ArrayList<ExperimentSeriesRun>();

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

	public ExperimentSeriesPK getPrimaryKey() {
		return primaryKey;
	}

	/*
	 * Utility Functions
	 */
	/**
	 * 
	 * @return all data measured during the experiment series runs of this
	 *         experiment series
	 */
	public DataSetAggregated getAllExperimentSeriesRunResultsInOneDataSet() {
		DataSetAppender appender = new DataSetAppender();
		for (ExperimentSeriesRun seriesRun : getExperimentSeriesRuns()) {
			if (seriesRun.getResultDataSet() != null) {
				appender.append(seriesRun.getResultDataSet());
			}
		}

		return appender.createDataSet();
	}

	/**
	 * 
	 * @return the latest experiment series run that has been executed
	 */
	public ExperimentSeriesRun getLatestExperimentSeriesRun() {
		// sorts the list of experiment runs descending based on their
		// timestamps
		Collections.sort(this.getExperimentSeriesRuns());

		return this.getExperimentSeriesRuns().get(0);
	}

	/*
	 * Overrides
	 */
	@Override
	public boolean equals(Object o) {

		// TODO cleanup
		if (this == o)
			return true;
		if (o == null || !getClass().equals(o.getClass()))
			return false;

		ExperimentSeries obj = (ExperimentSeries) o;
		if (primaryKey == null || obj.getPrimaryKey() == null)
			return false;
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

		return "ExperimentSeries{" + "name='" + this.primaryKey.getName() + "\' " + "scenarioInstance='" + scenarioInstance.toString() + '\'' + '}';
	}

}

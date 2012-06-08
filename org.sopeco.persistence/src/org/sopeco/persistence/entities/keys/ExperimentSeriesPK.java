package org.sopeco.persistence.entities.keys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.sopeco.persistence.entities.ExperimentSeries;

/**
 * Class that represents the primary key of the entity {@link ExperimentSeries}
 * 
 * @author Dennis Westermann
 */
@Embeddable
public class ExperimentSeriesPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;

	@Column(name = "version")
	private Long version;

	@Column(name = "scenarioInstanceName", nullable = false, insertable = false, updatable = false)
	private String scenarioInstanceName;

	@Column(name = "measurementEnvironmentUrl", nullable = false, insertable = false, updatable = false)
	private String measurementEnvironmentUrl;

	public ExperimentSeriesPK() {

	}

	public ExperimentSeriesPK(String name, Long version, String scenarioInstanceName, String measurementEnvironmentUrl) {
		super();
		this.name = name;
		this.version = version;
		this.scenarioInstanceName = scenarioInstanceName;
		this.measurementEnvironmentUrl = measurementEnvironmentUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getMeasurementEnvironmentUrl() {
		return measurementEnvironmentUrl;
	}

	public void setMeasurementEnvironmentUrl(String measurementEnvironmentUrl) {
		this.measurementEnvironmentUrl = measurementEnvironmentUrl;
	}

	public String getScenarioInstanceName() {
		return scenarioInstanceName;
	}

	public void setScenarioInstanceName(String scenarioInstanceName) {
		this.scenarioInstanceName = scenarioInstanceName;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ExperimentSeriesPK obj = (ExperimentSeriesPK) o;
		if (name == null || version == null || measurementEnvironmentUrl == null || scenarioInstanceName == null
				|| obj.name == null || obj.version == null || obj.measurementEnvironmentUrl == null
				|| obj.scenarioInstanceName == null)
			return false;

		if (!name.equals(obj.name) || !version.equals(obj.version)
				|| !measurementEnvironmentUrl.equals(obj.measurementEnvironmentUrl)
				|| !scenarioInstanceName.equals(obj.scenarioInstanceName))
			return false;

		return true;

	}

	@Override
	public int hashCode() {
		if (name != null && version != null && measurementEnvironmentUrl != null) {
			String hashString = name + version + measurementEnvironmentUrl + scenarioInstanceName;
			return hashString.hashCode();
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {

		return "ExperimentSeriesPK{" + "name='" + name + "\' " + "version='"
				+ version + "\' " + "measurementEnvironmentUrl='"
				+ measurementEnvironmentUrl + "\' " + "scenarioInstanceName='" + scenarioInstanceName + "\'" + '}';
	}

}

package org.sopeco.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class ArchivedScenarioDefinition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7768531529301574982L;

	@Id
	@Column(name = "id")
	private int id;

	@Lob
	@Column(name = "scenarioDefinitionXML")
	private String scenarioDefinitionXML;

	public ArchivedScenarioDefinition() {
		// standard constructor required for JPA
	}

	public ArchivedScenarioDefinition(String scenarioDefinitionXML) {
		this.scenarioDefinitionXML = scenarioDefinitionXML;
		id = scenarioDefinitionXML.hashCode();
	}

	/**
	 * @return the scenarioDefinitionXML
	 */
	public String getScenarioDefinitionXML() {
		return scenarioDefinitionXML;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getId();
		result = prime * result + ((getScenarioDefinitionXML() == null) ? 0 : getScenarioDefinitionXML().hashCode());
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
		ArchivedScenarioDefinition other = (ArchivedScenarioDefinition) obj;
		if (getId() != other.getId())
			return false;
		if (getScenarioDefinitionXML() == null) {
			if (other.getScenarioDefinitionXML() != null)
				return false;
		} else if (!getScenarioDefinitionXML().equals(other.getScenarioDefinitionXML()))
			return false;
		return true;
	}

}

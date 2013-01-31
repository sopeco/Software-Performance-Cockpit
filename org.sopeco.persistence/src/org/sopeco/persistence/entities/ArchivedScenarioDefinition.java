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

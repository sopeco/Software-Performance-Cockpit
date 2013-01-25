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

	@Column(name = "scenarioInstanceName", nullable = false, insertable = false, updatable = false)
	private String scenarioInstanceName;

	@Column(name = "measurementEnvironmentUrl", nullable = false, insertable = false, updatable = false)
	private String measurementEnvironmentUrl;

	public ExperimentSeriesPK() {

	}

	public ExperimentSeriesPK(String name, String scenarioInstanceName, String measurementEnvironmentUrl) {
		super();
		this.name = name;
		this.scenarioInstanceName = scenarioInstanceName;
		this.measurementEnvironmentUrl = measurementEnvironmentUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		if (name == null || measurementEnvironmentUrl == null || scenarioInstanceName == null || obj.name == null
				|| obj.measurementEnvironmentUrl == null || obj.scenarioInstanceName == null)
			return false;

		if (!name.equals(obj.name) || !measurementEnvironmentUrl.equals(obj.measurementEnvironmentUrl)
				|| !scenarioInstanceName.equals(obj.scenarioInstanceName))
			return false;

		return true;

	}

	@Override
	public int hashCode() {
		if (name != null && measurementEnvironmentUrl != null) {
			String hashString = name + measurementEnvironmentUrl + scenarioInstanceName;
			return hashString.hashCode();
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "ExperimentSeriesPK{" + "name='" + name + "\' " + "measurementEnvironmentUrl='"
				+ measurementEnvironmentUrl + "\' " + "scenarioInstanceName='" + scenarioInstanceName + "\'" + '}';
	}

}

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
package org.sopeco.persistence.entities.definition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dennis Westermann
 * 
 */
public class MeasurementSpecification implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	protected List<ExperimentSeriesDefinition> experimentSeriesDefinitions = new ArrayList<ExperimentSeriesDefinition>();

	protected List<ConstantValueAssignment> initializationAssignemts= new ArrayList<ConstantValueAssignment>();

	public MeasurementSpecification() {
		super();
	}

	public List<ExperimentSeriesDefinition> getExperimentSeriesDefinitions() {
		if (experimentSeriesDefinitions == null) {
			experimentSeriesDefinitions = new LinkedList<ExperimentSeriesDefinition>();
		}
		return experimentSeriesDefinitions;
	}

	public List<ConstantValueAssignment> getInitializationAssignemts() {
		if (initializationAssignemts == null) {
			initializationAssignemts = new LinkedList<ConstantValueAssignment>();
		}
		return initializationAssignemts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((experimentSeriesDefinitions == null) ? 0 : experimentSeriesDefinitions.hashCode());
		result = prime * result + ((initializationAssignemts == null) ? 0 : initializationAssignemts.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		MeasurementSpecification other = (MeasurementSpecification) obj;
		if (experimentSeriesDefinitions == null) {
			if (other.experimentSeriesDefinitions != null)
				return false;
		} else if (!experimentSeriesDefinitions.equals(other.experimentSeriesDefinitions))
			return false;
		if (initializationAssignemts == null) {
			if (other.initializationAssignemts != null)
				return false;
		} else if (!initializationAssignemts.equals(other.initializationAssignemts))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}



	

}
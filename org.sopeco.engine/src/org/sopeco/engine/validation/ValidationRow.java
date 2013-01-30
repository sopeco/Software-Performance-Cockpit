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
package org.sopeco.engine.validation;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.persistence.dataset.ParameterValue;

/**
 * Represents a row of a {@link ValidationObject} dataset.
 * 
 * @author Dennis Westermann
 * 
 */
public class ValidationRow {
	private ParameterValue<?> dependentParameterValue;
	private String dependentParameterName;
	private List<ParameterValue<?>> independentParameterValues = new ArrayList<ParameterValue<?>>();

	/**
	 * Returns the parameter value for the dependent parameter.
	 * 
	 * @return the parameter value for the dependent parameter
	 */
	public ParameterValue<?> getDependentParameterValue() {
		return dependentParameterValue;
	}

	/**
	 * Returns a list of parameter values for the independent parameters.
	 * 
	 * @return a list of parameter values for the independent parameters.
	 */
	public List<ParameterValue<?>> getIndependentParameterValues() {
		return independentParameterValues;
	}

	/**
	 * Sets the value for the dependent parameter.
	 * 
	 * @param value
	 *            the parameter value object for the dependent parameter
	 */
	public void setDependentParameterValue(ParameterValue<?> value) {
		this.dependentParameterValue = value;
	}

	/**
	 * Adds a value for an independent parameter.
	 * 
	 * @param value
	 *            the parameter value object for an independent parameter
	 */
	public void addIndependentParameterValue(ParameterValue<?> value) {
		independentParameterValues.add(value);
	}

	/**
	 * Sets the name of the dependent parameter.
	 * 
	 * @param independentParameterName
	 *            name of the dependent parameter.
	 */
	public void setDependentParameterName(String independentParameterName) {
		this.dependentParameterName = independentParameterName;
	}

	/**
	 * Returns the name of the dependent parameter.
	 * 
	 * @return the name of the dependent parameter
	 */
	public String getDependentParameterName() {
		return dependentParameterName;
	}
}

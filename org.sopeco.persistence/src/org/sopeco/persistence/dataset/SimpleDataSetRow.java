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
package org.sopeco.persistence.dataset;

import java.io.Serializable;
import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Row of a DataSet.
 * 
 * @author Jens Happe
 * 
 */
public class SimpleDataSetRow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ParameterValues of the row.
	 */
	@SuppressWarnings({ "rawtypes" })
	private List<ParameterValue> parameterValues;

	/**
	 * Constructor. Only to be used in builders & factories.
	 * 
	 * @param parameterValues
	 */
	@SuppressWarnings("rawtypes")
	protected SimpleDataSetRow(List<ParameterValue> parameterValues) {
		super();
		this.parameterValues = parameterValues;
	}

	/**
	 * @return The ParameterValues of this row.
	 */
	@SuppressWarnings("rawtypes")
	public List<ParameterValue> getRowValues() {
		return parameterValues;
	}

	/**
	 * @param parameter
	 *            Parameter of interest.
	 * @return The value of a particular parameter in this row.
	 */
	@SuppressWarnings("rawtypes")
	public ParameterValue getParameterValue(ParameterDefinition parameter) {
		for (ParameterValue pv : parameterValues) {
			if (pv.getParameter().equals(parameter)) {
				return pv;
			}
		}
		throw new IllegalArgumentException("Unknown Parameter: " + parameter);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SimpleDataSetRow)) {
			return false;
		}
		SimpleDataSetRow other = (SimpleDataSetRow) obj;
		if (this.getRowValues().size() != other.getRowValues().size()) {
			return false;
		}
		for (ParameterValue<?> pv : this.getRowValues()) {
			try {
				ParameterValue<?> otherValue = other.getParameterValue(pv
						.getParameter());
				if (!otherValue.getValue().equals(pv.getValue())) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}

		return true;
	}

	@Override
	public int hashCode() {
		int code = 0;
		for (ParameterValue<?> pv : this.getRowValues()) {
			code += pv.getParameter().hashCode() + pv.getValue().hashCode();
		}
		return code;
	}

	public boolean equalIndependentParameterValues(SimpleDataSetRow other,
			ParameterDefinition dependentParameter) {
		for (ParameterValue<?> pv : this.getRowValues()) {
			try {
				ParameterValue<?> otherValue = other.getParameterValue(pv
						.getParameter());
				if (otherValue.getParameter().getFullName()
						.equals(dependentParameter.getFullName())) {
					continue;
				}
				if (!otherValue.getValue().equals(pv.getValue())) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}

		return true;
	}

	public boolean equalIndependentParameterValues(SimpleDataSetRow other,
			List<ParameterDefinition> dependentParameters) {
		for (ParameterValue<?> pv : this.getRowValues()) {
			try {
				ParameterValue<?> otherValue = other.getParameterValue(pv
						.getParameter());
				if (dependentParameters.contains(otherValue.getParameter())) {
					continue;
				}
				if (!otherValue.getValue().equals(pv.getValue())) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}

		return true;
	}


}

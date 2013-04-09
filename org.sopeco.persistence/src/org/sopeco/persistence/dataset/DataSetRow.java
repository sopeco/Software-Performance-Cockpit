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
import org.sopeco.persistence.entities.definition.ParameterRole;

/**
 * Row of a DataSet.
 * 
 * @author Jens Happe
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class DataSetRow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ParameterValues of the row.
	 */
	private List<ParameterValue<?>> inputParameterValues;

	/**
	 * ParameterValues of the row.
	 */
	private List<ParameterValueList<?>> observationParameterValues;

	/**
	 * Constructor. Only to be used in builders & factories.
	 * 
	 * @param parameterValues
	 */
	protected DataSetRow(List<ParameterValue<?>> inputParameterValues,
			List<ParameterValueList<?>> observationParameterValues) {
		super();
		this.inputParameterValues = inputParameterValues;
		this.observationParameterValues = observationParameterValues;
	}

	/**
	 * @return The ParameterValues of this row.
	 */
	public List<ParameterValue<?>> getInputRowValues() {
		return inputParameterValues;
	}

	/**
	 * @return The ParameterValues of this row.
	 */
	public List<ParameterValueList<?>> getObservableRowValues() {
		return observationParameterValues;
	}

	/**
	 * @param parameter
	 *            Parameter of interest.
	 * @return The value of a particular parameter in this row.
	 */
	public ParameterValue getInputParameterValue(ParameterDefinition parameter) {
		if (!parameter.getRole().equals(ParameterRole.INPUT)) {
			throw new IllegalArgumentException("Parameter must be an input parameter");
		}
		for (ParameterValue pv : inputParameterValues) {
			if (pv.getParameter().equals(parameter)) {
				return pv;
			}
		}
		throw new IllegalArgumentException("Unknown Parameter: " + parameter);
	}

	/**
	 * @param parameter
	 *            Parameter of interest.
	 * @return The value of a particular parameter in this row.
	 */
	public ParameterValueList getObservableParameterValues(ParameterDefinition parameter) {
		if (!parameter.getRole().equals(ParameterRole.OBSERVATION)) {
			throw new IllegalArgumentException("Parameter must be an input parameter");
		}
		for (ParameterValueList pvl : observationParameterValues) {
			if (pvl.getParameter().equals(parameter)) {
				return pvl;
			}
		}
		throw new IllegalArgumentException("Unknown Parameter: " + parameter);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DataSetRow)) {
			return false;
		}
		DataSetRow other = (DataSetRow) obj;
		if ((this.getInputRowValues().size() != other.getInputRowValues().size())
				|| (this.getObservableRowValues().size() != other.getObservableRowValues().size())) {
			return false;
		}

		if (!equalIndependentParameterValues(other)) {
			return false;
		}

		// TODO: compare observation values
		return true;
	}

	@Override
	public int hashCode() {
		int code = 0;
		for (ParameterValue<?> pv : this.getInputRowValues()) {
			code += pv.getParameter().hashCode() + pv.getValue().hashCode();
		}
		for (ParameterValueList pvl : this.getObservableRowValues()) {

			for (Object value : pvl.getValues()) {
				code += pvl.getParameter().hashCode() + value.hashCode();
			}

		}
		return code;
	}

	public boolean equalIndependentParameterValues(DataSetRow other) {
		for (ParameterValue<?> pv : this.getInputRowValues()) {
			try {
				ParameterValue<?> otherValue = other.getInputParameterValue(pv.getParameter());
				if (!otherValue.getValue().equals(pv.getValue())) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}

		return true;
	}
	
	public int getMaxSize(){
		int maxSize = 1;
		for (ParameterValueList pvl : getObservableRowValues()) {
			if (pvl.getValues().size() > maxSize) {
				maxSize = pvl.getValues().size();
			}
		}
		return maxSize;
	}

}

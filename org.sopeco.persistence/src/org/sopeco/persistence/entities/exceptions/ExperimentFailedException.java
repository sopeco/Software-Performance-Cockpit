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
/**
 * 
 */
package org.sopeco.persistence.entities.exceptions;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Transient;

import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.persistence.util.ParameterCollectionFactory;

/**
 * Thrown by the measurement environment controller (see {@link IMeasurementEnvironmentController}) if an experiment fails.
 *  
 * It has information about the the input parameters and an error meta data in form of a key-value pair. The default key
 * ({@link ExperimentFailedException#DEFAULT_META_DATA_KEY}) can be used to retrieve the default metadata object, if there 
 * is only one.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class ExperimentFailedException extends Exception {

	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_META_DATA_KEY = "default";

	/** Holds the meta data attached to this exception. */
	protected Map<String, String> metaData = new HashMap<String, String>();
	
	/** Holds the set of input parameter values attached to this exception. */
	protected ParameterCollection<ParameterValue<?>> inputParameterValues = ParameterCollectionFactory.createParameterValueCollection();
	
	public ExperimentFailedException() {
		super();
	}


	public ExperimentFailedException(String msg) {
		super(msg);
	}

	/**
	 * Adds the given input parameter values to this exception.
	 * 
	 * @param inputParamValues a collection of input parameter valus
	 */
	public void setInputParameterValues(ParameterCollection<ParameterValue<?>> inputParameterValues) {
		this.inputParameterValues = inputParameterValues;
	}

	/**
	 * Returns the collection of input parameter values. 
	 * 
	 * @return input parameter values
	 */
	public ParameterCollection<ParameterValue<?>> getInputParameterValues() {
		return inputParameterValues;
	}

	/**
	 * Adds a meta data to this exception with the default key. Calling this method
	 * more than once will replace the default meta data.
	 * 
	 * @param data a String value
	 */
	public void addMetaData(String data) {
		metaData.put(DEFAULT_META_DATA_KEY, data);
	}
	
	/**
	 * Adds a meta data to this exception with the given key. 
	 * 
	 * @param key a key identifying this data
	 * @param data a String value
	 */
	public void addMetaData(String key, String data) {
		metaData.put(key, data);
	}
	
	/**
	 * Returns the default meta data associated with this exception. 
	 * 
	 * That is the meta data assigned to key {@link #DEFAULT_META_DATA_KEY}. 
	 * 
	 * @return the default meta data. The result can be null.
	 */
	public String getMetaData() {
		return metaData.get(DEFAULT_META_DATA_KEY);
	}
	
	/**
	 * Returns the meta data key-value map associated with this exception.
	 * 
	 * @return an unmodifiable key-value map
	 */
	public Map<String, String> getMetaDataMap() {
		return Collections.unmodifiableMap(metaData);
	}
	
	/**
	 * Returns the meta data associated with the given key.
	 * 
	 * @param key the key to the meta data
	 * @return the data object
	 */
	public Object getMetaData(String key) {
		return metaData.get(key);
	}
}

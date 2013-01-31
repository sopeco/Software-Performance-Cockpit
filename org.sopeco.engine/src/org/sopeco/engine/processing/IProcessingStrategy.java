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
package org.sopeco.engine.processing;

import java.util.Collection;
import java.util.Map;

import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.util.ParameterCollection;

/**
 * The interface of data processing strategies. A data processing strategy
 * produces a new data set out of an original data set.
 * 
 * @author Roozbeh Farahbod
 */
public interface IProcessingStrategy extends ISoPeCoExtensionArtifact {

	/**
	 * Sets the source data set(s) for this processing. This method has to be
	 * called before any call to other methods of this strategy.
	 * 
	 * @param source
	 *            a list of source data sets
	 */
	void setSourceDataSet(DataSetAggregated... source);

	/**
	 * Sets the source data set(s) for this processing. This method has to be
	 * called before any call to other methods of this strategy.
	 * 
	 * @param source
	 *            a collection of source data sets
	 */
	void setSourceDataSet(Collection<DataSetAggregated> source);

	/**
	 * Processes the source data set and returns the result.
	 * 
	 * @return the processed data set
	 */
	DataSetAggregated processData();

	/**
	 * Returns a collection of available output parameters based on the set
	 * source data set.
	 * 
	 * @return a collection of available output parameters
	 */
	ParameterCollection<ParameterDefinition> getAvailableOutputParameters();

	/**
	 * Sets a selected set of output parameters to be included in the processed
	 * data. If used, this method should be called after a call to
	 * {@link #setSourceDataSet(DataSetAggregated)} and before a call to
	 * {@link IProcessingStrategy#processData()}.
	 * 
	 * @param outputParams
	 *            the selected set of output parameters
	 */
	void setOutputParameters(ParameterCollection<ParameterDefinition> outputParams);

	/**
	 * Provides the collection of parameter definitions needed for the
	 * configuration of this strategy. The definitions are used to pass
	 * configuration values to the strategy using the
	 * {@link #configure(ParameterCollection)}.
	 * 
	 * @return a collection of parameter definitions
	 */
	ParameterCollection<ParameterDefinition> getConfigurationParameters();

	/**
	 * Configures the strategy with the given configuration values.
	 * 
	 * @param values
	 *            a collection of parameter values
	 * 
	 * @see #getConfigurationParameters()
	 */
	void configure(Map<ParameterDefinition, Object> values);
}

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
package org.sopeco.plugin.std.processing.basic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.sopeco.engine.processing.IProcessingStrategy;
import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetRow;
import org.sopeco.persistence.dataset.DataSetRowBuilder;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.persistence.util.ParameterCollectionFactory;
import org.sopeco.util.Tools;

/**
 * Provides some basic statistical data processing, mainly used as a proof of concept for the Data Processing extension.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class BasicStatisticalProcessingStrategy extends AbstractSoPeCoExtensionArtifact implements IProcessingStrategy {

	private static final String DOUBLE_TYPE_NAME = "Double";
	protected DataSetAggregated sourceDataSet = null;
	protected ParameterCollection<ParameterDefinition> availableParameters = ParameterCollectionFactory.createParameterDefinitionCollection();

	protected Map<ParameterDefinition, ParameterDefinition> meanParams = new HashMap<ParameterDefinition, ParameterDefinition>();
	protected Map<ParameterDefinition, ParameterDefinition> stdDevParams = new HashMap<ParameterDefinition, ParameterDefinition>();

	protected ParameterCollection<ParameterDefinition> outputParameters = null;
	
	public BasicStatisticalProcessingStrategy(ISoPeCoExtension<?> provider) {
		super(provider);
	}

	/**
	 * Updates available parameters collection.
	 */
	private void updateAvailableParameters() {
		availableParameters.clear();
		meanParams.clear();
		stdDevParams.clear();
		
		if (sourceDataSet.getRow(0) != null) {
			for (ParameterDefinition pd: sourceDataSet.getParameterDefinitions()) {

				availableParameters.add(pd);

				// among observation parameters 
				// if they are numeric and have more than one value in the first 
				// row, add mean and stddev as well
				// do this if the first 
				if (pd.getRole() == ParameterRole.OBSERVATION) {

					if (pd.isNumeric()) {
						final int valueCollectionSize = sourceDataSet.getRow(0).getObservableParameterValues(pd).getSize();
						if (valueCollectionSize > 1) {
							addStatVariations(pd, availableParameters);
						}
					}
				}
			}
		}
	}

	/**
	 * Adds mean and standard deviation parameters to the available parameter list.
	 * @param pd source parameter definition
	 * @param availableParams collection of available parameters
	 */
	private void addStatVariations(ParameterDefinition pd, ParameterCollection<ParameterDefinition> availableParams) {
		ParameterDefinition meanPD = EntityFactory.createParameterDefinition("Mean " + pd.getName(), DOUBLE_TYPE_NAME, pd.getRole());
		ParameterDefinition stdDevPD = EntityFactory.createParameterDefinition("Std. Dev. of " + pd.getName(), DOUBLE_TYPE_NAME, pd.getRole());
		
		availableParams.add(meanPD);
		availableParams.add(stdDevPD);
		
		meanParams.put(meanPD, pd);
		stdDevParams.put(stdDevPD, pd);
		
	}

	@Override
	public DataSetAggregated processData() {
		DataSetRowBuilder builder = new DataSetRowBuilder();
		
		for (DataSetRow row: sourceDataSet.getRowList()) {
			builder.startRow();

			// go over parameter definitions that must be included in the output
			for (ParameterDefinition pd: outputParameters) {
				
				if (pd.getRole() == ParameterRole.INPUT) {
					builder.addInputParameterValue(pd, row.getInputParameterValue(pd).getValue());
				}
				
				else 
					if (pd.getRole() == ParameterRole.OBSERVATION) {
						ParameterValueList<?> pvl = null;
						try { 
							pvl = row.getObservableParameterValues(pd);
						} catch (Exception e) {}
						
						if (pvl != null) {
							
							// if it is in the source data source
							builder.addObservationParameterValues(pd, pvl.getValues());
							
						} else {
							
							// if it is NOT in the source data source

							// mean values
							if (meanParams.containsKey(pd)) {
								pvl = row.getObservableParameterValues(meanParams.get(pd));
								
								double mean = Tools.average(pvl.getValuesAsDouble());
								
								builder.addObservationParameterValue(pd, mean);
							}
							
							// std dev values
							else 
								if (stdDevParams.containsKey(pd)) {
									pvl = row.getObservableParameterValues(stdDevParams.get(pd));
									
									double stdDev = Tools.stdDev(pvl.getValuesAsDouble());
									
									builder.addObservationParameterValue(pd, stdDev);
								}
						}
					}
			}
			
			builder.finishRow();
		}

		return builder.createDataSet();
	}

	@Override
	public ParameterCollection<ParameterDefinition> getAvailableOutputParameters() {
		return availableParameters;
	}

	@Override
	public void setOutputParameters(ParameterCollection<ParameterDefinition> outputParams) {
		this.outputParameters = outputParams;
	}

	@Override
	public void setSourceDataSet(DataSetAggregated... source) {
		if (source.length > 0)
			setSourceDataSet(source[0]);
	}	

	@Override
	public void setSourceDataSet(Collection<DataSetAggregated> source) {
		if (source.size() > 0)
			setSourceDataSet(source.iterator().next());
	}

	protected void setSourceDataSet(DataSetAggregated source) {
		this.sourceDataSet = source;
		updateAvailableParameters();
	}

	@Override
	public ParameterCollection<ParameterDefinition> getConfigurationParameters() {
		return ParameterCollectionFactory.createParameterDefinitionCollection();
	}

	@Override
	public void configure(Map<ParameterDefinition, Object> values) {
		// do nothing
	}

}

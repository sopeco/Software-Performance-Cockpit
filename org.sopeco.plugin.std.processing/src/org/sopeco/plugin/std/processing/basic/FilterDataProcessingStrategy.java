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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * Provides some basic filtering on data.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class FilterDataProcessingStrategy extends AbstractSoPeCoExtensionArtifact implements IProcessingStrategy {

	private static final Logger logger = LoggerFactory.getLogger(FilterDataProcessingStrategy.class);
	
	public static final String PN_UPPER_RANGE = "Upper Range";
	public static final String PN_LOWER_RANGE = "Lower Range";
	public static final String PN_FILTERING_PARAMETER = "Filtering Parameter";
	
	protected DataSetAggregated sourceDataSet = null;
	protected ParameterCollection<ParameterDefinition> availableParameters = ParameterCollectionFactory.createParameterDefinitionCollection();

	protected Map<ParameterDefinition, ParameterDefinition> meanParams = new HashMap<ParameterDefinition, ParameterDefinition>();
	protected Map<ParameterDefinition, ParameterDefinition> stdDevParams = new HashMap<ParameterDefinition, ParameterDefinition>();

	protected ParameterDefinition filteringParameterPD = null;
	protected ParameterDefinition lowerRangePD = null;
	protected ParameterDefinition upperRangePD = null;
	protected ParameterCollection<ParameterDefinition> configParams = null;

	protected ParameterDefinition filteringParameter = null;
	protected String loweRange = null;
	protected String upperRange = null;
	
	protected ParameterCollection<ParameterDefinition> outputParameters = null;
	
	public FilterDataProcessingStrategy(ISoPeCoExtension<?> provider) {
		super(provider);
		
		filteringParameterPD = EntityFactory.createParameterDefinition(PN_FILTERING_PARAMETER, ParameterDefinition.class.getSimpleName(), ParameterRole.INPUT);
		lowerRangePD = EntityFactory.createParameterDefinition(PN_LOWER_RANGE, String.class.getSimpleName(), ParameterRole.INPUT);
		upperRangePD = EntityFactory.createParameterDefinition(PN_UPPER_RANGE, String.class.getSimpleName(), ParameterRole.INPUT);
		
		List<ParameterDefinition> pdList = new ArrayList<ParameterDefinition>();
		pdList.add(filteringParameterPD);
		pdList.add(lowerRangePD);
		pdList.add(upperRangePD);

		configParams = ParameterCollectionFactory.createParameterDefinitionCollection(pdList);
//		configParams.add(filteringParameterPD);
//		configParams.add(lowerRangePD);
//		configParams.add(upperRangePD);
	}

	/**
	 * Updates available parameters collection.
	 */
	private void updateAvailableParameters() {
		availableParameters.clear();
		meanParams.clear();
		stdDevParams.clear();
		
		if (sourceDataSet.getRow(0) != null) {
			for (ParameterDefinition pd: sourceDataSet.getParameterDefinitions()) 
				availableParameters.add(pd);
		}
	}

	@Override
	public DataSetAggregated processData() {
		
		// check if properly configured
		if (filteringParameter == null || loweRange == null || upperRange == null) {
			logger.error(this.getClass().getSimpleName() + " is not properly configured.");
			return null;
		}

		DataSetRowBuilder builder = new DataSetRowBuilder();
		
		for (DataSetRow row: sourceDataSet.getRowList()) {

			if (isRowWithinRange(row)) {
				builder.startRow();
				
				//add input parameters
				for (ParameterValue pv: row.getInputRowValues()) 
					builder.addInputParameterValue(pv.getParameter(), pv.getValue());
				
				//add observation values
				for (ParameterValueList pvl: row.getObservableRowValues())
					builder.addObservationParameterValues(pvl);
				builder.finishRow();
			}

		}

		return builder.createDataSet();
	}

	private boolean isRowWithinRange(DataSetRow row) {
		boolean result = true;
		
		// go over parameter definitions that must be included in the output
		for (ParameterDefinition pd: outputParameters) {

			// if that is the filtering parameter
			if (pd.getName().equals(filteringParameter.getName())) {
				
				final Object value;
				// A) is it input parameter?
				if (pd.getRole() == ParameterRole.INPUT) {
					value = row.getInputParameterValue(pd).getValue();
				}
				// B) is it observation?
				else {
					final ParameterValueList pvl = row.getObservableParameterValues(pd);
					if (pvl.getSize() == 1) {
						value = pvl.getValues().get(0);
					} else {
						logger.warn("Cannot filter the data set over a parameter with more than one value per row. (parameter: {}, number of values: {})", pd.getName(), pvl.getSize());
						return false;
					}
				}

				return valueWithinRange(pd, value);
				
			}
		}
		
		return result;
	}

	private boolean valueWithinRange(ParameterDefinition pd, Object value) {
		if (loweRange.equals(upperRange)) {
			return value.equals(loweRange);
		} else {
			
			if (value instanceof Number) {
				if (value instanceof Double) {
					Double dl = Double.valueOf(loweRange);
					Double du = Double.valueOf(upperRange);
					Double dv = (Double) value;
					
					return dv >= dl && dv <= du;
				}

				if (value instanceof Integer) {
					Integer il = Integer.valueOf(loweRange);
					Integer iu = Integer.valueOf(upperRange);
					Integer iv = (Integer) value;
					
					return iv >= il && iv <= iu;
				}
				
			} else {
				
				if (loweRange instanceof Comparable && upperRange instanceof Comparable && value instanceof Comparable) {
					Comparable cl = (Comparable) loweRange;
					Comparable cu = (Comparable) upperRange;
					Comparable cv = (Comparable) value;
					
					return (cv.compareTo(cl) >= 0) && (cv.compareTo(cu) <= 0);
				}
			}
		}

		logger.error("Cannot compare the parameter value with the given range. (value: {}, lower-range: {}, upper-range: {})", new Object[] {value.toString(), loweRange, upperRange});
		return false;
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
		return configParams;
	}

	@Override
	public void configure(Map<ParameterDefinition, Object> values) {
		filteringParameter = (ParameterDefinition) values.get(filteringParameterPD);
		loweRange = (String) values.get(lowerRangePD);
		upperRange = (String) values.get(upperRangePD);
	}

}

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
package org.sopeco.plugin.std.processing.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

public class ObservationOutlierRemovalStrategy extends AbstractSoPeCoExtensionArtifact implements IProcessingStrategy {
	private static final Logger logger = LoggerFactory.getLogger(ObservationOutlierRemovalStrategy.class);

	public static final String PN_UPPER_PERCENTAGE = "Upper Percentage";
	public static final String PN_LOWER_PERCENTAGE = "Lower Percentage";
	public static final String PN_FILTERING_PARAMETER = "Filtering Parameter";

	protected DataSetAggregated sourceDataSet = null;
	protected ParameterCollection<ParameterDefinition> availableParameters = ParameterCollectionFactory.createParameterDefinitionCollection();

	protected ParameterDefinition filteringParameterPD = null;
	protected ParameterDefinition lowerPercentagePD = null;
	protected ParameterDefinition upperPercentagePD = null;
	protected ParameterCollection<ParameterDefinition> configParams = null;

	protected ParameterDefinition filteringParameter = null;
	protected Double lowePercentage = null;
	protected Double upperPercentage = null;

	protected ParameterCollection<ParameterDefinition> outputParameters = null;

	public ObservationOutlierRemovalStrategy(ISoPeCoExtension<?> provider) {
		super(provider);

		filteringParameterPD = EntityFactory.createParameterDefinition(PN_FILTERING_PARAMETER, ParameterDefinition.class.getSimpleName(), ParameterRole.INPUT);
		lowerPercentagePD = EntityFactory.createParameterDefinition(PN_LOWER_PERCENTAGE, String.class.getSimpleName(), ParameterRole.INPUT);
		upperPercentagePD = EntityFactory.createParameterDefinition(PN_UPPER_PERCENTAGE, String.class.getSimpleName(), ParameterRole.INPUT);

		List<ParameterDefinition> pdList = new ArrayList<ParameterDefinition>();
		pdList.add(filteringParameterPD);
		pdList.add(lowerPercentagePD);
		pdList.add(upperPercentagePD);

		configParams = ParameterCollectionFactory.createParameterDefinitionCollection(pdList);
	}

	/**
	 * Updates available parameters collection.
	 */
	private void updateAvailableParameters() {
		availableParameters.clear();

		if (sourceDataSet.getRow(0) != null) {
			for (ParameterDefinition pd : sourceDataSet.getParameterDefinitions())
				if (pd.getRole().equals(ParameterRole.OBSERVATION))
					availableParameters.add(pd);

		}
	}

	@Override
	public DataSetAggregated processData() {

		// check if properly configured
		if (filteringParameter == null || lowePercentage == null || upperPercentage == null) {
			logger.error(this.getClass().getSimpleName() + " is not properly configured.");
			return null;
		}

		DataSetRowBuilder builder = new DataSetRowBuilder();

		for (DataSetRow row : sourceDataSet.getRowList()) {
			builder.startRow();
			// add input parameters
			for (ParameterValue pv : row.getInputRowValues())
				builder.addInputParameterValue(pv.getParameter(), pv.getValue());

			for (ParameterValueList pvl : row.getObservableRowValues()) {
				List<Comparable> filteredObservationValues = removeOutliers(pvl.getValues());
				builder.addObservationParameterValues(pvl.getParameter(), filteredObservationValues);
			}
			builder.finishRow();
		}

		return builder.createDataSet();
	}

	private List<Comparable> removeOutliers(List<Object> originList) {
		int bottomIndex = Math.max(0, (int) (originList.size() * lowePercentage));
		int upperIndex = Math.min(originList.size(), (int) (originList.size() * upperPercentage) - 1);

		List<Comparable> comparableList = new ArrayList<Comparable>();
		for (Object obj : originList) {
			if (obj instanceof Comparable) {
				comparableList.add((Comparable) obj);
			} else {
				logger.warn("Cannot remove outliers from a non comparable list of values!");
				return null;
			}
		}

		Collections.sort(comparableList);

		return new ArrayList<Comparable>(comparableList.subList(bottomIndex, upperIndex));
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
		lowePercentage = Double.parseDouble((String) values.get(lowerPercentagePD)) / 100.0;
		upperPercentage = Double.parseDouble((String) values.get(upperPercentagePD)) / 100.0;
	}
}

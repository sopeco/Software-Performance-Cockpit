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
package org.sopeco.plugin.std.analysis.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sopeco.analysis.wrapper.AnalysisWrapper;
import org.sopeco.analysis.wrapper.common.AbstractRStrategy;
import org.sopeco.engine.analysis.IPredictionFunctionStrategy;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.dataset.AbstractDataSetColumn;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetColumnBuilder;
import org.sopeco.persistence.dataset.DataSetInputColumn;
import org.sopeco.persistence.dataset.DataSetObservationColumn;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Provides methods common to all implementations of
 * {@link IPredictionFunctionStrategy}
 * 
 * @author Dennis Westermann
 * 
 */
public abstract class AbstractAnalysisStrategy extends AbstractRStrategy {
	protected final AnalysisWrapper analysisWrapper;
	public AbstractAnalysisStrategy(ISoPeCoExtension<?> provider) {
		super(provider);
		analysisWrapper = new AnalysisWrapper();
	}

	@Override
	protected void finalize() throws Throwable {
		analysisWrapper.shutdown();
		super.finalize();
	}
	protected ParameterDefinition dependentParameterDefintion;
	protected List<ParameterDefinition> independentParameterDefinitions;

	protected Map<ParameterDefinition, Map<Object, Integer>> nonNumericParameterEncodings = new HashMap<ParameterDefinition, Map<Object, Integer>>();

	/**
	 * Checks whether the dependent and independent parameters can be derived,
	 * and sets them accordingly. If the dependent and the independent
	 * parameters are not set in the configuration, the strategy tries to use
	 * the input parameters of the dataset as independent parameters and the
	 * observation parameter of the dataset as the dependent parameter (however,
	 * then there has to be at least one input column and exactly one
	 * observation column in the dataset).
	 * 
	 * @param dataset
	 * @param config
	 */
	@SuppressWarnings("rawtypes")
	protected void deriveDependentAndIndependentParameters(DataSetAggregated dataset, AnalysisConfiguration config) {

		this.config = config;

		// determine dependent and independent parameter
		Collection<DataSetObservationColumn> observationColumns = dataset.getObservationColumns();
		if (config.getDependentParameters().size() == 0 && observationColumns.size() != 1
				|| config.getDependentParameters().size() > 1 || config.getDependentParameters().size() == 1
				&& !dataset.contains(config.getDependentParameters().get(0)))
			throw new IllegalArgumentException(
					"Either configuration has to specify exactly one dependent parameter which has to be in the data set, "
							+ "or data set must contain exactly one observation column. Actual size is "
							+ observationColumns.size());

		Collection<DataSetInputColumn> inputColumns = dataset.getInputColumns();
		if (config.getIndependentParameters().size() == 0 && inputColumns.size() == 0)
			throw new IllegalArgumentException(
					"Either configuration has to specify at least one dependent parameter which has to be in the data set,"
							+ "or data set must contain at least one input column.");

		if (config.getDependentParameters().size() == 1) {
			dependentParameterDefintion = config.getDependentParameters().get(0);
		} else {
			dependentParameterDefintion = ((DataSetObservationColumn<?>) observationColumns.toArray()[0])
					.getParameter();
			this.config.getDependentParameters().add(dependentParameterDefintion);
		}

		if (config.getIndependentParameters().size() > 0) {
			independentParameterDefinitions = config.getIndependentParameters();
		} else {
			independentParameterDefinitions = getParameterDefintions(inputColumns);
			this.config.getIndependentParameters().addAll(independentParameterDefinitions);
		}
	}

	protected DataSetAggregated createNumericDataSet(DataSetAggregated sourceDataSet) {

		DataSetColumnBuilder builder = new DataSetColumnBuilder();

		for (ParameterDefinition pd : config.getIndependentParameters()) {
			AbstractDataSetColumn<?> col = sourceDataSet.getColumn(pd);
			if (pd.isNumeric()) {
				builder.addColumn(col);
			} else {
				List<Integer> numericValues = mapValuesToInteger(col);
				ParameterDefinition numericPD = EntityFactory.createParameterDefinition(pd.getName(), "INTEGER", pd.getRole());
				numericPD.setNamespace(pd.getNamespace());
				
				switch (numericPD.getRole()) {
				case INPUT:
					builder.startInputColumn(numericPD);
					builder.addInputValueList(numericValues);
					break;
				case OBSERVATION:
					builder.startObservationColumn(numericPD);
					@SuppressWarnings("rawtypes")
					List<ParameterValueList> obsValueList = new ArrayList<ParameterValueList>();
					obsValueList.add(new ParameterValueList<Integer>(numericPD, numericValues));
					builder.addObservationValueLists(obsValueList);
					break;
				default:
					throw new IllegalArgumentException("Unkown role: " + numericPD.getRole().name());
				}
				builder.finishColumn();
			}
		}

		for (ParameterDefinition pd : config.getDependentParameters()) {
			AbstractDataSetColumn<?> col = sourceDataSet.getColumn(pd);
			if (pd.isNumeric()) {
				builder.addColumn(col);
			} else {
				throw new IllegalStateException("Dependent parameter should be numeric. Actual type is: "
						+ pd.getType());
			}
		}

		return builder.createDataSet();
	}

	/**
	 * Maps the values stored in the given column to an Integer representations.
	 * If two values in the source column are equal, they will have the same
	 * Integer representation. The encoding is stored in the Map
	 * nonNumericParameterEncodings which is a protected field in this abstract
	 * class.
	 * 
	 * @param col
	 *            the column for which the values should be encoded as integer
	 * @return a list of integers that represent the values of the columns
	 */
	private List<Integer> mapValuesToInteger(AbstractDataSetColumn<?> col) {
		HashMap<Object, Integer> nonNumericValueEncodingMap = new HashMap<Object, Integer>();
		List<Integer> mappedValues = new ArrayList<Integer>();
		int encodingCounter = 0;
		for (int i = 0; i < col.size(); i++) {
			Object valObj = col.getParameterValues().get(i).getValue();
			if (nonNumericValueEncodingMap.containsKey(valObj)) {
				mappedValues.add(nonNumericValueEncodingMap.get(valObj));
			} else {
				mappedValues.add(encodingCounter);
				nonNumericValueEncodingMap.put(valObj, encodingCounter);
				encodingCounter++;
			}
		}

		nonNumericParameterEncodings.put(col.getParameter(), nonNumericValueEncodingMap);

		return mappedValues;
	}

	/**
	 * Creates a new dataset and copies those columns that refer to dependent
	 * and independent parameters from the given source data set into the new
	 * dataset.
	 * 
	 * @param dataset
	 *            source dataset
	 * @return a dataset that contains only those columns that refer to
	 *         dependent and independent parameters
	 */
	protected DataSetAggregated extractAnalysisDataSet(DataSetAggregated dataset) {
		DataSetColumnBuilder builder = new DataSetColumnBuilder();

		if (dataset.contains(dependentParameterDefintion)) {
			builder.addColumn(dataset.getColumn(dependentParameterDefintion));
		} else {
			throw new IllegalArgumentException(
					"Dataset does not contain the parameter defined in the analysis configuration. Parameter: "
							+ dependentParameterDefintion);
		}

		for (ParameterDefinition indepParam : independentParameterDefinitions) {
			if (dataset.contains(indepParam)) {
				builder.addColumn(dataset.getColumn(indepParam));
			} else {
				throw new IllegalArgumentException(
						"Dataset does not contain the parameter defined in the analysis configuration. Parameter: "
								+ dependentParameterDefintion);
			}
		}

		return builder.createDataSet();
	}

	public boolean supports(AnalysisConfiguration strategyConf) {
		return strategyConf.getName().equalsIgnoreCase(getProvider().getName());
	}

}

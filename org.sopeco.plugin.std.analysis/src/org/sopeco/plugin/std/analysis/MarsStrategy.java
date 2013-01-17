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
package org.sopeco.plugin.std.analysis;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.analysis.wrapper.AnalysisWrapper;
import org.sopeco.engine.analysis.IPredictionFunctionResult;
import org.sopeco.engine.analysis.IPredictionFunctionStrategy;
import org.sopeco.engine.analysis.PredictionFunctionResult;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.SimpleDataSet;
import org.sopeco.persistence.dataset.SimpleDataSetRow;
import org.sopeco.persistence.dataset.SimpleDataSetRowBuilder;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.analysis.common.AbstractAnalysisStrategy;
import org.sopeco.plugin.std.analysis.util.RAdapter;
import org.sopeco.util.Tools;

/**
 * This analysis strategy allows deriving Multivariate Adaptive Regression
 * Splines (MARS) in R.
 * 
 * @author Dennis Westermann, Jens Happe
 * 
 */
public class MarsStrategy extends AbstractAnalysisStrategy implements
		IPredictionFunctionStrategy {

	Logger logger = LoggerFactory.getLogger(MarsStrategy.class);

	/**
	 * Instantiate a new MARS Analysis for R.
	 */
	public MarsStrategy(ISoPeCoExtension<?> provider) {
		super(provider);
		requireLibrary("leaps");
		requireLibrary("earth");
		loadLibraries();
	}

	@Override
	public void analyse(DataSetAggregated dataset, AnalysisConfiguration config) {

		logger.debug("Starting MARS analysis.");

		deriveDependentAndIndependentParameters(dataset, config);

		DataSetAggregated analysisDataSet = extractAnalysisDataSet(dataset);

		DataSetAggregated numericAnalysisDataSet = createNumericDataSet(analysisDataSet);

		loadDataSetInR(createValidSimpleDataSet(numericAnalysisDataSet));

		StringBuilder cmdBuilder = new StringBuilder();
		cmdBuilder.append(getId());
		cmdBuilder.append(" <- earth(");
		cmdBuilder.append(dependentParameterDefintion.getFullName("_"));
		cmdBuilder.append(" ~ . , data =");
		cmdBuilder.append(data.getId());
		cmdBuilder.append(", penalty=-1,  fast.k=0, degree=2)");
		logger.debug("Running R Command: {}", cmdBuilder.toString());
		RAdapter.getWrapper().executeCommandString(cmdBuilder.toString());
		RAdapter.shutDown();

	}

	@Override
	public IPredictionFunctionResult getPredictionFunctionResult() {
		return new PredictionFunctionResult(getFunctionAsString(),
				dependentParameterDefintion, config,
				nonNumericParameterEncodings);
	}

	/**
	 * @return the analysis result as a function string that conforms to the
	 *         JavaScript syntax
	 */
	private String getFunctionAsString() {
		String fs = RAdapter.getWrapper().executeCommandString(
				"format(" + getId() + ", style=\"max\")");
		RAdapter.shutDown();
		fs = fs.replace("\n ", "");
		fs = fs.replace("+  ", "+ ");
		fs = fs.replace("-  ", "- ");
		fs = fs.replace("max", "Math.max");
		fs = fs.replaceAll("\\s\\s+", " "); // remove double blanks
		for (ParameterDefinition pd : independentParameterDefinitions) {
			// workaround for the mars behaviour that it adds a 1 to the
			// variable name if it is a boolean value
			fs = fs.replaceAll(pd.getFullName("_") + "1", pd.getFullName("_"));
		}
		return fs;
	}

	/**
	 * Ensures that the dataset contains at least 8 rows. This is the minimum
	 * number required by the MARS implementation. If the dataset contains less
	 * than 8 rows the existing rows are duplicated.
	 * 
	 * @param dataSet
	 *            the dataset passed to the analysis strategy
	 * @return a {@link SimpleDataSet} that contains at least 8 rows
	 */
	private SimpleDataSet createValidSimpleDataSet(DataSetAggregated dataSet) {
		SimpleDataSet givenDataSet = dataSet.convertToSimpleDataSet();

		SimpleDataSetRowBuilder rb = new SimpleDataSetRowBuilder();
		while (rb.size() < 8) {

			for (SimpleDataSetRow row : givenDataSet.getRowList()) {
				rb.startRow();
				for (ParameterValue<?> pv : row.getRowValues()) {

					if (pv.getParameter().equals(dependentParameterDefintion)) {
						// scatter due to Mars Error
						// "cannot scale y (values are all equal to ..)"
						Object newValue;
						Random r = new Random();
						switch (Tools.SupportedTypes.get(pv.getParameter()
								.getType())) {
						case Double:
							newValue = pv.getValueAsDouble()
									* (1.0001 + 0.0001 * r.nextDouble());
							break;
						case Integer:
							if (r.nextBoolean()) {
								newValue = pv.getValueAsInteger()
										+ r.nextInt(2);
							} else {
								newValue = pv.getValueAsInteger()
										- r.nextInt(2);
							}
							break;
						default:
							throw new IllegalArgumentException(
									"Unsopported parameter type: "
											+ pv.getParameter().getType());
						}
						pv.setValue(newValue);
					}

					rb.addParameterValue(pv.getParameter(), pv.getValue());
				}
				rb.finishRow();
			}

		}
		return rb.createDataSet();
	}
}

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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.analysis.wrapper.common.CSVStringGenerator;
import org.sopeco.engine.analysis.AnovaCalculatedEffect;
import org.sopeco.engine.analysis.AnovaResult;
import org.sopeco.engine.analysis.IAnovaResult;
import org.sopeco.engine.analysis.IAnovaStrategy;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.dataset.AbstractDataSetColumn;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetColumnBuilder;
import org.sopeco.persistence.dataset.DataSetObservationColumn;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.dataset.SimpleDataSet;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.analysis.common.AbstractAnalysisStrategy;


/**
 * This analysis strategy allows using the ANOVA method in R.
 * 
 * @author Dennis Westermann
 * 
 */
/**
 * @author Dennis Westermann
 * 
 */
public class AnovaStrategy extends AbstractAnalysisStrategy implements IAnovaStrategy {

	Logger logger = LoggerFactory.getLogger(AnovaStrategy.class);

	AnovaResult latestAnalysisResult;

	/**
	 * Instantiate a new MARS Analysis for R.
	 */
	public AnovaStrategy(ISoPeCoExtension<?> provider) {
		super(provider);
		loadLibraries(analysisWrapper);
	}

	@Override
	public void analyse(DataSetAggregated dataset, AnalysisConfiguration config) {

		logger.debug("Starting Anova analysis.");

		deriveDependentAndIndependentParameters(dataset, config);

		DataSetAggregated analysisDataSet = extractAnalysisDataSet(dataset);

		DataSetAggregated numericAnalysisDataSet = createNumericDataSet(analysisDataSet);

		loadDataSetInR(analysisWrapper,createValidSimpleDataSet(numericAnalysisDataSet));

		/**
		 * Example for Anova in R: <br>
		 * a <- c(1, 1, 1, 1, 2, 2, 2, 2) <br>
		 * b <- c(1, 1, 2, 2, 1, 1, 2, 2) <br>
		 * c <- a*b + 2*b + 0.1*a*b + b <br>
		 * c <- jitter(c) <br>
		 * data <- data.frame(a,b,c) <br>
		 * anova(lm(c ~ a * b, data)) <br>
		 */

		StringBuilder cmdBuilder = new StringBuilder();
		cmdBuilder.append(getId());
		cmdBuilder.append(" <- anova(lm(");
		cmdBuilder.append(dependentParameterDefintion.getFullName("_"));
		cmdBuilder.append(" ~ ");
		cmdBuilder.append(CSVStringGenerator.generateParameterString("*", independentParameterDefinitions));
		cmdBuilder.append(", ");
		cmdBuilder.append(data.getId());
		cmdBuilder.append("))");
		logger.debug("Running R Command: {}", cmdBuilder.toString());
		analysisWrapper.executeCommandString(cmdBuilder.toString());

		extractResult();
	}

	private void extractResult() {
		latestAnalysisResult = new AnovaResult(config);

		// main effects
		for (ParameterDefinition pd : independentParameterDefinitions) {
			List<ParameterDefinition> paramList = new ArrayList<ParameterDefinition>();
			paramList.add(pd);
			latestAnalysisResult.addMainEffect(getEffect(paramList, pd.getFullName("_")));
		}

		// interaction effects
		String[] effectCodes = analysisWrapper.executeCommandStringArray("rownames(" + getId() + ")");

		for (int i = 0; i < effectCodes.length; i++) {
			List<ParameterDefinition> paramList = new ArrayList<ParameterDefinition>();
			String[] paramNames = effectCodes[i].split(":");
			if (paramNames.length >= 2) {
				for (int j = 0; j < paramNames.length; j++) {
					ParameterDefinition pd = getIndependentParameterDefiniton(paramNames[j]);
					if (pd != null) {
						paramList.add(pd);
					} else {
						throw new IllegalStateException(
								"Parameter in ANOVA result table is not in the list of independent parameters.");
					}
				}
				latestAnalysisResult.addInteractionEffect(getEffect(paramList, effectCodes[i]));
			}
		}
	}

	/**
	 * Searches in the list of independent parameters for a definition with the
	 * given name.
	 * 
	 * @param fullNameSeparatedByUnderscore
	 *            e.g. org_sopeco_MyParam
	 * @return the corresponding {@link ParameterDefinition} instance or
	 *         <code>null</code> if no parameter was found
	 */
	private ParameterDefinition getIndependentParameterDefiniton(String fullNameSeparatedByUnderscore) {
		for (ParameterDefinition pd : independentParameterDefinitions) {
			if (pd.getFullName("_").equalsIgnoreCase(fullNameSeparatedByUnderscore)) {
				return pd;
			}
		}

		return null;
	}

	/**
	 * Creates the effect instance by reading and interpreting the ANOVA result
	 * table from R.
	 * 
	 * @param indepParams
	 *            a single parameter for main effects and multiple parameters
	 *            for interaction effects
	 * @param effectCode
	 *            String representation of a parameter or a set of parameters
	 *            (in case of an interaction effect) in the ANOVA table provided
	 *            by R
	 * @return the effect of the given indep parameters on the dependent
	 *         parameter calculated by ANOVA
	 */
	private AnovaCalculatedEffect getEffect(List<ParameterDefinition> indepParams, String effectCode) {

		AnovaCalculatedEffect ace = new AnovaCalculatedEffect(indepParams, dependentParameterDefintion);

		ace.setDegreesOfFreedom(getValueAsDouble(effectCode, "Df").intValue());

		ace.setSumOfSquares(getValueAsDouble(effectCode, "Sum Sq"));

		ace.setMeanSquare(getValueAsDouble(effectCode, "Mean Sq"));

		ace.setfValue(getValueAsDouble(effectCode, "F value"));

		ace.setpValue(getValueAsDouble(effectCode, "Pr(>F)"));

		return ace;
	}

	private Double getValueAsDouble(String rowName, String colName) {
		StringBuilder cmdBuilder = new StringBuilder();
		cmdBuilder.append("as.double(");
		cmdBuilder.append(getId());
		cmdBuilder.append("[\"");
		cmdBuilder.append(rowName);
		cmdBuilder.append("\",]$\"");
		cmdBuilder.append(colName);
		cmdBuilder.append("\")");
		double result = analysisWrapper.executeCommandDouble(cmdBuilder.toString());

		return result;
	}

	@Override
	public IAnovaResult getAnovaResult() {

		return latestAnalysisResult;
	}

	/**
	 * Ensures that the observation parameter that is the dependent parameter
	 * contains at least 2 values. This is the minimum number required by the
	 * ANOVA implementation. If the dataset contains less than 2 values the
	 * existing value is duplicated.
	 * 
	 * @param dataSet
	 *            the dataset passed to the analysis strategy
	 * @return a {@link SimpleDataSet} that contains at least 2 values for the
	 *         dependent parameter
	 */
	private SimpleDataSet createValidSimpleDataSet(DataSetAggregated dataSet) {

		DataSetColumnBuilder builder = new DataSetColumnBuilder();
		for (AbstractDataSetColumn<?> column : dataSet.getColumns()) {
			if (column.getParameter().equals(dependentParameterDefintion)
					&& column instanceof DataSetObservationColumn<?>) {
				DataSetObservationColumn<?> depParamObsColumn = (DataSetObservationColumn<?>) column;
				for (ParameterValueList<?> pvl : depParamObsColumn.getValueLists()) {
					if (pvl.getSize() == 1) {
						pvl.addValue(pvl.getValues().get(0));
					}
				}
				builder.addColumn(depParamObsColumn);
			} else {
				builder.addColumn(column);
			}

		}

		return builder.createDataSet().convertToSimpleDataSet();
	}

}

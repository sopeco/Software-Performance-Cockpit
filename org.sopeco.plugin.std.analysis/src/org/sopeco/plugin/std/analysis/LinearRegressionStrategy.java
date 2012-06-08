package org.sopeco.plugin.std.analysis;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.analysis.IParameterInfluenceResult;
import org.sopeco.engine.analysis.IParameterInfluenceStrategy;
import org.sopeco.engine.analysis.IPredictionFunctionResult;
import org.sopeco.engine.analysis.IPredictionFunctionStrategy;
import org.sopeco.engine.analysis.ParameterInfluenceResult;
import org.sopeco.engine.analysis.ParameterRegressionCoefficient;
import org.sopeco.engine.analysis.PredictionFunctionResult;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.analysis.common.AbstractAnalysisStrategy;
import org.sopeco.plugin.std.analysis.util.CSVStringGenerator;
import org.sopeco.plugin.std.analysis.util.RAdapter;

/**
 * This analysis strategy allows executing a linear regression in R.
 * 
 * @author Dennis Westermann, Jens Happe
 * 
 */
public class LinearRegressionStrategy extends AbstractAnalysisStrategy implements IPredictionFunctionStrategy,
		IParameterInfluenceStrategy {

	Logger logger = LoggerFactory.getLogger(LinearRegressionStrategy.class);

	protected LinearRegressionStrategy(LinearRegressionStrategyExtension provider) {
		super(provider);
		loadLibraries();
	}

	@Override
	public void analyse(DataSetAggregated dataset, AnalysisConfiguration config) {

		logger.debug("Starting linear regression analysis.");

		deriveDependentAndIndependentParameters(dataset, config);

		DataSetAggregated analysisDataSet = extractAnalysisDataSet(dataset);

		DataSetAggregated numericAnalysisDataSet = createNumericDataSet(analysisDataSet);
		
		loadDataSetInR(numericAnalysisDataSet.convertToSimpleDataSet());

		// build and execute R command
		StringBuilder cmdBuilder = new StringBuilder();
		cmdBuilder.append(getId());
		cmdBuilder.append(" <- lm(");
		cmdBuilder.append(dependentParameterDefintion.getFullName("_"));
		cmdBuilder.append(" ~ ");
		cmdBuilder.append(CSVStringGenerator.generateParameterString(" + ", independentParameterDefinitions));
		cmdBuilder.append(")");
		logger.debug("Running R Command: {}", cmdBuilder.toString());
		RAdapter.getWrapper().executeRCommandString(cmdBuilder.toString());

	}

	@Override
	public IPredictionFunctionResult getPredictionFunctionResult() {
		// create and return result object
		return buildResultObject();
	}

	/**
	 * @return the analysis result as a function string that conforms to the
	 *         JavaScript syntax
	 */
	private String getFunctionAsString() {
		StringBuilder functionBuilder = new StringBuilder();
		functionBuilder.append(RAdapter.getWrapper().executeRCommandString("cat(" + getId() + "$coefficients[1])"));
		int index = 1;
		for (ParameterDefinition inputParameter : independentParameterDefinitions) {
			index++;
			functionBuilder.append(" + ");
			functionBuilder.append(RAdapter.getWrapper().executeRCommandString(
					"cat(" + getId() + "$coefficients[" + index + "])"));
			functionBuilder.append(" * ");
			functionBuilder.append(inputParameter.getFullName("_"));
		}

		/*
		 * Workaround for bug in linear regression implementation of R. R
		 * returns a function of type 10 + NA * paramID if the value of the
		 * parameter has not been varied. Fixing this by replacing NA with 0, to
		 * get the single value.
		 */
		String functionString = functionBuilder.toString();
		if(functionString.contains("NA")){
			functionString = functionString.replaceAll("NA", "0");
		}
		
		return functionString;
	}

	private IPredictionFunctionResult buildResultObject() {
		return new PredictionFunctionResult(getFunctionAsString(), dependentParameterDefintion, config, nonNumericParameterEncodings);
	}

	/**
	 * Returns the coefficient of the specified parameter from the linear model.
	 * 
	 * @param parameter
	 *            parameter for which the model coefficient is desired
	 * @return coefficient of the specified parameter
	 */
	public ParameterRegressionCoefficient getCoefficientByParameter(ParameterDefinition parameter) {

		int index = independentParameterDefinitions.indexOf(parameter);
		double coeff = RAdapter.getWrapper().executeRCommandDouble(getId() + "$coefficients[" + (index + 2) + "]");
		return new ParameterRegressionCoefficient(parameter, dependentParameterDefintion, coeff);
	}

	/**
	 * Returns the coefficient of all parameters specified in the
	 * parameterDependency from the linear model in the order as they are
	 * described in the dependency.
	 * 
	 * @param parameter
	 *            parameter for which the model coefficient is desired
	 * @return coefficient of the specified parameter
	 */
	public List<ParameterRegressionCoefficient> getAllParameterCoefficients() {
		ArrayList<ParameterRegressionCoefficient> resultList = new ArrayList<ParameterRegressionCoefficient>();
		int index = 1;

		for (int i = 0; i < independentParameterDefinitions.size(); i++) {
			ParameterDefinition parameter = independentParameterDefinitions.get(i);
			index++;
			double coeff = RAdapter.getWrapper().executeRCommandDouble(getId() + "$coefficients[" + index + "]");
			resultList.add(new ParameterRegressionCoefficient(parameter, dependentParameterDefintion, coeff));
		}
		return resultList;
	}

	@Override
	public IParameterInfluenceResult getParameterInfluenceResult() {
		ParameterInfluenceResult result = new ParameterInfluenceResult(config);
		for (ParameterRegressionCoefficient prc : getAllParameterCoefficients()) {
			result.addParameterInfluenceDescriptor(prc);
		}

		return result;
	}

}

package org.sopeco.plugin.std.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.analysis.IPredictionFunctionResult;
import org.sopeco.model.configuration.analysis.AnalysisConfiguration;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.plugin.std.analysis.common.AbstractPredictionFunctionStrategy;
import org.sopeco.plugin.std.analysis.common.PredictionFunctionResult;
import org.sopeco.plugin.std.analysis.util.CSVStringGenerator;
import org.sopeco.plugin.std.analysis.util.RAdapter;

/**
 * This analysis strategy allows executing a linear regression in R.
 * 
 * @author Dennis Westermann, Jens Happe
 * 
 */
public class LinearRegressionStrategy extends AbstractPredictionFunctionStrategy {

	Logger logger = LoggerFactory.getLogger(LinearRegressionStrategy.class);
	
	
	protected LinearRegressionStrategy(LinearRegressionStrategyExtension provider) {
		super(provider);
		loadLibraries();
	}

	@Override
	public void analyse(DataSetAggregated dataset, AnalysisConfiguration config) {
	
	
		logger.debug("Starting linear regression analysis.");
		
		validate(dataset, config);
	
		loadDataSetInR(dataset.convertToSimpleDataSet());
		
		// build and execute R command
		StringBuilder cmdBuilder = new StringBuilder();
		cmdBuilder.append(getId());
		cmdBuilder.append(" <- lm(");
		cmdBuilder.append(observationParameterDefintion.getFullName("_"));
		cmdBuilder.append(" ~ ");
		cmdBuilder.append(CSVStringGenerator.generateParameterString(" + ", inputParameterDefinitions));
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
	 * @return the analysis result as a function string that conforms to the JavaScript syntax
	 */
	private String getFunctionAsString() {
		StringBuilder functionBuilder = new StringBuilder();
		functionBuilder.append(RAdapter.getWrapper().executeRCommandString(
				"cat(" + getId() + "$coefficients[1])"));
		int index = 1;
		for (ParameterDefinition inputParameter : inputParameterDefinitions) {
			index++;
			functionBuilder.append(" + ");
			functionBuilder.append(RAdapter.getWrapper().executeRCommandString(
					"cat(" + getId() + "$coefficients[" + index + "])"));
			functionBuilder.append(" * ");
			functionBuilder.append(inputParameter.getFullName("_"));
		}
		return functionBuilder.toString();
	}
	
	
	private IPredictionFunctionResult buildResultObject(){
		return new PredictionFunctionResult(getFunctionAsString(), observationParameterDefintion, config);
	}


}

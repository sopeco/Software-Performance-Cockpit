package org.sopeco.plugin.std.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.analysis.IPredictionFunctionResult;
import org.sopeco.engine.analysis.IPredictionFunctionStrategy;
import org.sopeco.model.configuration.analysis.AnalysisConfiguration;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.persistence.dataset.AbstractDataSetColumn;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetInputColumn;
import org.sopeco.persistence.dataset.DataSetObservationColumn;
import org.sopeco.plugin.std.analysis.common.AbstractRStrategy;
import org.sopeco.plugin.std.analysis.common.PredictionFunctionResult;
import org.sopeco.plugin.std.analysis.common.RDataSet;
import org.sopeco.plugin.std.analysis.util.CSVStringGenerator;
import org.sopeco.plugin.std.analysis.util.RAdapter;

/**
 * This analysis strategy allows executing a linear regression in R.
 * 
 * @author Dennis Westermann, Jens Happe
 * 
 */
public class LinearRegressionStrategy extends AbstractRStrategy implements IPredictionFunctionStrategy {

	Logger logger = LoggerFactory.getLogger(LinearRegressionStrategy.class);
	
	ParameterDefinition observationParameterDefintion;
	List<ParameterDefinition> inputParameterDefinitions; 
	AnalysisConfiguration config;
	
	
	protected LinearRegressionStrategy(LinearRegressionStrategyExtension provider) {
		super(provider);
		loadLibraries();
	}

	@Override
	public boolean supports(AnalysisConfiguration strategyConf) {
		return strategyConf.getName().equalsIgnoreCase("Linear Regression");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void analyse(DataSetAggregated dataset, AnalysisConfiguration config) {
	
		this.config = config;
	
		logger.debug("Starting linear regression analysis.");
		
		// determine dependent and independent parameter
		Collection<DataSetObservationColumn> observationColumns = dataset.getObservationColumns();
		if (observationColumns.size() != 1)
			throw new IllegalArgumentException("DataSet must contain exactly one observation column. Actual size is " + observationColumns.size());

		Collection<DataSetInputColumn> inputColumns = dataset.getInputColumns();
		if (inputColumns.size() == 0)
			throw new IllegalArgumentException("DataSet must contain at least one input column.");

		observationParameterDefintion = ((DataSetObservationColumn<?>) observationColumns.toArray()[0]).getParameter();
		inputParameterDefinitions = getParameterDefintions(inputColumns);
		
		// load dataset in R
		RDataSet data = new RDataSet(dataset.convertToSimpleDataSet());
		data.loadDataSetInR();

		// build R command
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

	@SuppressWarnings("rawtypes")
	private List<ParameterDefinition> getParameterDefintions(Collection<DataSetInputColumn> inputColumns) {
		ArrayList<ParameterDefinition> resultList = new ArrayList<ParameterDefinition>();
		
		for (AbstractDataSetColumn<?> col : inputColumns) {
				resultList.add(col.getParameter());
		}
		return resultList;
	}


}

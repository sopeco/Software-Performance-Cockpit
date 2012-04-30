package org.sopeco.plugin.std.analysis.common;

import java.util.Collection;
import java.util.List;

import org.sopeco.engine.analysis.IPredictionFunctionStrategy;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.model.configuration.analysis.AnalysisConfiguration;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetInputColumn;
import org.sopeco.persistence.dataset.DataSetObservationColumn;

public abstract class AbstractPredictionFunctionStrategy extends AbstractRStrategy implements IPredictionFunctionStrategy {

	public AbstractPredictionFunctionStrategy(ISoPeCoExtension<?> provider) {
		super(provider);
	}

	protected ParameterDefinition observationParameterDefintion;
	protected List<ParameterDefinition> inputParameterDefinitions; 
	protected AnalysisConfiguration config;
	protected RDataSet data; 
	
	@SuppressWarnings("rawtypes")
	protected void initialize(DataSetAggregated dataset, AnalysisConfiguration config){
		
		this.config = config;
			
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
		data = new RDataSet(dataset.convertToSimpleDataSet());
		data.loadDataSetInR();
	}
	
	@Override
	public boolean supports(AnalysisConfiguration strategyConf) {
		return strategyConf.getName().equalsIgnoreCase(provider.getName());
	}

}

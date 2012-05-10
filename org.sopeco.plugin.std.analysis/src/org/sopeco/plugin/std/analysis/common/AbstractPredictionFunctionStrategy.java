package org.sopeco.plugin.std.analysis.common;

import java.util.Collection;
import java.util.List;

import org.sopeco.engine.analysis.IPredictionFunctionStrategy;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetColumnBuilder;
import org.sopeco.persistence.dataset.DataSetInputColumn;
import org.sopeco.persistence.dataset.DataSetObservationColumn;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Provides methods common to all implementations of
 * {@link IPredictionFunctionStrategy}
 * 
 * @author Dennis Westermann
 * 
 */
public abstract class AbstractPredictionFunctionStrategy extends AbstractRStrategy implements IPredictionFunctionStrategy {

	public AbstractPredictionFunctionStrategy(ISoPeCoExtension<?> provider) {
		super(provider);
	}

	protected ParameterDefinition dependentParameterDefintion;
	protected List<ParameterDefinition> independentParameterDefinitions;

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
		if (config.getDependentParameters().size() == 0 && observationColumns.size() != 1 || config.getDependentParameters().size() > 1
				|| config.getDependentParameters().size() == 1 && !dataset.contains(config.getDependentParameters().get(0)))
			throw new IllegalArgumentException("Either configuration has to specify exactly one dependent parameter which has to be in the data set, "
					+ "or data set must contain exactly one observation column. Actual size is " + observationColumns.size());

		Collection<DataSetInputColumn> inputColumns = dataset.getInputColumns();
		if (config.getIndependentParameters().size() == 0 && inputColumns.size() == 0)
			throw new IllegalArgumentException("Either configuration has to specify at least one dependent parameter which has to be in the data set,"
					+ "or data set must contain at least one input column.");

		if (config.getDependentParameters().size() == 1) {
			dependentParameterDefintion = config.getDependentParameters().get(0);
		} else {
			dependentParameterDefintion = ((DataSetObservationColumn<?>) observationColumns.toArray()[0]).getParameter();
			this.config.getDependentParameters().add(dependentParameterDefintion);
		}

		if (config.getIndependentParameters().size() > 0) {
			independentParameterDefinitions = config.getIndependentParameters();
		} else {
			independentParameterDefinitions = getParameterDefintions(inputColumns);
			this.config.getIndependentParameters().addAll(independentParameterDefinitions);
		}
	}

	/**
	 * Creates a new dataset and copies those columns that refer to dependent
	 * and independent parameters from the given source data set into the new dataset.
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
			throw new IllegalArgumentException("Dataset does not contain the parameter defined in the analysis configuration. Parameter: "
					+ dependentParameterDefintion);
		}

		for (ParameterDefinition indepParam : independentParameterDefinitions) {
			if (dataset.contains(indepParam)) {
				builder.addColumn(dataset.getColumn(indepParam));
			} else {
				throw new IllegalArgumentException("Dataset does not contain the parameter defined in the analysis configuration. Parameter: "
						+ dependentParameterDefintion);
			}
		}

		return builder.createDataSet();
	}

	@Override
	public boolean supports(AnalysisConfiguration strategyConf) {
		return strategyConf.getName().equalsIgnoreCase(provider.getName());
	}

}

package org.sopeco.plugin.std.exploration.breakdown.environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.sopeco.engine.analysis.IPredictionFunctionResult;
import org.sopeco.engine.analysis.IPredictionFunctionStrategy;
import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.experimentseries.IParameterVariation;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.dataset.AbstractDataSetColumn;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.DataSetModifier;
import org.sopeco.persistence.dataset.DataSetRowBuilder;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.persistence.util.ParameterCollectionFactory;
import org.sopeco.plugin.std.exploration.breakdown.space.RelativePosition;

/**
 * The AlgorithmsEnvironment which provides the algorithm the access to the
 * technical infrastructure and other components like analysis etc.
 * 
 */
public class AlgorithmsEnvironment {
	/**
	 * the strategy how the analysis is done.
	 */
	private AnalysisConfiguration analysisConfig;

	/**
	 * the analysis component used to add relevant measurements to the analysis
	 * model and creating the {@link #analysisResult}.
	 */
	private IPredictionFunctionStrategy analyser = null;

	/**
	 * The analysisResult is used to predict the value of a position. We store
	 * it extra to provide the generation of a new one every time a prediction
	 * should be executed.
	 */
	private IPredictionFunctionResult analysisResult = null;

	/**
	 * the data set used by the analysis. It contains the base for the analysis
	 * and the predictions.
	 */

	private DataSetRowBuilder dataSetBuilder = new DataSetRowBuilder();
	// private DataSet modelDataSet = DataFactory.eINSTANCE.createDataSet();

	/**
	 * the experiment controller used to execute a single experiment.
	 */
	private IExperimentController experimentController = null;

	/**
	 * ParameterUsage variations used to translate the {@link RelativePosition}s
	 * of the algorithm (exploration) into real world values. They are stored by
	 * there id to provide easier access.
	 */
	private HashMap<String, IParameterVariation> parameterVariationIdMap = new HashMap<String, IParameterVariation>();

	/**
	 * The observed parameter which should be predicted by the algorithm.
	 * 
	 */
	private ParameterDefinition observedParameter;

	private ExperimentSeriesRun expSeriesRun;

	private boolean resultIsInvalid;

	private double sizeOfParameterSpace = 1;
	
	private IPersistenceProvider persistenceProvider;

	/**
	 * creates a new instance.
	 * 
	 * @param experimentController
	 *            to start experiments.
	 * @param repository
	 *            the storage of the data used by the current measurements.
	 * @param analyser
	 *            used for prediction and model.
	 * @param analysisStrategy
	 *            used by the analyser to configure the analysis.
	 * @param parameterVariationIterators
	 *            the varied parameters.
	 * @param observedParameter
	 *            the observed parameter.
	 * @param expSeriesConfig
	 */
	public AlgorithmsEnvironment(IExperimentController experimentController, 
			IPersistenceProvider persistenceProvider, IPredictionFunctionStrategy analyser, 
			AnalysisConfiguration analysisConfig, List<IParameterVariation> parameterVariations, 
			ParameterDefinition observedParameter, ExperimentSeriesRun expSeriesRun) {
		this.experimentController = experimentController;
		this.analyser = analyser;
		this.analysisConfig = analysisConfig;
		this.expSeriesRun = expSeriesRun;

		this.observedParameter = observedParameter;

		// copy list into hashmap for easier handling (id as key)
		// and calculate size of parameter space
		for (IParameterVariation paramVariation : parameterVariations) {
			this.parameterVariationIdMap.put(paramVariation.getParameter().getFullName(), paramVariation);
			if (paramVariation.size() != 0.0) {
				this.sizeOfParameterSpace *= paramVariation.size();
			}
		}

	}

	/**
	 * Add a value to the analysis model base. And invalidates the
	 * analysisResult from the analyser, because the model was modified.
	 * 
	 * @param the
	 *            relative position the position which is not used at that time
	 * @param value
	 *            the Environment value which has the position info as part of
	 *            the runInfo.
	 * @throws FrameworkException
	 *             if the data could not be added to the model for any reason.
	 */
	public void addToModel(RelativePosition position, AbstractEnvironmentValue value) {
		// data is added to the data set.
		List<ParameterValue<?>> realDataPoint = this.getRealPosition(position);
		realDataPoint.add(value.getValue());

		dataSetBuilder.startRow();
		for (ParameterValue<?> parameterValue : realDataPoint) {
//			AbstractDataSetColumn<?> column = null;
//			column = modifier.getColumn(parameterValue.getParameter());
//			if (column == null) {
//				column = modifier.addColumn(parameterValue.getParameter());
//			}
//			column.getParameterValues().add(parameterValue);
			if(parameterValue.getParameter().getRole().equals(ParameterRole.INPUT)) {
				dataSetBuilder.addInputParameterValue(parameterValue.getParameter(), parameterValue.getValue());
			} else {
				dataSetBuilder.addObservationParameterValue(parameterValue.getParameter(), parameterValue.getValue());
			}
		}

		dataSetBuilder.finishRow();
		
		// create the analysis result
		this.resultIsInvalid = true;

	}

	/**
	 * measures data at this position.
	 * 
	 * @param position
	 *            is translated into real world position data
	 * @return the measurement result.
	 * @throws EnvironmentNotConfiguredException
	 *             if environment is not well configured.
	 * @throws UnsupportedDataType
	 *             if an unsupported parametertype was used.
	 */
	public AbstractEnvironmentValue measure(RelativePosition position) {

		ParameterCollection<ParameterValue<?>> realPositionList = ParameterCollectionFactory.createParameterValueCollection(this.getRealPosition(position));
		ParameterCollection<ParameterValue<?>> newRealPositionList = ParameterCollectionFactory.createParameterValueCollection(realPositionList);
		this.experimentController.runExperiment(newRealPositionList, this.expSeriesRun.getExperimentSeries().getExperimentSeriesDefinition()
				.getExperimentTerminationCondition());

		// TODO check failed results
		DataSetAggregated measuredData = this.experimentController.getLastSuccessfulExperimentResults();
		
		return EnvironmentValueFactory.createAbstractEnvironmentValue(measuredData.getObservationColumn(this.observedParameter).getParameterValues(0)
				.getMeanAsParameterValue());

	}

	/**
	 * predicts a value with help of the analysis component.
	 * 
	 * @param position
	 *            position to be predicted.
	 * @return the predicted value.
	 * @throws FrameworkException
	 */
	public AbstractEnvironmentValue predictValue(RelativePosition position) {
		if (this.resultIsInvalid) {
			this.analyser.analyse(dataSetBuilder.createDataSet(), this.analysisConfig);
			this.analysisResult = analyser.getPredictionFunctionResult();

			this.resultIsInvalid = false;
		}
		ParameterValue<?> predictedValue;
		predictedValue = this.analysisResult.predictOutputParameter(this.getRealPosition(position));

		return EnvironmentValueFactory.createAbstractEnvironmentValue(predictedValue);
	}

	/**
	 * calculates the real world position based on a relative one.
	 * 
	 * @param position
	 *            the relative Position
	 * @return a list with {@link ParameterValue}s containing the real world
	 *         position.
	 */
	public List<ParameterValue<?>> getRealPosition(RelativePosition position) {
		List<ParameterValue<?>> returnList = new ArrayList<ParameterValue<?>>(position.size());

		for (Entry<String, Double> e : position.entrySet()) {
			IParameterVariation paramVariation = this.parameterVariationIdMap.get(e.getKey());
			int indexInVariation = (int) Math.ceil(paramVariation.size() * e.getValue()) -1;
			if (indexInVariation == -1) { // special case if if e.getValue() is 0
				indexInVariation = 0;
			}
			returnList.add(paramVariation.get(indexInVariation));
		}

		return returnList;
	}

	/**
	 * TODO: REMOVE WHEN DATA EXPORT IS AVAILABLE IN THE PERFORMANCE COCKPIT.
	 * just for writing the data.
	 * 
	 * @return
	 */
	public DataSetAggregated getModelDataSet() {
		return this.dataSetBuilder.createDataSet();
	}

	/**
	 * stores an experiment run result that has been calculated by the algorithm
	 * instead of measured by the experiment controller.
	 * 
	 * @param value
	 *            - the value to be stored
	 * @param position
	 *            - the relative position of the value;
	 */
	public void storeCalculatedExperimentRunResult(AbstractEnvironmentValue value, RelativePosition position) {
//		List<ParameterValue<?>> realDataPoint = this.getRealPosition(position);
//		realDataPoint.add(value.getValue());
//
//		DataSetModifier modifierTemp = new DataSetModifier();
//
//		for (ParameterValue<?> parameterValue : realDataPoint) {
//			AbstractDataSetColumn<?> column = null;
//			column = modifierTemp.getColumn(parameterValue.getParameter());
//			if (column == null) {
//				column = modifierTemp.addColumn(parameterValue.getParameter());
//			}
//			column.getParameterValues().add(parameterValue);
//		}
//
//		
//		try {
//			ExperimentSeriesRun currentExperimentSeriesRun = 
//					persistenceProvider.loadExperimentSeriesRun(this.expSeriesRun.getPrimaryKey());
//			currentExperimentSeriesRun.append(modifierTemp.getDataSet());
//			persistenceProvider.store(currentExperimentSeriesRun);	
//		} catch (DataNotFoundException e) {
//			throw new IllegalStateException("ExperimentSeriesRun with Id " + this.expSeriesRun + " could not be loaded from database.", e);
//		}

	}

	public IPredictionFunctionResult getAnalysisResult() {
		return analysisResult;
	}

	public void setAnalysisResult(IPredictionFunctionResult analysisResult) {
		this.analysisResult = analysisResult;
	}

	public AnalysisConfiguration getAnalysisConfiguration() {
		return analysisConfig;
	}

	public IPredictionFunctionStrategy getAnalyser() {
		return analyser;
	}

	public double getSizeOfParameterSpace() {
		return sizeOfParameterSpace;
	}

}

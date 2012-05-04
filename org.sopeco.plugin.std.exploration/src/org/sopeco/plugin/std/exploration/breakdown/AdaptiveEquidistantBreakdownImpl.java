package org.sopeco.plugin.std.exploration.breakdown;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.exploration.breakdown.environment.AbstractEnvironmentValue;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess.MeasurementCacheResult;
import org.sopeco.plugin.std.exploration.breakdown.space.RelativePosition;
import org.sopeco.plugin.std.exploration.breakdown.space.Sector;
import org.sopeco.plugin.std.exploration.breakdown.space.SectorCenterPointPredictionResult;
import org.sopeco.plugin.std.exploration.breakdown.stop.AbstractStopController;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration.AccuracyDetermination;

public class AdaptiveEquidistantBreakdownImpl extends AbstractAdaptiveBreakdown implements IBreakdownExploration{

	
	public AdaptiveEquidistantBreakdownImpl(ExplorationStrategy config, 
											EnvironmentCachedAccess cachedEnvironmentAccess, 
											List<ParameterDefinition> allParameters,
											AbstractStopController stopController,
											ISoPeCoExtension<?> provider) {
		super(allParameters, cachedEnvironmentAccess, config, stopController, provider);
		
		irrelevantParameterThreshold = BreakdownConfiguration.getIrrelevantParameterThreshold(config);
		
	}
	
	
	/**
	 * Executes the iterations of the algorithm by selecting the n most
	 * errornous tuple and calling the measurement and validation process.
	 * 
	 */
	public void doIteration() {
		long numPointsInModelBeforeIteration = cachedEnvironmentAccess.getNumberOfPointsInModel();
		
		// we collect the Tuples with the highest errors and measure them (we
		// need all because of BFS)
		Set<Sector> setOfHighestErrorSectors = getSectorsWithHighestError();

		// for each bad predicted sector
		for (Sector s : setOfHighestErrorSectors) {
			highErrorSectors.remove(s);

			// compare prediction and measurement of center point
			SectorCenterPointPredictionResult prediction = getCenterPointPrediction(s);
			
			// determine next steps depending on configured accuracy determination method
			if (BreakdownConfiguration.getAccuracyDeterminationMethod(strategyConfig).equals(AccuracyDetermination.DynamicSector)) {	
				// if the prediction error is good enough	
				if (prediction.getPredictionError() < BreakdownConfiguration.getDesiredModelAccuracy(strategyConfig)) {
					
					// test if we have enough training data 
					if (cachedEnvironmentAccess.getNumberOfPointsInModel() < 40) {
						// if not, yet still split the sector to be sure that it is no lucky guess 
						addToModelAndSplitSector(prediction);
					} else if (!cachedEnvironmentAccess.isInModel(prediction.getCenterPoint())) { //TODO: Why
						// if we have enough training data, we do not split the sector and do not 
						// store the point in the training data but in the validation set
						s.error = prediction.getPredictionError();
						validationSectors.add(s);
					}
					
//					if (cachedEnvironmentAccess.getNumberOfPointsInModel() < 40) {
//						createErrorTuple(t, middlePosition, measuredValue, error);
//					} else if (error < this.treshhold
//							&& !cachedEnvironmentAccess.isInModel(middlePosition)) {
//						t.error = error;
//						tuplesForValidation.add(t);
//					} else if (!cachedEnvironmentAccess.isInModel(middlePosition)) {
//						createErrorTuple(t, middlePosition, measuredValue, error);
//					}


					
				} else { // if the prediction error is not good enough
					if (!cachedEnvironmentAccess.isInModel(prediction.getCenterPoint())) { // TODO: Why
						addToModelAndSplitSector(prediction);
					}
				}
			} else { // if the model validation is not based on the dynamic sector method
				addToModelAndSplitSector(prediction);
			}

			
			
			if (reachedStopCondition()) {
				return;
			}
		}
		
		// check if new points have been added to the model
		hasNewModelPoints = numPointsInModelBeforeIteration < cachedEnvironmentAccess.getNumberOfPointsInModel();
	}


	private SectorCenterPointPredictionResult getCenterPointPrediction(Sector s){
		// measure edges of sector and add the values to the training set
		List<RelativePosition> listOfSectorEdges = measureSectorEdges(s);
		
		// calculate center point of sector
		RelativePosition centerPoint = getSectorCenterPoint(s);

		// measure center point
		MeasurementCacheResult measuredCenterPointValue = measure(centerPoint, s.parametersToIgnore);
	
		// predict center point
		AbstractEnvironmentValue predictedCenterPointValue = 
				cachedEnvironmentAccess.predictValue(centerPoint);

		// calculate prediction error
		Double error = measuredCenterPointValue.value
				.calculateDifferenceInPercent(predictedCenterPointValue);
		
		// determine parameter areas that can be ignored in further iterations
		Map<String, Double> parametersToIgnore = null;
		if (irrelevantParameterThreshold != null && irrelevantParameterThreshold >= 0.0) {
			parametersToIgnore = getParametersToIgnore(listOfSectorEdges,
					s.parametersToIgnore);
		}
		
		return new SectorCenterPointPredictionResult(listOfSectorEdges, centerPoint, 
				measuredCenterPointValue, predictedCenterPointValue, error, parametersToIgnore);
	}
	
	private void addToModelAndSplitSector(SectorCenterPointPredictionResult prediction) {
		// add measured center point value to training set
		boolean furtherSplitPossible = cachedEnvironmentAccess.addToModel(prediction.getCenterPoint(),
				 prediction.getMeasuredValue().value);
		
		// split sector
		if (furtherSplitPossible) {
			splitSector(prediction.getAllEdges(), 
				prediction.getCenterPoint(), prediction.getPredictionError(),
				prediction.getParametersToIgnore());
		}
	}
	
	
	/**
	 * Check if old measurements rated with an error lower then the threshold
	 * (validationTuples) still have an error lower then the threshold. If not,
	 * they are added to the high error sectors.
	 * 
	 * @throws FrameworkException
	 */
	protected boolean checkValidationSectors() {

		boolean allSectorsStillAccurateEnough = true;
		
		Set<Sector> oldValidationSectors = validationSectors;

		validationSectors = new HashSet<Sector>();
		
		for (Sector s : oldValidationSectors) {	
			
			// compare prediction and measurement of center point
			SectorCenterPointPredictionResult prediction = getCenterPointPrediction(s);
		
			// if the prediction error is good enough	
			if (prediction.getPredictionError() < BreakdownConfiguration.getDesiredModelAccuracy(strategyConfig)) {
					// do not split the sector and do not 
					// store the point in the training data but in the validation set
					s.error = prediction.getPredictionError();
					validationSectors.add(s);
				
			} else { // if the prediction error is not good enough
				allSectorsStillAccurateEnough = false;
				addToModelAndSplitSector(prediction);
			}
			
		}

		return allSectorsStillAccurateEnough;
	}
}

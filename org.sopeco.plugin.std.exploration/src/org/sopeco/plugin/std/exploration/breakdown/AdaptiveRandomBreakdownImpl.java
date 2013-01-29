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
package org.sopeco.plugin.std.exploration.breakdown;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.exploration.breakdown.environment.AbstractEnvironmentValue;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess.MeasurementCacheResult;
import org.sopeco.plugin.std.exploration.breakdown.space.RelativePosition;
import org.sopeco.plugin.std.exploration.breakdown.space.Sector;
import org.sopeco.plugin.std.exploration.breakdown.space.SectorRandomPointPredictionResult;
import org.sopeco.plugin.std.exploration.breakdown.stop.AbstractStopController;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration.AccuracyDetermination;

public class AdaptiveRandomBreakdownImpl extends AbstractAdaptiveBreakdown implements IBreakdownExploration {

	
	public AdaptiveRandomBreakdownImpl(ExplorationStrategy config, 
			EnvironmentCachedAccess cachedEnvironmentAccess, 
			List<ParameterDefinition> allParameters,
			AbstractStopController stopController,
			ISoPeCoExtension<?> provider) {
		super(allParameters, cachedEnvironmentAccess, config, stopController, provider);
	}

	@Override
	public void doIteration() {
		long numPointsInModelBeforeIteration = cachedEnvironmentAccess.getNumberOfPointsInModel();
		
		// we collect the Tuples with the highest errors and measure them (we
		// need all because of BFS)
		Set<Sector> setOfHighestErrorSectors = getSectorsWithHighestError();

		// for each bad predicted sector
		for (Sector s : setOfHighestErrorSectors) {
			highErrorSectors.remove(s);

			measureSectorEdges(s);
			
			// get n new (=not in model) random points within this sector
			double sumOfPredictionErrors = 0.0;
			LinkedList<SectorRandomPointPredictionResult> newPointsInThisSector 
					= new LinkedList<SectorRandomPointPredictionResult>();
			
			for (int i = 0; i < BreakdownConfiguration.getNumberOfExperimentsPerSectionSplit(strategyConfig); i++) {
				boolean isInModel = true;
				int numAttempts = 0;
				// create a new random point (should not be part of the model set)
				RelativePosition newPoint = null;
				while (isInModel && numAttempts < 3) {
					newPoint = createRandomPoint(s);
					isInModel = cachedEnvironmentAccess.isInModel(newPoint);
					numAttempts++;
				}
				
				// if we found a new point that is not already part of the model
				if (!isInModel) {
					
					SectorRandomPointPredictionResult prediction = getRandomPointPrediction(newPoint);
		
					newPointsInThisSector.add(prediction);
					
					sumOfPredictionErrors += prediction.getPredictionError();
			
				}
				
			}
			
			double avgSectorPredictionError  = 0.0;
			if (!newPointsInThisSector.isEmpty()) {
				 avgSectorPredictionError = sumOfPredictionErrors / newPointsInThisSector.size();
			}
		
			determineNextStepsBasedOnAccuracyDeterminationMethod(s,
					newPointsInThisSector, avgSectorPredictionError);
			
			
			if (reachedStopCondition()) {
				return;
			}
		}
		
		// check if new points have been added to the model
		hasNewModelPoints = numPointsInModelBeforeIteration < cachedEnvironmentAccess.getNumberOfPointsInModel();
	}

	
	private SectorRandomPointPredictionResult getRandomPointPrediction(RelativePosition newPoint){
		// measure point
		MeasurementCacheResult measuredValue = measure(newPoint, null);
	
		// predict point
		AbstractEnvironmentValue predictedValue = 
				cachedEnvironmentAccess.predictValue(newPoint);

		// calculate prediction error
		Double error = measuredValue.value
				.calculateDifferenceInPercent(predictedValue);
		
		return new SectorRandomPointPredictionResult(
				newPoint, measuredValue, predictedValue, error);
	}
	
	private void determineNextStepsBasedOnAccuracyDeterminationMethod(
			Sector s,
			LinkedList<SectorRandomPointPredictionResult> newPointsInThisSector,
			double avgSectorPredictionError) {
		// determine next steps depending on configured accuracy determination method
		if (BreakdownConfiguration.getAccuracyDeterminationMethod(strategyConfig).equals(AccuracyDetermination.DynamicSector)){	
			// if the prediction error is good enough	
			if (avgSectorPredictionError < BreakdownConfiguration.getDesiredModelAccuracy(strategyConfig)) {
				
				// test if we have enough training data 
				if (cachedEnvironmentAccess.getNumberOfPointsInModel() < 40) {
					addToModelAndSplitSector(s, newPointsInThisSector,
							avgSectorPredictionError);
				} else {
					// if we have enough training data, we do not split the sector and do not 
					// store the points in the training data but in the validation set
					s.error = avgSectorPredictionError;
					for (SectorRandomPointPredictionResult prediction : newPointsInThisSector) {
						s.addSectorValidationPoint(prediction.getPoint());
					}
					validationSectors.add(s);
				}
				
			} else { // if the prediction error is not good enough
				
					addToModelAndSplitSector(s, newPointsInThisSector,
						avgSectorPredictionError);
			}
		} else { // if the model validation is not based on the dynamic sector method
			addToModelAndSplitSector(s, newPointsInThisSector,
					avgSectorPredictionError);
		}
	}

	private void addToModelAndSplitSector(
			Sector s,
			LinkedList<SectorRandomPointPredictionResult> newPointsInThisSector,
			double avgSectorPredictionError) {
		boolean furtherPointsAvailable = addNewPointsToModel(newPointsInThisSector);
		if (furtherPointsAvailable) {
			splitSector(getAllEdges(s.position1, s.position2), 
					getMiddle(s.position1, s.position2), 
					avgSectorPredictionError, null);
		}
	}

	private boolean addNewPointsToModel(List<SectorRandomPointPredictionResult> newPoints) {
		boolean returnValue = false;
		for (SectorRandomPointPredictionResult prediction : newPoints) {
			boolean added = cachedEnvironmentAccess.addToModel(prediction.getPoint(), prediction.getMeasuredValue().value);
			if (added) {
				returnValue  = true;
			}
		}
		return returnValue;
	}
	
	/**
	 * check if old measurements rated with an error lower then the threshold
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
			
			LinkedList<SectorRandomPointPredictionResult> validationPointPredictions = new LinkedList<SectorRandomPointPredictionResult>();
			double avgSectorPredictionError = 0.0;
			double sumOfPredictionErrors = 0.0;
			int i = 0;
			for (RelativePosition p : s.getSectorValidationPoints()){
				SectorRandomPointPredictionResult prediction = getRandomPointPrediction(p);
				validationPointPredictions.add(prediction);
				sumOfPredictionErrors += prediction.getPredictionError();
				i++;
			}
			if (i > 0) {
				avgSectorPredictionError = sumOfPredictionErrors / i;
			}
			
			// if the prediction error is good enough	
			if (avgSectorPredictionError < BreakdownConfiguration.getDesiredModelAccuracy(strategyConfig)) {
					// do not split the sector and do not 
					// store the point in the training data but in the validation set
					s.error = avgSectorPredictionError;
					validationSectors.add(s);
				
			} else { // if the prediction error is not good enough
				allSectorsStillAccurateEnough = false;
				addToModelAndSplitSector(s, validationPointPredictions, avgSectorPredictionError);
			}
			
		}

		return allSectorsStillAccurateEnough;
	}

	
	
}

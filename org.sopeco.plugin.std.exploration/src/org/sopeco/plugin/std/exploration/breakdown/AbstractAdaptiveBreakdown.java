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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.exploration.breakdown.environment.AbstractEnvironmentValue;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess.MeasurementCacheResult;
import org.sopeco.plugin.std.exploration.breakdown.space.RelativePosition;
import org.sopeco.plugin.std.exploration.breakdown.space.Sector;
import org.sopeco.plugin.std.exploration.breakdown.stop.AbstractStopController;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration.AccuracyDetermination;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration.DynamicSectorAccuracyScope;
import org.sopeco.plugin.std.exploration.breakdown.util.Projection;

public abstract class AbstractAdaptiveBreakdown extends AbstractBreakdownExploration {

	public AbstractAdaptiveBreakdown(List<ParameterDefinition> allParameters,
			EnvironmentCachedAccess cachedEnvironmentAccess, ExplorationStrategy config, AbstractStopController stopController, ISoPeCoExtension<?> provider) {
		super(allParameters, cachedEnvironmentAccess, config, stopController, provider);	
	}


	/**
	 * A threshold defining if a parameter is relevant influencing for the
	 * parameter under investigation.
	 */
	protected Double irrelevantParameterThreshold = null;
	
	
	
	/**
	 * contains tuples with a high error. The space described by these tuples
	 * needs further measurements.
	 */
	protected Set<Sector> highErrorSectors = new HashSet<Sector>();
	
	


	/**
	 * contains tuples with a low error to validate the model circular. Because
	 * of new points in the model the prediction in one area may become bad.
	 */
	protected Set<Sector> validationSectors = new HashSet<Sector>();
	
	

	
	/**
	 * Adds the first tuple to be measured to the queue. It defines the whole
	 * space of the parameters to be varied to observe the parameter under
	 * investigation.
	 */
	public void initialise() {
		Sector s = createFullSpaceSector(allParameters);
		// there are no other Tuples in the queue --> we can keep the error with
		// value 0.
		highErrorSectors.add(s);
	}
	
	@Override
	public boolean hasMoreIterations() {
		return !this.highErrorSectors.isEmpty() && super.hasMoreIterations();
	}
	
	@Override
	public void validate() {
		
			// determine accuracy of model
			if (BreakdownConfiguration.getAccuracyDeterminationMethod(strategyConfig).
					equals(AccuracyDetermination.DynamicSector)) {
				boolean allSectorsStillAccurateEnough = false;
				if ((hasNewModelPoints && isTimeForValidation()) || (!hasMoreIterations())) {
					// check if all validation sectors are still good enough predicted
					allSectorsStillAccurateEnough = checkValidationSectors();
					DynamicSectorAccuracyScope scope = BreakdownConfiguration.getDynamicSectorAccuracyScope(strategyConfig);
					if (scope == DynamicSectorAccuracyScope.Local) {
						accurateEnoughModel = this.highErrorSectors.isEmpty() && allSectorsStillAccurateEnough;
					} else if (scope == DynamicSectorAccuracyScope.Global) {
						if (getAvgSectorPredictionError() < BreakdownConfiguration.getDesiredModelAccuracy(strategyConfig)){
							accurateEnoughModel = true;
						}
					}
				}
			} else {
				super.validate();
			}
		
	}
	
	protected abstract boolean checkValidationSectors();
	
	private double getAvgSectorPredictionError() {
		
		double sumOfSectorPredictionErrors = 0;
		int numSectors = 0;
		
		for (Sector s : highErrorSectors) {
			sumOfSectorPredictionErrors += s.error;
			numSectors++;
		}
		for (Sector s : validationSectors) {
			sumOfSectorPredictionErrors += s.error;
			numSectors++;
		}
		
		return sumOfSectorPredictionErrors / numSectors;
	}
	
	/**
	 * The last work to be executed. It copies the data from the validation
	 * repository to the model.
	 * 
	 * @throws EnvironmentNotConfiguredException
	 * @throws FrameworkException
	 */
	public void finishWork() {
		for (Sector t : this.validationSectors) {
			RelativePosition pMiddle = this.getMiddle(t.position2, t.position1);
			AbstractEnvironmentValue value = this.cachedEnvironmentAccess
					.measure(pMiddle).value;
			this.cachedEnvironmentAccess.addToModel(pMiddle, value);
		}
		
		cachedEnvironmentAccess.cleanUp();
	}
	
	
	protected void splitSector(List<RelativePosition> listOfSectorEdges,
			RelativePosition middlePosition,
			Double error, Map<String, Double> parametersToIgnore) {
		
		Sector newSector;
		for (RelativePosition p : listOfSectorEdges) {
			newSector = new Sector();
			newSector.position2 = middlePosition;
			newSector.position1 = p;
			newSector.error = error;
			newSector.parametersToIgnore = parametersToIgnore;
			this.highErrorSectors.add(newSector);
		}

	}
	
	/**
	 * returns the set of Tuples with the highest error stored in
	 * tuplesToBeMeasured.
	 * 
	 * @return the set of Tuples with the highest error stored in
	 *         tuplesToBeMeasured.
	 */
	protected Set<Sector> getSectorsWithHighestError() {
		// sorted Set is not working because errors may be the same
		// maybe we could implement our own DT later.
		Set<Sector> returnSet = new HashSet<Sector>();
		Sector referenceTuple = new Sector();
		referenceTuple.error = Double.NEGATIVE_INFINITY;

		for (Sector t : highErrorSectors) {
			if (t.error > referenceTuple.error) {
				referenceTuple = t;
				returnSet = new HashSet<Sector>();
				returnSet.add(t);
			} else if (t.error.equals(referenceTuple.error)) { // is ok, because
																// they must
																// have the
																// exact same
																// value,
																// because they
																// got there
																// value by the
																// same
																// calculation.
			
				returnSet.add(t);
			}
		}

		return returnSet;
	}
	
	
	
	

	/**
	 * searches for parameters not influencing the value of the measurement.
	 * These parameters are mapped to a default value which is used instead for
	 * the measurements later. Thus the measurement can be decreased to a lower
	 * dimension, because one parameter can called with a fix value every time.
	 * 
	 * This behavior could be interpreted as a projection to a lower dimensional
	 * space where the colliding positions are checked for equivalence to decide
	 * if the eliminated parameter is an important one in this area of the
	 * space.
	 * 
	 * @param edges
	 *            the edges where the irrelevant parameters should be searched.
	 * @param parametersToIgnore
	 *            contains mappings of unimportant Parameters already known.
	 * @return Map containing parameters key and value of parameters not
	 *         relevant in this area. The value is the one from the projection
	 *         plane.
	 * @throws FrameworkException
	 */
	protected Map<String, Double> getParametersToIgnore(
			List<RelativePosition> edges, Map<String, Double> parametersToIgnore) {
		Map<String, Double> parametersWithoutInfluence = new TreeMap<String, Double>();

		RelativePosition parametersToCheck = (RelativePosition) edges.get(0)
				.clone();

		// we check every parameter if it influences
		for (Entry<String, Double> parameterUnderInvestigation : parametersToCheck
				.entrySet()) {
			// but not for those already known as influencing and therefore
			// stored in existingMappings.
			if (!parametersToIgnore.containsKey(parameterUnderInvestigation
					.getKey())) {
				List<Projection> projections = new ArrayList<Projection>();
				// we replace the parameter of the edges with the parameter
				// under investigation and the already known mappings
				for (RelativePosition originalEdge : edges) {
					Projection projection = new Projection();
					projection.original.putAll(originalEdge);
					projection.projected.putAll(originalEdge);

					projection.projected.putAll(parametersToIgnore);
					// we set it all to the value of the first edge
					projection.projected.put(
							parameterUnderInvestigation.getKey(),
							parameterUnderInvestigation.getValue());

					projections.add(projection);
				}

				// Now we check for the relevance of the parameter currently
				// replaced.
				// If it is not of relevance it is added to the set of
				// replacements.
				if (!hasSignificantRelevance(projections,
						parametersToIgnore)) {
					parametersWithoutInfluence.put(
							parameterUnderInvestigation.getKey(),
							parameterUnderInvestigation.getValue());
				}
			}
		}

		// the new one contains at least the old ones.
		parametersWithoutInfluence.putAll(parametersToIgnore);

		return parametersWithoutInfluence;
	}

	
	/**
	 * Decides if a parameter has significant impact.
	 * 
	 * @param projections
	 *            the projections an the real positions
	 * @param existingReplacements
	 *            the replacements (Parameters without significant impact)
	 *            already known.
	 * @return true if the Projection eliminated significant Parameters. false
	 *         the other case.
	 * @throws FrameworkException
	 */
	private boolean hasSignificantRelevance(List<Projection> projections,
			Map<String, Double> existingReplacements) {
		for (List<Projection> listOfEqualProjections : Projection
				.getEqualProjections(projections)) {
			MeasurementCacheResult lowestResult = null;
			MeasurementCacheResult highestResult = null;

			// lets get the highest and lowest one.
			for (Projection oneOfTheEqualProjections : listOfEqualProjections) {
				MeasurementCacheResult result = this
						.measure(
								oneOfTheEqualProjections.original,
								existingReplacements);

				if ((lowestResult == null)
						|| (result.value.isLower(lowestResult.value))) {
					lowestResult = result;
				}

				if ((highestResult == null)
						|| !(result.value.isLower(highestResult.value))) {
					highestResult = result;
				}
			}

			// is the difference low enough to expect it as non influencing the
			// result?
			if (lowestResult.value
					.calculateDifferenceToAverage(highestResult.value) > this.irrelevantParameterThreshold) {
				return true;
			}
			
		}

		return false;
	}
	
	

	
	
	
}

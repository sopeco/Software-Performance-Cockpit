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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess.MeasurementCacheResult;
import org.sopeco.plugin.std.exploration.breakdown.space.RelativePosition;
import org.sopeco.plugin.std.exploration.breakdown.space.Sector;
import org.sopeco.plugin.std.exploration.breakdown.stop.AbstractStopController;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration;
import org.sopeco.plugin.std.exploration.breakdown.util.BreakdownConfiguration.AccuracyDetermination;
import org.sopeco.plugin.std.exploration.breakdown.util.ValidationSetManager;

/**
 * This class provides functionality that is common to all breakdown
 * explorations.
 * 
 * @author Rouven Krebs, Dennis Westermann
 */
public abstract class AbstractBreakdownExploration extends AbstractSoPeCoExtensionArtifact {

	ExplorationStrategy strategyConfig;

	/**
	 * contains all parameters defining the measurement space. They are used to
	 * create the positions to be measured.
	 */
	protected List<ParameterDefinition> allParameters;

	protected boolean hasNewModelPoints = false;

	/**
	 * The Facade to access the environment (Analysis, Storage,
	 * ExperimentController etc.). Used to prevent measuring the same point two
	 * times, or adding the same point two times to the model. We prevent the
	 * algorithms implementation of this technical work by using a separate
	 * class.
	 */
	protected EnvironmentCachedAccess cachedEnvironmentAccess;

	/**
	 * a controller decides if a stop condition is reached. The condition if the
	 * model is accurate enough is checked extra. The stopController is checking
	 * for things like Runtime, or number of measurements.
	 */
	protected AbstractStopController stopController;

	/**
	 * true if the model fullfils the accuracy criteria.
	 */
	protected boolean accurateEnoughModel = false;

	private ValidationSetManager validationSetManager = null;

	/**
	 * An artificial time stamp increased every time a validation is executed.
	 * The timestamp is calculated by {@link #isTimeForValidation()}.
	 */
	private long lastValidationExecutionTimestamp = 0;

	/**
	 * this is the base used in {@link #isTimeForValidation()} to calculate an
	 * artificial time stamp defining if the validation of
	 * {@link #validationSectors} should be executed. <br>
	 * A Low value means lesser validations.
	 */
	private Double baseForValidationExecutionCheck;

	public AbstractBreakdownExploration(List<ParameterDefinition> allParameters, EnvironmentCachedAccess cachedEnvironmentAccess, ExplorationStrategy config,
			AbstractStopController stopController, ISoPeCoExtension<?> provider) {
		super(provider);
		this.allParameters = allParameters;
		this.cachedEnvironmentAccess = cachedEnvironmentAccess;
		this.stopController = stopController;
		this.strategyConfig = config;
		baseForValidationExecutionCheck = BreakdownConfiguration.getDiminutionOfValidation(strategyConfig);

	}

	/**
	 * @return true if the exploration should be stopped due to a stop criteria
	 */
	public boolean reachedStopCondition() {
		return accurateEnoughModel || this.stopController.shouldStop() || !hasMoreIterations();
	}

	/**
	 * @return a randomly selected point in the parameter space explored by the strategy
	 */
	protected RelativePosition createRandomPoint() {
		Random random = new Random();
		RelativePosition result = new RelativePosition();
		for (ParameterDefinition dimension : allParameters) {
			result.put(dimension.getFullName(), random.nextDouble());
		}
		return result;
	}

	/**
	 * @param s - the sector from which a random point should be selected
	 * @return a randomly selected point within the given sector
	 */
	protected RelativePosition createRandomPoint(Sector s) {
		Random random = new Random();
		RelativePosition result = new RelativePosition();
		for (ParameterDefinition dimension : allParameters) {
			double min = getLowerBound(s, dimension.getFullName());
			double max = getUpperBound(s, dimension.getFullName());
			double randomBetween = random.nextDouble() * (max - min) + min;
			result.put(dimension.getFullName(), randomBetween);
		}
		return result;
	}

	private double getLowerBound(Sector s, String parameterId) {
		double v1 = s.position1.get(parameterId);
		double v2 = s.position2.get(parameterId);
		return Math.min(v1, v2);
	}

	private double getUpperBound(Sector s, String parameterId) {
		double v1 = s.position1.get(parameterId);
		double v2 = s.position2.get(parameterId);
		return Math.max(v1, v2);
	}

	/**
	 * start to measure the values of the borders of the parameter space. It
	 * selects every single border as that one, have to be measured and
	 * delegates the measurement itself to
	 * {@link #measureSingleBorder(RelativePosition, String, int, double, double)}
	 * .
	 * 
	 * @throws EnvironmentNotConfiguredException
	 * @throws FrameworkException
	 */
	public void measureBordersOfParameterSpace() {
		if (BreakdownConfiguration.getBorderMeasurementDepth(strategyConfig) == 0) {
			return;
		}
		Sector fullRoomTuple = createFullSpaceSector(allParameters);

		List<RelativePosition> edgePositions = getAllEdges(fullRoomTuple.position1, fullRoomTuple.position2);

		// we iterate every border two times, but that doesn't really matters
		// because the measurement itself is cached
		// and therefore not executed two times, so we can keep it simple.
		for (RelativePosition rp : edgePositions) {
			for (String id : rp.keySet()) {
				RelativePosition position = new RelativePosition(rp);
				measureSingleBorder(position, id, BreakdownConfiguration.getBorderMeasurementDepth(strategyConfig), 0d, 1d);
			}
		}
	}

	/**
	 * Creates a tuple enclosing the whole measurement space.
	 * 
	 * @return a tuple enclosing the whole possible space.
	 */
	protected Sector createFullSpaceSector(List<ParameterDefinition> allParameters) {
		// create the tuple describing the full space.
		RelativePosition lowPosition = new RelativePosition();
		RelativePosition highPosition = new RelativePosition();

		for (ParameterDefinition dimension : allParameters) {
			lowPosition.put(dimension.getFullName(), 0d);
			highPosition.put(dimension.getFullName(), 1d);
		}
		// create new Tuple to be measured --> no error have to be calculated
		Sector t = new Sector();
		t.position2 = highPosition;
		t.position1 = lowPosition;
		t.error = 0d;
		return t;
	}

	/**
	 * This method calculates the edges of a defined space. The space is defined
	 * by two positions enclosing it.
	 * 
	 * @param position1
	 *            the first Position.
	 * @param position2
	 *            the second Position.
	 * @return a List containing all the Positions of the space enclosed by
	 *         position1 and position2.
	 */
	protected static List<RelativePosition> getAllEdges(RelativePosition position1, RelativePosition position2) {
		/*
		 * the edges calculated by permuting all combinations of elements from
		 * position1 and position2 this is implemented with a recursive call.
		 */
		ArrayList<RelativePosition> listOfEdges = new ArrayList<RelativePosition>();
		calculateEdgesRecursive(position1, position2, listOfEdges);

		return listOfEdges;
	}

	/**
	 * helps to calculates edges of a space by copy elements from the constant
	 * position to the modified one.
	 * 
	 * @param modified
	 *            the modified position which is changed every recursive call.
	 * @param constant
	 *            unmodified position where the elements came from an copied to
	 *            the modified one.
	 * @param edges
	 *            collects all the discovered edges
	 */
	protected static void calculateEdgesRecursive(RelativePosition modified, RelativePosition constant, List<RelativePosition> edges) {

		RelativePosition p = new RelativePosition();
		p.putAll(modified);

		if (!edges.contains(p)) { // this happens if the method is the first on
								// stack.
			edges.add(p);
		}
		
		// we don't use Threads, therefore we don't suggest a change of the map
		for (int i = 0; i < modified.size(); i++) {
			p = new RelativePosition();
			int j = 0;
			for (Entry<String, Double> entry : modified.entrySet()) {
				if (j++ != i) {
					p.put(entry.getKey(), entry.getValue());
				} else {
					p.put(entry.getKey(), constant.get(entry.getKey()));
				}
			}

			if (!edges.contains(p)) {
				edges.add(p);
				calculateEdgesRecursive(p, constant, edges);
			}
		}
	}

	/**
	 * Measures a single border by recursive calling itself. Every time it
	 * calculates middle = (highValue + lowValue) / 2 where the measurement is
	 * done at middle. The recursive call is done once with lowValue = middle,
	 * and once with highValue = middle. The parameter borderMeasurementDepth is
	 * decremented every time. It stops when borderMeasurementDepth is 0.
	 * 
	 * @param position
	 *            defines the values of the parameters not changed and even
	 *            contains the parameter to change.
	 * @param parameterIdVaried
	 *            the id of the parameter to change.
	 * @param borderMeasurementDepth
	 *            defines the number of measurements for each border.
	 * @param lowValue
	 *            the lowValue for the recursive call.
	 * @param highValue
	 *            the highValue for the recursive call.
	 * @throws EnvironmentNotConfiguredException
	 *             from the measurement
	 * @throws FrameworkException
	 */
	protected void measureSingleBorder(RelativePosition position, String parameterIdVaried, int borderMeasurementDepth, double lowValue, double highValue) {
		double middle = (highValue + lowValue) / 2;

		position.put(parameterIdVaried, middle);

		MeasurementCacheResult result = measure(position);

		this.cachedEnvironmentAccess.addToModel(position, result.value);

		if (borderMeasurementDepth - 1 == 0) {
			return;
		}

		if (this.stopController.shouldStop()) { 
			return;
		}
		
		measureSingleBorder(position, parameterIdVaried, borderMeasurementDepth - 1, middle, highValue);

		if (this.stopController.shouldStop()) {
			return;
		}

		measureSingleBorder(position, parameterIdVaried, borderMeasurementDepth - 1, lowValue, middle);
	}

	/**
	 * Executes a measurement at the given position.
	 * 
	 * @param position
	 *            to be measured
	 */
	protected MeasurementCacheResult measure(final RelativePosition position) {
		RelativePosition measurementPosition = (RelativePosition) position.clone();

		MeasurementCacheResult measurement = cachedEnvironmentAccess.measure(measurementPosition);

		return measurement;
	}

	public long getNumberOfMeasurements() {
		return cachedEnvironmentAccess.getNumberOfCachedMeasurements();
	}

	public double getSizeOfParameterSpace() {
		return cachedEnvironmentAccess.getSizeOfParameterSpace();
	}

	public void validate() {
		if ((hasNewModelPoints && isTimeForValidation()) || !hasMoreIterations()) {
			if (BreakdownConfiguration.getAccuracyDeterminationMethod(strategyConfig).equals(AccuracyDetermination.RandomValidationSet)) {
				if (validationSetManager == null) {
					// create a validation set manager instance
					validationSetManager = new ValidationSetManager(cachedEnvironmentAccess, allParameters);
					validationSetManager.createRandomValidationSet(BreakdownConfiguration.getSizeOfValidationSet(strategyConfig));
				} 
				// determine and check current relative prediction error on validation set
				double avgRelativePredictionError = validationSetManager.getAvgRelativePredictionError();
				if (avgRelativePredictionError < BreakdownConfiguration.getDesiredModelAccuracy(strategyConfig)) {
					accurateEnoughModel = true;
				}
				
			}
		}
	}

	public boolean hasMoreIterations() {
		return cachedEnvironmentAccess.getNumberOfPointsInModel() < cachedEnvironmentAccess.getSizeOfParameterSpace();
	}

	/**
	 * returns the center of the two positions by creation the average of every
	 * element.
	 * 
	 * @param position1
	 *            a position
	 * @param position2
	 *            a position
	 * @return the center of the two positions
	 */
	protected RelativePosition getMiddle(RelativePosition position1, RelativePosition position2) {
		RelativePosition p = new RelativePosition();

		for (Entry<String, Double> e : position1.entrySet()) {
			p.put(e.getKey(), (e.getValue() + position2.get(e.getKey())) / 2);
		}

		return p;
	}

	protected RelativePosition getSectorCenterPoint(Sector s) {
		return getMiddle(s.position2, s.position1);
	}

	/**
	 * measures all edges of the enclosed room (defined by the two positions).
	 * The edges are calculated by
	 * {@link #getAllEdges(RelativePosition, RelativePosition)}. The measured
	 * data is stored in the repository and is added to the model. The
	 * measurements are stored without replacements in the model.
	 * 
	 * @param position1
	 *            a position where no element is the same as in position2.
	 * @param position2
	 *            a position where no element is the same as in position1.
	 * @param replacements
	 *            replaces parameters of the edges with a defined value.
	 */
	protected List<RelativePosition> measureSectorEdges(Sector s) {

		List<RelativePosition> allEdges = getAllEdges(s.position2, s.position1);

		for (RelativePosition positionList : allEdges) {
			MeasurementCacheResult measurement = measure(positionList, s.parametersToIgnore);
			cachedEnvironmentAccess.addToModel(positionList, measurement.value);
			if (reachedStopCondition()) {
				return allEdges;	
			}
		}

		return allEdges;
	}

	protected MeasurementCacheResult measure(final RelativePosition position, Map<String, Double> replacements) {

		// we are not working on the original because it is declared as
		// final and used on other places
		RelativePosition measurementPosition = (RelativePosition) position.clone();
		if (replacements != null) {
			measurementPosition.putAll(replacements);
		}

		MeasurementCacheResult measurement = cachedEnvironmentAccess.measure(measurementPosition);

		if (replacements != null && replacements.size() > 0) {
			measurementPosition = (RelativePosition) position.clone();

			// we store it on the original position
			cachedEnvironmentAccess.addToRepository(measurement, position);

			// and on every allowed possible projection
			List<RelativePosition> allProjections = new LinkedList<RelativePosition>();
			calculateAllPossibleProjections(position, replacements, allProjections);
			for (RelativePosition projectedPosition : allProjections) {
				cachedEnvironmentAccess.addToRepository(measurement, projectedPosition);
			}
		}

		return measurement;

	}

	/**
	 * calculates all possible projections of this point.
	 * 
	 * @param position
	 *            the original point
	 * @param replacements
	 *            the possible projections
	 * @param allProjections
	 *            all possible projections of position stored there.
	 */
	private void calculateAllPossibleProjections(RelativePosition position, Map<String, Double> replacements, List<RelativePosition> allProjections) {
		allProjections.add(position);

		if (replacements.size() == 0) {
			return;
		}
		Map<String, Double> modifiedReplacements = new HashMap<String, Double>(replacements);

		for (Entry<String, Double> singleReplacement : replacements.entrySet()) {
			RelativePosition projectedPosition = (RelativePosition) position.clone();
			projectedPosition.put(singleReplacement.getKey(), singleReplacement.getValue());

			modifiedReplacements.remove(singleReplacement.getKey());

			this.calculateAllPossibleProjections(projectedPosition, modifiedReplacements, allProjections);

		}
	}

	/**
	 * checks if it is time for a validation. The decision is based on the
	 * number of elements in the model. We do the following assumptions: <br>
	 * 1. Only a few data points in the model means that one new data point
	 * influences the prediction of the wide area. Therefore a new data point at
	 * the beginning of the measurement series can cause a high prediction
	 * error. <br>
	 * 2. A lot data points in the model means that one new data point
	 * influences the predictio only at it nearest region. Therefore a new data
	 * point at the end of the measurement series has a low risk to cause a high
	 * prediction error. <br>
	 * The conclusion is to decrease the intervals for validation with the
	 * number of elements in the model. The number of validations is calculated
	 * as a logarithm of the number of Elements in the model with a base given
	 * by the attribute {@link #baseForValidationExecutionCheck}.
	 * 
	 * @return true if a validation should be executed.
	 */
	protected boolean isTimeForValidation() {
		// if it is null, every time should be validated
		if (this.baseForValidationExecutionCheck == null || this.baseForValidationExecutionCheck == 0) {
			return true;
		}
		
		// log_b(x) = log_a(x) / log_a(b)
		// where b is any base d and a base d.
		Double timestamp = Math.log10(this.cachedEnvironmentAccess.getNumberOfPointsInModel()) / Math.log10(this.baseForValidationExecutionCheck);

		if (timestamp.longValue() > this.lastValidationExecutionTimestamp) {
			this.lastValidationExecutionTimestamp = timestamp.longValue();
			return true;
		}

		return false;
	}

}

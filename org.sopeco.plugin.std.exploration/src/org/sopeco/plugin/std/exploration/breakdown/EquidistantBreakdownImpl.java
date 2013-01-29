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
import java.util.List;

import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess;
import org.sopeco.plugin.std.exploration.breakdown.environment.EnvironmentCachedAccess.MeasurementCacheResult;
import org.sopeco.plugin.std.exploration.breakdown.space.RelativePosition;
import org.sopeco.plugin.std.exploration.breakdown.space.Sector;
import org.sopeco.plugin.std.exploration.breakdown.stop.AbstractStopController;

/**
 * This class implements the equidistant breakdown strategy. This strategy
 * explores the parameter space by splitting it in equidistant sectors.
 * 
 * @author Dennis Westermann
 * 
 */
public class EquidistantBreakdownImpl extends AbstractBreakdownExploration implements IBreakdownExploration {

	/**
	 * This list contains all sectors produced in the iterations of the
	 * algorithm.
	 */
	private List<Sector> sectors = new ArrayList<Sector>();

	public EquidistantBreakdownImpl(ExplorationStrategy config, EnvironmentCachedAccess cachedEnvironmentAccess, List<ParameterDefinition> allParameters,
			AbstractStopController stopController, ISoPeCoExtension<?> provider) {
		super(allParameters, cachedEnvironmentAccess, config, stopController, provider);
	}

	@Override
	public void doIteration() {
		long numPointsInModelBeforeIteration = cachedEnvironmentAccess.getNumberOfPointsInModel();

		List<Sector> oldSet = new ArrayList<Sector>();
		oldSet.addAll(sectors);
		sectors.clear();

		for (Sector s : oldSet) {
			splitSector(s);
		}

		// check if new points have been added to the model
		hasNewModelPoints = numPointsInModelBeforeIteration < cachedEnvironmentAccess.getNumberOfPointsInModel();

	}

	@Override
	public void finishWork() {
		cachedEnvironmentAccess.cleanUp();

	}

	protected void splitSector(Sector s) {
		RelativePosition centerPoint = getSectorCenterPoint(s);
		measureAndAddToModel(centerPoint);

		Sector newSector;
		for (RelativePosition p : getAllEdges(s.position1, s.position2)) {
			newSector = new Sector();
			newSector.position2 = centerPoint;
			newSector.position1 = p;
			this.sectors.add(newSector);
			measureSectorEdges(newSector);
		}

	}

	private void measureAndAddToModel(RelativePosition point) {
		MeasurementCacheResult measured = measure(point);
		cachedEnvironmentAccess.addToModel(point, measured.value);
	}

	/**
	 * Adds the first tuple to be measured to the queue. It defines the whole
	 * space of the parameters to be varied to observe the parameter under
	 * investigation.
	 */
	@Override
	public void initialise() {
		Sector s = createFullSpaceSector(allParameters);
		measureSectorEdges(s);
		sectors.add(s);

	}

}

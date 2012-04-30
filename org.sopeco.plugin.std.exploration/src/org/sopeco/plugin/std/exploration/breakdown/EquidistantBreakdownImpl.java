package org.sopeco.plugin.std.exploration.breakdown;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.measurements.ExplorationStrategy;
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
		// TODO Auto-generated method stub

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

package org.sopeco.plugin.std.exploration.breakdown.stop;

/**
 * The Contoller evaluates to stop if at least one Condition evaluated to stop.
 *
 */
public class OneStopController extends AbstractStopController
{

	@Override
	public boolean evaluatedToStop() 
	{
		return this.stopEvaluatedConditions() != 0;
	}

	
}

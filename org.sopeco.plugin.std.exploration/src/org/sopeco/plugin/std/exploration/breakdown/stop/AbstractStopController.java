package org.sopeco.plugin.std.exploration.breakdown.stop;

import java.util.LinkedList;
import java.util.List;

/**
 * A stop Controller can decide if a measurement series should be stopped. This
 * abstract class supports some basic information to the class implementing it and 
 * initializes the single conditions at the first call of the {@link #shouldStop()} method.
 * It also provides the option to manual stop the series by calling {@link AbstractStopController#manualStop()}.  
 * 
 *  @author Rouven Krebs
 */
public abstract class AbstractStopController {

	/**
	 * contains all the conditions to be checked if it is true.
	 */
	private List<IStopCondition> conditions = new LinkedList<IStopCondition>();
	
	/**
	 * the flag if a manual stop is executed. This means that the result of {@link #shouldStop()} is
	 * <code>true</code> without regards to the conditions.
	 */
	private boolean manuelStop = false;
	
	/**
	 * indicates if the {@link #shouldStop()} method was called the first time.
	 */
	private boolean firstRun = true;

	
	/**
	 * adds a condition to be evaluated.
	 * @param condition a condition.
	 */
	public final void addCondition(IStopCondition condition) {
		this.conditions.add(condition);
	}
	
	/**
	 * checks every condition for its result and returns the number of Conditions 
	 * evaluated to stop the series.  
	 * @return number of true evaluated conditions.
	 */
	protected final int stopEvaluatedConditions() {
		int stopEvaluatedConditions = 0;
		for (IStopCondition cond : this.conditions) {
			stopEvaluatedConditions += cond.shouldStop() ? 1 : 0;
		}
		return stopEvaluatedConditions;
	}
	
	/**
	 * evaluates if the series should be stopped. It initializes the conditions if called
	 * for the first time.
	 * @return true if series should be stopped. False in the other case.
	 */
	public final boolean shouldStop() {
		if (this.firstRun) {
			this.firstRun = false;
			this.initAllConditions();
		}
		return this.evaluatedToStop() || this.manuelStop;
	}
	
	/**
	 * initializes all the conditions.
	 */
	private void initAllConditions() {
		for (IStopCondition cond : this.conditions) {
			cond.init();
		}	
	}

	/**
	 * called if the series should be stopped without regard to the conditions.
	 */
	public final void manualStop() {
		this.manuelStop = true;
	}
	
	/**
	 * Is called to decide if the series should be stopped.
	 * @return true if series should be stopped.
	 */
	protected abstract boolean evaluatedToStop();
	
}

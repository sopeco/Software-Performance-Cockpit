package org.sopeco.persistence.entities.definition;


/**
 * @author Dennis Westermann
 *
 */
public class TimeOut extends ExperimentTerminationCondition {
	
	private static final long serialVersionUID = 1L;

	protected long maxDuration = 0L;

	public TimeOut() {
		super();
	}

	public long getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(long newMaxDuration) {
		maxDuration = newMaxDuration;
	}

} 
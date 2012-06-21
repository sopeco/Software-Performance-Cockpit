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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (maxDuration ^ (maxDuration >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeOut other = (TimeOut) obj;
		if (maxDuration != other.maxDuration)
			return false;
		return true;
	}

} 
package org.sopeco.persistence.entities.definition;


/**
 * @author Dennis Westermann
 *
 */
public class NumberOfRepetitions extends ExperimentTerminationCondition {

	private static final long serialVersionUID = 1L;

	protected long numberOfRepetitions = 0;
	
	public NumberOfRepetitions() {
		super();
	}

	public long getNumberOfRepetitions() {
		return numberOfRepetitions;
	}

	
	public void setNumberOfRepetitions(long newNumberOfRepetitions) {
		numberOfRepetitions = newNumberOfRepetitions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (numberOfRepetitions ^ (numberOfRepetitions >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
	
		NumberOfRepetitions other = (NumberOfRepetitions) obj;
		if (numberOfRepetitions != other.numberOfRepetitions)
			return false;
		return true;
	}

} 
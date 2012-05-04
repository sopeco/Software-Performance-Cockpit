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

} 
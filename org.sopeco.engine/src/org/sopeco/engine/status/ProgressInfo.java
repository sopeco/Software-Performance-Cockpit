package org.sopeco.engine.status;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ProgressInfo implements IStatusInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String experimentSeriesName;
	private int numberOfRepetition;
	private int repetition;

	/**
	 * Constructor.
	 */
	public ProgressInfo() {
		this(null, -1, -1);
	}

	/**
	 * Constructor.
	 */
	public ProgressInfo(String pExperimentSeriesName) {
		this(pExperimentSeriesName, -1, -1);
	}

	/**
	 * Constructor.
	 */
	public ProgressInfo(String pExperimentSeriesName, int pRepetition) {
		this(pExperimentSeriesName, pRepetition, -1);
	}

	/**
	 * Constructor.
	 */
	public ProgressInfo(String pExperimentSeriesName, int pRepetition, int pNumberOfRepetition) {
		experimentSeriesName = pExperimentSeriesName;
		repetition = pRepetition;
		numberOfRepetition = pNumberOfRepetition;
	}

	/**
	 * @return the experimentSeriesName
	 */
	public String getExperimentSeriesName() {
		return experimentSeriesName;
	}

	/**
	 * @return the numberOfRepetition
	 */
	public int getNumberOfRepetition() {
		return numberOfRepetition;
	}

	/**
	 * @return the repetition
	 */
	public int getRepetition() {
		return repetition;
	}

	/**
	 * @param pExperimentSeriesName
	 *            the experimentSeriesName to set
	 */
	public void setExperimentSeriesName(String pExperimentSeriesName) {
		experimentSeriesName = pExperimentSeriesName;
	}

	/**
	 * @param pNumberOfRepetition
	 *            the numberOfRepetition to set
	 */
	public void setNumberOfRepetition(int pNumberOfRepetition) {
		numberOfRepetition = pNumberOfRepetition;
	}

	/**
	 * @param pRepetition
	 *            the repetition to set
	 */
	public void setRepetition(int pRepetition) {
		repetition = pRepetition;
	}

}

package org.sopeco.engine.status;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ProgressInfo implements IStatusInfo {
	private String experimentSeriesName;

	public String getExperimentSeriesName() {
		return experimentSeriesName;
	}

	public void setExperimentSeriesName(String pExperimentSeriesName) {
		experimentSeriesName = pExperimentSeriesName;
	}

}

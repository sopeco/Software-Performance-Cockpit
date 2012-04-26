package org.sopeco.plugin.std.analysis.common;

import org.sopeco.persistence.dataset.SimpleDataSet;
import org.sopeco.persistence.dataset.SimpleDataSetColumn;
import org.sopeco.plugin.std.analysis.util.CSVStringGenerator;
import org.sopeco.plugin.std.analysis.util.RAdapter;

/**
 * Representation of a DataSet in R. This class provides the functionality to
 * load and use a DataSet in R.
 * 
 * @author Jens Happe, Dennis Westermann
 * 
 */
public class RDataSet extends RIdentifiableObject {

	/**
	 * DataSet under consideration.
	 */
	private SimpleDataSet dataset;

	/**
	 * 
	 */
	private boolean dataLoaded;

	/**
	 * Create a new RDataSet.
	 * 
	 * @param dataset
	 *            DataSet that is to be passed to R.
	 */
	public RDataSet(SimpleDataSet dataset) {
		this.dataset = dataset;
		dataLoaded = false;
	}

	/**
	 * Transfers this DataSet to R and makes it available under a newly
	 * generated ID.
	 */
	public void loadDataSetInR() {

		String parameterIdString = "";
		String parameterIdStringInQuotes = "";

		int i = 0;
		for (SimpleDataSetColumn<?> column : dataset.getColumns()) {
			String columnId = column.getParameter().getFullName("_");
			RAdapter.getWrapper().executeRCommandString(
					columnId
							+ " <- c("
							+ new CSVStringGenerator()
									.generateValueString(column
											.getParameterValues()) + ")");
			if (i == 0) {
				parameterIdString = columnId;
				parameterIdStringInQuotes = "\"" + columnId + "\"";
			} else {
				parameterIdString += ", " + columnId;
				parameterIdStringInQuotes += ", \"" + columnId + "\"";
			}
			i++;
		}

		RAdapter.getWrapper().executeRCommandString(
				getId() + " <- data.frame(" + parameterIdString + ");");
		RAdapter.getWrapper().executeRCommandString(
				"colnames(" + getId() + ") <- c(" + parameterIdStringInQuotes
						+ ")");
		dataLoaded = true;

	}

	/**
	 * Removes this DataSet from R to free the used memory.
	 */
	public void removeDataSetInR() {
		if (dataLoaded) {
			// Delete all columns
			for (SimpleDataSetColumn<?> column : dataset.getColumns()) {
				String columnId = column.getParameter().getFullName("_");
				RAdapter.getWrapper().executeRCommandString(
						"rm(" + columnId + ");");
			}
			// Delete data table
			RAdapter.getWrapper().executeRCommandString("rm(" + getId() + ");");
			dataLoaded = false;
		}
	}
}

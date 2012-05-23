package org.sopeco.plugin.std.analysis.common;

import org.sopeco.persistence.dataset.SimpleDataSet;
import org.sopeco.persistence.dataset.SimpleDataSetColumn;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
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
			RAdapter.getWrapper().executeRCommandString(columnId + " <- c(" + new CSVStringGenerator().generateValueString(column.getParameterValues()) + ")");
			if (i == 0) {
				parameterIdString = columnId;
				parameterIdStringInQuotes = "\"" + columnId + "\"";
			} else {
				parameterIdString += ", " + columnId;
				parameterIdStringInQuotes += ", \"" + columnId + "\"";
			}
			i++;
		}

		RAdapter.getWrapper().executeRCommandString(getId() + " <- data.frame(" + parameterIdString + ");");
		RAdapter.getWrapper().executeRCommandString("colnames(" + getId() + ") <- c(" + parameterIdStringInQuotes + ")");
		dataLoaded = true;

	}

	/**
	 * Jitters the values of the given parameter in the RDataSet. Example:
	 * vector [10, 10, 10] is transfered to [10.0001, 9.9998, 1.0003].
	 * 
	 * Note: Precondition is that the data set has already been loaded in R and
	 * that it contains the given parameter.
	 * 
	 * @param parameter for which the values should be jittered
	 */
	public void jitter(ParameterDefinition parameter) {

		
		StringBuilder cmdBuilder = new StringBuilder();
		cmdBuilder.append(getId());
		cmdBuilder.append("$");
		cmdBuilder.append(parameter.getFullName("_")); 
		cmdBuilder.append(" <- jitter(");
		cmdBuilder.append(getId());
		cmdBuilder.append("$");
		cmdBuilder.append(parameter.getFullName("_")); 
		cmdBuilder.append(", 0.0000001)");
		RAdapter.getWrapper().executeRCommandString(cmdBuilder.toString());
	}

	/**
	 * Removes this DataSet from R to free the used memory.
	 */
	public void removeDataSetInR() {
		if (dataLoaded) {
			// Delete all columns
			for (SimpleDataSetColumn<?> column : dataset.getColumns()) {
				String columnId = column.getParameter().getFullName("_");
				RAdapter.getWrapper().executeRCommandString("rm(" + columnId + ");");
			}
			// Delete data table
			RAdapter.getWrapper().executeRCommandString("rm(" + getId() + ");");
			dataLoaded = false;
		}
	}
}

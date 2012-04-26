package org.sopeco.plugin.std.analysis.util;

import java.util.List;

import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.dataset.util.ParameterUtil;

/**
 * Class creates a comma seperated string representation for a given set of
 * values. The order of the values is thereby defined by the parameterList
 * passed by to its constructors. The class can process any value type defined
 * in the Performance Cockpit's data metamodel.
 * 
 * @author Jens Happe
 * 
 */
public class CSVStringGenerator {

	/**
	 * This list defines the order in which parameter values are concatenated.
	 */
	private List<ParameterDefinition> parameterList;

	/**
	 * New CSVStringGenerator that can process the given set of parameters.
	 * 
	 * @param parameterList
	 *            Defines the order in which parameter values are concatenated.
	 */
	public CSVStringGenerator(List<ParameterDefinition> parameterList) {
		this.parameterList = parameterList;
	}

	/**
	 * New CSVStringGenerator without parameters.
	 * 
	 */
	public CSVStringGenerator() {
		this.parameterList = null;
	}

	/**
	 * Generates a comma separated representation of all values. The order is
	 * defined by the parameterList.
	 * 
	 * @param valueList
	 *            Values to be stringified.
	 * @return String representation of the given values.
	 */
	public String generateValueString(List<ParameterValue<?>> valueList) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (ParameterValue<?> pv : valueList) {
			if (!first) {
				builder.append(", ");
			} else {
				first = false;
			}
			if (ParameterUtil.getTypeEnumeration(pv.getParameter().getType()).equals(ParameterType.STRING)){
				builder.append("'");
				builder.append(pv.getValueAsString());
				builder.append("'");
			} else {
				builder.append(pv.getValueAsString());
			}
		}
		return builder.toString();
	}

	/**
	 * Generates a comma separated representation of all values. The order is
	 * defined by the parameterList.
	 * 
	 * @param valueList
	 *            Values to be stringified.
	 * @return String representation of the given values.
	 */
	public String generateDoubleValueString(List<Double> valueList) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (Double d : valueList) {
			if (!first) {
				builder.append(", ");
			} else {
				first = false;
			}
			builder.append(String.valueOf(d));
		}
		return builder.toString();
	}

	/**
	 * Generates a string representation of the given list of parameters.
	 * 
	 * @param separator
	 *            String used to separate the individual parameters.
	 * @param parameterList
	 *            parameters to be stringified.
	 * 
	 * @return
	 */
	public static String generateParameterString(String separator,
			List<ParameterDefinition> parameterList) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (ParameterDefinition p : parameterList) {
			if (!first) {
				builder.append(separator);
			} else {
				first = false;
			}
			builder.append( p.getFullName("_"));
		}
		return builder.toString();
	}
	

	/**
	 * Generates a string representation of the given list of parameters.
	 * 
	 * @param separator
	 *            String used to separate the individual parameters.
	 * @param parameterList
	 *            parameters to be stringified.
	 * 
	 * @return
	 */
	private String generateParameterString(String separator) {
		return generateParameterString(separator, parameterList);
	}

	/**
	 * Generates a comma separated string representation of the given list of
	 * parameters.
	 * 
	 * @return String representation of the parameters.
	 */
	public String generateParameterString() {
		if (parameterList == null) {
			throw new IllegalStateException(
					"This function can not be used, if the empty-attribute-list constructor was called!");
		}
		return generateParameterString(", ");
	}
}

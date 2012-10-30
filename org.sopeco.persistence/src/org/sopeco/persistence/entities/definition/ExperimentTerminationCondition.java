package org.sopeco.persistence.entities.definition;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a termination condition defined by a measurement environment.
 * This class can be used both as the definition and as the instance. 
 * 
 * @author Dennis Westermann
 *
 */
public class ExperimentTerminationCondition implements Serializable{

	private static final long serialVersionUID = 1L;

	public static final String TC_NUMBER_OF_REPETITIONS_NAME = "Number Of Repetitions";
	public static final String PARAM_REPETITIONS_NAME = "repetitions";
	public static final Integer DEFAULT_NUMBER_OF_REPS = 1;

	/**
	 * The name that identifies the termination condition
	 */
	private String name;

	/**
	 * A description that is shown to the user of the measurement environment.
	 */
	private String description;
	
	/** Holds a mapping of configuration parameters to their optional default values. */
	protected Map<String, String> paramDefaultValues = new HashMap<String, String>();
	
	/** Holds a mapping of configuration parameters to their configured values. */
	protected Map<String, String> paramValues = new HashMap<String, String>();

	/**
	 * Standard constructor required for serialization.
	 */
	public ExperimentTerminationCondition(){
		super();
	}
	
	/**
	 * Creates a termination condition supported by the measurement environment.
	 * 
	 * @param name
	 * @param description
	 */
	public ExperimentTerminationCondition(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Adds a new configurable parameter to this termination condition. 
	 * 
	 * @param param parameter name
	 * @param defaultValue default value
	 */
	public void addParameter(String param, String defaultValue) {
		paramDefaultValues.put(param, defaultValue);
	}

	/**
	 * Returns the configuration parameters with their default values.
	 * 
	 * @return a mapping of names to default values
	 */
	public Map<String, String> getParametersDefaultValues() {
		return paramDefaultValues;
	}

	/**
	 * Returns the set value of a configuration parameter. If the value 
	 * is not set, it returns the default value.
	 * 
	 * @param param parameter name
	 * @return value
	 */
	public String getParamValue(String param) {
		String value = paramValues.get(param);
		if (value == null)
			value = paramDefaultValues.get(param);
		return value;
	}

	/**
	 * Returns the configuration parameters with their configured values.
	 * 
	 * @return a mapping of names to configured values
	 */
	public Map<String, String> getParametersValues() {
		return paramValues;
	}

	/**
	 * For convenience, provides a predefined definition of a Number of Repetitions termination condition.
	 * 
	 * @return a new definition of Number of Repetitions termination condition
	 */
	public static ExperimentTerminationCondition createNumberOfRepetitionsTC() {
		final ExperimentTerminationCondition tc = new ExperimentTerminationCondition(TC_NUMBER_OF_REPETITIONS_NAME, "Sets the number of repetitions for every experiment.");
		tc.addParameter(PARAM_REPETITIONS_NAME, DEFAULT_NUMBER_OF_REPS.toString());
		return tc;
	}

	/**
	 * Finds a given termination condition by its name.
	 * 
	 * @param tcName name of the termination condition
	 * @param terminationConditions a collection of termination conditions
	 * @return the found TC, or null if it is not found.
	 */
	public static ExperimentTerminationCondition findTerminationCondition(String tcName, Collection<ExperimentTerminationCondition> terminationConditions) {
		for (ExperimentTerminationCondition tc: terminationConditions) {
			if (tc.getName().equalsIgnoreCase(tcName)) 
				return tc;
		}
		
		return null;
	}

	/**
	 * If a number of repetitions termination condition is defined in the given collection of termination conditions, 
	 * it returns the set value; otherwise returns the default value of {@value AbstractMEController#DEFAULT_NUMBER_OF_REPS}.
	 * 
	 * @return the set number of repetitions or its default value
	 */
	public static Integer getNumberOfRepetitions(Collection<ExperimentTerminationCondition> terminationConditions) {
		ExperimentTerminationCondition tc = findTerminationCondition(TC_NUMBER_OF_REPETITIONS_NAME, terminationConditions);
		if (tc != null) {
			return Integer.valueOf(tc.getParamValue(PARAM_REPETITIONS_NAME));
		} else
			return DEFAULT_NUMBER_OF_REPS;
	}

}

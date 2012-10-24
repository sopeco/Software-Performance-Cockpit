package org.sopeco.persistence.entities.definition;

import java.io.Serializable;
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

	
	/**
	 * The name that identifies the termination condition
	 */
	private String name;

	/**
	 * A description that is shown to the user of the measurement environment.
	 */
	private String description;
	
	/** Holds a mapping of configuration parameters to their optional default values. */
	protected final Map<String, String> paramDefaultValues = new HashMap<String, String>();
	
	/** Holds a mapping of configuration parameters to their configured values. */
	protected final Map<String, String> paramValues = new HashMap<String, String>();

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

}

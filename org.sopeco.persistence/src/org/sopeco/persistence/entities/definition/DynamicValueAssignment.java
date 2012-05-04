package org.sopeco.persistence.entities.definition;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Dennis Westermann
 *
 */
public class DynamicValueAssignment extends ParameterValueAssignment {
	
	private static final long serialVersionUID = 1L;

	protected Map<String, String> configuration;

	protected String name = null;

	public DynamicValueAssignment() {
		super();
	}

	public Map<String, String> getConfiguration() {
		if (configuration == null) {
			configuration = new HashMap<String,String>();
		}
		return configuration;
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}
	
} 
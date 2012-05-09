package org.sopeco.persistence.entities.definition;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Dennis Westermann
 *
 */
public class DynamicValueAssignment extends ParameterValueAssignment {
	
	private static final long serialVersionUID = 1L;

	protected HashMap<String, String> configuration;

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
	
	/* (non-Javadoc)
	 * @see org.sopeco.persistence.entities.definition.ParameterValueAssignment#clone()
	 */
	@Override
	public DynamicValueAssignment clone() {
		DynamicValueAssignment target = new DynamicValueAssignment();
		target.setParameter(this.getParameter());
		target.getConfiguration().putAll(configuration);
		return target;
	}
	
} 
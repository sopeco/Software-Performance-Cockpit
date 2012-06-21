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
			configuration = new HashMap<String, String>();
		}
		return configuration;
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sopeco.persistence.entities.definition.ParameterValueAssignment#clone
	 * ()
	 */
	@Override
	public DynamicValueAssignment clone() {
		DynamicValueAssignment target = new DynamicValueAssignment();
		target.setParameter(this.getParameter());
		target.getConfiguration().putAll(configuration);
		return target;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
		result = prime * result + ((configuration == null|| configuration.isEmpty()) ? 0 : configuration.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;

		DynamicValueAssignment other = (DynamicValueAssignment) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter))
			return false;
		if (configuration == null || configuration.isEmpty()) {
			if (other.configuration != null && !other.configuration.isEmpty())
				return false;
		} else if (!configuration.equals(other.configuration))
			return false;

		return true;
	}

}
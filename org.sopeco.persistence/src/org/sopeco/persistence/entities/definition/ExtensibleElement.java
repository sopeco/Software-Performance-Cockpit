package org.sopeco.persistence.entities.definition;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dennis Westermann
 * 
 */
public abstract class ExtensibleElement implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Map<String, String> configuration;

	protected String name = null;

	protected ExtensibleElement() {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExtensibleElement other = (ExtensibleElement) obj;
		if (configuration == null) {
			if (other.configuration != null)
				return false;
		} else if (!configuration.equals(other.configuration))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
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
package org.sopeco.model.configuration.ext;

import org.sopeco.model.configuration.impl.ScenarioDefinitionImpl;

/**
 * Extension class for the generated model code that implements additional methods.
 * 
 * @author Dennis Westermann
 *
 */
public class ScenarioDefinitionExt extends ScenarioDefinitionImpl{

	private static final long serialVersionUID = 1L;

	public ScenarioDefinitionExt(){
		super();
	}
	
	@Override
	public boolean equals(Object o) {

		 if (this == o) return true;

		 if (o == null || getClass() != o.getClass()) return false;

		 ScenarioDefinitionExt obj = (ScenarioDefinitionExt) o;

		 if (name != null ? !name.equals(obj.name) : obj.name != null) return false;

		 return true;

	}

	@Override
	public int hashCode() {
		return (name != null ? name.hashCode() : 0);
	}

	@Override
    public String toString() {

       return "ScenarioDefinition{" +
	                 "name='" + name + '\'' + '}';
    }
	
}

package org.sopeco.persistence.entities.definition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Westermann
 * 
 */
public class AnalysisConfiguration extends ExtensibleElement {

	private static final long serialVersionUID = 1L;

	public AnalysisConfiguration() {
		super();
	}

	protected List<ParameterDefinition> dependentParameters;

	protected List<ParameterDefinition> independentParameters;

	public List<ParameterDefinition> getDependentParameters() {
		if (dependentParameters == null) {
			dependentParameters = new ArrayList<ParameterDefinition>();
		}
		return dependentParameters;
	}

	public List<ParameterDefinition> getIndependentParameters() {
		if (independentParameters == null) {
			independentParameters = new ArrayList<ParameterDefinition>();
		}
		return independentParameters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dependentParameters == null) ? 0 : dependentParameters.hashCode());
		result = prime * result + ((independentParameters == null) ? 0 : independentParameters.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		AnalysisConfiguration other = (AnalysisConfiguration) obj;

		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;

		if (dependentParameters == null) {
			if (other.dependentParameters != null)
				return false;
		} else if (!dependentParameters.equals(other.dependentParameters))
			return false;

		if (independentParameters == null) {
			if (other.independentParameters != null)
				return false;
		} else if (!independentParameters.equals(other.independentParameters))
			return false;

		if (configuration == null) {
			if (other.configuration != null)
				return false;
		} else if (!configuration.equals(other.configuration))
			return false;

		return true;
	}

}
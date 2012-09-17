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


	private List<ParameterDefinition> dependentParameters;


	private List<ParameterDefinition> independentParameters;

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
		result = prime * result + ((getDependentParameters() == null || getDependentParameters().isEmpty()) ? 0 : getDependentParameters().hashCode());
		result = prime * result + ((getIndependentParameters() == null || getIndependentParameters().isEmpty()) ? 0 : getIndependentParameters().hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((configuration == null || configuration.isEmpty()) ? 0 : configuration.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AnalysisConfiguration other = (AnalysisConfiguration) obj;

		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}

		if (getDependentParameters() == null || getDependentParameters().isEmpty()) {
			if (other.getDependentParameters() != null && !other.getDependentParameters().isEmpty()) {
				return false;
			}
		} else if (!getDependentParameters().equals(other.getDependentParameters())) {
			return false;
		}

		if (getIndependentParameters() == null || getIndependentParameters().isEmpty()) {
			if (other.getIndependentParameters() != null && !other.getIndependentParameters().isEmpty()) {
				return false;
			}
		} else if (!getIndependentParameters().equals(other.getIndependentParameters())) {
			return false;
		}

		if (configuration == null || configuration.isEmpty()) {
			if (other.configuration != null && !other.configuration.isEmpty()) {
				return false;
			}
		} else if (!configuration.equals(other.configuration)) {
			return false;
		}

		return true;
	}

	protected void setDependentParameters(List<ParameterDefinition> dependentParameters) {
		this.dependentParameters = dependentParameters;
	}

	protected void setIndependentParameters(List<ParameterDefinition> independentParameters) {
		this.independentParameters = independentParameters;
	}

}
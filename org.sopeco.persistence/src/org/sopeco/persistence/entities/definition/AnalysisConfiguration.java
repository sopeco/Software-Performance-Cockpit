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

} 
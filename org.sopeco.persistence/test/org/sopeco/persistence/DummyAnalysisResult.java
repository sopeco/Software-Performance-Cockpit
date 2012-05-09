package org.sopeco.persistence;

import java.util.HashSet;

import org.sopeco.persistence.entities.analysis.IStorableAnalysisResult;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * For test purposes only.
 * 
 * @author Dennis Westermann
 *
 */
public class DummyAnalysisResult implements IStorableAnalysisResult{


	private static final long serialVersionUID = 1L;
	String resultId;
	String predictionFunction;
	ParameterDefinition dependentParameter;
	HashSet<String> indepParameterNames = new HashSet<String>();
	HashSet<ParameterDefinition> indepPaparameters = new HashSet<ParameterDefinition>();
	AnalysisConfiguration analysisConfiguration;

	public DummyAnalysisResult(String predictionFunction, ParameterDefinition dependentParameter, AnalysisConfiguration analysisConfiguration) {
		this.predictionFunction = predictionFunction;
		this.dependentParameter = dependentParameter;
		this.analysisConfiguration = analysisConfiguration;
	}


	public ParameterDefinition getDependentParameter(){
		return this.dependentParameter;
	}
	
	public String getFunctionAsString() {

		return predictionFunction;
	}


	public void setFunctionAsString(String function){
		this.predictionFunction = function;
	}
	
	public AnalysisConfiguration getAnalysisStrategyConfiguration() {
		return analysisConfiguration;
	}

	@Override
	public String getId() {
		return this.resultId;
	}

	@Override
	public String setId(String id) {
		return this.resultId = id;
	}
}

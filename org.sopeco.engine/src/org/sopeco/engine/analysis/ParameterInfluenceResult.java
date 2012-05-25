package org.sopeco.engine.analysis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Class represents the result of a parameter influence analysis.
 * 
 * @author Dennis Westermann
 * 
 */
public class ParameterInfluenceResult implements IParameterInfluenceResult {

	
	private static final long serialVersionUID = 1L;
	
	/** An id that uniquely identifies this result instance **/
	private String id;
	
	/** List to store influence values of the parameters */
	private HashMap<ParameterDefinition, IParameterInfluenceDescriptor> parameterInfluenceDescriptors;
	
	/**
	 * Configuration used to derive this result object.
	 */
	private AnalysisConfiguration configuration;

	/**
	 * Constructor
	 * 
	 * @param configuration
	 *            configuration of the correlation analysis
	 */
	public ParameterInfluenceResult(AnalysisConfiguration configuration) {
		this.configuration = configuration;
		parameterInfluenceDescriptors = new HashMap<ParameterDefinition, IParameterInfluenceDescriptor>();
	}

	/**
	 * Used to store a new ParameterCorrelation-object in the result.
	 * 
	 * @param ParameterCorrelation
	 *            ParameterCorrelation that describes the correlation of a
	 *            parameter with the observation parameter.
	 */
	public void addParameterInfluenceDescriptor(IParameterInfluenceDescriptor influenceDescriptor) {
		parameterInfluenceDescriptors.put(influenceDescriptor.getIndependentParameter(), influenceDescriptor);
	}

	@Override
	public AnalysisConfiguration getAnalysisStrategyConfiguration() {
		return configuration;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public List<IParameterInfluenceDescriptor> getAllParameterInfluenceDescriptors() {
		List<IParameterInfluenceDescriptor> resultList  = new LinkedList<IParameterInfluenceDescriptor>();
		resultList.addAll(parameterInfluenceDescriptors.values());
		return resultList;
	}

	@Override
	public IParameterInfluenceDescriptor getParameterInfluenceDescriptorByParam(ParameterDefinition parameter) {
		return parameterInfluenceDescriptors.get(parameter);
	}


}
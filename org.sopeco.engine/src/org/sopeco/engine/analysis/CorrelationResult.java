package org.sopeco.engine.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Class represents the result of a correlation-based analysis in R.
 * 
 * @author Pascal Meier
 * @author Dennis Westermann
 * 
 */
public class CorrelationResult implements ICorrelationResult {

	
	private static final long serialVersionUID = 1L;
	
	/** An id that uniquely identifies this result instance **/
	private String id;
	
	/** List to store correlation values of the parameters */
	private HashMap<ParameterDefinition, ParameterCorrelation> parameterCorrelations;
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
	public CorrelationResult(AnalysisConfiguration configuration) {
		this.configuration = configuration;
		parameterCorrelations = new HashMap<ParameterDefinition, ParameterCorrelation>();
	}

	/**
	 * Used to store a new ParameterCorrelation-object in the result.
	 * 
	 * @param ParameterCorrelation
	 *            ParameterCorrelation that describes the correlation of a
	 *            parameter with the observation parameter.
	 */
	public void addParameterCorrelation(ParameterCorrelation corr) {
		parameterCorrelations.put(corr.getIndependentParameter(), corr);
	}

	@Override
	public AnalysisConfiguration getAnalysisStrategyConfiguration() {
		return configuration;
	}

	@Override
	public List<ParameterCorrelation> getAllParameterCorrelations() {
		ArrayList<ParameterCorrelation> result = new ArrayList<ParameterCorrelation>();
		for (ParameterCorrelation corr : parameterCorrelations.values()) {
			result.add(corr);
		}
		return result;
	}

	@Override
	public ParameterCorrelation getParameterCorrelationByParam(
			ParameterDefinition parameter) {
		return parameterCorrelations.get(parameter);
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

}
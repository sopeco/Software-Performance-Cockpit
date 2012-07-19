package org.sopeco.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sopeco.model.configuration.ScenarioDefinition;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.environment.ParameterNamespace;
import org.sopeco.model.configuration.environment.ParameterRole;
import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.model.configuration.measurements.MeasurementSpecification;

/**
 * This class contains helper methods for querying a SoPeCo scenario definition ({@see ScenarioDefinition}).
 * 
 * @author Dennis Westermann
 *
 */
public class ScenarioDefinitionUtil {
	
	/**
	 * Looks for the experiment series definition with the given name in the given {@link ScenarioDefinition}.
	 * 
	 * @param name - the name of the requested experiment series definition
	 * @param scenario - a scenario definition that contains an experiment series with the given name
	 * @return the {@link ExperimentSeriesDefinition} instance with the given name; <code>null</code> if no {@link ExperimentSeriesDefinition} could be found
	 */
	public static ExperimentSeriesDefinition getExperimentSeriesDefinition(String name, ScenarioDefinition scenario){
		for (MeasurementSpecification measurementSpecification : scenario.getMeasurementSpecification()) {
			for (ExperimentSeriesDefinition esd : measurementSpecification.getExperimentSeriesDefinitions()) {
				if(esd.getName().equals(name))
					return esd;
			}
		}
		return null;
	}

	
	/**
	 * Looks for the parameter definition with the given full name (namespace + name) in the given {@link ScenarioDefinition}.
	 * 
	 * @param fullName - the full name (namespace + name) of the requested {@link ParameterDefinition}
	 * @param scenario - a scenario definition that contains a parameter definition with the given name
	 * @return the {@link ParameterDefinition} instance with the given full name; <code>null</code> if no {@link ParameterDefinition} could be found
	 */
	public static ParameterDefinition getParameterDefinition(String fullName, ScenarioDefinition scenario){
		
		List<ParameterDefinition> listOfAllParameters = new ArrayList<ParameterDefinition>();
		collectAllParameters(scenario.getMeasurementEnvironmentDefinition().getRoot(), listOfAllParameters);
		
		for (ParameterDefinition parameterDefinition : listOfAllParameters) {
			if(parameterDefinition.getFullName().equals(fullName)){
				return parameterDefinition;
			}
		}
		
		return null;
	}
	
	
	/**
	 * Collects all observation parameters of the given namespace (including all child namespaces). 
	 * 
	 * @param namespace
	 * @return the set of parameter definitions
	 */
	public static Set<ParameterDefinition> collectObservationParameters(ParameterNamespace namespace) {
		Set<ParameterDefinition> result = new HashSet<ParameterDefinition>();
		collectObservationParameters(namespace, result);
		return result;
	}
	
	/**
	 * Collects all observation parameters of the given namespace (including all child namespaces). 
	 * 
	 * @param namespace
	 * @param observationParameterList - the collection in which the observation parameters should be stored (must not be null)
	 */
	public static void collectObservationParameters(ParameterNamespace namespace, Collection<ParameterDefinition> observationParameterList) {
		
		for(ParameterDefinition parameter : namespace.getParameters()){
			if(parameter.getRole().equals(ParameterRole.OBSERVATION)){
				observationParameterList.add(parameter);
			}
		}
		
		for(ParameterNamespace child : namespace.getChildren()){
			collectObservationParameters(child, observationParameterList);
		}
		
	}
	
	/**
	 * Collects all parameters of the given namespace (including all child namespaces). 
	 * 
	 * @param namespace
	 * @param parameterList - the list in which the parameters should be stored (must not be null)
	 */
	private static void collectAllParameters(ParameterNamespace namespace, Collection<ParameterDefinition> parameterList) {
		
		for(ParameterDefinition parameter : namespace.getParameters()){
			parameterList.add(parameter);
		}
		
		for(ParameterNamespace child : namespace.getChildren()){
			collectAllParameters(child, parameterList);
		}
		
	}
}

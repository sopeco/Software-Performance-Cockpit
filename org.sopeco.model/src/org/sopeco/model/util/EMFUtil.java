package org.sopeco.model.util;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.sopeco.model.configuration.ConfigurationPackage;
import org.sopeco.model.configuration.ScenarioDefinition;
import org.sopeco.model.configuration.analysis.AnalysisConfiguration;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.environment.ParameterNamespace;
import org.sopeco.model.configuration.environment.ParameterRole;
import org.sopeco.model.configuration.measurements.ConstantValueAssignment;
import org.sopeco.model.configuration.measurements.DynamicValueAssignment;
import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.model.configuration.measurements.ExplorationStrategy;
import org.sopeco.model.configuration.measurements.MeasurementSpecification;
import org.sopeco.model.configuration.measurements.NumberOfRepetitions;
import org.sopeco.model.configuration.measurements.ParameterValueAssignment;
import org.sopeco.model.configuration.measurements.TimeOut;
import org.sopeco.persistence.EntityFactory;

/**
 * This class provides several helper methods to work with EMF models.
 * 
 * @author Dennis Westermann
 * 
 */
public class EMFUtil {

	private static ResourceSet createSoPeCoModelResourceSet() {
		ResourceSet resourceSet = new ResourceSetImpl();

		// Register the appropriate resource factory to handle all file
		// extensions.
		//
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		// Register the package to ensure it is available during loading.
		//
		resourceSet.getPackageRegistry().put(ConfigurationPackage.eNS_URI, ConfigurationPackage.eINSTANCE);

		return resourceSet;
	}

	/**
	 * Loads a EMF scenario definition from the given URI.
	 * 
	 * @param uri
	 *            URI of the scenario configuration
	 * @return an instance of Scenario Definition
	 * @throws IOException
	 */
	private static ScenarioDefinition loadEMFObjectsFromURI(URI uri) throws IOException {

		ScenarioDefinition sd = (ScenarioDefinition) ModelUtils.load(uri, createSoPeCoModelResourceSet());
		return sd;
	}

	
	/**
	 * Loads a SoPeCo scenario definition from the given URI (which belongs to an EMF file).
	 * 
	 * @param uri
	 *            URI of the EMF scenario configuration
	 * @return an instance of a SoPeCo Scenario Definition
	 * @throws IOException
	 */
	public static org.sopeco.persistence.entities.definition.ScenarioDefinition loadFromURI(URI uri) throws IOException {

		ScenarioDefinition emfScenarioDefinition = loadEMFObjectsFromURI(uri);
		return convertToPojo(emfScenarioDefinition);
		
	}

	/**
	 * Loads a SoPeCo scenario definition from the given file path (which belongs to an EMF file).
	 * 
	 * @param filePath
	 *            path to the scenario configuration file
	 * @return an instance of the EObject
	 * @throws IOException
	 */
	public static org.sopeco.persistence.entities.definition.ScenarioDefinition loadFromFilePath(String filePath) throws IOException {

		File file = new File(filePath);
		org.eclipse.emf.common.util.URI uri = null;
		if (file.isFile()) {
			uri = URI.createFileURI(file.getAbsolutePath());
		} else {
			uri = URI.createURI(filePath);
		}
		return loadFromURI(uri);
	}

	/*
	 * Conversion from generated objects to SoPeCo entities (could go to a
	 * separate class)
	 */
	
	private static org.sopeco.persistence.entities.definition.ScenarioDefinition convertToPojo(ScenarioDefinition emfScenarioDefinition) {

		org.sopeco.persistence.entities.definition.ScenarioDefinition pojoScenarioDefinition = EntityFactory.createScenarioDefinition(emfScenarioDefinition
				.getName());

		convertMeasurementEnvironmentDefinition(emfScenarioDefinition, pojoScenarioDefinition);

		convertMeasurementSpecification(emfScenarioDefinition, pojoScenarioDefinition);

		return pojoScenarioDefinition;

	}

	protected static void convertMeasurementEnvironmentDefinition(ScenarioDefinition emfScenarioDefinition,
			org.sopeco.persistence.entities.definition.ScenarioDefinition pojoScenarioDefinition) {
		org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition pojoMeasurementEnvDef = EntityFactory
				.createMeasurementEnvironmentDefinition(pojoScenarioDefinition);

		ParameterNamespace emfRootNamespace = emfScenarioDefinition.getMeasurementEnvironmentDefinition().getRoot();
		org.sopeco.persistence.entities.definition.ParameterNamespace pojoRootNamespace = EntityFactory.createRootNamespace(emfRootNamespace.getName(),
				pojoMeasurementEnvDef);
		convertParameters(emfRootNamespace, pojoRootNamespace);
		convertChildren(emfRootNamespace, pojoRootNamespace);
	}

	protected static void convertMeasurementSpecification(ScenarioDefinition emfScenarioDefinition,
			org.sopeco.persistence.entities.definition.ScenarioDefinition pojoScenarioDefinition) {
		MeasurementSpecification emfMeasurementSpecification = emfScenarioDefinition.getMeasurementSpecification();
		org.sopeco.persistence.entities.definition.MeasurementSpecification pojoMeasurementSpecification = EntityFactory
				.createMeasurementSpecification(pojoScenarioDefinition);

		convertInitializationAssignments(pojoScenarioDefinition, emfMeasurementSpecification, pojoMeasurementSpecification);

		convertExperimentSeriesDefinitions(pojoScenarioDefinition, emfMeasurementSpecification, pojoMeasurementSpecification);
	}

	protected static void convertExperimentSeriesDefinitions(org.sopeco.persistence.entities.definition.ScenarioDefinition pojoScenarioDefinition,
			MeasurementSpecification emfMeasurementSpecification,
			org.sopeco.persistence.entities.definition.MeasurementSpecification pojoMeasurementSpecification) {
		for (Object emfExpSeriesDefObj : emfMeasurementSpecification.getExperimentSeriesDefinitions()) {
			ExperimentSeriesDefinition emfExpSeriesDef = (ExperimentSeriesDefinition) emfExpSeriesDefObj;

			org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition pojoExpSeriesDef = EntityFactory.createExperimentSeriesDefinition(
					emfExpSeriesDef.getName(), convertExperimentTerminationCondition(emfExpSeriesDef.getExperimentTerminationCondition()),
					pojoMeasurementSpecification);

			convertExplorationStrategy(emfExpSeriesDef, pojoExpSeriesDef);

			convertPreparationAssignments(pojoScenarioDefinition, emfExpSeriesDef, pojoExpSeriesDef);

			convertExperimentAssignments(pojoScenarioDefinition, emfExpSeriesDef, pojoExpSeriesDef);

		}
	}

	protected static void convertExperimentAssignments(org.sopeco.persistence.entities.definition.ScenarioDefinition pojoScenarioDefinition,
			ExperimentSeriesDefinition emfExpSeriesDef, org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition pojoExpSeriesDef) {
		for (Object emfPVAobj : emfExpSeriesDef.getExperimentAssignments()) {
			ParameterValueAssignment emfPVA = (ParameterValueAssignment) emfPVAobj;

			org.sopeco.persistence.entities.definition.ParameterValueAssignment pojoPVA;
			if (emfPVA instanceof ConstantValueAssignment) {
				pojoPVA = EntityFactory.createConstantValueAssignment(pojoScenarioDefinition.getParameterDefinition(emfPVA.getParameter().getFullName()),
						((ConstantValueAssignment) emfPVA).getValue());
			} else if (emfPVA instanceof DynamicValueAssignment) {
				pojoPVA = EntityFactory.createDynamicValueAssignment(((DynamicValueAssignment) emfPVA).getName(), pojoScenarioDefinition
						.getParameterDefinition(emfPVA.getParameter().getFullName()), ((DynamicValueAssignment) emfPVA).getConfiguration().map());
			} else {
				throw new IllegalArgumentException("Unkown experiment assignment type: " + emfPVA.getClass().getName());
			}

			pojoExpSeriesDef.getExperimentAssignments().add(pojoPVA);
		}
	}

	protected static void convertPreparationAssignments(org.sopeco.persistence.entities.definition.ScenarioDefinition pojoScenarioDefinition,
			ExperimentSeriesDefinition emfExpSeriesDef, org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition pojoExpSeriesDef) {
		for (Object emfCVAobj : emfExpSeriesDef.getPreperationAssignments()) {
			ConstantValueAssignment emfCVA = (ConstantValueAssignment) emfCVAobj;

			org.sopeco.persistence.entities.definition.ConstantValueAssignment pojoCVA = EntityFactory.createConstantValueAssignment(
					pojoScenarioDefinition.getParameterDefinition(emfCVA.getParameter().getFullName()), emfCVA.getValue());
			pojoExpSeriesDef.getPreperationAssignments().add(pojoCVA);
		}
	}

	protected static void convertExplorationStrategy(ExperimentSeriesDefinition emfExpSeriesDef,
			org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition pojoExpSeriesDef) {
		ExplorationStrategy emfExplorationStrategy = emfExpSeriesDef.getExplorationStrategy();

		org.sopeco.persistence.entities.definition.ExplorationStrategy pojoExplorationStrategy = EntityFactory.createExplorationStrategy(
				emfExplorationStrategy.getName(), emfExplorationStrategy.getConfiguration().map(), pojoExpSeriesDef);

		convertAnalysisStrategies(emfExplorationStrategy, pojoExplorationStrategy);
	}

	protected static void convertAnalysisStrategies(ExplorationStrategy emfExplorationStrategy,
			org.sopeco.persistence.entities.definition.ExplorationStrategy pojoExplorationStrategy) {
		for (Object emfAnalysisConfigObj : emfExplorationStrategy.getAnalysisConfigurations()) {
			AnalysisConfiguration emfAnalysisConfig = (AnalysisConfiguration) emfAnalysisConfigObj;

			EntityFactory.createAnalysisConfiguration(emfAnalysisConfig.getName(), emfAnalysisConfig.getConfiguration().map(), pojoExplorationStrategy);
		}
	}

	private static void convertInitializationAssignments(org.sopeco.persistence.entities.definition.ScenarioDefinition pojoScenarioDefinition,
			MeasurementSpecification emfMeasurementSpecification,
			org.sopeco.persistence.entities.definition.MeasurementSpecification pojoMeasurementSpecification) {
		for (Object emfCVAobj : emfMeasurementSpecification.getInitializationAssignemts()) {
			ConstantValueAssignment emfCVA = (ConstantValueAssignment) emfCVAobj;

			org.sopeco.persistence.entities.definition.ConstantValueAssignment pojoCVA = EntityFactory.createConstantValueAssignment(
					pojoScenarioDefinition.getParameterDefinition(emfCVA.getParameter().getFullName()), emfCVA.getValue());
			pojoMeasurementSpecification.getInitializationAssignemts().add(pojoCVA);
		}
	}

	private static org.sopeco.persistence.entities.definition.ExperimentTerminationCondition convertExperimentTerminationCondition(
			ExperimentTerminationCondition emfTerminationCondition) {
		if (emfTerminationCondition instanceof TimeOut) {
			return EntityFactory.createTimeOutTerminationCondition(((TimeOut) emfTerminationCondition).getMaxDuration());
		} else if (emfTerminationCondition instanceof NumberOfRepetitions) {
			return EntityFactory.createNumberOfRepetitionsTerminationCondition(((NumberOfRepetitions) emfTerminationCondition).getNumberOfRepetitions());
		}
		throw new IllegalArgumentException("Unknown experiment termination condition: " + emfTerminationCondition.getClass().getName());
	}

	private static void convertChildren(ParameterNamespace emfNamespace, org.sopeco.persistence.entities.definition.ParameterNamespace pojoNamespace) {
		for (Object emfChildNamespaceObj : emfNamespace.getChildren()) {
			ParameterNamespace emfChildNamespace = (ParameterNamespace) emfChildNamespaceObj;

			org.sopeco.persistence.entities.definition.ParameterNamespace pojoChildNamespace = EntityFactory.createChildNamespace(emfChildNamespace.getName(),
					pojoNamespace);

			convertParameters(emfChildNamespace, pojoChildNamespace);
			convertChildren(emfChildNamespace, pojoChildNamespace);
		}
	}

	private static void convertParameters(ParameterNamespace emfNamespace, org.sopeco.persistence.entities.definition.ParameterNamespace pojoNamespace) {

		for (Object emfParamDefObj : emfNamespace.getParameters()) {
			ParameterDefinition emfParamDef = (ParameterDefinition) emfParamDefObj;

			EntityFactory.createParameterDefinition(emfParamDef.getName(), emfParamDef.getType(), convertRole(emfParamDef.getRole()), pojoNamespace);
		}
	}

	private static org.sopeco.persistence.entities.definition.ParameterRole convertRole(ParameterRole emfRole) {
		switch (emfRole) {
		case INPUT:
			return org.sopeco.persistence.entities.definition.ParameterRole.INPUT;
		case OBSERVATION:
			return org.sopeco.persistence.entities.definition.ParameterRole.OBSERVATION;
		default:
			throw new IllegalArgumentException("Unkown role: " + emfRole.name());
		}
	}

}

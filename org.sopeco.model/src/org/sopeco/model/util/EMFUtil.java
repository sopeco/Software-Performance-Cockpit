package org.sopeco.model.util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.sopeco.model.configuration.ConfigurationPackage;
import org.sopeco.model.configuration.ScenarioDefinition;
import org.sopeco.model.configuration.SoPeCoModelFactoryHandler;
import org.sopeco.model.configuration.analysis.AnalysisConfiguration;
import org.sopeco.model.configuration.environment.MeasurementEnvironmentDefinition;
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

	private static org.sopeco.persistence.entities.definition.ScenarioDefinition pojoScenarioDefinition;

	private static ResourceSet createSoPeCoModelResourceSet() {
		ResourceSet resourceSet = new ResourceSetImpl();

		// Register the appropriate resource factory to handle all file
		// extensions.
		//
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

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
		SoPeCoModelFactoryHandler.initFactories();

		ScenarioDefinition sd = (ScenarioDefinition) ModelUtils.load(uri, createSoPeCoModelResourceSet());

		return sd;
	}

	/**
	 * Loads a SoPeCo scenario definition from the given URI (which belongs to
	 * an EMF file).
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
	 * Loads a SoPeCo scenario definition from the given file path (which
	 * belongs to an EMF file).
	 * 
	 * @param filePath
	 *            path to the scenario configuration file
	 * @return an instance of the EObject
	 * @throws IOException
	 */
	public static org.sopeco.persistence.entities.definition.ScenarioDefinition loadFromFilePath(String filePath)
			throws IOException {

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

	private static org.sopeco.persistence.entities.definition.ScenarioDefinition convertToPojo(
			ScenarioDefinition emfScenarioDefinition) {

		// TODO: add scenario definitionId to model and adjust editor (it
		// should set the scenario name as the definition id if the id is not
		// set)
		pojoScenarioDefinition = EntityFactory.createScenarioDefinition(emfScenarioDefinition.getName(),
				emfScenarioDefinition.getName());

		org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition pojoMeasurementEnvironmentDefinition = convertMeasurementEnvironmentDefinition(emfScenarioDefinition
				.getMeasurementEnvironmentDefinition());
		pojoScenarioDefinition.setMeasurementEnvironmentDefinition(pojoMeasurementEnvironmentDefinition);

		org.sopeco.persistence.entities.definition.MeasurementSpecification pojoMeasurementSpecification = convertMeasurementSpecification(emfScenarioDefinition);
		pojoScenarioDefinition.setMeasurementSpecification(pojoMeasurementSpecification);
		return pojoScenarioDefinition;

	}

	private static org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition convertMeasurementEnvironmentDefinition(
			MeasurementEnvironmentDefinition emfMeasurementEnvironmentDefinition) {
		org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition pojoMeasurementEnvDef = EntityFactory
				.createMeasurementEnvironmentDefinition();

		ParameterNamespace emfRootNamespace = emfMeasurementEnvironmentDefinition.getRoot();
		org.sopeco.persistence.entities.definition.ParameterNamespace pojoRootNamespace = EntityFactory
				.createNamespace(emfRootNamespace.getName());
		pojoMeasurementEnvDef.setRoot(pojoRootNamespace);

		List<org.sopeco.persistence.entities.definition.ParameterDefinition> pojoParameters = convertParameters(emfRootNamespace
				.getParameters());
		for (org.sopeco.persistence.entities.definition.ParameterDefinition pojoParameter : pojoParameters) {
			pojoParameter.setNamespace(pojoRootNamespace);
			pojoRootNamespace.getParameters().add(pojoParameter);
		}

		List<org.sopeco.persistence.entities.definition.ParameterNamespace> grandChildren = convertChildren(emfRootNamespace);
		for (org.sopeco.persistence.entities.definition.ParameterNamespace grandChild : grandChildren) {
			pojoRootNamespace.getChildren().add(grandChild);
			grandChild.setParent(pojoRootNamespace);
		}

		return pojoMeasurementEnvDef;
	}

	private static org.sopeco.persistence.entities.definition.MeasurementSpecification convertMeasurementSpecification(
			ScenarioDefinition emfScenarioDefinition) {
		MeasurementSpecification emfMeasurementSpecification = emfScenarioDefinition.getMeasurementSpecification();
		org.sopeco.persistence.entities.definition.MeasurementSpecification pojoMeasurementSpecification = EntityFactory
				.createMeasurementSpecification();

		pojoMeasurementSpecification.getInitializationAssignemts().addAll(
				convertInitializationAssignments(emfMeasurementSpecification));

		pojoMeasurementSpecification.getExperimentSeriesDefinitions().addAll(
				convertExperimentSeriesDefinitions(emfMeasurementSpecification));

		return pojoMeasurementSpecification;
	}

	private static List<org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition> convertExperimentSeriesDefinitions(
			MeasurementSpecification emfMeasurementSpecification) {
		List<org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition> pojoExpSeriesDefinitions = new LinkedList<org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition>();

		for (Object emfExpSeriesDefObj : emfMeasurementSpecification.getExperimentSeriesDefinitions()) {
			ExperimentSeriesDefinition emfExpSeriesDef = (ExperimentSeriesDefinition) emfExpSeriesDefObj;

			org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition pojoExpSeriesDef = EntityFactory
					.createExperimentSeriesDefinition(emfExpSeriesDef.getName(),
							convertExperimentTerminationCondition(emfExpSeriesDef.getExperimentTerminationCondition()));

			pojoExpSeriesDef.setExplorationStrategy(convertExplorationStrategy(emfExpSeriesDef));

			pojoExpSeriesDef.getPreperationAssignments().addAll(convertPreparationAssignments(emfExpSeriesDef));

			pojoExpSeriesDef.getExperimentAssignments().addAll(convertExperimentAssignments(emfExpSeriesDef));

			pojoExpSeriesDefinitions.add(pojoExpSeriesDef);

		}

		return pojoExpSeriesDefinitions;
	}

	private static List<org.sopeco.persistence.entities.definition.ParameterValueAssignment> convertExperimentAssignments(
			ExperimentSeriesDefinition emfExpSeriesDef) {
		List<org.sopeco.persistence.entities.definition.ParameterValueAssignment> pojoAssignments = new LinkedList<org.sopeco.persistence.entities.definition.ParameterValueAssignment>();

		for (Object emfPVAobj : emfExpSeriesDef.getExperimentAssignments()) {
			ParameterValueAssignment emfPVA = (ParameterValueAssignment) emfPVAobj;

			org.sopeco.persistence.entities.definition.ParameterValueAssignment pojoPVA;
			if (emfPVA instanceof ConstantValueAssignment) {
				pojoPVA = EntityFactory.createConstantValueAssignment(
						pojoScenarioDefinition.getParameterDefinition(emfPVA.getParameter().getFullName()),
						((ConstantValueAssignment) emfPVA).getValue());
			} else if (emfPVA instanceof DynamicValueAssignment) {
				pojoPVA = EntityFactory.createDynamicValueAssignment(((DynamicValueAssignment) emfPVA).getName(),
						pojoScenarioDefinition.getParameterDefinition(emfPVA.getParameter().getFullName()),
						((DynamicValueAssignment) emfPVA).getConfiguration().map());
			} else {
				throw new IllegalArgumentException("Unkown experiment assignment type: " + emfPVA.getClass().getName());
			}

			pojoAssignments.add(pojoPVA);
		}

		return pojoAssignments;
	}

	private static List<org.sopeco.persistence.entities.definition.ConstantValueAssignment> convertPreparationAssignments(
			ExperimentSeriesDefinition emfExpSeriesDef) {
		List<org.sopeco.persistence.entities.definition.ConstantValueAssignment> pojoAssignments = new LinkedList<org.sopeco.persistence.entities.definition.ConstantValueAssignment>();
		for (Object emfCVAobj : emfExpSeriesDef.getPreperationAssignments()) {
			ConstantValueAssignment emfCVA = (ConstantValueAssignment) emfCVAobj;

			org.sopeco.persistence.entities.definition.ConstantValueAssignment pojoCVA = EntityFactory
					.createConstantValueAssignment(
							pojoScenarioDefinition.getParameterDefinition(emfCVA.getParameter().getFullName()),
							emfCVA.getValue());
			pojoAssignments.add(pojoCVA);
		}
		return pojoAssignments;
	}

	private static org.sopeco.persistence.entities.definition.ExplorationStrategy convertExplorationStrategy(
			ExperimentSeriesDefinition emfExpSeriesDef) {
		ExplorationStrategy emfExplorationStrategy = emfExpSeriesDef.getExplorationStrategy();

		org.sopeco.persistence.entities.definition.ExplorationStrategy pojoExplorationStrategy = EntityFactory
				.createExplorationStrategy(emfExplorationStrategy.getName(), emfExplorationStrategy.getConfiguration()
						.map());

		pojoExplorationStrategy.getAnalysisConfigurations().addAll(
				convertAnalysisStrategies(emfExplorationStrategy, pojoExplorationStrategy));

		return pojoExplorationStrategy;
	}

	private static List<org.sopeco.persistence.entities.definition.AnalysisConfiguration> convertAnalysisStrategies(
			ExplorationStrategy emfExplorationStrategy,
			org.sopeco.persistence.entities.definition.ExplorationStrategy pojoExplorationStrategy) {

		List<org.sopeco.persistence.entities.definition.AnalysisConfiguration> pojoAnalysisConfigurations = new LinkedList<org.sopeco.persistence.entities.definition.AnalysisConfiguration>();

		for (Object emfAnalysisConfigObj : emfExplorationStrategy.getAnalysisConfigurations()) {
			AnalysisConfiguration emfAnalysisConfig = (AnalysisConfiguration) emfAnalysisConfigObj;

			org.sopeco.persistence.entities.definition.AnalysisConfiguration pojoAnalysisConfiguration = EntityFactory
					.createAnalysisConfiguration(emfAnalysisConfig.getName(), emfAnalysisConfig.getConfiguration()
							.map());

			for (ParameterDefinition emfParameter : emfAnalysisConfig.getDependentParameters()) {
				pojoAnalysisConfiguration.getDependentParameters().add(
						pojoScenarioDefinition.getParameterDefinition(emfParameter.getFullName()));
			}

			for (ParameterDefinition emfParameter : emfAnalysisConfig.getIndependentParameters()) {
				pojoAnalysisConfiguration.getIndependentParameters().add(
						pojoScenarioDefinition.getParameterDefinition(emfParameter.getFullName()));
			}

			pojoAnalysisConfigurations.add(pojoAnalysisConfiguration);
		}

		return pojoAnalysisConfigurations;
	}

	private static List<org.sopeco.persistence.entities.definition.ConstantValueAssignment> convertInitializationAssignments(
			MeasurementSpecification emfMeasurementSpecification) {
		List<org.sopeco.persistence.entities.definition.ConstantValueAssignment> pojoAssignments = new LinkedList<org.sopeco.persistence.entities.definition.ConstantValueAssignment>();
		for (Object emfCVAobj : emfMeasurementSpecification.getInitializationAssignemts()) {
			ConstantValueAssignment emfCVA = (ConstantValueAssignment) emfCVAobj;

			org.sopeco.persistence.entities.definition.ConstantValueAssignment pojoCVA = EntityFactory
					.createConstantValueAssignment(
							pojoScenarioDefinition.getParameterDefinition(emfCVA.getParameter().getFullName()),
							emfCVA.getValue());
			pojoAssignments.add(pojoCVA);
		}
		return pojoAssignments;
	}

	private static org.sopeco.persistence.entities.definition.ExperimentTerminationCondition convertExperimentTerminationCondition(
			ExperimentTerminationCondition emfTerminationCondition) {
		if (emfTerminationCondition instanceof TimeOut) {
			return EntityFactory
					.createTimeOutTerminationCondition(((TimeOut) emfTerminationCondition).getMaxDuration());
		} else if (emfTerminationCondition instanceof NumberOfRepetitions) {
			return EntityFactory
					.createNumberOfRepetitionsTerminationCondition(((NumberOfRepetitions) emfTerminationCondition)
							.getNumberOfRepetitions());
		}
		throw new IllegalArgumentException("Unknown experiment termination condition: "
				+ emfTerminationCondition.getClass().getName());
	}

	private static List<org.sopeco.persistence.entities.definition.ParameterNamespace> convertChildren(
			ParameterNamespace emfParentNamespace) {

		List<org.sopeco.persistence.entities.definition.ParameterNamespace> pojoChildNameSpaces = new LinkedList<org.sopeco.persistence.entities.definition.ParameterNamespace>();

		for (Object emfChildNamespaceObj : emfParentNamespace.getChildren()) {
			ParameterNamespace emfChildNamespace = (ParameterNamespace) emfChildNamespaceObj;

			org.sopeco.persistence.entities.definition.ParameterNamespace pojoChildNamespace = EntityFactory
					.createNamespace(emfChildNamespace.getName());

			List<org.sopeco.persistence.entities.definition.ParameterDefinition> pojoParameters = convertParameters(emfChildNamespace
					.getParameters());
			for (org.sopeco.persistence.entities.definition.ParameterDefinition pojoParameter : pojoParameters) {
				pojoParameter.setNamespace(pojoChildNamespace);
				pojoChildNamespace.getParameters().add(pojoParameter);
			}

			List<org.sopeco.persistence.entities.definition.ParameterNamespace> grandChildren = convertChildren(emfChildNamespace);
			for (org.sopeco.persistence.entities.definition.ParameterNamespace grandChild : grandChildren) {
				pojoChildNamespace.getChildren().add(grandChild);
				grandChild.setParent(pojoChildNamespace);
			}

			pojoChildNameSpaces.add(pojoChildNamespace);
		}

		return pojoChildNameSpaces;
	}

	private static List<org.sopeco.persistence.entities.definition.ParameterDefinition> convertParameters(
			List<ParameterDefinition> emfParameters) {

		List<org.sopeco.persistence.entities.definition.ParameterDefinition> resultList = new LinkedList<org.sopeco.persistence.entities.definition.ParameterDefinition>();
		for (Object emfParamDefObj : emfParameters) {
			ParameterDefinition emfParamDef = (ParameterDefinition) emfParamDefObj;

			resultList.add(EntityFactory.createParameterDefinition(emfParamDef.getName(), emfParamDef.getType(),
					convertRole(emfParamDef.getRole())));
		}

		return resultList;
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

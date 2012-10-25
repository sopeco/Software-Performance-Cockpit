package org.sopeco.engine.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.engine.model.xmlentities.XAnalysisConfiguration;
import org.sopeco.engine.model.xmlentities.XConfigurationNode;
import org.sopeco.engine.model.xmlentities.XConstantValueAssignment;
import org.sopeco.engine.model.xmlentities.XDynamicValueAssignment;
import org.sopeco.engine.model.xmlentities.XExperimentSeriesDefinition;
import org.sopeco.engine.model.xmlentities.XExplorationStrategy;
import org.sopeco.engine.model.xmlentities.XExtensibleElement;
import org.sopeco.engine.model.xmlentities.XMeasurementSpecification;
import org.sopeco.engine.model.xmlentities.XScenarioDefinition;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * The {@link ScenarioDefinitionFileReader} is responsible for reading and
 * parsing an XML-File representation of the scenario definition.
 * 
 * @author Alexander Wert
 * 
 */
public class ScenarioDefinitionFileReader {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioDefinitionFileReader.class);
	private MeasurementEnvironmentDefinition meDefinition;

	/**
	 * Constructor of the {@link ScenarioDefinitionFileReader}.
	 * 
	 * @param meDefinition
	 *            The passed measurement environment definition instance will be
	 *            used for resolution of parameter names to
	 *            {@link ParameterDefinition} instances.
	 */
	public ScenarioDefinitionFileReader(MeasurementEnvironmentDefinition meDefinition) {
		this.meDefinition = meDefinition;
	}

	/**
	 * Reads the file specified by the given {@link pathToFile} parameter and
	 * returns a complete {@link ScenarioDefinition} instance.
	 * 
	 * @param pathToFile
	 *            absolute path to the XML file
	 * @return Returns a {@link ScenarioDefinition} instance.
	 */
	public ScenarioDefinition read(String pathToFile) {
		LOGGER.debug("Reading scenario definition from file: {}", pathToFile);
		XScenarioDefinition sd = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(Configuration.getSessionUnrelatedSingleton().getPropertyAsStr(
					IConfiguration.CONF_SCENARIO_DEFINITION_PACKAGE));
			Unmarshaller u = jc.createUnmarshaller();

			sd = ((JAXBElement<XScenarioDefinition>) u.unmarshal(new FileInputStream(pathToFile))).getValue();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Could not find scenario definition xml file.", e);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		ScenarioDefinition scenarioDefinition = convertScenarioDefinition(sd);
		scenarioDefinition.setMeasurementEnvironmentDefinition(meDefinition);
		return scenarioDefinition;

	}

	/**
	 * Converts the xml representation of an scneario definition to a
	 * {@link ScenarioDefinition} object.
	 * 
	 * @param xScenDef
	 *            xml representation of an scneario definition
	 * @return {@link ScenarioDefinition} object
	 */
	private ScenarioDefinition convertScenarioDefinition(XScenarioDefinition xScenDef) {
		ScenarioDefinition scenarioDefinition = EntityFactory.createScenarioDefinition(xScenDef.getName());

		for (XMeasurementSpecification xMesSpec : xScenDef.getMeasurementSpecification()) {
			scenarioDefinition.getMeasurementSpecifications().add(convertMeasurementSpecification(xMesSpec));
		}

		return scenarioDefinition;
	}

	/**
	 * Converts the xml representation of an measurement specification to a
	 * {@link MeasurementSpecification} object.
	 * 
	 * @param xMesSpec
	 *            xml representation of an measurement specification
	 * @return {@link MeasurementSpecification} objec
	 */
	private MeasurementSpecification convertMeasurementSpecification(XMeasurementSpecification xMesSpec) {
		MeasurementSpecification mesSpec = EntityFactory.createMeasurementSpecification(xMesSpec.getName());

		for (XConstantValueAssignment xCVA : xMesSpec.getInitializationAssignments()) {
			mesSpec.getInitializationAssignemts().add(convertConstantValueAssignment(xCVA));
		}

		for (XExperimentSeriesDefinition xSeries : xMesSpec.getExperimentSeries()) {
			mesSpec.getExperimentSeriesDefinitions().add(convertExperimentSeriesDefinition(xSeries));
		}

		return mesSpec;
	}

	/**
	 * Converts the xml representation of an experiment series definition to a
	 * {@link ExperimentSeriesDefinition} object.
	 * 
	 * @param xSeries
	 *            xml representation of an experiment series definition
	 * @return {@link ExperimentSeriesDefinition} object
	 */
	private ExperimentSeriesDefinition convertExperimentSeriesDefinition(XExperimentSeriesDefinition xSeries) {
		ExperimentSeriesDefinition esd = EntityFactory.createExperimentSeriesDefinition(xSeries.getName());
		if (xSeries.getTerminationCondition() != null && xSeries.getTerminationCondition().getConditions() != null) {
			for (XExtensibleElement ee : xSeries.getTerminationCondition().getConditions()) {
				ExperimentTerminationCondition etc = convertTerminationCondition(ee);
				esd.addTerminationCondition(etc);
			}
		}

		esd.setExplorationStrategy(convertExplorationStrategy(xSeries.getExplorationStrategy()));

		if (xSeries.getExperimentSeriesPreparation() != null) {
			for (XConstantValueAssignment xCVA : xSeries.getExperimentSeriesPreparation().getConstantAssignment()) {
				esd.getPreperationAssignments().add(convertConstantValueAssignment(xCVA));
			}
		}
		if (xSeries.getExperimentSeriesExecution() != null) {
			for (XConstantValueAssignment xCVA : xSeries.getExperimentSeriesExecution().getConstantAssignment()) {
				esd.getExperimentAssignments().add(convertConstantValueAssignment(xCVA));
			}

			for (XDynamicValueAssignment xDVA : xSeries.getExperimentSeriesExecution().getDynamicAssignment()) {
				esd.getExperimentAssignments().add(convertDynamicValueAssignment(xDVA));
			}
		}

		return esd;
	}

	/**
	 * Converts the xml representation of a termination condition to a
	 * {@link ExperimentTerminationCondition} object.
	 * 
	 * @param xTerminationCondition
	 *            xml representation of a termination condition
	 * @return {@link ExperimentTerminationCondition} object
	 */
	private ExperimentTerminationCondition convertTerminationCondition(XExtensibleElement xTerminationCondition) {
		Map<String, String> config = new HashMap<String, String>();
		for (XConfigurationNode xConfigNode : xTerminationCondition.getConfig()) {
			config.put(xConfigNode.getKey(), xConfigNode.getValue());
		}
		return EntityFactory.createTerminationCondition(xTerminationCondition.getName(), config);
	}

	/**
	 * Converts the xml representation of an exploration strategy to a
	 * {@link ExplorationStrategy} object.
	 * 
	 * @param xExplorationStrategy
	 *            xml representation of an exploration strategy
	 * @return {@link ExplorationStrategy} object
	 */
	private ExplorationStrategy convertExplorationStrategy(XExplorationStrategy xExplorationStrategy) {
		Map<String, String> config = new HashMap<String, String>();
		for (XConfigurationNode xConfigNode : xExplorationStrategy.getConfig()) {
			config.put(xConfigNode.getKey(), xConfigNode.getValue());
		}
		ExplorationStrategy explStrategy = EntityFactory.createExplorationStrategy(xExplorationStrategy.getName(),
				config);

		for (XAnalysisConfiguration xAnalysisConfig : xExplorationStrategy.getAnalysisConfig()) {
			explStrategy.getAnalysisConfigurations().add(convertAnalysisConfiguration(xAnalysisConfig));
		}

		return explStrategy;
	}

	/**
	 * Converts the xml representation of an analysis strategy to a
	 * {@link AnalysisConfiguration} object.
	 * 
	 * @param xAnalysisConfig
	 *            xml representation of an analysis strategy
	 * @return {@link AnalysisConfiguration} object
	 */
	private AnalysisConfiguration convertAnalysisConfiguration(XAnalysisConfiguration xAnalysisConfig) {

		Map<String, String> xConfig = new HashMap<String, String>();
		for (XConfigurationNode xNode : xAnalysisConfig.getConfig()) {
			xConfig.put(xNode.getKey(), xNode.getValue());
		}
		AnalysisConfiguration analysisConfiguration = EntityFactory.createAnalysisConfiguration(
				xAnalysisConfig.getName(), xConfig);

		for (String parameterName : xAnalysisConfig.getDependentParameter()) {
			ParameterDefinition pDef = findParameter(parameterName);
			analysisConfiguration.getDependentParameters().add(pDef);
		}

		for (String parameterName : xAnalysisConfig.getIndependentParameter()) {
			ParameterDefinition pDef = findParameter(parameterName);
			analysisConfiguration.getIndependentParameters().add(pDef);
		}

		return analysisConfiguration;
	}

	/**
	 * Converts the xml representation of a constant value assignment strategy
	 * to a {@link ConstantValueAssignment} object.
	 * 
	 * @param xCVA
	 *            xml representation of a constant value assignment strategy
	 * @return {@link ConstantValueAssignment} object
	 */
	private ConstantValueAssignment convertConstantValueAssignment(XConstantValueAssignment xCVA) {
		ParameterDefinition parameter = findParameter(xCVA.getParameter());
		return EntityFactory.createConstantValueAssignment(parameter, xCVA.getValue());
	}

	/**
	 * Converts the xml representation of a dynamic value assignment strategy to
	 * a {@link DynamicValueAssignment} object.
	 * 
	 * @param xDVA
	 *            xml representation of a dynamic value assignment strategy
	 * @return {@link DynamicValueAssignment} object
	 */
	private DynamicValueAssignment convertDynamicValueAssignment(XDynamicValueAssignment xDVA) {
		ParameterDefinition parameter = findParameter(xDVA.getParameter());
		Map<String, String> config = new HashMap<String, String>();
		for (XConfigurationNode xConfigNode : xDVA.getConfig()) {
			config.put(xConfigNode.getKey(), xConfigNode.getValue());
		}

		return EntityFactory.createDynamicValueAssignment(xDVA.getName(), parameter, config);
	}

	private ParameterDefinition findParameter(String fullParameterName) {
		for (ParameterDefinition pDef : meDefinition.getRoot().getAllParameters()) {
			if (pDef.getFullName().equals(fullParameterName)) {
				return pDef;
			}
		}
		throw new RuntimeException("Invalid parameter type in measurement specification! Parameter with name "
				+ fullParameterName + " is not defined in the ME-Definition!");

	}

}

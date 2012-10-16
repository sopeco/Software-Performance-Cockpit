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
		return convert(sd);

	}

	private ScenarioDefinition convert(XScenarioDefinition xScenDef) {
		ScenarioDefinition scenarioDefinition = EntityFactory.createScenarioDefinition(xScenDef.getName());

		for (XMeasurementSpecification xMesSpec : xScenDef.getMeasurementSpecification()) {
			scenarioDefinition.getMeasurementSpecifications().add(convert(xMesSpec));
		}

		return scenarioDefinition;
	}

	private MeasurementSpecification convert(XMeasurementSpecification xMesSpec) {
		MeasurementSpecification mesSpec = EntityFactory.createMeasurementSpecification(xMesSpec.getName());

		for (XConstantValueAssignment xCVA : xMesSpec.getInitializationAssignments()) {
			mesSpec.getInitializationAssignemts().add(convert(xCVA));
		}

		for (XExperimentSeriesDefinition xSeries : xMesSpec.getExperimentSeries()) {
			mesSpec.getExperimentSeriesDefinitions().add(convert(xSeries));
		}

		return mesSpec;
	}

	private ExperimentSeriesDefinition convert(XExperimentSeriesDefinition xSeries) {
		ExperimentTerminationCondition etc = convertTerminationCondition(xSeries.getTerminationCondition());
		ExperimentSeriesDefinition esd = EntityFactory.createExperimentSeriesDefinition(xSeries.getName(), etc);
		esd.setExplorationStrategy(convertExplorationStrategy(xSeries.getExplorationStrategy()));

		if (xSeries.getExperimentSeriesPreparation() != null) {
			for (XConstantValueAssignment xCVA : xSeries.getExperimentSeriesPreparation().getConstantAssignment()) {
				esd.getPreperationAssignments().add(convert(xCVA));
			}
		}
		if (xSeries.getExperimentSeriesExecution() != null) {
			for (XConstantValueAssignment xCVA : xSeries.getExperimentSeriesExecution().getConstantAssignment()) {
				esd.getExperimentAssignments().add(convert(xCVA));
			}

			for (XDynamicValueAssignment xDVA : xSeries.getExperimentSeriesExecution().getDynamicAssignment()) {
				esd.getExperimentAssignments().add(convert(xDVA));
			}
		}

		return esd;
	}

	private ExplorationStrategy convertExplorationStrategy(XExplorationStrategy xExplorationStrategy) {
		Map<String, String> config = new HashMap<String, String>();
		for (XConfigurationNode xConfigNode : xExplorationStrategy.getConfig()) {
			config.put(xConfigNode.getKey(), xConfigNode.getValue());
		}
		ExplorationStrategy explStrategy = EntityFactory.createExplorationStrategy(xExplorationStrategy.getName(),
				config);

		for (XAnalysisConfiguration xAnalysisConfig : xExplorationStrategy.getAnalysisConfig()) {
			explStrategy.getAnalysisConfigurations().add(convert(xAnalysisConfig));
		}

		return explStrategy;
	}

	private AnalysisConfiguration convert(XAnalysisConfiguration xAnalysisConfig) {

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

	private ExperimentTerminationCondition convertTerminationCondition(XExtensibleElement xTerminationCondition) {
		Map<String, String> config = new HashMap<String, String>();
		for (XConfigurationNode xConfigNode : xTerminationCondition.getConfig()) {
			config.put(xConfigNode.getKey(), xConfigNode.getValue());
		}

		return EntityFactory.createTerminationCondition(xTerminationCondition.getName(), config);
	}

	private ConstantValueAssignment convert(XConstantValueAssignment xCVA) {
		ParameterDefinition parameter = findParameter(xCVA.getParameter());
		return EntityFactory.createConstantValueAssignment(parameter, xCVA.getValue());
	}

	private DynamicValueAssignment convert(XDynamicValueAssignment xDVA) {
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

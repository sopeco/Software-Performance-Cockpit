package org.sopeco.engine.model;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.engine.model.xmlentities.ObjectFactory;
import org.sopeco.engine.model.xmlentities.XAnalysisConfiguration;
import org.sopeco.engine.model.xmlentities.XConfigurationNode;
import org.sopeco.engine.model.xmlentities.XConstantValueAssignment;
import org.sopeco.engine.model.xmlentities.XDynamicValueAssignment;
import org.sopeco.engine.model.xmlentities.XExperimentSeriesDefinition;
import org.sopeco.engine.model.xmlentities.XExperimentTerminationCondition;
import org.sopeco.engine.model.xmlentities.XExplorationStrategy;
import org.sopeco.engine.model.xmlentities.XExtensibleElement;
import org.sopeco.engine.model.xmlentities.XMeasurementSpecification;
import org.sopeco.engine.model.xmlentities.XScenarioDefinition;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * The {@link ScenarioDefinitionFileWriter} is responsible for writing an XML
 * representation of a scenario definition to a file..
 * 
 * @author Alexander Wert
 * 
 */
public class ScenarioDefinitionFileWriter {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioDefinitionFileReader.class);

	/**
	 * Converts the given {@link ScenarioDefinition} to an XML representation
	 * and writes the XML string to a file specified by the parameter
	 * {@link pathToFile}.
	 * 
	 * @param scenarioDefinition
	 *            scenario definition to be stored
	 * @param pathToFile
	 *            absolute path to the file in which the scenario definition
	 *            should be stored
	 */
	public void writeScenarioDefinition(ScenarioDefinition scenarioDefinition, String pathToFile) {
		LOGGER.debug("Writing scenario definition {} to file: {}", scenarioDefinition.getScenarioName(), pathToFile);

		XScenarioDefinition xScenarioDefinition = convertScenarioDefinition(scenarioDefinition);

		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(Configuration.getSessionUnrelatedSingleton().getPropertyAsStr(
					IConfiguration.CONF_SCENARIO_DEFINITION_PACKAGE));
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			FileWriter fileWriter = new FileWriter(pathToFile);
			JAXBElement<XScenarioDefinition> jaxbElement = new JAXBElement<XScenarioDefinition>(
					ObjectFactory.SCENARIO_QNAME, XScenarioDefinition.class, xScenarioDefinition);
			m.marshal(jaxbElement, fileWriter);
		} catch (Exception e) {
			throw new RuntimeException("Failed writing scenario definition to file!", e);
		}

	}

	/**
	 * Converts the {@link ScenarioDefinition} object to an XML representation
	 * 
	 * @param scenarioDefinition
	 * @return
	 */
	private XScenarioDefinition convertScenarioDefinition(ScenarioDefinition scenarioDefinition) {
		XScenarioDefinition xScenarioDefinition = new XScenarioDefinition();
		xScenarioDefinition.setName(scenarioDefinition.getScenarioName());

		for (MeasurementSpecification ms : scenarioDefinition.getMeasurementSpecifications()) {
			xScenarioDefinition.getMeasurementSpecification().add(convertMeasurementSpecification(ms));
		}
		return xScenarioDefinition;
	}

	/**
	 * Converts the {@link MeasurementSpecification} object to an XML
	 * representation
	 * 
	 * @param ms
	 * @return
	 */
	private XMeasurementSpecification convertMeasurementSpecification(MeasurementSpecification ms) {
		XMeasurementSpecification xMeasurementSpecification = new XMeasurementSpecification();
		xMeasurementSpecification.setName(ms.getName());

		for (ConstantValueAssignment cva : ms.getInitializationAssignemts()) {
			XConstantValueAssignment xCVA = new XConstantValueAssignment();
			xCVA.setParameter(cva.getParameter().getFullName());
			xCVA.setValue(cva.getValue());
			xMeasurementSpecification.getInitializationAssignments().add(xCVA);
		}

		for (ExperimentSeriesDefinition esd : ms.getExperimentSeriesDefinitions()) {
			xMeasurementSpecification.getExperimentSeries().add(convertExperimentSeriesDefinition(esd));
		}
		return xMeasurementSpecification;
	}

	/**
	 * Converts the {@link ExperimentSeriesDefinition} object to an XML
	 * representation
	 * 
	 * @param esd
	 * @return
	 */
	private XExperimentSeriesDefinition convertExperimentSeriesDefinition(ExperimentSeriesDefinition esd) {
		XExperimentSeriesDefinition xESD = new XExperimentSeriesDefinition();
		xESD.setName(esd.getName());
		if (esd.getTerminationConditions() != null && !esd.getTerminationConditions().isEmpty()) {
			XExperimentTerminationCondition xTC = new XExperimentTerminationCondition();
			for (ExperimentTerminationCondition tc : esd.getTerminationConditions()) {
				xTC.getConditions().add(convertTerminationCondition(tc));
			}
			xESD.setTerminationCondition(xTC);
		}

		xESD.setExplorationStrategy(convertExplorationStrategy(esd.getExplorationStrategy()));

		for (ConstantValueAssignment cva : esd.getPreperationAssignments()) {
			if (xESD.getExperimentSeriesPreparation() == null) {
				xESD.setExperimentSeriesPreparation(new XExperimentSeriesDefinition.ExperimentSeriesPreparation());
			}
			XConstantValueAssignment xCVA = new XConstantValueAssignment();
			xCVA.setParameter(cva.getParameter().getFullName());
			xCVA.setValue(cva.getValue());

			xESD.getExperimentSeriesPreparation().getConstantAssignment().add(xCVA);
		}

		for (ParameterValueAssignment pva : esd.getExperimentAssignments()) {
			if (xESD.getExperimentSeriesExecution() == null) {
				xESD.setExperimentSeriesExecution(new XExperimentSeriesDefinition.ExperimentSeriesExecution());
			}
			if (pva instanceof ConstantValueAssignment) {
				ConstantValueAssignment cva = (ConstantValueAssignment) pva;
				XConstantValueAssignment xCVA = new XConstantValueAssignment();
				xCVA.setParameter(cva.getParameter().getFullName());
				xCVA.setValue(cva.getValue());
				xESD.getExperimentSeriesExecution().getConstantAssignment().add(xCVA);
			} else if (pva instanceof DynamicValueAssignment) {
				DynamicValueAssignment dva = (DynamicValueAssignment) pva;
				XDynamicValueAssignment xDVA = new XDynamicValueAssignment();
				xDVA.setName(dva.getName());
				xDVA.setParameter(dva.getParameter().getFullName());
				xDVA.getConfig().addAll(convertConfigurationNodes(dva.getConfiguration()));
				xESD.getExperimentSeriesExecution().getDynamicAssignment().add(xDVA);
			}
		}

		return xESD;
	}

	/**
	 * Converts the {@link ExperimentTerminationCondition} object to an XML
	 * representation
	 * 
	 * @param experimentTerminationCondition
	 * @return
	 */
	private XExtensibleElement convertTerminationCondition(ExperimentTerminationCondition experimentTerminationCondition) {
		XExtensibleElement xTermCondition = new XExtensibleElement();
		xTermCondition.setName(experimentTerminationCondition.getName());
		xTermCondition.getConfig().addAll(
				convertConfigurationNodes(experimentTerminationCondition.getParametersValues()));
		return xTermCondition;
	}

	/**
	 * Converts the {@link ExplorationStrategy} object to an XML representation
	 * 
	 * @param explorationStrategy
	 * @return
	 */
	private XExplorationStrategy convertExplorationStrategy(ExplorationStrategy explorationStrategy) {
		XExplorationStrategy xExplStrategy = new XExplorationStrategy();
		xExplStrategy.setName(explorationStrategy.getName());
		xExplStrategy.getConfig().addAll(convertConfigurationNodes(explorationStrategy.getConfiguration()));
		for (AnalysisConfiguration analysisConfig : explorationStrategy.getAnalysisConfigurations()) {
			xExplStrategy.getAnalysisConfig().add(convertAnalysisCOnfiguration(analysisConfig));
		}

		return xExplStrategy;
	}

	/**
	 * Converts the {@link AnalysisConfiguration} object to an XML
	 * representation
	 * 
	 * @param analysisConfig
	 * @return
	 */
	private XAnalysisConfiguration convertAnalysisCOnfiguration(AnalysisConfiguration analysisConfig) {
		XAnalysisConfiguration xAnalysisConfig = new XAnalysisConfiguration();
		xAnalysisConfig.setName(analysisConfig.getName());
		xAnalysisConfig.getConfig().addAll(convertConfigurationNodes(analysisConfig.getConfiguration()));

		for (ParameterDefinition pDef : analysisConfig.getDependentParameters()) {
			xAnalysisConfig.getDependentParameter().add(pDef.getFullName());
		}

		for (ParameterDefinition pDef : analysisConfig.getIndependentParameters()) {
			xAnalysisConfig.getIndependentParameter().add(pDef.getFullName());
		}

		return xAnalysisConfig;
	}

	/**
	 * Converts the configuration map to an XML representation
	 * 
	 * @param config
	 * @return
	 */
	private List<XConfigurationNode> convertConfigurationNodes(Map<String, String> config) {
		List<XConfigurationNode> xConfig = new ArrayList<XConfigurationNode>();
		for (Entry<String, String> entry : config.entrySet()) {
			XConfigurationNode xConfigNode = new XConfigurationNode();
			xConfigNode.setKey(entry.getKey());
			xConfigNode.setValue(entry.getValue());
			xConfig.add(xConfigNode);
		}
		return xConfig;
	}

}

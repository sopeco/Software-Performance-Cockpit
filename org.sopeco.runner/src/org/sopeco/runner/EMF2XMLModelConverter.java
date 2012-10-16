package org.sopeco.runner;

import java.io.IOException;

import org.sopeco.config.Configuration;
import org.sopeco.engine.model.ScenarioDefinitionFileWriter;
import org.sopeco.model.util.EMFUtil;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.util.Tools;

public class EMF2XMLModelConverter {

	public EMF2XMLModelConverter() {
		super();

	}

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			throw new RuntimeException("Programm arguments must contain the üath to the EMF file "
					+ "and the path to the target XML file!");
		}
		EMF2XMLModelConverter converter = new EMF2XMLModelConverter();
		converter.convert(args[0], args[1]);
	}

	public void convert(String emfFile, String xmlFile) throws IOException {

		if (!Tools.isAbsolutePath(emfFile)) {
			emfFile = Tools.concatFileName(Configuration.getSessionUnrelatedSingleton().getAppRootDirectory(), emfFile);
		}
		ScenarioDefinition scenario = (ScenarioDefinition) EMFUtil.loadFromFilePath(emfFile);

		ScenarioDefinitionFileWriter writer = new ScenarioDefinitionFileWriter();
		writer.writeScenarioDefinition(scenario, xmlFile);
	}
}

package org.sopeco.persistence.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.entities.ScenarioInstance;

public class PersistenceFileTransformer {
	private static final String FILE_EXTENSION = ".sdf";
	private static Logger logger = LoggerFactory.getLogger(PersistenceFileTransformer.class);

	/**
	 * Stores the passed list of scenario to the passed file
	 * 
	 * @param scenarios
	 *            list of scenarios to be stored
	 * @param filePath
	 *            path and name of the file, where the scenarios should be
	 *            stored in. Note: this string should not contain the file
	 *            extension
	 * @return Returns true if saving was successful, otherwise false
	 */
	public static boolean saveAsFile(ScenarioInstance scenario, String filePath) {
		if (scenario == null) {
			logger.error("Cannot save a null scenario!");
			return false;
		}
		List<ScenarioInstance> scenarios = new ArrayList<ScenarioInstance>();
		scenarios.add(scenario);
		return saveAsFile(scenarios, filePath);

	}

	/**
	 * Stores the passed list of scenario to the passed file
	 * 
	 * @param scenarios
	 *            list of scenarios to be stored
	 * @param filePath
	 *            path and name of the file, where the scenarios should be
	 *            stored in. Note: this string should not contain the file
	 *            extension
	 * @return Returns true if saving was successful, otherwise false
	 */
	public static boolean saveAsFile(List<ScenarioInstance> scenarios, String filePath) {
		if (scenarios == null) {
			logger.error("Cannot save a null list!");
			return false;
		}
		try {
			filePath = filePath.endsWith(FILE_EXTENSION) ? filePath : (filePath + FILE_EXTENSION);
			FileOutputStream saveFile = new FileOutputStream(filePath);
			ObjectOutputStream saveStream = new ObjectOutputStream(saveFile);

			saveStream.writeObject(scenarios);
			saveStream.close();
			return true;
		} catch (Exception exc) {
			logger.error("Failed saving scenarios to file {}", filePath);
			return false;
		}
	}

	/**
	 * Loads a list of scenario instances from the passed file
	 * 
	 * @param filePath
	 *            file, where to load the scenarios from
	 * @return returns a list of scenarios, or null if loading fails
	 */
	public static List<ScenarioInstance> loadFromFile(String filePath) {
		List<ScenarioInstance> scenarios = null;
		try {
			filePath = filePath.endsWith(FILE_EXTENSION) ? filePath : (filePath + FILE_EXTENSION);
			FileInputStream loadFile = new FileInputStream(filePath);

			ObjectInputStream loadStream = new ObjectInputStream(loadFile);
			scenarios = (List<ScenarioInstance>) loadStream.readObject();
			loadStream.close();

		} catch (Exception exc) {
			logger.error("Failed loading scenarios from file {}", filePath);
		}
		return scenarios;
	}
}

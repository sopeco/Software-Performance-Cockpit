package org.sopeco.plugin.std.exploration.screening.container;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Container used to store the levels of a parameter for every run of a design.
 * 
 * @author Pascal Meier
 */
public class ParameterRunLevels {

	/**
	 * Parameter of which the run levels are stored
	 */
	private ParameterDefinition parameter;
	/**
	 * List of run levels (1, -1 or 0)
	 */
	private List<Integer> runLevels;
	
	/**
	 * Constructor.
	 * @param parameter parameter of which the run levels are stored
	 */
	public ParameterRunLevels(ParameterDefinition parameter) {
		super();
		this.parameter = parameter;
		runLevels = new ArrayList<Integer>();
	}

	public ParameterDefinition getParameter() {
		return parameter;
	}
	
	/**
	 * Adds a run level to the container. Only levels -1, 1 or 0 are allowed.
	 * @param level value of the level
	 * @throws FrameworkException
	 */
	public void addRunLevel(int level) {
		if (!(level == 1 || level == -1 || level == 0)) {
			throw new IllegalStateException("Unknown level specified.");
		}
		runLevels.add(level);
	}
	
	/**
	 * Inserts a run level at the specified position.
	 * @param position position where the run-level will be inserted
	 * @param level the value of the level that will be inserted. has to be 1, -1 or 0
	 * @throws Exception
	 */
	public void insertRunLevelAtPosition(int position, int level) {
		if (!(level == 1 || level == -1 || level == 0)) {
			throw new IllegalStateException("Unknown level specified.");
		}
		runLevels.add(position, level);
	}
	
	public int getLevelByRunNumber(int runNumber) {
		return runLevels.get(runNumber);
	}
	
	public List<Integer> getAllRunLevels() {
		return runLevels;
	}
	
	public int getNumberOfRunLevels() {
		return runLevels.size();
	}
}

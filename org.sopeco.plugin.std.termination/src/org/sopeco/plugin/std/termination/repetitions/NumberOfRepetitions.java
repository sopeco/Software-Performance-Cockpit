package org.sopeco.plugin.std.termination.repetitions;

import java.util.Map;

import org.sopeco.engine.experimentseries.ITerminationCondition;
import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.engine.registry.ISoPeCoExtension;

public class NumberOfRepetitions extends AbstractSoPeCoExtensionArtifact implements ITerminationCondition {
	public static final String NUMBEROFREPETITION_KEY = "repetitions";
	private int numberOfRepetitions;

	public NumberOfRepetitions(ISoPeCoExtension<?> provider) {
		super(provider);
	}

	public int getNumberOfRepetitions() {
		return numberOfRepetitions;
	}



	@Override
	public void initialize(Map<String, String> configuration) {
		if (!configuration.containsKey(NUMBEROFREPETITION_KEY)) {
			throw new RuntimeException("Invalid configuration for number of repetition termination condition. "
					+ "The configuration must contain the key '" + NUMBEROFREPETITION_KEY + "' !");
		}
		
		numberOfRepetitions = Integer.parseInt(configuration.get(NUMBEROFREPETITION_KEY));
		
	}

}

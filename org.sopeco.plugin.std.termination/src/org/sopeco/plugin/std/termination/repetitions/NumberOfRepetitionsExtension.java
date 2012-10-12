package org.sopeco.plugin.std.termination.repetitions;

import java.util.HashMap;
import java.util.Map;

import org.sopeco.engine.experimentseries.ITerminationCondition;
import org.sopeco.engine.experimentseries.ITerminationConditionExtension;

public class NumberOfRepetitionsExtension implements ITerminationConditionExtension {

	public static final String NAME = "Number Of Repetitions";
	
	
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Map<String, String> getConfigParameters() {
		Map<String, String> configs = new HashMap<String, String>();
		configs.put(NumberOfRepetitions.NUMBEROFREPETITION_KEY, "1");
		return configs;
	}

	@Override
	public ITerminationCondition createExtensionArtifact() {
		return new NumberOfRepetitions(this);
	}

}

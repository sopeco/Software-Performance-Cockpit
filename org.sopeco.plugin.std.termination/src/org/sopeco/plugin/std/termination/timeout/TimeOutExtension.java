package org.sopeco.plugin.std.termination.timeout;

import java.util.HashMap;
import java.util.Map;


import org.sopeco.engine.experimentseries.ITerminationCondition;
import org.sopeco.engine.experimentseries.ITerminationConditionExtension;

public class TimeOutExtension implements ITerminationConditionExtension{
	public static final String NAME = "Timeout";
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Map<String, String> getConfigParameters() {
		Map<String, String> configs = new HashMap<String, String>();
		configs.put(TimeOut.TIMEOUT_KEY, "30000");
		return configs;
	}

	@Override
	public ITerminationCondition createExtensionArtifact() {
		return new TimeOut(this);
	}

}

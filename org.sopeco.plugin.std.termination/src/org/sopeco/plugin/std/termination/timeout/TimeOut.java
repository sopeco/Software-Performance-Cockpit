package org.sopeco.plugin.std.termination.timeout;

import java.util.Map;

import org.sopeco.engine.experimentseries.ITerminationCondition;
import org.sopeco.engine.registry.AbstractSoPeCoExtensionArtifact;
import org.sopeco.engine.registry.ISoPeCoExtension;

public class TimeOut extends AbstractSoPeCoExtensionArtifact implements ITerminationCondition {

	public static final String TIMEOUT_KEY = "timeout";
	private long timeOut;

	public TimeOut(ISoPeCoExtension<?> provider) {
		super(provider);

	}

	public long getTimeOut() {
		return timeOut;
	}



	@Override
	public void initialize(Map<String, String> configuration) {
		if (!configuration.containsKey(TIMEOUT_KEY)) {
			throw new RuntimeException("Invalid configuration for timeout termination condition. "
					+ "The configuration must contain the key '" + TIMEOUT_KEY + "' !");
		}
		
		timeOut = Long.parseLong(configuration.get(TIMEOUT_KEY));

	}

}

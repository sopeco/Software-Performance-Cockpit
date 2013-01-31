/**
 * 
 */
package org.sopeco.plugin.test.dummydataprocessing;

import java.util.Collections;
import java.util.Map;

import org.sopeco.engine.processing.IProcessingStrategy;
import org.sopeco.engine.processing.IProcessingStrategyExtension;

/**
 * A test extension for the runner project.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class DummyRunnerExtension implements IProcessingStrategyExtension {

	public static final String NAME = "Dummy Processing Extension";
	
	public DummyRunnerExtension() {	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProcessingStrategy createExtensionArtifact() {
		return new DummyRunnerStrategy(this);
	}

	@Override
	public Map<String, String> getConfigParameters() {
		return Collections.emptyMap();
	}


}

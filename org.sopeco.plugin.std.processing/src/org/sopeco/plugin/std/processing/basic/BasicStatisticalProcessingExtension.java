/**
 * 
 */
package org.sopeco.plugin.std.processing.basic;

import java.util.Collections;
import java.util.Map;

import org.sopeco.engine.processing.IProcessingStrategy;
import org.sopeco.engine.processing.IProcessingStrategyExtension;

/**
 * @author Roozbeh Farahbod
 *
 */
public class BasicStatisticalProcessingExtension implements IProcessingStrategyExtension {

	public static final String NAME = "Basic Statistical Processing";
	
	public BasicStatisticalProcessingExtension() {	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProcessingStrategy createExtensionArtifact() {
		return new BasicStatisticalProcessingStrategy(this);
	}

	@Override
	public Map<String, String> getConfigParameters() {
		return Collections.emptyMap();
	}


}

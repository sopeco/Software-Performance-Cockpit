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
public class FilterDataProcessingExtension implements IProcessingStrategyExtension {

	public static final String NAME = "Filter Data Processing";
	
	public FilterDataProcessingExtension() {	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProcessingStrategy createExtensionArtifact() {
		return new FilterDataProcessingStrategy(this);
	}

	@Override
	public Map<String, String> getConfigParameters() {
		return Collections.emptyMap();
	}


}

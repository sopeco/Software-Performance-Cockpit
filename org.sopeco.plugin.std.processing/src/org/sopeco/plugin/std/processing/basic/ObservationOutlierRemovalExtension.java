package org.sopeco.plugin.std.processing.basic;

import java.util.Collections;
import java.util.Map;

import org.sopeco.engine.processing.IProcessingStrategy;
import org.sopeco.engine.processing.IProcessingStrategyExtension;

/**
 * 
 * @author Alexander Wert
 * 
 */
public class ObservationOutlierRemovalExtension implements IProcessingStrategyExtension {
	public static final String NAME = "Outlier Removal for Observation Parameters";

	public ObservationOutlierRemovalExtension() {
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProcessingStrategy createExtensionArtifact() {
		return new ObservationOutlierRemovalStrategy(this);
	}

	@Override
	public Map<String, String> getConfigParameters() {
		return Collections.emptyMap();
	}
}

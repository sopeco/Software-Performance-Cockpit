package org.sopeco.engine.registry;

import java.util.Map;

/**
 * This is root interface of all SoPeCo extensions.
 * 
 * @author Roozbeh Farahbod
 *
 */
public interface ISoPeCoExtension<EA extends ISoPeCoExtensionArtifact> {
	
	/**
	 * Returns the name of the extension which is expected to be unique in the framework.
	 * 
	 * The name is expected to be specific to the extension that is 
	 * provided, for example 'MARS' or 'GP'. 
	 */
	public String getName();

	/**
	 * Creates a new artifact for this extension. 
	 */
	public EA createExtensionArtifact();
	
	/**
	 * Returns a set of configuration parameters needed by the artifacts
	 * of this extension. The return value is a mapping of parameter names
	 * to an optional default value. It is expected that in most cases the 
	 * default values are empty String instances since they depend on the 
	 * runtime instance of the artifact.
	 * 
	 * @return a map of configuration parameter names to optional default values
	 */
	public Map<String, String> getConfigParameters();
}

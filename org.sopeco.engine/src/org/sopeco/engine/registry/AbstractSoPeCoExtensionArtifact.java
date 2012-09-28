package org.sopeco.engine.registry;

/**
 * The abstract class for SoPeCo extension artifacts.
 * 
 * @author Roozbeh Farahbod
 *
 */
public abstract class AbstractSoPeCoExtensionArtifact implements ISoPeCoExtensionArtifact {

	/**
	 * Extension provider.
	 */
	private final ISoPeCoExtension<?> provider; 
	
	/**
	 * Creates a new extension artifact with the given extension provider.
	 * 
	 * @param provider the provider of this artifact
	 */
	public AbstractSoPeCoExtensionArtifact(ISoPeCoExtension<?> provider) {
		this.provider = provider;
	}
	
	@Override
	public ISoPeCoExtension<?> getProvider() {
		return this.provider;
	}

}

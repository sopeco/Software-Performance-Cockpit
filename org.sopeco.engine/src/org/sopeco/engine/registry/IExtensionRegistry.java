/**
 * 
 */
package org.sopeco.engine.registry;

import java.util.Collection;

/**
 * The interface of the SoPeCo extension registry.
 * 
 * @author Roozbeh Farahbod
 * 
 */
public interface IExtensionRegistry {

	/**
	 * Returns a collection of all registered SoPeCo extensions.
	 * 
	 * @return Collection of supported extensions
	 */
	Collection<? extends ISoPeCoExtension<?>> getExtensions();

	/**
	 * Returns an extensions object that filters in only those extensions that
	 * support the extension type.
	 * 
	 * @param c
	 *            the extension class
	 * @param <E>
	 *            type of the extension to be retrieved
	 * @see Extensions
	 * 
	 * @return an extensions object that filters in only those extensions that
	 *         support the extension type.
	 */
	<E extends ISoPeCoExtension<?>> Extensions<E> getExtensions(Class<E> c);

	/**
	 * Assuming that the extension names are unique for any category of SoPeCo
	 * extensions, this method returns an extension artifact from the extension
	 * identified by the extension type.
	 * 
	 * @param c
	 *            the extension class
	 * @param name
	 *            name of the extension
	 * @param <EA>
	 *            type of the extension artefact to be retrieved
	 * @return an extension artifact produced by the extension
	 */
	<EA extends ISoPeCoExtensionArtifact> EA getExtensionArtifact(Class<? extends ISoPeCoExtension<EA>> c, String name);

	/**
	 * Adds a new extension to the registry.
	 * 
	 * It is assumed that extensions have unique names.
	 * 
	 * @param ext
	 *            an instantiated extension
	 */
	void addExtension(ISoPeCoExtension<?> ext);

	/**
	 * Removes the extension with the given name from the registry.
	 * 
	 * @param name
	 *            extension name
	 */
	void removeExtension(String name);

}

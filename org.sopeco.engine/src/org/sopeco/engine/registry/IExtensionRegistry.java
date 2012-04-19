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
	 */
	public Collection<? extends ISoPeCoExtension<?>> getExtensions();

	/**
	 * Returns an extensions object that filters in only
	 * those extensions that support the given class (or interface).
	 *  
	 * @param c a class instance
	 * @see Extensions
	 */
	public <E extends ISoPeCoExtension<?>> Extensions<E> getExtensions(Class<E> c);

	/**
	 * Assuming that the extension names are unique for any category of SoPeCo 
	 * extensions, this method returns an extension artifact from the extension identified by
	 * the interface class and the given name.
	 * 
	 * @param c a class instance
	 * @param name name of the extension
	 * @return an extension artifact produced by the extension
	 */
	public <EA extends ISoPeCoExtensionArtifact> EA getExtensionArtifact(Class<? extends ISoPeCoExtension<EA>> c, String name);
	
	/**
	 * Adds a new extension to the registry.
	 * 
	 * It is assumed that extensions have unique names.
	 * 
	 * @param ext an instantiated extension
	 */
	public void addExtension(ISoPeCoExtension<?> ext);
	
	/**
	 * Removes the extension with the given name from the registry. 
	 * 
	 * @param name extension name
	 */
	public void removeExtension(String name);
	

}

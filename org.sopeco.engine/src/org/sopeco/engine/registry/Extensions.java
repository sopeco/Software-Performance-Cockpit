/**
 * 
 */
package org.sopeco.engine.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class offers a generic view towards available extensions.
 * 
 * @author Roozbeh Farahbod
 *
 */
public class Extensions<E extends ISoPeCoExtension> {

	private final ExtensionRegistry registry;
	
	private final List<E> extensions;
	
	/**
	 * Creates an extensions view that filters extensions such that
	 * it reports only those that implement the given class (or interface).
	 * 
	 * @filter a class instance
	 */
	@SuppressWarnings("unchecked")
	public Extensions(Class<E> filter) {
		registry = ExtensionRegistry.getRegistry();
		
		// load the relevant extensions
		extensions = new ArrayList<E>();
		for (ISoPeCoExtension ext: registry.getExtensions()) {
			if (filter.isAssignableFrom(ext.getClass()))
				extensions.add((E)ext);
		}
	}

	/**
	 * Returns a list of extensions that provide the desired extension interface.
	 */
	public List<E> getExtensions() {
		return Collections.unmodifiableList(extensions);
	}
}

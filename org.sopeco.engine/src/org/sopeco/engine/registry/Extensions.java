/**
 * 
 */
package org.sopeco.engine.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class offers a generic view towards available extensions. It implements
 * {@link Iterable}.
 * 
 * @param <E>
 *            type of the SoPeCo extension
 * 
 * @author Roozbeh Farahbod
 * 
 */
@SuppressWarnings("rawtypes")
public class Extensions<E extends ISoPeCoExtension> implements Iterable<E> {

	private final IExtensionRegistry registry;

	private final List<E> extensions;

	/**
	 * Creates an extensions view that filters extensions such that it reports
	 * only those that implement the given class (or interface).
	 * 
	 * @filter a class instance
	 */
	@SuppressWarnings("unchecked")
	protected Extensions(Class<E> filter) {
		registry = ExtensionRegistry.getSingleton();

		// load the relevant extensions
		extensions = new ArrayList<E>();
		for (ISoPeCoExtension ext : registry.getExtensions()) {
			if (filter.isAssignableFrom(ext.getClass())) {
				extensions.add((E) ext);
			}
		}
	}

	/**
	 * Returns a list of extensions that provide the desired extension
	 * interface.
	 * 
	 * @return a list of extensions that provide the desired extension
	 *         interface.
	 */
	public List<E> getList() {
		return Collections.unmodifiableList(extensions);
	}

	@Override
	public Iterator<E> iterator() {
		return extensions.iterator();
	}
}

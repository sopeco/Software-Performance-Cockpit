/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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

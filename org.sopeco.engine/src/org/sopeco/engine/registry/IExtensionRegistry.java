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

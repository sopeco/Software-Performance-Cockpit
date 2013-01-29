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
package org.sopeco.engine.registry;

import java.util.Map;

/**
 * This is root interface of all SoPeCo extensions.
 * 
 * @param <EA>
 *            Type of the extension artifact
 * @author Roozbeh Farahbod
 * 
 */
public interface ISoPeCoExtension<EA extends ISoPeCoExtensionArtifact> {

	/**
	 * Returns the name of the extension which is expected to be unique in the
	 * framework.
	 * 
	 * The name is expected to be specific to the extension that is provided,
	 * for example 'MARS' or 'GP'.
	 * 
	 * @return the name of this extension
	 */
	String getName();

	/**
	 * Creates a new artifact for this extension.
	 * 
	 * @return a new artifact for this extension.
	 */
	EA createExtensionArtifact();

	/**
	 * Returns a set of configuration parameters needed by the artifacts of this
	 * extension. The return value is a mapping of parameter names to an
	 * optional default value. It is expected that in most cases the default
	 * values are empty String instances since they depend on the runtime
	 * instance of the artifact.
	 * 
	 * @return a map of configuration parameter names to optional default values
	 */
	Map<String, String> getConfigParameters();
}

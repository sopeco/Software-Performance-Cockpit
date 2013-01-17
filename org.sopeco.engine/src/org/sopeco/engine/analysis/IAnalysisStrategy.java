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
package org.sopeco.engine.analysis;

import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;

/**
 * This interface has to implemented by any analysis strategy applied on a
 * result dataset.
 * 
 * @author Alexander Wert
 * 
 */
public interface IAnalysisStrategy extends ISoPeCoExtensionArtifact {

	/**
	 * Conducts the corresponding analysis implemented by the underlying class
	 * to the given <code>dataset</code> with the given analysis configuration
	 * <code>config</code>.
	 * 
	 * @param dataset
	 *            dataset on which the analysis should be executed
	 * @param config
	 *            configuration for the analysis strategy
	 */
	void analyse(DataSetAggregated dataset, AnalysisConfiguration config);

	/**
	 * Indicates whether the given analysis configuration is supported by this
	 * strategy.
	 * 
	 * @param config
	 *            analysis configuration to be checked
	 * @return <code>true</code>, if the given analysis configuration is
	 *         supported by this strategy, otherwise returns <code>false</code>
	 */
	boolean supports(AnalysisConfiguration config);
}

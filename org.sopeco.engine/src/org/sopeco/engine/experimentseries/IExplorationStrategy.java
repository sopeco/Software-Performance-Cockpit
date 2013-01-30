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
package org.sopeco.engine.experimentseries;

import java.util.List;

import org.sopeco.engine.experiment.IExperimentController;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;

/**
 * Plugin interface for exploration strategies.
 * 
 * @author Dennis Westermann
 * 
 */
public interface IExplorationStrategy extends ISoPeCoExtensionArtifact {

	/**
	 * Indicates whether the given experiment series definition (
	 * {@link ExperimentSeriesDefinition}) is ready to be executed.
	 * 
	 * @param expSeries
	 *            experiment series definition (
	 *            {@link ExperimentSeriesDefinition}) to be checked
	 * @return true if the given experiment series definition fulfills all
	 *         requirements to be executed
	 */
	boolean canRun(ExperimentSeriesDefinition expSeries);

	/**
	 * Executes an experiment series.
	 * 
	 * @param expSeriesRun
	 *            experiment series run where to store results
	 * @param parameterVariations
	 *            list of parameter variations to be used for series execution
	 */
	void runExperimentSeries(ExperimentSeriesRun expSeriesRun, List<IParameterVariation> parameterVariations);

	/**
	 * Sets the persistence provider.
	 * 
	 * @param persistenceProvider
	 *            new persistence provider to be used
	 */
	void setPersistenceProvider(IPersistenceProvider persistenceProvider);

	/**
	 * Sets the experiment controller.
	 * 
	 * @param experimentController
	 *            new experiment controller to be used
	 */
	void setExperimentController(IExperimentController experimentController);

	/**
	 * Sets the extension registry.
	 * 
	 * @param registry
	 *            New registry to be used
	 */
	void setExtensionRegistry(IExtensionRegistry registry);

}

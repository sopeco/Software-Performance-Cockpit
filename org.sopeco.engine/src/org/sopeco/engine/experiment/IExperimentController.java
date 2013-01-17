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
package org.sopeco.engine.experiment;

import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.exceptions.ExperimentFailedException;
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.util.session.ISessionAwareObject;

/**
 * An ExperimentController controls execution of any single experiment.
 * 
 * @author D053711
 * 
 */
public interface IExperimentController extends ISessionAwareObject {

	/**
	 * Acquires the used MEController for measurement execution.
	 */
	void acquireMEController();

	/**
	 * Releases used MEController.
	 */
	void releaseMEController();

	/**
	 * Initializes the experiment controller before the one set of experiments.
	 * 
	 * @param initializationPVs
	 *            initialization arguments
	 * @param meDefinition
	 *            mesearuement environment definition
	 */
	void initialize(ParameterCollection<ParameterValue<?>> initializationPVs,
			MeasurementEnvironmentDefinition meDefinition);

	/**
	 * Prepares the experiment controller for one single experiment series. This
	 * is called before subsequent calls to
	 * {@link #runExperiment(ParameterCollection)}
	 * .
	 * 
	 * @param experimentSeriesRun
	 *            an instance of experiment series run
	 * @param preparationPVs
	 *            preparation arguments
	 */
	public void prepareExperimentSeries(ExperimentSeriesRun experimentSeriesRun, 
			ParameterCollection<ParameterValue<?>> preparationPVs);

	/**
	 * Runs a single experiment with the given arguments. It also appends the
	 * data to the already-persisted data using the persistence provider.
	 * 
	 * @param inputPVs
	 *            a collection of parameter values
	 */
	void runExperiment(ParameterCollection<ParameterValue<?>> inputPVs);

	/**
	 * Finalizes the experiment; a proxy method to the measurement environment
	 * controller.
	 * 
	 * 
	 */
	void finalizeExperimentSeries();

	/**
	 * Returns the single data row that is generated as the result of the last
	 * successful experiment.
	 * 
	 * @return successful result set, if the last experiment was successful, or
	 *         <code>null</code> otherwise.
	 */
	DataSetAggregated getLastSuccessfulExperimentResults();

	/**
	 * Returns the experiment failed exception that is generated as the result
	 * of the last failed experiment.
	 * 
	 * @return an experiment failed exception, if the last experiment failed, or
	 *         <code>null</code> otherwise.
	 */
	ExperimentFailedException getLastFailedExperimentException();
}

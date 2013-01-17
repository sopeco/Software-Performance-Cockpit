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
package org.sopeco.runner;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.AbstractMEController;
import org.sopeco.engine.measurementenvironment.InputParameter;
import org.sopeco.engine.measurementenvironment.ObservationParameter;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.util.Tools;

/**
 * Dummy measurement environment controller that logs the input parameter values
 * passed to it and returns random values for the observation parameters. Class
 * can be used for test and debug purposes.
 * 
 * @author Dennis Westermann
 * 
 */
public class DummyMEController extends AbstractMEController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyMEController.class);

	@InputParameter(namespace = "init")
	private int initParameter;
	@InputParameter(namespace = "prepare")
	private int prepareParameter;
	@InputParameter(namespace = "test.input")
	private int inputParameter;
	@InputParameter(namespace = "test.input")
	private int inputParameter2;
	@ObservationParameter(namespace = "test.observation")
	private ParameterValueList<Integer> observationParameterOne;
	@ObservationParameter(namespace = "test.observation")
	private ParameterValueList<String> observationParameterTwo;

	@Override
	public void finalizeExperimentSeries() {
		LOGGER.debug("Finalize experiment series.");
	}

	private Object createRandomValue(ParameterDefinition parameter) {
		Random r = new Random(System.nanoTime());
		switch (Tools.SupportedTypes.get(parameter.getType())) {
		case Integer:
			return r.nextInt();
		case Double:
			return r.nextDouble();
		case Boolean:
			return r.nextBoolean();
		case String:
			return "Random_" + r.nextInt();
		default:
			throw new IllegalArgumentException("ParameterType not supported: " + parameter);
		}
	}

	@Override
	protected void initialize() {
		LOGGER.debug("Initialize measurement environment: initParameter = {}", initParameter);

	}

	@Override
	protected void prepareExperimentSeries() {
		LOGGER.debug("Initialize measurement environment: prepareParameter = {}", prepareParameter);

	}

	@Override
	protected void runExperiment() {

		LOGGER.debug("Run experiment: ");
		LOGGER.debug("Initialize measurement environment: inputParameter = {}", inputParameter);

		for (int i = 0; i < getNumberOfRepetitions(); i++) {
			LOGGER.debug("Running repetition {}.", i + 1);
			observationParameterOne.addValue(createRandomValue(observationParameterOne.getParameter()));
			observationParameterTwo.addValue(createRandomValue(observationParameterTwo.getParameter()));
		}
		LOGGER.debug("Finished experiment.");

	}

	@Override
	protected void defineResultSet() {
		addParameterObservationsToResult(observationParameterOne);
		addParameterObservationsToResult(observationParameterTwo);

	}
}

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
package org.sopeco.persistence;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.analysis.AnalysisResultStorageContainer;
import org.sopeco.persistence.entities.analysis.IStorableAnalysisResult;
import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;

/**
 * The factory is used to create new instances of the SoPeCo persistence
 * entities.
 * 
 * @author Dennis Westermann
 * 
 */
public final class EntityFactory {

	private EntityFactory() {

	}

	/**
	 * Creates a new instance of the {@link ScenarioInstance} entity. Sets the
	 * name and measurementEnvironmentUrl according to the given parameters.
	 * 
	 * @param scenarioDefinition
	 * @param measurementEnvironmentUrl
	 * 
	 * @return a new instance of {@link ScenarioInstance} based on the given
	 *         parameters
	 */
	public static ScenarioInstance createScenarioInstance(ScenarioDefinition scenarioDefinition,
			String measurementEnvironmentUrl) {
		ScenarioInstance si = new ScenarioInstance();
		si.setScenarioDefinition(scenarioDefinition);
		si.setMeasurementEnvironmentUrl(measurementEnvironmentUrl);
		return si;
	}

	/**
	 * Creates a new instance of the {@link ExperimentSeries} entity. Sets the
	 * name and experimentDefintion according to the given parameters.
	 * 
	 * @param scenarioInstance
	 * @param expSeriesDefinition
	 * @return a new instance of {@link ExperimentSeries} based on the given
	 *         parameters
	 */
	public static ExperimentSeries createExperimentSeries(ExperimentSeriesDefinition expSeriesDefinition) {
		ExperimentSeries expSeries = new ExperimentSeries();
		expSeries.setName(expSeriesDefinition.getName());
		expSeries.setExperimentSeriesDefinition(expSeriesDefinition);
		return expSeries;
	}

	/**
	 * Creates a new termination condition given the configuration map.
	 * 
	 * @param name the name of the condition
	 * @param config map of param names to values
	 * @return an instance of {@link ExperimentTerminationCondition}
	 */
	public static ExperimentTerminationCondition createTerminationCondition(String name, Map<String, String> config) {
		ExperimentTerminationCondition etc = new ExperimentTerminationCondition(name, "");
		if (config != null) {
			etc.getParametersValues().putAll(config);
		}
		return etc;
	}


	/**
	 * Creates a new instance of the {@link ExperimentSeriesRun} entity. Sets
	 * the timestamp value to the current {@link System.nanoTime()}).
	 * 
	 * @return a new instance of {@link ExperimentSeriesRun} based on the given
	 *         parameters
	 */
	public static ExperimentSeriesRun createExperimentSeriesRun() {
		ExperimentSeriesRun expSeriesRun = new ExperimentSeriesRun();
		expSeriesRun.setTimestamp(System.nanoTime());
		expSeriesRun.setLabel(getTimeStamp());
		return expSeriesRun;
	}

	public static AnalysisResultStorageContainer createAnalysisResultStorageContainer(String resultId,
			IStorableAnalysisResult resultObject) {
		AnalysisResultStorageContainer container = new AnalysisResultStorageContainer();
		container.setResultId(resultId);
		container.setResultObject(resultObject);
		return container;
	}

	public static ScenarioDefinition createScenarioDefinition(String scenarioName) {
		ScenarioDefinition sd = new ScenarioDefinition();
		sd.setScenarioName(scenarioName);
		return sd;
	}

	public static MeasurementEnvironmentDefinition createMeasurementEnvironmentDefinition() {
		MeasurementEnvironmentDefinition med = new MeasurementEnvironmentDefinition();
		return med;
	}

	public static ParameterNamespace createNamespace(String name) {
		ParameterNamespace child = new ParameterNamespace();
		child.setName(name);
		return child;
	}

	public static ParameterDefinition createParameterDefinition(String name, String type, ParameterRole role) {
		ParameterDefinition pd = new ParameterDefinition();
		pd.setName(name);
		pd.setRole(role);
		pd.setType(type);
		return pd;
	}

	public static MeasurementSpecification createMeasurementSpecification(String name) {
		MeasurementSpecification ms = new MeasurementSpecification();
		ms.setName(name);
		return ms;
	}

	public static ExperimentSeriesDefinition createExperimentSeriesDefinition(String name) {
		ExperimentSeriesDefinition esd = new ExperimentSeriesDefinition();
		esd.setName(name);
		return esd;
	}

	public static ExplorationStrategy createExplorationStrategy(String name, Map<String, String> config) {
		ExplorationStrategy es = new ExplorationStrategy();
		es.setName(name);
		if (config != null) {
			es.getConfiguration().putAll(config);
		}
		return es;
	}

	public static AnalysisConfiguration createAnalysisConfiguration(String name, Map<String, String> config) {
		AnalysisConfiguration ac = new AnalysisConfiguration();
		ac.setName(name);
		ac.getConfiguration().putAll(config);
		return ac;
	}

	public static ConstantValueAssignment createConstantValueAssignment(final ParameterDefinition parameter,
			String value) {
		ConstantValueAssignment cva = new ConstantValueAssignment();
		cva.setParameter(parameter);
		cva.setValue(value);
		return cva;
	}

	public static DynamicValueAssignment createDynamicValueAssignment(String name, final ParameterDefinition parameter,
			Map<String, String> config) {
		DynamicValueAssignment dva = new DynamicValueAssignment();
		dva.setParameter(parameter);
		dva.setName(name);
		dva.getConfiguration().putAll(config);
		return dva;
	}

	// TODO remove and replace by Tools.getTimeStamp(...)
	/**
	 * Returns a time stamp of the format "yy.MM.dd - HH:mm" for the given date.
	 * 
	 * @param date
	 */
	public static String getTimeStamp(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd - HH:mm");
		return formatter.format(date);
	}

	/**
	 * Returns a time stamp for the current time and date.
	 * 
	 * @see #getTimeStamp(Date)
	 */
	public static String getTimeStamp() {
		return getTimeStamp(new Date());
	}

}

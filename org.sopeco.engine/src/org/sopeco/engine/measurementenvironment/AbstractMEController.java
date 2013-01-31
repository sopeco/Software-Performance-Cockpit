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
package org.sopeco.engine.measurementenvironment;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.sopeco.engine.measurementenvironment.status.StatusProvider;
import org.sopeco.engine.status.StatusMessage;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.exceptions.ExperimentFailedException;
import org.sopeco.persistence.util.ParameterCollection;
import org.sopeco.util.Tools;

/**
 * This class encapsulates interpretation of ME parameters and scheduling of
 * measurement environment controller jobs.
 * 
 * @author Alexander Wert
 * @author Roozbeh Farahbod
 * 
 */

public abstract class AbstractMEController extends MEControllerResource {
	/**
	 * namespace used per default for the root namespace.
	 */
	private static final String DEFAULT_ROOT_NAMESPACE = "";
	/**
	 * MEDefinition instance specified by the specific MEcontroller.
	 */
	private final MeasurementEnvironmentDefinition meDefinition;
	/**
	 * Mapping from full qualified parameter names to fields (attributes) of the
	 * concrete MEController (all fields including those not explicitely set for
	 * this experiment run)
	 */
	private final Map<String, Field> sopecoParameterFields;

	/**
	 * Mapping from full qualified parameter names to fields (attributes) of the
	 * concrete MEController (only those fields that have been explicitly set
	 * for this run by the user)
	 */
	private final Map<String, Field> currentRunSopecoParameterFields;

	/**
	 * A set of parameter value lists containing all observation values which
	 * should be returned after experiment run execution
	 */
	private List<ParameterValueList<?>> resultSet;

	/**
	 * The set of termination conditions that are configured by the user.
	 */
	protected Set<ExperimentTerminationCondition> configuredTerminationConditions;

	/**
	 * This Constructor is responsible for creating the MEDefinition by
	 * interpreting the attribute annotations.
	 */
	public AbstractMEController() {
		sopecoParameterFields = new HashMap<String, Field>();
		currentRunSopecoParameterFields = new HashMap<String, Field>();
		meDefinition = createMEDefinition();
		resultSet = new ArrayList<ParameterValueList<?>>();
		configuredTerminationConditions = new HashSet<ExperimentTerminationCondition>();
	}

	/**
	 * Adds a list of termination conditions to the ME Controller definition.
	 * NOTE: This method must only be called in the constructor of the ME
	 * Controller, given that the constructor of the super class (
	 * {@link AbstractMEController}) is called.
	 * 
	 * @param conditions
	 *            an array of {@link ExperimentTerminationCondition}
	 */
	protected void addSupportedTerminationConditions(ExperimentTerminationCondition... conditions) {
		if (meDefinition == null)
			throw new RuntimeException(
					"Adding supported termination conditions must be called from the ME Controller constructor.");

		for (ExperimentTerminationCondition tc : conditions) {
			meDefinition.getSupportedTerminationConditions().add(tc);
		}
	}

	@Override
	public synchronized MeasurementEnvironmentDefinition getMEDefinition() throws RemoteException {
		return meDefinition;
	}

	@Override
	protected void initialize(ParameterCollection<ParameterValue<?>> initializationPVs) {
		setParameterValues(initializationPVs);
		initialize();
	}

	@Override
	protected void prepareExperimentSeries(ParameterCollection<ParameterValue<?>> preparationPVs,
			Set<ExperimentTerminationCondition> terminationConditions) {
		setParameterValues(preparationPVs);
		configuredTerminationConditions = terminationConditions;
		prepareExperimentSeries();
	}

	@Override
	protected Collection<ParameterValueList<?>> runExperiment(ParameterCollection<ParameterValue<?>> inputPVs)
			throws ExperimentFailedException {
		cleanUpObservations();
		resultSet.clear();
		setParameterValues(inputPVs);
		runExperiment();
		defineResultSet();
		return resultSet;
	}

	@Override
	protected void cleanUpMEController() {
		resultSet.clear();
		cleanUpInpuValues();
		cleanUpObservations();

	}

	protected void addParameterObservationsToResult(ParameterValueList<?> pvl) {
		resultSet.add(pvl);
	}

	/**
	 * Returns the values for all input parameters.
	 * 
	 * @return values for all input parameters
	 */
	protected Map<String, Object> getInputValues() {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			for (Entry<String, Field> entry : sopecoParameterFields.entrySet()) {
				if (entry.getValue().isAnnotationPresent(InputParameter.class)) {
					result.put(entry.getKey(), entry.getValue().get(this));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to read value from input parameter!");
		}
		return result;
	}

	/**
	 * Returns the values for those input parameters that have been explicitly
	 * set by the user for this run..
	 * 
	 * @return values for all input parameters
	 */
	protected Map<String, Object> getCurrentRunInputValues() {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			for (Entry<String, Field> entry : currentRunSopecoParameterFields.entrySet()) {
				if (entry.getValue().isAnnotationPresent(InputParameter.class)) {
					result.put(entry.getKey(), entry.getValue().get(this));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to read value from input parameter!");
		}
		return result;
	}

	/**
	 * Returns the values for all input parameters within the given namespace.
	 * 
	 * @param namespace
	 *            namespace specifying the parameters for which the values
	 *            should be returned.
	 * @return the values for all input parameters within the given namespace.
	 */
	protected Map<String, Object> getInputValues(String namespace) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			for (Entry<String, Field> entry : sopecoParameterFields.entrySet()) {
				if (entry.getValue().isAnnotationPresent(InputParameter.class) && entry.getKey().startsWith(namespace)) {
					result.put(entry.getKey(), entry.getValue().get(this));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to read value from input parameter!");
		}
		return result;
	}

	/**
	 * Reads the parameter values and assignes them to the correct MEControlelr
	 * fields.
	 * 
	 * @param parameterValues
	 *            set of input parameter values
	 */
	private void setParameterValues(ParameterCollection<ParameterValue<?>> parameterValues) {

		currentRunSopecoParameterFields.clear();
		for (ParameterValue pv : parameterValues) {
			ParameterDefinition parameter = pv.getParameter();
			Field parameterField = sopecoParameterFields.get(parameter.getFullName());
			if (parameterField == null) {
				throw new RuntimeException("Failed setting parameter value. Parameter name " + parameter.getFullName()
						+ " is not known to this measurement environment controller!");
			}
			try {
				parameterField.set(this, pv.getValue());
			} catch (Exception e) {
				throw new RuntimeException("Failed setting parameter value for Parameter " + parameter.getFullName(), e);
			}

			currentRunSopecoParameterFields.put(parameter.getFullName(), parameterField);
		}

	}

	/**
	 * Creates the model for the measurement environment definition by
	 * interpreting annotations ({@link InputParameter}) of class attributes.
	 */
	private MeasurementEnvironmentDefinition createMEDefinition() {
		MeasurementEnvironmentDefinition meDef = EntityFactory.createMeasurementEnvironmentDefinition();
		meDef.setRoot(EntityFactory.createNamespace(DEFAULT_ROOT_NAMESPACE));

		for (Field field : getAllParameterFields()) {
			field.setAccessible(true);
			ParameterRole role = getParameterRole(field);
			String name = getParameterName(field);
			String type = getParameterType(field);
			String namespace = getParameterNamespace(field);

			ParameterDefinition newParameter = addParameterToMEDefinition(name, namespace, role, type, meDef.getRoot());

			sopecoParameterFields.put(newParameter.getFullName(), field);

			// for all observation parameters: create the parameter
			// value list
			if (role.equals(ParameterRole.OBSERVATION)) {
				try {
					field.set(this, new ParameterValueList(newParameter));
				} catch (Exception e) {
					throw new RuntimeException("Failed creating ParametervalueList for parameter "
							+ newParameter.getFullName(), e);
				}
			}
		}

		return meDef;
	}

	/**
	 * Returns the list of fields that define input parameter and observation
	 * parameters.
	 * 
	 * @return list of input/observation parameter fields
	 */
	protected List<Field> getAllParameterFields() {
		List<Field> fields = new ArrayList<Field>();

		Class clazz = this.getClass();
		while (!clazz.equals(AbstractMEController.class)) {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.isAnnotationPresent(InputParameter.class)
						|| field.isAnnotationPresent(ObservationParameter.class)) {
					fields.add(field);
				}
			}
			clazz = clazz.getSuperclass();
		}

		return fields;
	}

	protected String getParameterNamespace(Field field) {
		if (field.isAnnotationPresent(InputParameter.class)) {
			return field.getAnnotation(InputParameter.class).namespace();
		} else if (field.isAnnotationPresent(ObservationParameter.class)) {
			return field.getAnnotation(ObservationParameter.class).namespace();
		}
		throw new RuntimeException("Unsupported parameter role.");
	}

	protected ParameterRole getParameterRole(Field field) {
		if (field.isAnnotationPresent(InputParameter.class)) {
			return ParameterRole.INPUT;
		} else if (field.isAnnotationPresent(ObservationParameter.class)) {
			return ParameterRole.OBSERVATION;
		}
		throw new RuntimeException("Unsupported parameter role.");
	}

	protected String getParameterName(Field field) {
		if (field.isAnnotationPresent(InputParameter.class)) {
			InputParameter spcAnn = field.getAnnotation(InputParameter.class);

			if (spcAnn.name().equals(InputParameter.DEFAULT)) {
				return field.getName();
			} else {
				return spcAnn.name();
			}
		} else if (field.isAnnotationPresent(ObservationParameter.class)) {
			ObservationParameter spcAnn = field.getAnnotation(ObservationParameter.class);

			if (spcAnn.name().equals(InputParameter.DEFAULT)) {
				return field.getName();
			} else {
				return spcAnn.name();
			}
		}
		throw new RuntimeException("Unsupported parameter role.");
	}

	protected String getParameterType(Field field) {
		if (field.isAnnotationPresent(InputParameter.class)) {
			return interpreteType(field.getType());
		} else if (field.isAnnotationPresent(ObservationParameter.class)) {
			if (!(field.getGenericType() instanceof ParameterizedType)
					|| !(field.getType().equals(ParameterValueList.class))) {
				throw new RuntimeException(
						"Invalid type of observation parameter. Observation parameters have to be of type ParameterValueList<T>! ");
			}

			ParameterizedType genericType = (ParameterizedType) field.getGenericType();
			return interpreteType(genericType.getActualTypeArguments()[0]);
		}
		throw new RuntimeException("Unsupported parameter role.");
	}

	/**
	 * Interprets the type of the given class and returns the corresponding
	 * string representation used for SoPeCo Parameter types. Throws a runtime
	 * exception if the given type is not supported.
	 * 
	 * @param type
	 *            a given type
	 */
	private String interpreteType(Type type) {
		if (type instanceof Class) {
			Tools.SupportedTypes sType = Tools.SupportedTypes.get(((Class) type).getSimpleName());

			if (sType != null) {
				return sType.toString();
			}
		}

		// else
		throw new RuntimeException("Unsupported parameter type for input parameter! "
				+ "Supported values are Integer, Double, Boolean or String");
	}

	private ParameterDefinition addParameterToMEDefinition(String parameterName, String fullnamespace,
			ParameterRole role, String type, ParameterNamespace parent) {
		ParameterDefinition newParameter = EntityFactory.createParameterDefinition(parameterName, type, role);
		if (fullnamespace.startsWith(ParameterNamespace.DEFAULT_NAMESPACE_DELIMITER)
				|| fullnamespace.endsWith(ParameterNamespace.DEFAULT_NAMESPACE_DELIMITER)) {
			throw new RuntimeException("Invalid namespace name: " + fullnamespace
					+ " A namespace must not begin or end with a dot!");
		}
		return findNameSpaceToAddParameter(fullnamespace, parent, newParameter);
	}

	/**
	 * Tries to find recursively an existing namespace for the target namespace
	 * where to put the new parameter. If no existing namespace can be found a
	 * new one is created.
	 * 
	 */
	private ParameterDefinition findNameSpaceToAddParameter(String targetNamespace,
			ParameterNamespace currentNamespace, ParameterDefinition parameter) {
		int index = targetNamespace.indexOf(ParameterNamespace.DEFAULT_NAMESPACE_DELIMITER);
		String nsPrefix = null;
		String nsSuffix = null;
		if (index >= 0) {
			nsPrefix = targetNamespace.substring(0, index);
			nsSuffix = targetNamespace.substring(index + 1, targetNamespace.length());
		} else {
			nsPrefix = targetNamespace;
		}

		ParameterNamespace nextNamespace = findChildNamespace(currentNamespace, nsPrefix);
		if (nextNamespace == null) {
			nextNamespace = EntityFactory.createNamespace(nsPrefix);
			nextNamespace.setParent(currentNamespace);
			currentNamespace.getChildren().add(nextNamespace);
		}

		if (nsSuffix != null) {
			return findNameSpaceToAddParameter(nsSuffix, nextNamespace, parameter);
		} else {
			nextNamespace.getParameters().add(parameter);
			parameter.setNamespace(nextNamespace);
			return parameter;
		}
	}

	private ParameterNamespace findChildNamespace(ParameterNamespace currentNamespace, String nsPrefix) {
		for (ParameterNamespace pns : currentNamespace.getChildren()) {
			if (pns.getName().equals(nsPrefix)) {
				return pns;
			}
		}
		return null;
	}

	protected void cleanUpObservations() {
		for (ParameterDefinition observationParameter : meDefinition.getRoot().getObservationParameters()) {
			Field obsParField = sopecoParameterFields.get(observationParameter.getFullName());
			try {
				obsParField.set(this, new ParameterValueList(observationParameter));
			} catch (Exception e) {
				throw new RuntimeException("Failed setting parameter value.");
			}
		}
	}

	private void cleanUpInpuValues() {
		try {
			for (ParameterDefinition parameter : meDefinition.getRoot().getAllParameters()) {
				if (parameter.getRole().equals(ParameterRole.INPUT)) {
					Field inputField = sopecoParameterFields.get(parameter.getFullName());

					switch (Tools.SupportedTypes.get(parameter.getType())) {
					case Boolean:
						inputField.set(this, false);
						break;
					case Double:
						inputField.set(this, 0.0);
						break;
					case Integer:
						inputField.set(this, 0);
						break;
					case String:
						inputField.set(this, null);
						break;

					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed cleaning up input parameters!", e);
		}
	}

	/**
	 * This method should define which ParameterValueList should be included
	 * into the result set of an experiment run. Use
	 * <code>addParameterObservationsToResult</code> method to add a PVL to the
	 * result set.
	 */
	protected abstract void defineResultSet();

	/**
	 * This method should encapsulate the initialization routine of the specific
	 * MEController.
	 */
	protected abstract void initialize();

	/**
	 * This method should contain preparation tasks of an experiment series of a
	 * concrete MEController.
	 */
	protected abstract void prepareExperimentSeries();

	/**
	 * This method should contain the execution logic of an experiment within a
	 * concrete MEController.
	 */
	protected abstract void runExperiment() throws ExperimentFailedException;

	/**
	 * This method should encapsulate the finalization routine for the
	 * experiment series of the specific MEController.
	 */
	protected abstract void finalizeExperimentSeries();

	protected Set<ExperimentTerminationCondition> getConfiguredTerminationConditions() {
		return configuredTerminationConditions;
	}

	/**
	 * If a number of repetitions termination condition is defined by the
	 * scenario, it returns the set value; otherwise returns the default value
	 * of {@value AbstractMEController#DEFAULT_NUMBER_OF_REPS}.
	 * 
	 * @return the set number of repetitions or its default value
	 * 
	 * @see ExperimentTerminationCondition#getNumberOfRepetitions(Collection)
	 */
	public Integer getNumberOfRepetitions() {
		return ExperimentTerminationCondition.getNumberOfRepetitions(configuredTerminationConditions);
	}

	@Override
	public List<StatusMessage> fetchStatusMessages() {
		List<StatusMessage> result = StatusProvider.getProvider(this).getNotFetched();
		if (result == null) {
			result = new ArrayList<StatusMessage>();
		}
		return result;
	}
}

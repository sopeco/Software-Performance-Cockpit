package org.sopeco.engine.measurementenvironment;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.ParameterValueList;
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
	 * concrete MEController
	 */
	private final Map<String, Field> sopecoParameterFields;

	/**
	 * A set of parameter value lists containing all observation values which
	 * should be returned after experiment run execution
	 */
	private List<ParameterValueList<?>> resultSet;

	/**
	 * This Constructor is responsible for creating the MEDefinition by
	 * interpreting the attribute annotations.
	 */
	public AbstractMEController() {
		sopecoParameterFields = new HashMap<String, Field>();
		meDefinition = createMEDefinition();
		resultSet = new ArrayList<ParameterValueList<?>>();
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
	protected void prepareExperimentSeries(ParameterCollection<ParameterValue<?>> preparationPVs) {
		setParameterValues(preparationPVs);
		prepareExperimentSeries();
	}

	@Override
	protected Collection<ParameterValueList<?>> runExperiment(ParameterCollection<ParameterValue<?>> inputPVs) throws ExperimentFailedException {
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
		}

	}

	/**
	 * Creates the model for the measurement environment definition by
	 * interpreting annotations ({@link InputParameter}) of class attributes.
	 */
	private MeasurementEnvironmentDefinition createMEDefinition() {
		MeasurementEnvironmentDefinition meDef = EntityFactory.createMeasurementEnvironmentDefinition();
		meDef.setRoot(EntityFactory.createNamespace(DEFAULT_ROOT_NAMESPACE));

		Class clazz = this.getClass();
		while (!clazz.equals(AbstractMEController.class)) {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.isAnnotationPresent(InputParameter.class)
						|| field.isAnnotationPresent(ObservationParameter.class)) {
					field.setAccessible(true);
					ParameterRole role = getParameterRole(field);
					String name = getParameterName(field);
					String type = getParameterType(field);
					String namespace = getParameterNamespace(field);

					ParameterDefinition newParameter = addParameterToMEDefinition(name, namespace, role, type,
							meDef.getRoot());

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

			}
			clazz = clazz.getSuperclass();
		}
		return meDef;

	}

	private String getParameterNamespace(Field field) {
		if (field.isAnnotationPresent(InputParameter.class)) {
			return field.getAnnotation(InputParameter.class).namespace();
		} else if (field.isAnnotationPresent(ObservationParameter.class)) {
			return field.getAnnotation(ObservationParameter.class).namespace();
		}
		throw new RuntimeException("Unsupported parameter role.");
	}

	private ParameterRole getParameterRole(Field field) {
		if (field.isAnnotationPresent(InputParameter.class)) {
			return ParameterRole.INPUT;
		} else if (field.isAnnotationPresent(ObservationParameter.class)) {
			return ParameterRole.OBSERVATION;
		}
		throw new RuntimeException("Unsupported parameter role.");
	}

	private String getParameterName(Field field) {
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

	private String getParameterType(Field field) {
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
	 * @param type a given type
	 */
	private String interpreteType(Type type) {
		if (type instanceof Class) {
			Tools.SupportedTypes sType = Tools.SupportedTypes.get(((Class)type).getSimpleName());
		
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

}

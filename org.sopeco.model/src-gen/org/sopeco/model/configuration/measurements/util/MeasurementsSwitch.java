/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.measurements.util;

import java.io.Serializable;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.sopeco.model.configuration.measurements.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage
 * @generated
 */
public class MeasurementsSwitch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static MeasurementsPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MeasurementsSwitch() {
		if (modelPackage == null) {
			modelPackage = MeasurementsPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public T doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		else {
			List<EClass> eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject) :
					doSwitch(eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case MeasurementsPackage.MEASUREMENT_SPECIFICATION: {
				MeasurementSpecification measurementSpecification = (MeasurementSpecification)theEObject;
				T result = caseMeasurementSpecification(measurementSpecification);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION: {
				ExperimentSeriesDefinition experimentSeriesDefinition = (ExperimentSeriesDefinition)theEObject;
				T result = caseExperimentSeriesDefinition(experimentSeriesDefinition);
				if (result == null) result = caseSerializable(experimentSeriesDefinition);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MeasurementsPackage.PARAMETER_VALUE_ASSIGNMENT: {
				ParameterValueAssignment parameterValueAssignment = (ParameterValueAssignment)theEObject;
				T result = caseParameterValueAssignment(parameterValueAssignment);
				if (result == null) result = caseSerializable(parameterValueAssignment);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MeasurementsPackage.EXPLORATION_STRATEGY: {
				ExplorationStrategy explorationStrategy = (ExplorationStrategy)theEObject;
				T result = caseExplorationStrategy(explorationStrategy);
				if (result == null) result = caseExtensibleElement(explorationStrategy);
				if (result == null) result = caseSerializable(explorationStrategy);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MeasurementsPackage.EXPERIMENT_TERMINATION_CONDITION: {
				ExperimentTerminationCondition experimentTerminationCondition = (ExperimentTerminationCondition)theEObject;
				T result = caseExperimentTerminationCondition(experimentTerminationCondition);
				if (result == null) result = caseSerializable(experimentTerminationCondition);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MeasurementsPackage.CONFIGURATION_NODE: {
				@SuppressWarnings("unchecked") Map.Entry<String, String> configurationNode = (Map.Entry<String, String>)theEObject;
				T result = caseConfigurationNode(configurationNode);
				if (result == null) result = caseSerializable((Serializable)configurationNode);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MeasurementsPackage.EXTENSIBLE_ELEMENT: {
				ExtensibleElement extensibleElement = (ExtensibleElement)theEObject;
				T result = caseExtensibleElement(extensibleElement);
				if (result == null) result = caseSerializable(extensibleElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MeasurementsPackage.NUMBER_OF_REPETITIONS: {
				NumberOfRepetitions numberOfRepetitions = (NumberOfRepetitions)theEObject;
				T result = caseNumberOfRepetitions(numberOfRepetitions);
				if (result == null) result = caseExperimentTerminationCondition(numberOfRepetitions);
				if (result == null) result = caseSerializable(numberOfRepetitions);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MeasurementsPackage.TIME_OUT: {
				TimeOut timeOut = (TimeOut)theEObject;
				T result = caseTimeOut(timeOut);
				if (result == null) result = caseExperimentTerminationCondition(timeOut);
				if (result == null) result = caseSerializable(timeOut);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MeasurementsPackage.CONSTANT_VALUE_ASSIGNMENT: {
				ConstantValueAssignment constantValueAssignment = (ConstantValueAssignment)theEObject;
				T result = caseConstantValueAssignment(constantValueAssignment);
				if (result == null) result = caseParameterValueAssignment(constantValueAssignment);
				if (result == null) result = caseSerializable(constantValueAssignment);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MeasurementsPackage.DYNAMIC_VALUE_ASSIGNMENT: {
				DynamicValueAssignment dynamicValueAssignment = (DynamicValueAssignment)theEObject;
				T result = caseDynamicValueAssignment(dynamicValueAssignment);
				if (result == null) result = caseExtensibleElement(dynamicValueAssignment);
				if (result == null) result = caseParameterValueAssignment(dynamicValueAssignment);
				if (result == null) result = caseSerializable(dynamicValueAssignment);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Measurement Specification</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Measurement Specification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMeasurementSpecification(MeasurementSpecification object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Experiment Series Definition</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Experiment Series Definition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExperimentSeriesDefinition(ExperimentSeriesDefinition object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Parameter Value Assignment</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Parameter Value Assignment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseParameterValueAssignment(ParameterValueAssignment object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Exploration Strategy</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Exploration Strategy</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExplorationStrategy(ExplorationStrategy object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Experiment Termination Condition</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Experiment Termination Condition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExperimentTerminationCondition(ExperimentTerminationCondition object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Configuration Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Configuration Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConfigurationNode(Map.Entry<String, String> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Extensible Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Extensible Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExtensibleElement(ExtensibleElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Number Of Repetitions</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Number Of Repetitions</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNumberOfRepetitions(NumberOfRepetitions object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Time Out</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Time Out</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTimeOut(TimeOut object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Constant Value Assignment</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Constant Value Assignment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConstantValueAssignment(ConstantValueAssignment object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Dynamic Value Assignment</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Dynamic Value Assignment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDynamicValueAssignment(DynamicValueAssignment object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Serializable</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Serializable</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSerializable(Serializable object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public T defaultCase(EObject object) {
		return null;
	}

} //MeasurementsSwitch

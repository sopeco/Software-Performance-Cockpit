/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.measurements;

import java.io.Serializable;

import org.eclipse.emf.common.util.EList;

import org.sopeco.model.configuration.common.ext.ISerializableEObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Experiment Series Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getExplorationStrategy <em>Exploration Strategy</em>}</li>
 *   <li>{@link org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getExperimentAssignments <em>Experiment Assignments</em>}</li>
 *   <li>{@link org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getExperimentTerminationCondition <em>Experiment Termination Condition</em>}</li>
 *   <li>{@link org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getPreperationAssignments <em>Preperation Assignments</em>}</li>
 *   <li>{@link org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage#getExperimentSeriesDefinition()
 * @model superTypes="org.sopeco.model.configuration.common.Serializable"
 * @extends ISerializableEObject
 * @generated
 */
public interface ExperimentSeriesDefinition extends ISerializableEObject, Serializable {
	/**
	 * Returns the value of the '<em><b>Exploration Strategy</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exploration Strategy</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exploration Strategy</em>' containment reference.
	 * @see #setExplorationStrategy(ExplorationStrategy)
	 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage#getExperimentSeriesDefinition_ExplorationStrategy()
	 * @model containment="true" required="true"
	 * @generated
	 */
	ExplorationStrategy getExplorationStrategy();

	/**
	 * Sets the value of the '{@link org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getExplorationStrategy <em>Exploration Strategy</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Exploration Strategy</em>' containment reference.
	 * @see #getExplorationStrategy()
	 * @generated
	 */
	void setExplorationStrategy(ExplorationStrategy value);

	/**
	 * Returns the value of the '<em><b>Experiment Assignments</b></em>' containment reference list.
	 * The list contents are of type {@link org.sopeco.model.configuration.measurements.ParameterValueAssignment}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Experiment Assignments</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Experiment Assignments</em>' containment reference list.
	 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage#getExperimentSeriesDefinition_ExperimentAssignments()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<ParameterValueAssignment> getExperimentAssignments();

	/**
	 * Returns the value of the '<em><b>Experiment Termination Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Experiment Termination Condition</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Experiment Termination Condition</em>' containment reference.
	 * @see #setExperimentTerminationCondition(ExperimentTerminationCondition)
	 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage#getExperimentSeriesDefinition_ExperimentTerminationCondition()
	 * @model containment="true" required="true"
	 * @generated
	 */
	ExperimentTerminationCondition getExperimentTerminationCondition();

	/**
	 * Sets the value of the '{@link org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getExperimentTerminationCondition <em>Experiment Termination Condition</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Experiment Termination Condition</em>' containment reference.
	 * @see #getExperimentTerminationCondition()
	 * @generated
	 */
	void setExperimentTerminationCondition(ExperimentTerminationCondition value);

	/**
	 * Returns the value of the '<em><b>Preperation Assignments</b></em>' containment reference list.
	 * The list contents are of type {@link org.sopeco.model.configuration.measurements.ConstantValueAssignment}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Preperation Assignments</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Preperation Assignments</em>' containment reference list.
	 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage#getExperimentSeriesDefinition_PreperationAssignments()
	 * @model containment="true"
	 * @generated
	 */
	EList<ConstantValueAssignment> getPreperationAssignments();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage#getExperimentSeriesDefinition_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // ExperimentSeriesDefinition

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.core.model.configuration.measurements;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Experiment Series Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.sopeco.core.model.configuration.measurements.ExperimentSeriesDefinition#getExplorationStrategy <em>Exploration Strategy</em>}</li>
 *   <li>{@link org.sopeco.core.model.configuration.measurements.ExperimentSeriesDefinition#getExperimentAssignments <em>Experiment Assignments</em>}</li>
 *   <li>{@link org.sopeco.core.model.configuration.measurements.ExperimentSeriesDefinition#getExperimentTerminationCondition <em>Experiment Termination Condition</em>}</li>
 *   <li>{@link org.sopeco.core.model.configuration.measurements.ExperimentSeriesDefinition#getPreperationAssignments <em>Preperation Assignments</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.sopeco.core.model.configuration.measurements.MeasurementsPackage#getExperimentSeriesDefinition()
 * @model
 * @generated
 */
public interface ExperimentSeriesDefinition extends EObject {
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
	 * @see org.sopeco.core.model.configuration.measurements.MeasurementsPackage#getExperimentSeriesDefinition_ExplorationStrategy()
	 * @model containment="true" required="true"
	 * @generated
	 */
	ExplorationStrategy getExplorationStrategy();

	/**
	 * Sets the value of the '{@link org.sopeco.core.model.configuration.measurements.ExperimentSeriesDefinition#getExplorationStrategy <em>Exploration Strategy</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Exploration Strategy</em>' containment reference.
	 * @see #getExplorationStrategy()
	 * @generated
	 */
	void setExplorationStrategy(ExplorationStrategy value);

	/**
	 * Returns the value of the '<em><b>Experiment Assignments</b></em>' containment reference list.
	 * The list contents are of type {@link org.sopeco.core.model.configuration.measurements.ParameterValueAssignment}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Experiment Assignments</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Experiment Assignments</em>' containment reference list.
	 * @see org.sopeco.core.model.configuration.measurements.MeasurementsPackage#getExperimentSeriesDefinition_ExperimentAssignments()
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
	 * @see org.sopeco.core.model.configuration.measurements.MeasurementsPackage#getExperimentSeriesDefinition_ExperimentTerminationCondition()
	 * @model containment="true" required="true"
	 * @generated
	 */
	ExperimentTerminationCondition getExperimentTerminationCondition();

	/**
	 * Sets the value of the '{@link org.sopeco.core.model.configuration.measurements.ExperimentSeriesDefinition#getExperimentTerminationCondition <em>Experiment Termination Condition</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Experiment Termination Condition</em>' containment reference.
	 * @see #getExperimentTerminationCondition()
	 * @generated
	 */
	void setExperimentTerminationCondition(ExperimentTerminationCondition value);

	/**
	 * Returns the value of the '<em><b>Preperation Assignments</b></em>' containment reference list.
	 * The list contents are of type {@link org.sopeco.core.model.configuration.measurements.ConstantValueAssignment}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Preperation Assignments</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Preperation Assignments</em>' containment reference list.
	 * @see org.sopeco.core.model.configuration.measurements.MeasurementsPackage#getExperimentSeriesDefinition_PreperationAssignments()
	 * @model containment="true"
	 * @generated
	 */
	EList<ConstantValueAssignment> getPreperationAssignments();

} // ExperimentSeriesDefinition

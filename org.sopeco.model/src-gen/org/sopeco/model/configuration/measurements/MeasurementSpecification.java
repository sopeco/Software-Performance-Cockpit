/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.measurements;

import org.eclipse.emf.common.util.EList;

import org.sopeco.model.configuration.common.ext.ISerializableEObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Measurement Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.sopeco.model.configuration.measurements.MeasurementSpecification#getExperimentSeriesDefinitions <em>Experiment Series Definitions</em>}</li>
 *   <li>{@link org.sopeco.model.configuration.measurements.MeasurementSpecification#getInitializationAssignemts <em>Initialization Assignemts</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage#getMeasurementSpecification()
 * @model
 * @extends ISerializableEObject
 * @generated
 */
public interface MeasurementSpecification extends ISerializableEObject {
	/**
	 * Returns the value of the '<em><b>Experiment Series Definitions</b></em>' containment reference list.
	 * The list contents are of type {@link org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Experiment Series Definitions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Experiment Series Definitions</em>' containment reference list.
	 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage#getMeasurementSpecification_ExperimentSeriesDefinitions()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<ExperimentSeriesDefinition> getExperimentSeriesDefinitions();

	/**
	 * Returns the value of the '<em><b>Initialization Assignemts</b></em>' containment reference list.
	 * The list contents are of type {@link org.sopeco.model.configuration.measurements.ConstantValueAssignment}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initialization Assignemts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initialization Assignemts</em>' containment reference list.
	 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage#getMeasurementSpecification_InitializationAssignemts()
	 * @model containment="true"
	 * @generated
	 */
	EList<ConstantValueAssignment> getInitializationAssignemts();

} // MeasurementSpecification
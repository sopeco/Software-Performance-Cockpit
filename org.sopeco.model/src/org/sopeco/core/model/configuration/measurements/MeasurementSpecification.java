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
 * A representation of the model object '<em><b>Measurement Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.sopeco.core.model.configuration.measurements.MeasurementSpecification#getExperimentSeriesDefintions <em>Experiment Series Defintions</em>}</li>
 *   <li>{@link org.sopeco.core.model.configuration.measurements.MeasurementSpecification#getInitializationAssignemts <em>Initialization Assignemts</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.sopeco.core.model.configuration.measurements.MeasurementsPackage#getMeasurementSpecification()
 * @model
 * @generated
 */
public interface MeasurementSpecification extends EObject {
	/**
	 * Returns the value of the '<em><b>Experiment Series Defintions</b></em>' containment reference list.
	 * The list contents are of type {@link org.sopeco.core.model.configuration.measurements.ExperimentSeriesDefinition}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Experiment Series Defintions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Experiment Series Defintions</em>' containment reference list.
	 * @see org.sopeco.core.model.configuration.measurements.MeasurementsPackage#getMeasurementSpecification_ExperimentSeriesDefintions()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<ExperimentSeriesDefinition> getExperimentSeriesDefintions();

	/**
	 * Returns the value of the '<em><b>Initialization Assignemts</b></em>' containment reference list.
	 * The list contents are of type {@link org.sopeco.core.model.configuration.measurements.ConstantValueAssignment}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initialization Assignemts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initialization Assignemts</em>' containment reference list.
	 * @see org.sopeco.core.model.configuration.measurements.MeasurementsPackage#getMeasurementSpecification_InitializationAssignemts()
	 * @model containment="true"
	 * @generated
	 */
	EList<ConstantValueAssignment> getInitializationAssignemts();

} // MeasurementSpecification

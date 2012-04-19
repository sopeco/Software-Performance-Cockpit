/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration;

import org.sopeco.model.configuration.common.ext.ISerializableEObject;

import org.sopeco.model.configuration.environment.MeasurementEnvironmentDefinition;

import org.sopeco.model.configuration.measurements.MeasurementSpecification;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scenario Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.sopeco.model.configuration.ScenarioDefinition#getName <em>Name</em>}</li>
 *   <li>{@link org.sopeco.model.configuration.ScenarioDefinition#getMeasurementEnvironmentDefinition <em>Measurement Environment Definition</em>}</li>
 *   <li>{@link org.sopeco.model.configuration.ScenarioDefinition#getMeasurementSpecification <em>Measurement Specification</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.sopeco.model.configuration.ConfigurationPackage#getScenarioDefinition()
 * @model
 * @extends ISerializableEObject
 * @generated
 */
public interface ScenarioDefinition extends ISerializableEObject {
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
	 * @see org.sopeco.model.configuration.ConfigurationPackage#getScenarioDefinition_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.sopeco.model.configuration.ScenarioDefinition#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Measurement Environment Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Measurement Environment Definition</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Measurement Environment Definition</em>' containment reference.
	 * @see #setMeasurementEnvironmentDefinition(MeasurementEnvironmentDefinition)
	 * @see org.sopeco.model.configuration.ConfigurationPackage#getScenarioDefinition_MeasurementEnvironmentDefinition()
	 * @model containment="true" required="true"
	 * @generated
	 */
	MeasurementEnvironmentDefinition getMeasurementEnvironmentDefinition();

	/**
	 * Sets the value of the '{@link org.sopeco.model.configuration.ScenarioDefinition#getMeasurementEnvironmentDefinition <em>Measurement Environment Definition</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Measurement Environment Definition</em>' containment reference.
	 * @see #getMeasurementEnvironmentDefinition()
	 * @generated
	 */
	void setMeasurementEnvironmentDefinition(MeasurementEnvironmentDefinition value);

	/**
	 * Returns the value of the '<em><b>Measurement Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Measurement Specification</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Measurement Specification</em>' containment reference.
	 * @see #setMeasurementSpecification(MeasurementSpecification)
	 * @see org.sopeco.model.configuration.ConfigurationPackage#getScenarioDefinition_MeasurementSpecification()
	 * @model containment="true" required="true"
	 * @generated
	 */
	MeasurementSpecification getMeasurementSpecification();

	/**
	 * Sets the value of the '{@link org.sopeco.model.configuration.ScenarioDefinition#getMeasurementSpecification <em>Measurement Specification</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Measurement Specification</em>' containment reference.
	 * @see #getMeasurementSpecification()
	 * @generated
	 */
	void setMeasurementSpecification(MeasurementSpecification value);

} // ScenarioDefinition

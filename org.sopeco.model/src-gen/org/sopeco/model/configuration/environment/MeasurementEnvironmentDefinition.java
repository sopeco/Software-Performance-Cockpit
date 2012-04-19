/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.environment;

import java.io.Serializable;

import org.sopeco.model.configuration.common.ext.ISerializableEObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Measurement Environment Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.sopeco.model.configuration.environment.MeasurementEnvironmentDefinition#getRoot <em>Root</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.sopeco.model.configuration.environment.EnvironmentPackage#getMeasurementEnvironmentDefinition()
 * @model superTypes="org.sopeco.model.configuration.common.Serializable"
 * @extends ISerializableEObject
 * @generated
 */
public interface MeasurementEnvironmentDefinition extends ISerializableEObject, Serializable {
	/**
	 * Returns the value of the '<em><b>Root</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Root</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Root</em>' containment reference.
	 * @see #setRoot(ParameterNamespace)
	 * @see org.sopeco.model.configuration.environment.EnvironmentPackage#getMeasurementEnvironmentDefinition_Root()
	 * @model containment="true" required="true"
	 * @generated
	 */
	ParameterNamespace getRoot();

	/**
	 * Sets the value of the '{@link org.sopeco.model.configuration.environment.MeasurementEnvironmentDefinition#getRoot <em>Root</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Root</em>' containment reference.
	 * @see #getRoot()
	 * @generated
	 */
	void setRoot(ParameterNamespace value);

} // MeasurementEnvironmentDefinition

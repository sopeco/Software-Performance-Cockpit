/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.measurements;

import java.io.Serializable;

import org.sopeco.model.configuration.common.ext.ISerializableEObject;

import org.sopeco.model.configuration.environment.ParameterDefinition;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter Value Assignment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.sopeco.model.configuration.measurements.ParameterValueAssignment#getParameter <em>Parameter</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage#getParameterValueAssignment()
 * @model abstract="true" superTypes="org.sopeco.model.configuration.common.Serializable"
 * @extends ISerializableEObject
 * @generated
 */
public interface ParameterValueAssignment extends ISerializableEObject, Serializable {
	/**
	 * Returns the value of the '<em><b>Parameter</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter</em>' reference.
	 * @see #setParameter(ParameterDefinition)
	 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage#getParameterValueAssignment_Parameter()
	 * @model required="true"
	 * @generated
	 */
	ParameterDefinition getParameter();

	/**
	 * Sets the value of the '{@link org.sopeco.model.configuration.measurements.ParameterValueAssignment#getParameter <em>Parameter</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter</em>' reference.
	 * @see #getParameter()
	 * @generated
	 */
	void setParameter(ParameterDefinition value);

} // ParameterValueAssignment
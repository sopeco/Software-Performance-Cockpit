/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.measurements;

import java.io.Serializable;

import org.eclipse.emf.common.util.EMap;

import org.sopeco.model.configuration.common.ext.ISerializableEObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Extensible Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.sopeco.model.configuration.measurements.ExtensibleElement#getConfiguration <em>Configuration</em>}</li>
 *   <li>{@link org.sopeco.model.configuration.measurements.ExtensibleElement#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage#getExtensibleElement()
 * @model abstract="true" superTypes="org.sopeco.model.configuration.common.Serializable"
 * @extends ISerializableEObject
 * @generated
 */
public interface ExtensibleElement extends ISerializableEObject, Serializable {
	/**
	 * Returns the value of the '<em><b>Configuration</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Configuration</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Configuration</em>' map.
	 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage#getExtensibleElement_Configuration()
	 * @model mapType="org.sopeco.model.configuration.measurements.ConfigurationNode<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>"
	 * @generated
	 */
	EMap<String, String> getConfiguration();

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
	 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage#getExtensibleElement_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.sopeco.model.configuration.measurements.ExtensibleElement#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // ExtensibleElement

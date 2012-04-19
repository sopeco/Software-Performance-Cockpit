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
 * A representation of the model object '<em><b>Parameter Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.sopeco.model.configuration.environment.ParameterDefinition#getName <em>Name</em>}</li>
 *   <li>{@link org.sopeco.model.configuration.environment.ParameterDefinition#getType <em>Type</em>}</li>
 *   <li>{@link org.sopeco.model.configuration.environment.ParameterDefinition#getRole <em>Role</em>}</li>
 *   <li>{@link org.sopeco.model.configuration.environment.ParameterDefinition#getFullName <em>Full Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.sopeco.model.configuration.environment.EnvironmentPackage#getParameterDefinition()
 * @model superTypes="org.sopeco.model.configuration.common.Serializable"
 * @extends ISerializableEObject
 * @generated
 */
public interface ParameterDefinition extends ISerializableEObject, Serializable {
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
	 * @see org.sopeco.model.configuration.environment.EnvironmentPackage#getParameterDefinition_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.sopeco.model.configuration.environment.ParameterDefinition#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see org.sopeco.model.configuration.environment.EnvironmentPackage#getParameterDefinition_Type()
	 * @model required="true"
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link org.sopeco.model.configuration.environment.ParameterDefinition#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Role</b></em>' attribute.
	 * The literals are from the enumeration {@link org.sopeco.model.configuration.environment.ParameterRole}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Role</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Role</em>' attribute.
	 * @see org.sopeco.model.configuration.environment.ParameterRole
	 * @see #setRole(ParameterRole)
	 * @see org.sopeco.model.configuration.environment.EnvironmentPackage#getParameterDefinition_Role()
	 * @model required="true"
	 * @generated
	 */
	ParameterRole getRole();

	/**
	 * Sets the value of the '{@link org.sopeco.model.configuration.environment.ParameterDefinition#getRole <em>Role</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Role</em>' attribute.
	 * @see org.sopeco.model.configuration.environment.ParameterRole
	 * @see #getRole()
	 * @generated
	 */
	void setRole(ParameterRole value);

	/**
	 * Returns the value of the '<em><b>Full Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This is a 'derived' attribute computed out of the namespace structure and the parameter's name.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Full Name</em>' attribute.
	 * @see #isSetFullName()
	 * @see org.sopeco.model.configuration.environment.EnvironmentPackage#getParameterDefinition_FullName()
	 * @model unique="false" unsettable="true" transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	String getFullName();

	/**
	 * Returns whether the value of the '{@link org.sopeco.model.configuration.environment.ParameterDefinition#getFullName <em>Full Name</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Full Name</em>' attribute is set.
	 * @see #getFullName()
	 * @generated
	 */
	boolean isSetFullName();

} // ParameterDefinition

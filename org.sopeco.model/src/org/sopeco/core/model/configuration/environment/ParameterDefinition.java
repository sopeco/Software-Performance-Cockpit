/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.core.model.configuration.environment;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.sopeco.core.model.configuration.environment.ParameterDefinition#getName <em>Name</em>}</li>
 *   <li>{@link org.sopeco.core.model.configuration.environment.ParameterDefinition#getType <em>Type</em>}</li>
 *   <li>{@link org.sopeco.core.model.configuration.environment.ParameterDefinition#getDirection <em>Direction</em>}</li>
 *   <li>{@link org.sopeco.core.model.configuration.environment.ParameterDefinition#getFullName <em>Full Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.sopeco.core.model.configuration.environment.EnvironmentPackage#getParameterDefinition()
 * @model
 * @generated
 */
public interface ParameterDefinition extends EObject {
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
	 * @see org.sopeco.core.model.configuration.environment.EnvironmentPackage#getParameterDefinition_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.sopeco.core.model.configuration.environment.ParameterDefinition#getName <em>Name</em>}' attribute.
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
	 * @see org.sopeco.core.model.configuration.environment.EnvironmentPackage#getParameterDefinition_Type()
	 * @model
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link org.sopeco.core.model.configuration.environment.ParameterDefinition#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Direction</b></em>' attribute.
	 * The literals are from the enumeration {@link org.sopeco.core.model.configuration.environment.Direction}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Direction</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Direction</em>' attribute.
	 * @see org.sopeco.core.model.configuration.environment.Direction
	 * @see #setDirection(Direction)
	 * @see org.sopeco.core.model.configuration.environment.EnvironmentPackage#getParameterDefinition_Direction()
	 * @model
	 * @generated
	 */
	Direction getDirection();

	/**
	 * Sets the value of the '{@link org.sopeco.core.model.configuration.environment.ParameterDefinition#getDirection <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Direction</em>' attribute.
	 * @see org.sopeco.core.model.configuration.environment.Direction
	 * @see #getDirection()
	 * @generated
	 */
	void setDirection(Direction value);

	/**
	 * Returns the value of the '<em><b>Full Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This is a 'derived' attribute computed out of the namespace structure and the parameter's name.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Full Name</em>' attribute.
	 * @see #isSetFullName()
	 * @see org.sopeco.core.model.configuration.environment.EnvironmentPackage#getParameterDefinition_FullName()
	 * @model unique="false" unsettable="true" transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	String getFullName();

	/**
	 * Returns whether the value of the '{@link org.sopeco.core.model.configuration.environment.ParameterDefinition#getFullName <em>Full Name</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Full Name</em>' attribute is set.
	 * @see #getFullName()
	 * @generated
	 */
	boolean isSetFullName();

} // ParameterDefinition

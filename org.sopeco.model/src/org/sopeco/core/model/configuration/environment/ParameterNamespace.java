/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.core.model.configuration.environment;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter Namespace</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.sopeco.core.model.configuration.environment.ParameterNamespace#getChildren <em>Children</em>}</li>
 *   <li>{@link org.sopeco.core.model.configuration.environment.ParameterNamespace#getName <em>Name</em>}</li>
 *   <li>{@link org.sopeco.core.model.configuration.environment.ParameterNamespace#getParameters <em>Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.sopeco.core.model.configuration.environment.EnvironmentPackage#getParameterNamespace()
 * @model
 * @generated
 */
public interface ParameterNamespace extends EObject {
	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link org.sopeco.core.model.configuration.environment.ParameterNamespace}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see org.sopeco.core.model.configuration.environment.EnvironmentPackage#getParameterNamespace_Children()
	 * @model containment="true"
	 * @generated
	 */
	EList<ParameterNamespace> getChildren();

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
	 * @see org.sopeco.core.model.configuration.environment.EnvironmentPackage#getParameterNamespace_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.sopeco.core.model.configuration.environment.ParameterNamespace#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link org.sopeco.core.model.configuration.environment.ParameterDefinition}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference list.
	 * @see org.sopeco.core.model.configuration.environment.EnvironmentPackage#getParameterNamespace_Parameters()
	 * @model containment="true"
	 * @generated
	 */
	EList<ParameterDefinition> getParameters();

} // ParameterNamespace

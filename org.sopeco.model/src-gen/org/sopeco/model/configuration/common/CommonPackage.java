/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.common;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.sopeco.model.configuration.common.CommonFactory
 * @model kind="package"
 * @generated
 */
public interface CommonPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "common";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://sopeco.org/configuration/common";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "common";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CommonPackage eINSTANCE = org.sopeco.model.configuration.common.impl.CommonPackageImpl.init();

	/**
	 * The meta object id for the '{@link java.io.Serializable <em>Serializable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.io.Serializable
	 * @see org.sopeco.model.configuration.common.impl.CommonPackageImpl#getSerializable()
	 * @generated
	 */
	int SERIALIZABLE = 0;

	/**
	 * The number of structural features of the '<em>Serializable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERIALIZABLE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.sopeco.model.configuration.common.SerializableEObject <em>Serializable EObject</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.model.configuration.common.SerializableEObject
	 * @see org.sopeco.model.configuration.common.impl.CommonPackageImpl#getSerializableEObject()
	 * @generated
	 */
	int SERIALIZABLE_EOBJECT = 1;

	/**
	 * The number of structural features of the '<em>Serializable EObject</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERIALIZABLE_EOBJECT_FEATURE_COUNT = SERIALIZABLE_FEATURE_COUNT + 0;


	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecore.EObject <em>EObject</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EObject
	 * @see org.sopeco.model.configuration.common.impl.CommonPackageImpl#getEObject()
	 * @generated
	 */
	int EOBJECT = 2;

	/**
	 * The number of structural features of the '<em>EObject</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EOBJECT_FEATURE_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link java.io.Serializable <em>Serializable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Serializable</em>'.
	 * @see java.io.Serializable
	 * @model instanceClass="java.io.Serializable"
	 * @generated
	 */
	EClass getSerializable();

	/**
	 * Returns the meta object for class '{@link org.sopeco.model.configuration.common.SerializableEObject <em>Serializable EObject</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Serializable EObject</em>'.
	 * @see org.sopeco.model.configuration.common.SerializableEObject
	 * @generated
	 */
	EClass getSerializableEObject();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecore.EObject <em>EObject</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EObject</em>'.
	 * @see org.eclipse.emf.ecore.EObject
	 * @model instanceClass="org.eclipse.emf.ecore.EObject"
	 * @generated
	 */
	EClass getEObject();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CommonFactory getCommonFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link java.io.Serializable <em>Serializable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.io.Serializable
		 * @see org.sopeco.model.configuration.common.impl.CommonPackageImpl#getSerializable()
		 * @generated
		 */
		EClass SERIALIZABLE = eINSTANCE.getSerializable();

		/**
		 * The meta object literal for the '{@link org.sopeco.model.configuration.common.SerializableEObject <em>Serializable EObject</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.model.configuration.common.SerializableEObject
		 * @see org.sopeco.model.configuration.common.impl.CommonPackageImpl#getSerializableEObject()
		 * @generated
		 */
		EClass SERIALIZABLE_EOBJECT = eINSTANCE.getSerializableEObject();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecore.EObject <em>EObject</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.ecore.EObject
		 * @see org.sopeco.model.configuration.common.impl.CommonPackageImpl#getEObject()
		 * @generated
		 */
		EClass EOBJECT = eINSTANCE.getEObject();

	}

} //CommonPackage

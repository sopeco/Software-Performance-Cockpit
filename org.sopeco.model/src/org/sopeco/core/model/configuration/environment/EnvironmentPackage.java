/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.core.model.configuration.environment;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see org.sopeco.core.model.configuration.environment.EnvironmentFactory
 * @model kind="package"
 * @generated
 */
public interface EnvironmentPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "environment";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://sopeco.org/configuration/environment";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "environment";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EnvironmentPackage eINSTANCE = org.sopeco.core.model.configuration.environment.impl.EnvironmentPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.sopeco.core.model.configuration.environment.impl.MeasurementEnvironmentDefinitionImpl <em>Measurement Environment Definition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.core.model.configuration.environment.impl.MeasurementEnvironmentDefinitionImpl
	 * @see org.sopeco.core.model.configuration.environment.impl.EnvironmentPackageImpl#getMeasurementEnvironmentDefinition()
	 * @generated
	 */
	int MEASUREMENT_ENVIRONMENT_DEFINITION = 0;

	/**
	 * The feature id for the '<em><b>Root</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEASUREMENT_ENVIRONMENT_DEFINITION__ROOT = 0;

	/**
	 * The number of structural features of the '<em>Measurement Environment Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEASUREMENT_ENVIRONMENT_DEFINITION_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.sopeco.core.model.configuration.environment.impl.ParameterNamespaceImpl <em>Parameter Namespace</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.core.model.configuration.environment.impl.ParameterNamespaceImpl
	 * @see org.sopeco.core.model.configuration.environment.impl.EnvironmentPackageImpl#getParameterNamespace()
	 * @generated
	 */
	int PARAMETER_NAMESPACE = 1;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_NAMESPACE__CHILDREN = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_NAMESPACE__NAME = 1;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_NAMESPACE__PARAMETERS = 2;

	/**
	 * The number of structural features of the '<em>Parameter Namespace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_NAMESPACE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.sopeco.core.model.configuration.environment.impl.ParameterDefinitionImpl <em>Parameter Definition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.core.model.configuration.environment.impl.ParameterDefinitionImpl
	 * @see org.sopeco.core.model.configuration.environment.impl.EnvironmentPackageImpl#getParameterDefinition()
	 * @generated
	 */
	int PARAMETER_DEFINITION = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_DEFINITION__NAME = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_DEFINITION__TYPE = 1;

	/**
	 * The feature id for the '<em><b>Direction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_DEFINITION__DIRECTION = 2;

	/**
	 * The feature id for the '<em><b>Full Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_DEFINITION__FULL_NAME = 3;

	/**
	 * The number of structural features of the '<em>Parameter Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_DEFINITION_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.sopeco.core.model.configuration.environment.Direction <em>Direction</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.core.model.configuration.environment.Direction
	 * @see org.sopeco.core.model.configuration.environment.impl.EnvironmentPackageImpl#getDirection()
	 * @generated
	 */
	int DIRECTION = 3;


	/**
	 * Returns the meta object for class '{@link org.sopeco.core.model.configuration.environment.MeasurementEnvironmentDefinition <em>Measurement Environment Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Measurement Environment Definition</em>'.
	 * @see org.sopeco.core.model.configuration.environment.MeasurementEnvironmentDefinition
	 * @generated
	 */
	EClass getMeasurementEnvironmentDefinition();

	/**
	 * Returns the meta object for the containment reference '{@link org.sopeco.core.model.configuration.environment.MeasurementEnvironmentDefinition#getRoot <em>Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Root</em>'.
	 * @see org.sopeco.core.model.configuration.environment.MeasurementEnvironmentDefinition#getRoot()
	 * @see #getMeasurementEnvironmentDefinition()
	 * @generated
	 */
	EReference getMeasurementEnvironmentDefinition_Root();

	/**
	 * Returns the meta object for class '{@link org.sopeco.core.model.configuration.environment.ParameterNamespace <em>Parameter Namespace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameter Namespace</em>'.
	 * @see org.sopeco.core.model.configuration.environment.ParameterNamespace
	 * @generated
	 */
	EClass getParameterNamespace();

	/**
	 * Returns the meta object for the containment reference list '{@link org.sopeco.core.model.configuration.environment.ParameterNamespace#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.sopeco.core.model.configuration.environment.ParameterNamespace#getChildren()
	 * @see #getParameterNamespace()
	 * @generated
	 */
	EReference getParameterNamespace_Children();

	/**
	 * Returns the meta object for the attribute '{@link org.sopeco.core.model.configuration.environment.ParameterNamespace#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.sopeco.core.model.configuration.environment.ParameterNamespace#getName()
	 * @see #getParameterNamespace()
	 * @generated
	 */
	EAttribute getParameterNamespace_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.sopeco.core.model.configuration.environment.ParameterNamespace#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see org.sopeco.core.model.configuration.environment.ParameterNamespace#getParameters()
	 * @see #getParameterNamespace()
	 * @generated
	 */
	EReference getParameterNamespace_Parameters();

	/**
	 * Returns the meta object for class '{@link org.sopeco.core.model.configuration.environment.ParameterDefinition <em>Parameter Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameter Definition</em>'.
	 * @see org.sopeco.core.model.configuration.environment.ParameterDefinition
	 * @generated
	 */
	EClass getParameterDefinition();

	/**
	 * Returns the meta object for the attribute '{@link org.sopeco.core.model.configuration.environment.ParameterDefinition#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.sopeco.core.model.configuration.environment.ParameterDefinition#getName()
	 * @see #getParameterDefinition()
	 * @generated
	 */
	EAttribute getParameterDefinition_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.sopeco.core.model.configuration.environment.ParameterDefinition#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.sopeco.core.model.configuration.environment.ParameterDefinition#getType()
	 * @see #getParameterDefinition()
	 * @generated
	 */
	EAttribute getParameterDefinition_Type();

	/**
	 * Returns the meta object for the attribute '{@link org.sopeco.core.model.configuration.environment.ParameterDefinition#getDirection <em>Direction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Direction</em>'.
	 * @see org.sopeco.core.model.configuration.environment.ParameterDefinition#getDirection()
	 * @see #getParameterDefinition()
	 * @generated
	 */
	EAttribute getParameterDefinition_Direction();

	/**
	 * Returns the meta object for the attribute '{@link org.sopeco.core.model.configuration.environment.ParameterDefinition#getFullName <em>Full Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Full Name</em>'.
	 * @see org.sopeco.core.model.configuration.environment.ParameterDefinition#getFullName()
	 * @see #getParameterDefinition()
	 * @generated
	 */
	EAttribute getParameterDefinition_FullName();

	/**
	 * Returns the meta object for enum '{@link org.sopeco.core.model.configuration.environment.Direction <em>Direction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Direction</em>'.
	 * @see org.sopeco.core.model.configuration.environment.Direction
	 * @generated
	 */
	EEnum getDirection();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	EnvironmentFactory getEnvironmentFactory();

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
		 * The meta object literal for the '{@link org.sopeco.core.model.configuration.environment.impl.MeasurementEnvironmentDefinitionImpl <em>Measurement Environment Definition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.core.model.configuration.environment.impl.MeasurementEnvironmentDefinitionImpl
		 * @see org.sopeco.core.model.configuration.environment.impl.EnvironmentPackageImpl#getMeasurementEnvironmentDefinition()
		 * @generated
		 */
		EClass MEASUREMENT_ENVIRONMENT_DEFINITION = eINSTANCE.getMeasurementEnvironmentDefinition();

		/**
		 * The meta object literal for the '<em><b>Root</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MEASUREMENT_ENVIRONMENT_DEFINITION__ROOT = eINSTANCE.getMeasurementEnvironmentDefinition_Root();

		/**
		 * The meta object literal for the '{@link org.sopeco.core.model.configuration.environment.impl.ParameterNamespaceImpl <em>Parameter Namespace</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.core.model.configuration.environment.impl.ParameterNamespaceImpl
		 * @see org.sopeco.core.model.configuration.environment.impl.EnvironmentPackageImpl#getParameterNamespace()
		 * @generated
		 */
		EClass PARAMETER_NAMESPACE = eINSTANCE.getParameterNamespace();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETER_NAMESPACE__CHILDREN = eINSTANCE.getParameterNamespace_Children();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER_NAMESPACE__NAME = eINSTANCE.getParameterNamespace_Name();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETER_NAMESPACE__PARAMETERS = eINSTANCE.getParameterNamespace_Parameters();

		/**
		 * The meta object literal for the '{@link org.sopeco.core.model.configuration.environment.impl.ParameterDefinitionImpl <em>Parameter Definition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.core.model.configuration.environment.impl.ParameterDefinitionImpl
		 * @see org.sopeco.core.model.configuration.environment.impl.EnvironmentPackageImpl#getParameterDefinition()
		 * @generated
		 */
		EClass PARAMETER_DEFINITION = eINSTANCE.getParameterDefinition();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER_DEFINITION__NAME = eINSTANCE.getParameterDefinition_Name();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER_DEFINITION__TYPE = eINSTANCE.getParameterDefinition_Type();

		/**
		 * The meta object literal for the '<em><b>Direction</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER_DEFINITION__DIRECTION = eINSTANCE.getParameterDefinition_Direction();

		/**
		 * The meta object literal for the '<em><b>Full Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER_DEFINITION__FULL_NAME = eINSTANCE.getParameterDefinition_FullName();

		/**
		 * The meta object literal for the '{@link org.sopeco.core.model.configuration.environment.Direction <em>Direction</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.core.model.configuration.environment.Direction
		 * @see org.sopeco.core.model.configuration.environment.impl.EnvironmentPackageImpl#getDirection()
		 * @generated
		 */
		EEnum DIRECTION = eINSTANCE.getDirection();

	}

} //EnvironmentPackage

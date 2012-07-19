/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see org.sopeco.model.configuration.ConfigurationFactory
 * @model kind="package"
 * @generated
 */
public interface ConfigurationPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "configuration";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://sopeco.org/configuration";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.sopeco.core.model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ConfigurationPackage eINSTANCE = org.sopeco.model.configuration.impl.ConfigurationPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.sopeco.model.configuration.impl.ScenarioDefinitionImpl <em>Scenario Definition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.model.configuration.impl.ScenarioDefinitionImpl
	 * @see org.sopeco.model.configuration.impl.ConfigurationPackageImpl#getScenarioDefinition()
	 * @generated
	 */
	int SCENARIO_DEFINITION = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_DEFINITION__NAME = 0;

	/**
	 * The feature id for the '<em><b>Measurement Environment Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_DEFINITION__MEASUREMENT_ENVIRONMENT_DEFINITION = 1;

	/**
	 * The feature id for the '<em><b>Measurement Specification</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_DEFINITION__MEASUREMENT_SPECIFICATION = 2;

	/**
	 * The number of structural features of the '<em>Scenario Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_DEFINITION_FEATURE_COUNT = 3;


	/**
	 * Returns the meta object for class '{@link org.sopeco.model.configuration.ScenarioDefinition <em>Scenario Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scenario Definition</em>'.
	 * @see org.sopeco.model.configuration.ScenarioDefinition
	 * @generated
	 */
	EClass getScenarioDefinition();

	/**
	 * Returns the meta object for the attribute '{@link org.sopeco.model.configuration.ScenarioDefinition#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.sopeco.model.configuration.ScenarioDefinition#getName()
	 * @see #getScenarioDefinition()
	 * @generated
	 */
	EAttribute getScenarioDefinition_Name();

	/**
	 * Returns the meta object for the containment reference '{@link org.sopeco.model.configuration.ScenarioDefinition#getMeasurementEnvironmentDefinition <em>Measurement Environment Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Measurement Environment Definition</em>'.
	 * @see org.sopeco.model.configuration.ScenarioDefinition#getMeasurementEnvironmentDefinition()
	 * @see #getScenarioDefinition()
	 * @generated
	 */
	EReference getScenarioDefinition_MeasurementEnvironmentDefinition();

	/**
	 * Returns the meta object for the containment reference list '{@link org.sopeco.model.configuration.ScenarioDefinition#getMeasurementSpecification <em>Measurement Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Measurement Specification</em>'.
	 * @see org.sopeco.model.configuration.ScenarioDefinition#getMeasurementSpecification()
	 * @see #getScenarioDefinition()
	 * @generated
	 */
	EReference getScenarioDefinition_MeasurementSpecification();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ConfigurationFactory getConfigurationFactory();

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
		 * The meta object literal for the '{@link org.sopeco.model.configuration.impl.ScenarioDefinitionImpl <em>Scenario Definition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.model.configuration.impl.ScenarioDefinitionImpl
		 * @see org.sopeco.model.configuration.impl.ConfigurationPackageImpl#getScenarioDefinition()
		 * @generated
		 */
		EClass SCENARIO_DEFINITION = eINSTANCE.getScenarioDefinition();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_DEFINITION__NAME = eINSTANCE.getScenarioDefinition_Name();

		/**
		 * The meta object literal for the '<em><b>Measurement Environment Definition</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO_DEFINITION__MEASUREMENT_ENVIRONMENT_DEFINITION = eINSTANCE.getScenarioDefinition_MeasurementEnvironmentDefinition();

		/**
		 * The meta object literal for the '<em><b>Measurement Specification</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO_DEFINITION__MEASUREMENT_SPECIFICATION = eINSTANCE.getScenarioDefinition_MeasurementSpecification();

	}

} //ConfigurationPackage

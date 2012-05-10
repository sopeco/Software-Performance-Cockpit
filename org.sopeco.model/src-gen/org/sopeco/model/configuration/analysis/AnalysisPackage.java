/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.analysis;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.EReference;
import org.sopeco.model.configuration.measurements.MeasurementsPackage;

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
 * @see org.sopeco.model.configuration.analysis.AnalysisFactory
 * @model kind="package"
 * @generated
 */
public interface AnalysisPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "analysis";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://sopeco.org/configuration/analysis";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "analysis";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AnalysisPackage eINSTANCE = org.sopeco.model.configuration.analysis.impl.AnalysisPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.sopeco.model.configuration.analysis.impl.AnalysisConfigurationImpl <em>Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.model.configuration.analysis.impl.AnalysisConfigurationImpl
	 * @see org.sopeco.model.configuration.analysis.impl.AnalysisPackageImpl#getAnalysisConfiguration()
	 * @generated
	 */
	int ANALYSIS_CONFIGURATION = 0;

	/**
	 * The feature id for the '<em><b>Configuration</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_CONFIGURATION__CONFIGURATION = MeasurementsPackage.EXTENSIBLE_ELEMENT__CONFIGURATION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_CONFIGURATION__NAME = MeasurementsPackage.EXTENSIBLE_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Dependent Parameters</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_CONFIGURATION__DEPENDENT_PARAMETERS = MeasurementsPackage.EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Independent Parameters</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_CONFIGURATION__INDEPENDENT_PARAMETERS = MeasurementsPackage.EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_CONFIGURATION_FEATURE_COUNT = MeasurementsPackage.EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;


	/**
	 * Returns the meta object for class '{@link org.sopeco.model.configuration.analysis.AnalysisConfiguration <em>Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Configuration</em>'.
	 * @see org.sopeco.model.configuration.analysis.AnalysisConfiguration
	 * @generated
	 */
	EClass getAnalysisConfiguration();

	/**
	 * Returns the meta object for the reference list '{@link org.sopeco.model.configuration.analysis.AnalysisConfiguration#getDependentParameters <em>Dependent Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Dependent Parameters</em>'.
	 * @see org.sopeco.model.configuration.analysis.AnalysisConfiguration#getDependentParameters()
	 * @see #getAnalysisConfiguration()
	 * @generated
	 */
	EReference getAnalysisConfiguration_DependentParameters();

	/**
	 * Returns the meta object for the reference list '{@link org.sopeco.model.configuration.analysis.AnalysisConfiguration#getIndependentParameters <em>Independent Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Independent Parameters</em>'.
	 * @see org.sopeco.model.configuration.analysis.AnalysisConfiguration#getIndependentParameters()
	 * @see #getAnalysisConfiguration()
	 * @generated
	 */
	EReference getAnalysisConfiguration_IndependentParameters();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AnalysisFactory getAnalysisFactory();

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
		 * The meta object literal for the '{@link org.sopeco.model.configuration.analysis.impl.AnalysisConfigurationImpl <em>Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.model.configuration.analysis.impl.AnalysisConfigurationImpl
		 * @see org.sopeco.model.configuration.analysis.impl.AnalysisPackageImpl#getAnalysisConfiguration()
		 * @generated
		 */
		EClass ANALYSIS_CONFIGURATION = eINSTANCE.getAnalysisConfiguration();
		/**
		 * The meta object literal for the '<em><b>Dependent Parameters</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYSIS_CONFIGURATION__DEPENDENT_PARAMETERS = eINSTANCE.getAnalysisConfiguration_DependentParameters();
		/**
		 * The meta object literal for the '<em><b>Independent Parameters</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYSIS_CONFIGURATION__INDEPENDENT_PARAMETERS = eINSTANCE.getAnalysisConfiguration_IndependentParameters();

	}

} //AnalysisPackage

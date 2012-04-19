/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.environment;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.sopeco.model.configuration.environment.EnvironmentPackage
 * @generated
 */
public interface EnvironmentFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EnvironmentFactory eINSTANCE = org.sopeco.model.configuration.environment.impl.EnvironmentFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Measurement Environment Definition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Measurement Environment Definition</em>'.
	 * @generated
	 */
	MeasurementEnvironmentDefinition createMeasurementEnvironmentDefinition();

	/**
	 * Returns a new object of class '<em>Parameter Namespace</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Parameter Namespace</em>'.
	 * @generated
	 */
	ParameterNamespace createParameterNamespace();

	/**
	 * Returns a new object of class '<em>Parameter Definition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Parameter Definition</em>'.
	 * @generated
	 */
	ParameterDefinition createParameterDefinition();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	EnvironmentPackage getEnvironmentPackage();

} //EnvironmentFactory

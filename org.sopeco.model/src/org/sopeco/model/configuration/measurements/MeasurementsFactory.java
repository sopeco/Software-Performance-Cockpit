/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.measurements;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage
 * @generated
 */
public interface MeasurementsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MeasurementsFactory eINSTANCE = org.sopeco.model.configuration.measurements.impl.MeasurementsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Measurement Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Measurement Specification</em>'.
	 * @generated
	 */
	MeasurementSpecification createMeasurementSpecification();

	/**
	 * Returns a new object of class '<em>Experiment Series Definition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Experiment Series Definition</em>'.
	 * @generated
	 */
	ExperimentSeriesDefinition createExperimentSeriesDefinition();

	/**
	 * Returns a new object of class '<em>Exploration Strategy</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Exploration Strategy</em>'.
	 * @generated
	 */
	ExplorationStrategy createExplorationStrategy();

	/**
	 * Returns a new object of class '<em>Number Of Repetitions</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Number Of Repetitions</em>'.
	 * @generated
	 */
	NumberOfRepetitions createNumberOfRepetitions();

	/**
	 * Returns a new object of class '<em>Time Out</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Time Out</em>'.
	 * @generated
	 */
	TimeOut createTimeOut();

	/**
	 * Returns a new object of class '<em>Constant Value Assignment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Constant Value Assignment</em>'.
	 * @generated
	 */
	ConstantValueAssignment createConstantValueAssignment();

	/**
	 * Returns a new object of class '<em>Dynamic Value Assignment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Dynamic Value Assignment</em>'.
	 * @generated
	 */
	DynamicValueAssignment createDynamicValueAssignment();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	MeasurementsPackage getMeasurementsPackage();

} //MeasurementsFactory

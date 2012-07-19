/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.measurements.impl;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.sopeco.model.configuration.measurements.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MeasurementsFactoryImpl extends EFactoryImpl implements MeasurementsFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MeasurementsFactory init() {
		try {
			MeasurementsFactory theMeasurementsFactory = (MeasurementsFactory)EPackage.Registry.INSTANCE.getEFactory("http://sopeco.org/configuration/measurements"); 
			if (theMeasurementsFactory != null) {
				return theMeasurementsFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MeasurementsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MeasurementsFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case MeasurementsPackage.MEASUREMENT_SPECIFICATION: return (EObject)createMeasurementSpecification();
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION: return (EObject)createExperimentSeriesDefinition();
			case MeasurementsPackage.EXPLORATION_STRATEGY: return (EObject)createExplorationStrategy();
			case MeasurementsPackage.CONFIGURATION_NODE: return (EObject)createConfigurationNode();
			case MeasurementsPackage.NUMBER_OF_REPETITIONS: return (EObject)createNumberOfRepetitions();
			case MeasurementsPackage.TIME_OUT: return (EObject)createTimeOut();
			case MeasurementsPackage.CONSTANT_VALUE_ASSIGNMENT: return (EObject)createConstantValueAssignment();
			case MeasurementsPackage.DYNAMIC_VALUE_ASSIGNMENT: return (EObject)createDynamicValueAssignment();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MeasurementSpecification createMeasurementSpecification() {
		MeasurementSpecificationImpl measurementSpecification = new MeasurementSpecificationImpl();
		return measurementSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExperimentSeriesDefinition createExperimentSeriesDefinition() {
		ExperimentSeriesDefinitionImpl experimentSeriesDefinition = new ExperimentSeriesDefinitionImpl();
		return experimentSeriesDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExplorationStrategy createExplorationStrategy() {
		ExplorationStrategyImpl explorationStrategy = new ExplorationStrategyImpl();
		return explorationStrategy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, String> createConfigurationNode() {
		ConfigurationNodeImpl configurationNode = new ConfigurationNodeImpl();
		return configurationNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NumberOfRepetitions createNumberOfRepetitions() {
		NumberOfRepetitionsImpl numberOfRepetitions = new NumberOfRepetitionsImpl();
		return numberOfRepetitions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TimeOut createTimeOut() {
		TimeOutImpl timeOut = new TimeOutImpl();
		return timeOut;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConstantValueAssignment createConstantValueAssignment() {
		ConstantValueAssignmentImpl constantValueAssignment = new ConstantValueAssignmentImpl();
		return constantValueAssignment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DynamicValueAssignment createDynamicValueAssignment() {
		DynamicValueAssignmentImpl dynamicValueAssignment = new DynamicValueAssignmentImpl();
		return dynamicValueAssignment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MeasurementsPackage getMeasurementsPackage() {
		return (MeasurementsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MeasurementsPackage getPackage() {
		return MeasurementsPackage.eINSTANCE;
	}

} //MeasurementsFactoryImpl

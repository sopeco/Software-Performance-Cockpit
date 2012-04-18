/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.measurements.util;

import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.sopeco.model.configuration.measurements.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage
 * @generated
 */
public class MeasurementsAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static MeasurementsPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MeasurementsAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = MeasurementsPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MeasurementsSwitch<Adapter> modelSwitch =
		new MeasurementsSwitch<Adapter>() {
			@Override
			public Adapter caseMeasurementSpecification(MeasurementSpecification object) {
				return createMeasurementSpecificationAdapter();
			}
			@Override
			public Adapter caseExperimentSeriesDefinition(ExperimentSeriesDefinition object) {
				return createExperimentSeriesDefinitionAdapter();
			}
			@Override
			public Adapter caseParameterValueAssignment(ParameterValueAssignment object) {
				return createParameterValueAssignmentAdapter();
			}
			@Override
			public Adapter caseExplorationStrategy(ExplorationStrategy object) {
				return createExplorationStrategyAdapter();
			}
			@Override
			public Adapter caseExperimentTerminationCondition(ExperimentTerminationCondition object) {
				return createExperimentTerminationConditionAdapter();
			}
			@Override
			public Adapter caseConfigurationNode(Map.Entry<String, String> object) {
				return createConfigurationNodeAdapter();
			}
			@Override
			public Adapter caseExtensibleElement(ExtensibleElement object) {
				return createExtensibleElementAdapter();
			}
			@Override
			public Adapter caseNumberOfRepetitions(NumberOfRepetitions object) {
				return createNumberOfRepetitionsAdapter();
			}
			@Override
			public Adapter caseTimeOut(TimeOut object) {
				return createTimeOutAdapter();
			}
			@Override
			public Adapter caseConstantValueAssignment(ConstantValueAssignment object) {
				return createConstantValueAssignmentAdapter();
			}
			@Override
			public Adapter caseDynamicValueAssignment(DynamicValueAssignment object) {
				return createDynamicValueAssignmentAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.sopeco.model.configuration.measurements.MeasurementSpecification <em>Measurement Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.sopeco.model.configuration.measurements.MeasurementSpecification
	 * @generated
	 */
	public Adapter createMeasurementSpecificationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition <em>Experiment Series Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition
	 * @generated
	 */
	public Adapter createExperimentSeriesDefinitionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.sopeco.model.configuration.measurements.ParameterValueAssignment <em>Parameter Value Assignment</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.sopeco.model.configuration.measurements.ParameterValueAssignment
	 * @generated
	 */
	public Adapter createParameterValueAssignmentAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.sopeco.model.configuration.measurements.ExplorationStrategy <em>Exploration Strategy</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.sopeco.model.configuration.measurements.ExplorationStrategy
	 * @generated
	 */
	public Adapter createExplorationStrategyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.sopeco.model.configuration.measurements.ExperimentTerminationCondition <em>Experiment Termination Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.sopeco.model.configuration.measurements.ExperimentTerminationCondition
	 * @generated
	 */
	public Adapter createExperimentTerminationConditionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>Configuration Node</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createConfigurationNodeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.sopeco.model.configuration.measurements.ExtensibleElement <em>Extensible Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.sopeco.model.configuration.measurements.ExtensibleElement
	 * @generated
	 */
	public Adapter createExtensibleElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.sopeco.model.configuration.measurements.NumberOfRepetitions <em>Number Of Repetitions</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.sopeco.model.configuration.measurements.NumberOfRepetitions
	 * @generated
	 */
	public Adapter createNumberOfRepetitionsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.sopeco.model.configuration.measurements.TimeOut <em>Time Out</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.sopeco.model.configuration.measurements.TimeOut
	 * @generated
	 */
	public Adapter createTimeOutAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.sopeco.model.configuration.measurements.ConstantValueAssignment <em>Constant Value Assignment</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.sopeco.model.configuration.measurements.ConstantValueAssignment
	 * @generated
	 */
	public Adapter createConstantValueAssignmentAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.sopeco.model.configuration.measurements.DynamicValueAssignment <em>Dynamic Value Assignment</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.sopeco.model.configuration.measurements.DynamicValueAssignment
	 * @generated
	 */
	public Adapter createDynamicValueAssignmentAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //MeasurementsAdapterFactory

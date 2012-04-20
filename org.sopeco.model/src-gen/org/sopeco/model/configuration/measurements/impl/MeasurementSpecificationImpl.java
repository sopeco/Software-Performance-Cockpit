/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.measurements.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.sopeco.model.configuration.common.ext.SerializableEObject;

import org.sopeco.model.configuration.measurements.ConstantValueAssignment;
import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.model.configuration.measurements.MeasurementSpecification;
import org.sopeco.model.configuration.measurements.MeasurementsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Measurement Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.sopeco.model.configuration.measurements.impl.MeasurementSpecificationImpl#getExperimentSeriesDefinitions <em>Experiment Series Definitions</em>}</li>
 *   <li>{@link org.sopeco.model.configuration.measurements.impl.MeasurementSpecificationImpl#getInitializationAssignemts <em>Initialization Assignemts</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MeasurementSpecificationImpl extends SerializableEObject implements MeasurementSpecification {
	/**
	 * The cached value of the '{@link #getExperimentSeriesDefinitions() <em>Experiment Series Definitions</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExperimentSeriesDefinitions()
	 * @generated
	 * @ordered
	 */
	protected EList<ExperimentSeriesDefinition> experimentSeriesDefinitions;

	/**
	 * The cached value of the '{@link #getInitializationAssignemts() <em>Initialization Assignemts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitializationAssignemts()
	 * @generated
	 * @ordered
	 */
	protected EList<ConstantValueAssignment> initializationAssignemts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MeasurementSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MeasurementsPackage.Literals.MEASUREMENT_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ExperimentSeriesDefinition> getExperimentSeriesDefinitions() {
		if (experimentSeriesDefinitions == null) {
			experimentSeriesDefinitions = new EObjectContainmentEList<ExperimentSeriesDefinition>(ExperimentSeriesDefinition.class, this, MeasurementsPackage.MEASUREMENT_SPECIFICATION__EXPERIMENT_SERIES_DEFINITIONS);
		}
		return experimentSeriesDefinitions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ConstantValueAssignment> getInitializationAssignemts() {
		if (initializationAssignemts == null) {
			initializationAssignemts = new EObjectContainmentEList<ConstantValueAssignment>(ConstantValueAssignment.class, this, MeasurementsPackage.MEASUREMENT_SPECIFICATION__INITIALIZATION_ASSIGNEMTS);
		}
		return initializationAssignemts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MeasurementsPackage.MEASUREMENT_SPECIFICATION__EXPERIMENT_SERIES_DEFINITIONS:
				return ((InternalEList<?>)getExperimentSeriesDefinitions()).basicRemove(otherEnd, msgs);
			case MeasurementsPackage.MEASUREMENT_SPECIFICATION__INITIALIZATION_ASSIGNEMTS:
				return ((InternalEList<?>)getInitializationAssignemts()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MeasurementsPackage.MEASUREMENT_SPECIFICATION__EXPERIMENT_SERIES_DEFINITIONS:
				return getExperimentSeriesDefinitions();
			case MeasurementsPackage.MEASUREMENT_SPECIFICATION__INITIALIZATION_ASSIGNEMTS:
				return getInitializationAssignemts();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MeasurementsPackage.MEASUREMENT_SPECIFICATION__EXPERIMENT_SERIES_DEFINITIONS:
				getExperimentSeriesDefinitions().clear();
				getExperimentSeriesDefinitions().addAll((Collection<? extends ExperimentSeriesDefinition>)newValue);
				return;
			case MeasurementsPackage.MEASUREMENT_SPECIFICATION__INITIALIZATION_ASSIGNEMTS:
				getInitializationAssignemts().clear();
				getInitializationAssignemts().addAll((Collection<? extends ConstantValueAssignment>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case MeasurementsPackage.MEASUREMENT_SPECIFICATION__EXPERIMENT_SERIES_DEFINITIONS:
				getExperimentSeriesDefinitions().clear();
				return;
			case MeasurementsPackage.MEASUREMENT_SPECIFICATION__INITIALIZATION_ASSIGNEMTS:
				getInitializationAssignemts().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case MeasurementsPackage.MEASUREMENT_SPECIFICATION__EXPERIMENT_SERIES_DEFINITIONS:
				return experimentSeriesDefinitions != null && !experimentSeriesDefinitions.isEmpty();
			case MeasurementsPackage.MEASUREMENT_SPECIFICATION__INITIALIZATION_ASSIGNEMTS:
				return initializationAssignemts != null && !initializationAssignemts.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //MeasurementSpecificationImpl
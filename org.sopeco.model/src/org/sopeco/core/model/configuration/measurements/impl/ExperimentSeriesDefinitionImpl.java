/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.core.model.configuration.measurements.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.sopeco.core.model.configuration.measurements.ConstantValueAssignment;
import org.sopeco.core.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.core.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.core.model.configuration.measurements.ExplorationStrategy;
import org.sopeco.core.model.configuration.measurements.MeasurementsPackage;
import org.sopeco.core.model.configuration.measurements.ParameterValueAssignment;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Experiment Series Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.sopeco.core.model.configuration.measurements.impl.ExperimentSeriesDefinitionImpl#getExplorationStrategy <em>Exploration Strategy</em>}</li>
 *   <li>{@link org.sopeco.core.model.configuration.measurements.impl.ExperimentSeriesDefinitionImpl#getExperimentAssignments <em>Experiment Assignments</em>}</li>
 *   <li>{@link org.sopeco.core.model.configuration.measurements.impl.ExperimentSeriesDefinitionImpl#getExperimentTerminationCondition <em>Experiment Termination Condition</em>}</li>
 *   <li>{@link org.sopeco.core.model.configuration.measurements.impl.ExperimentSeriesDefinitionImpl#getPreperationAssignments <em>Preperation Assignments</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExperimentSeriesDefinitionImpl extends EObjectImpl implements ExperimentSeriesDefinition {
	/**
	 * The cached value of the '{@link #getExplorationStrategy() <em>Exploration Strategy</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExplorationStrategy()
	 * @generated
	 * @ordered
	 */
	protected ExplorationStrategy explorationStrategy;

	/**
	 * The cached value of the '{@link #getExperimentAssignments() <em>Experiment Assignments</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExperimentAssignments()
	 * @generated
	 * @ordered
	 */
	protected EList<ParameterValueAssignment> experimentAssignments;

	/**
	 * The cached value of the '{@link #getExperimentTerminationCondition() <em>Experiment Termination Condition</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExperimentTerminationCondition()
	 * @generated
	 * @ordered
	 */
	protected ExperimentTerminationCondition experimentTerminationCondition;

	/**
	 * The cached value of the '{@link #getPreperationAssignments() <em>Preperation Assignments</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPreperationAssignments()
	 * @generated
	 * @ordered
	 */
	protected EList<ConstantValueAssignment> preperationAssignments;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExperimentSeriesDefinitionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MeasurementsPackage.Literals.EXPERIMENT_SERIES_DEFINITION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExplorationStrategy getExplorationStrategy() {
		return explorationStrategy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetExplorationStrategy(ExplorationStrategy newExplorationStrategy, NotificationChain msgs) {
		ExplorationStrategy oldExplorationStrategy = explorationStrategy;
		explorationStrategy = newExplorationStrategy;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPLORATION_STRATEGY, oldExplorationStrategy, newExplorationStrategy);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExplorationStrategy(ExplorationStrategy newExplorationStrategy) {
		if (newExplorationStrategy != explorationStrategy) {
			NotificationChain msgs = null;
			if (explorationStrategy != null)
				msgs = ((InternalEObject)explorationStrategy).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPLORATION_STRATEGY, null, msgs);
			if (newExplorationStrategy != null)
				msgs = ((InternalEObject)newExplorationStrategy).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPLORATION_STRATEGY, null, msgs);
			msgs = basicSetExplorationStrategy(newExplorationStrategy, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPLORATION_STRATEGY, newExplorationStrategy, newExplorationStrategy));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ParameterValueAssignment> getExperimentAssignments() {
		if (experimentAssignments == null) {
			experimentAssignments = new EObjectContainmentEList<ParameterValueAssignment>(ParameterValueAssignment.class, this, MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_ASSIGNMENTS);
		}
		return experimentAssignments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExperimentTerminationCondition getExperimentTerminationCondition() {
		return experimentTerminationCondition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetExperimentTerminationCondition(ExperimentTerminationCondition newExperimentTerminationCondition, NotificationChain msgs) {
		ExperimentTerminationCondition oldExperimentTerminationCondition = experimentTerminationCondition;
		experimentTerminationCondition = newExperimentTerminationCondition;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_TERMINATION_CONDITION, oldExperimentTerminationCondition, newExperimentTerminationCondition);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExperimentTerminationCondition(ExperimentTerminationCondition newExperimentTerminationCondition) {
		if (newExperimentTerminationCondition != experimentTerminationCondition) {
			NotificationChain msgs = null;
			if (experimentTerminationCondition != null)
				msgs = ((InternalEObject)experimentTerminationCondition).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_TERMINATION_CONDITION, null, msgs);
			if (newExperimentTerminationCondition != null)
				msgs = ((InternalEObject)newExperimentTerminationCondition).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_TERMINATION_CONDITION, null, msgs);
			msgs = basicSetExperimentTerminationCondition(newExperimentTerminationCondition, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_TERMINATION_CONDITION, newExperimentTerminationCondition, newExperimentTerminationCondition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ConstantValueAssignment> getPreperationAssignments() {
		if (preperationAssignments == null) {
			preperationAssignments = new EObjectContainmentEList<ConstantValueAssignment>(ConstantValueAssignment.class, this, MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__PREPERATION_ASSIGNMENTS);
		}
		return preperationAssignments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPLORATION_STRATEGY:
				return basicSetExplorationStrategy(null, msgs);
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_ASSIGNMENTS:
				return ((InternalEList<?>)getExperimentAssignments()).basicRemove(otherEnd, msgs);
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_TERMINATION_CONDITION:
				return basicSetExperimentTerminationCondition(null, msgs);
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__PREPERATION_ASSIGNMENTS:
				return ((InternalEList<?>)getPreperationAssignments()).basicRemove(otherEnd, msgs);
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
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPLORATION_STRATEGY:
				return getExplorationStrategy();
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_ASSIGNMENTS:
				return getExperimentAssignments();
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_TERMINATION_CONDITION:
				return getExperimentTerminationCondition();
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__PREPERATION_ASSIGNMENTS:
				return getPreperationAssignments();
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
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPLORATION_STRATEGY:
				setExplorationStrategy((ExplorationStrategy)newValue);
				return;
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_ASSIGNMENTS:
				getExperimentAssignments().clear();
				getExperimentAssignments().addAll((Collection<? extends ParameterValueAssignment>)newValue);
				return;
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_TERMINATION_CONDITION:
				setExperimentTerminationCondition((ExperimentTerminationCondition)newValue);
				return;
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__PREPERATION_ASSIGNMENTS:
				getPreperationAssignments().clear();
				getPreperationAssignments().addAll((Collection<? extends ConstantValueAssignment>)newValue);
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
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPLORATION_STRATEGY:
				setExplorationStrategy((ExplorationStrategy)null);
				return;
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_ASSIGNMENTS:
				getExperimentAssignments().clear();
				return;
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_TERMINATION_CONDITION:
				setExperimentTerminationCondition((ExperimentTerminationCondition)null);
				return;
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__PREPERATION_ASSIGNMENTS:
				getPreperationAssignments().clear();
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
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPLORATION_STRATEGY:
				return explorationStrategy != null;
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_ASSIGNMENTS:
				return experimentAssignments != null && !experimentAssignments.isEmpty();
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_TERMINATION_CONDITION:
				return experimentTerminationCondition != null;
			case MeasurementsPackage.EXPERIMENT_SERIES_DEFINITION__PREPERATION_ASSIGNMENTS:
				return preperationAssignments != null && !preperationAssignments.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ExperimentSeriesDefinitionImpl

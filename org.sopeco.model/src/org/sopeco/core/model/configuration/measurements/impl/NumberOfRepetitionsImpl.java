/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.core.model.configuration.measurements.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.sopeco.core.model.configuration.measurements.MeasurementsPackage;
import org.sopeco.core.model.configuration.measurements.NumberOfRepetitions;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Number Of Repetitions</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.sopeco.core.model.configuration.measurements.impl.NumberOfRepetitionsImpl#getNumberOfRepetitions <em>Number Of Repetitions</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NumberOfRepetitionsImpl extends ExperimentTerminationConditionImpl implements NumberOfRepetitions {
	/**
	 * The default value of the '{@link #getNumberOfRepetitions() <em>Number Of Repetitions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfRepetitions()
	 * @generated
	 * @ordered
	 */
	protected static final long NUMBER_OF_REPETITIONS_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getNumberOfRepetitions() <em>Number Of Repetitions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfRepetitions()
	 * @generated
	 * @ordered
	 */
	protected long numberOfRepetitions = NUMBER_OF_REPETITIONS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NumberOfRepetitionsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MeasurementsPackage.Literals.NUMBER_OF_REPETITIONS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getNumberOfRepetitions() {
		return numberOfRepetitions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNumberOfRepetitions(long newNumberOfRepetitions) {
		long oldNumberOfRepetitions = numberOfRepetitions;
		numberOfRepetitions = newNumberOfRepetitions;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MeasurementsPackage.NUMBER_OF_REPETITIONS__NUMBER_OF_REPETITIONS, oldNumberOfRepetitions, numberOfRepetitions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MeasurementsPackage.NUMBER_OF_REPETITIONS__NUMBER_OF_REPETITIONS:
				return getNumberOfRepetitions();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MeasurementsPackage.NUMBER_OF_REPETITIONS__NUMBER_OF_REPETITIONS:
				setNumberOfRepetitions((Long)newValue);
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
			case MeasurementsPackage.NUMBER_OF_REPETITIONS__NUMBER_OF_REPETITIONS:
				setNumberOfRepetitions(NUMBER_OF_REPETITIONS_EDEFAULT);
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
			case MeasurementsPackage.NUMBER_OF_REPETITIONS__NUMBER_OF_REPETITIONS:
				return numberOfRepetitions != NUMBER_OF_REPETITIONS_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (numberOfRepetitions: ");
		result.append(numberOfRepetitions);
		result.append(')');
		return result.toString();
	}

} //NumberOfRepetitionsImpl

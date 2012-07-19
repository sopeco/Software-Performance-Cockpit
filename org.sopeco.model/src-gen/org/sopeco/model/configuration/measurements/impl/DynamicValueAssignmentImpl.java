/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.measurements.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.sopeco.model.configuration.environment.ParameterDefinition;

import org.sopeco.model.configuration.measurements.DynamicValueAssignment;
import org.sopeco.model.configuration.measurements.MeasurementsPackage;
import org.sopeco.model.configuration.measurements.ParameterValueAssignment;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dynamic Value Assignment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.sopeco.model.configuration.measurements.impl.DynamicValueAssignmentImpl#getParameter <em>Parameter</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DynamicValueAssignmentImpl extends ExtensibleElementImpl implements DynamicValueAssignment {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The cached value of the '{@link #getParameter() <em>Parameter</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameter()
	 * @generated
	 * @ordered
	 */
	protected ParameterDefinition parameter;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DynamicValueAssignmentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MeasurementsPackage.Literals.DYNAMIC_VALUE_ASSIGNMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterDefinition getParameter() {
		if (parameter != null && ((EObject)parameter).eIsProxy()) {
			InternalEObject oldParameter = (InternalEObject)parameter;
			parameter = (ParameterDefinition)eResolveProxy(oldParameter);
			if (parameter != oldParameter) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MeasurementsPackage.DYNAMIC_VALUE_ASSIGNMENT__PARAMETER, oldParameter, parameter));
			}
		}
		return parameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterDefinition basicGetParameter() {
		return parameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameter(ParameterDefinition newParameter) {
		ParameterDefinition oldParameter = parameter;
		parameter = newParameter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MeasurementsPackage.DYNAMIC_VALUE_ASSIGNMENT__PARAMETER, oldParameter, parameter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MeasurementsPackage.DYNAMIC_VALUE_ASSIGNMENT__PARAMETER:
				if (resolve) return getParameter();
				return basicGetParameter();
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
			case MeasurementsPackage.DYNAMIC_VALUE_ASSIGNMENT__PARAMETER:
				setParameter((ParameterDefinition)newValue);
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
			case MeasurementsPackage.DYNAMIC_VALUE_ASSIGNMENT__PARAMETER:
				setParameter((ParameterDefinition)null);
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
			case MeasurementsPackage.DYNAMIC_VALUE_ASSIGNMENT__PARAMETER:
				return parameter != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == ParameterValueAssignment.class) {
			switch (derivedFeatureID) {
				case MeasurementsPackage.DYNAMIC_VALUE_ASSIGNMENT__PARAMETER: return MeasurementsPackage.PARAMETER_VALUE_ASSIGNMENT__PARAMETER;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == ParameterValueAssignment.class) {
			switch (baseFeatureID) {
				case MeasurementsPackage.PARAMETER_VALUE_ASSIGNMENT__PARAMETER: return MeasurementsPackage.DYNAMIC_VALUE_ASSIGNMENT__PARAMETER;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

} //DynamicValueAssignmentImpl

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.sopeco.model.configuration.ConfigurationPackage;
import org.sopeco.model.configuration.ScenarioDefinition;

import org.sopeco.model.configuration.common.ext.SerializableEObject;

import org.sopeco.model.configuration.environment.MeasurementEnvironmentDefinition;

import org.sopeco.model.configuration.measurements.MeasurementSpecification;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scenario Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.sopeco.model.configuration.impl.ScenarioDefinitionImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.sopeco.model.configuration.impl.ScenarioDefinitionImpl#getMeasurementEnvironmentDefinition <em>Measurement Environment Definition</em>}</li>
 *   <li>{@link org.sopeco.model.configuration.impl.ScenarioDefinitionImpl#getMeasurementSpecification <em>Measurement Specification</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScenarioDefinitionImpl extends SerializableEObject implements ScenarioDefinition {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMeasurementEnvironmentDefinition() <em>Measurement Environment Definition</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMeasurementEnvironmentDefinition()
	 * @generated
	 * @ordered
	 */
	protected MeasurementEnvironmentDefinition measurementEnvironmentDefinition;

	/**
	 * The cached value of the '{@link #getMeasurementSpecification() <em>Measurement Specification</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMeasurementSpecification()
	 * @generated
	 * @ordered
	 */
	protected EList<MeasurementSpecification> measurementSpecification;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScenarioDefinitionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigurationPackage.Literals.SCENARIO_DEFINITION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.SCENARIO_DEFINITION__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MeasurementEnvironmentDefinition getMeasurementEnvironmentDefinition() {
		return measurementEnvironmentDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMeasurementEnvironmentDefinition(MeasurementEnvironmentDefinition newMeasurementEnvironmentDefinition, NotificationChain msgs) {
		MeasurementEnvironmentDefinition oldMeasurementEnvironmentDefinition = measurementEnvironmentDefinition;
		measurementEnvironmentDefinition = newMeasurementEnvironmentDefinition;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_ENVIRONMENT_DEFINITION, oldMeasurementEnvironmentDefinition, newMeasurementEnvironmentDefinition);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMeasurementEnvironmentDefinition(MeasurementEnvironmentDefinition newMeasurementEnvironmentDefinition) {
		if (newMeasurementEnvironmentDefinition != measurementEnvironmentDefinition) {
			NotificationChain msgs = null;
			if (measurementEnvironmentDefinition != null)
				msgs = ((InternalEObject)measurementEnvironmentDefinition).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_ENVIRONMENT_DEFINITION, null, msgs);
			if (newMeasurementEnvironmentDefinition != null)
				msgs = ((InternalEObject)newMeasurementEnvironmentDefinition).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_ENVIRONMENT_DEFINITION, null, msgs);
			msgs = basicSetMeasurementEnvironmentDefinition(newMeasurementEnvironmentDefinition, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_ENVIRONMENT_DEFINITION, newMeasurementEnvironmentDefinition, newMeasurementEnvironmentDefinition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MeasurementSpecification> getMeasurementSpecification() {
		if (measurementSpecification == null) {
			measurementSpecification = new EObjectContainmentEList<MeasurementSpecification>(MeasurementSpecification.class, this, ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_SPECIFICATION);
		}
		return measurementSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_ENVIRONMENT_DEFINITION:
				return basicSetMeasurementEnvironmentDefinition(null, msgs);
			case ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_SPECIFICATION:
				return ((InternalEList<?>)getMeasurementSpecification()).basicRemove(otherEnd, msgs);
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
			case ConfigurationPackage.SCENARIO_DEFINITION__NAME:
				return getName();
			case ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_ENVIRONMENT_DEFINITION:
				return getMeasurementEnvironmentDefinition();
			case ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_SPECIFICATION:
				return getMeasurementSpecification();
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
			case ConfigurationPackage.SCENARIO_DEFINITION__NAME:
				setName((String)newValue);
				return;
			case ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_ENVIRONMENT_DEFINITION:
				setMeasurementEnvironmentDefinition((MeasurementEnvironmentDefinition)newValue);
				return;
			case ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_SPECIFICATION:
				getMeasurementSpecification().clear();
				getMeasurementSpecification().addAll((Collection<? extends MeasurementSpecification>)newValue);
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
			case ConfigurationPackage.SCENARIO_DEFINITION__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_ENVIRONMENT_DEFINITION:
				setMeasurementEnvironmentDefinition((MeasurementEnvironmentDefinition)null);
				return;
			case ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_SPECIFICATION:
				getMeasurementSpecification().clear();
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
			case ConfigurationPackage.SCENARIO_DEFINITION__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_ENVIRONMENT_DEFINITION:
				return measurementEnvironmentDefinition != null;
			case ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_SPECIFICATION:
				return measurementSpecification != null && !measurementSpecification.isEmpty();
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
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //ScenarioDefinitionImpl

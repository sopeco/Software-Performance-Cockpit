/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.core.model.configuration.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.sopeco.core.model.configuration.ConfigurationPackage;
import org.sopeco.core.model.configuration.ScenarioDefinition;

import org.sopeco.core.model.configuration.environment.MeasurementEnvironmentDefinition;

import org.sopeco.core.model.configuration.measurements.MeasurementSpecification;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scenario Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.sopeco.core.model.configuration.impl.ScenarioDefinitionImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.sopeco.core.model.configuration.impl.ScenarioDefinitionImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.sopeco.core.model.configuration.impl.ScenarioDefinitionImpl#getMeasurementEnvironmentDefinition <em>Measurement Environment Definition</em>}</li>
 *   <li>{@link org.sopeco.core.model.configuration.impl.ScenarioDefinitionImpl#getMeasurementSpecification <em>Measurement Specification</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScenarioDefinitionImpl extends EObjectImpl implements ScenarioDefinition {
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
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

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
	 * The cached value of the '{@link #getMeasurementSpecification() <em>Measurement Specification</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMeasurementSpecification()
	 * @generated
	 * @ordered
	 */
	protected MeasurementSpecification measurementSpecification;

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
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.SCENARIO_DEFINITION__VERSION, oldVersion, version));
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
	public MeasurementSpecification getMeasurementSpecification() {
		return measurementSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMeasurementSpecification(MeasurementSpecification newMeasurementSpecification, NotificationChain msgs) {
		MeasurementSpecification oldMeasurementSpecification = measurementSpecification;
		measurementSpecification = newMeasurementSpecification;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_SPECIFICATION, oldMeasurementSpecification, newMeasurementSpecification);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMeasurementSpecification(MeasurementSpecification newMeasurementSpecification) {
		if (newMeasurementSpecification != measurementSpecification) {
			NotificationChain msgs = null;
			if (measurementSpecification != null)
				msgs = ((InternalEObject)measurementSpecification).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_SPECIFICATION, null, msgs);
			if (newMeasurementSpecification != null)
				msgs = ((InternalEObject)newMeasurementSpecification).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_SPECIFICATION, null, msgs);
			msgs = basicSetMeasurementSpecification(newMeasurementSpecification, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_SPECIFICATION, newMeasurementSpecification, newMeasurementSpecification));
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
				return basicSetMeasurementSpecification(null, msgs);
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
			case ConfigurationPackage.SCENARIO_DEFINITION__VERSION:
				return getVersion();
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
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ConfigurationPackage.SCENARIO_DEFINITION__NAME:
				setName((String)newValue);
				return;
			case ConfigurationPackage.SCENARIO_DEFINITION__VERSION:
				setVersion((String)newValue);
				return;
			case ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_ENVIRONMENT_DEFINITION:
				setMeasurementEnvironmentDefinition((MeasurementEnvironmentDefinition)newValue);
				return;
			case ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_SPECIFICATION:
				setMeasurementSpecification((MeasurementSpecification)newValue);
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
			case ConfigurationPackage.SCENARIO_DEFINITION__VERSION:
				setVersion(VERSION_EDEFAULT);
				return;
			case ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_ENVIRONMENT_DEFINITION:
				setMeasurementEnvironmentDefinition((MeasurementEnvironmentDefinition)null);
				return;
			case ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_SPECIFICATION:
				setMeasurementSpecification((MeasurementSpecification)null);
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
			case ConfigurationPackage.SCENARIO_DEFINITION__VERSION:
				return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
			case ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_ENVIRONMENT_DEFINITION:
				return measurementEnvironmentDefinition != null;
			case ConfigurationPackage.SCENARIO_DEFINITION__MEASUREMENT_SPECIFICATION:
				return measurementSpecification != null;
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
		result.append(", version: ");
		result.append(version);
		result.append(')');
		return result.toString();
	}

} //ScenarioDefinitionImpl

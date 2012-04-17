/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.core.model.configuration.measurements.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.sopeco.core.model.configuration.analysis.AnalysisConfiguration;
import org.sopeco.core.model.configuration.measurements.ExplorationStrategy;
import org.sopeco.core.model.configuration.measurements.MeasurementsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Exploration Strategy</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.sopeco.core.model.configuration.measurements.impl.ExplorationStrategyImpl#getAnalysisConfigurations <em>Analysis Configurations</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExplorationStrategyImpl extends ExtensibleElementImpl implements ExplorationStrategy {
	/**
	 * The cached value of the '{@link #getAnalysisConfigurations() <em>Analysis Configurations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnalysisConfigurations()
	 * @generated
	 * @ordered
	 */
	protected EList<AnalysisConfiguration> analysisConfigurations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExplorationStrategyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MeasurementsPackage.Literals.EXPLORATION_STRATEGY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AnalysisConfiguration> getAnalysisConfigurations() {
		if (analysisConfigurations == null) {
			analysisConfigurations = new EObjectContainmentEList<AnalysisConfiguration>(AnalysisConfiguration.class, this, MeasurementsPackage.EXPLORATION_STRATEGY__ANALYSIS_CONFIGURATIONS);
		}
		return analysisConfigurations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MeasurementsPackage.EXPLORATION_STRATEGY__ANALYSIS_CONFIGURATIONS:
				return ((InternalEList<?>)getAnalysisConfigurations()).basicRemove(otherEnd, msgs);
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
			case MeasurementsPackage.EXPLORATION_STRATEGY__ANALYSIS_CONFIGURATIONS:
				return getAnalysisConfigurations();
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
			case MeasurementsPackage.EXPLORATION_STRATEGY__ANALYSIS_CONFIGURATIONS:
				getAnalysisConfigurations().clear();
				getAnalysisConfigurations().addAll((Collection<? extends AnalysisConfiguration>)newValue);
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
			case MeasurementsPackage.EXPLORATION_STRATEGY__ANALYSIS_CONFIGURATIONS:
				getAnalysisConfigurations().clear();
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
			case MeasurementsPackage.EXPLORATION_STRATEGY__ANALYSIS_CONFIGURATIONS:
				return analysisConfigurations != null && !analysisConfigurations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ExplorationStrategyImpl

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.analysis.impl;

import java.util.Collection;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.sopeco.model.configuration.analysis.AnalysisConfiguration;
import org.sopeco.model.configuration.analysis.AnalysisPackage;

import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.measurements.impl.ExtensibleElementImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.sopeco.model.configuration.analysis.impl.AnalysisConfigurationImpl#getDependentParameters <em>Dependent Parameters</em>}</li>
 *   <li>{@link org.sopeco.model.configuration.analysis.impl.AnalysisConfigurationImpl#getIndependentParameters <em>Independent Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AnalysisConfigurationImpl extends ExtensibleElementImpl implements AnalysisConfiguration {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The cached value of the '{@link #getDependentParameters() <em>Dependent Parameters</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependentParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<ParameterDefinition> dependentParameters;
	/**
	 * The cached value of the '{@link #getIndependentParameters() <em>Independent Parameters</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndependentParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<ParameterDefinition> independentParameters;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AnalysisConfigurationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalysisPackage.Literals.ANALYSIS_CONFIGURATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ParameterDefinition> getDependentParameters() {
		if (dependentParameters == null) {
			dependentParameters = new EObjectResolvingEList<ParameterDefinition>(ParameterDefinition.class, this, AnalysisPackage.ANALYSIS_CONFIGURATION__DEPENDENT_PARAMETERS);
		}
		return dependentParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ParameterDefinition> getIndependentParameters() {
		if (independentParameters == null) {
			independentParameters = new EObjectResolvingEList<ParameterDefinition>(ParameterDefinition.class, this, AnalysisPackage.ANALYSIS_CONFIGURATION__INDEPENDENT_PARAMETERS);
		}
		return independentParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalysisPackage.ANALYSIS_CONFIGURATION__DEPENDENT_PARAMETERS:
				return getDependentParameters();
			case AnalysisPackage.ANALYSIS_CONFIGURATION__INDEPENDENT_PARAMETERS:
				return getIndependentParameters();
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
			case AnalysisPackage.ANALYSIS_CONFIGURATION__DEPENDENT_PARAMETERS:
				getDependentParameters().clear();
				getDependentParameters().addAll((Collection<? extends ParameterDefinition>)newValue);
				return;
			case AnalysisPackage.ANALYSIS_CONFIGURATION__INDEPENDENT_PARAMETERS:
				getIndependentParameters().clear();
				getIndependentParameters().addAll((Collection<? extends ParameterDefinition>)newValue);
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
			case AnalysisPackage.ANALYSIS_CONFIGURATION__DEPENDENT_PARAMETERS:
				getDependentParameters().clear();
				return;
			case AnalysisPackage.ANALYSIS_CONFIGURATION__INDEPENDENT_PARAMETERS:
				getIndependentParameters().clear();
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
			case AnalysisPackage.ANALYSIS_CONFIGURATION__DEPENDENT_PARAMETERS:
				return dependentParameters != null && !dependentParameters.isEmpty();
			case AnalysisPackage.ANALYSIS_CONFIGURATION__INDEPENDENT_PARAMETERS:
				return independentParameters != null && !independentParameters.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //AnalysisConfigurationImpl

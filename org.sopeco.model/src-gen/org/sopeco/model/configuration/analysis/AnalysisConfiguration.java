/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.analysis;

import org.eclipse.emf.common.util.EList;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.measurements.ExtensibleElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.sopeco.model.configuration.analysis.AnalysisConfiguration#getDependentParameters <em>Dependent Parameters</em>}</li>
 *   <li>{@link org.sopeco.model.configuration.analysis.AnalysisConfiguration#getIndependentParameters <em>Independent Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.sopeco.model.configuration.analysis.AnalysisPackage#getAnalysisConfiguration()
 * @model
 * @generated
 */
public interface AnalysisConfiguration extends ExtensibleElement {

	/**
	 * Returns the value of the '<em><b>Dependent Parameters</b></em>' reference list.
	 * The list contents are of type {@link org.sopeco.model.configuration.environment.ParameterDefinition}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dependent Parameters</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dependent Parameters</em>' reference list.
	 * @see org.sopeco.model.configuration.analysis.AnalysisPackage#getAnalysisConfiguration_DependentParameters()
	 * @model
	 * @generated
	 */
	EList<ParameterDefinition> getDependentParameters();

	/**
	 * Returns the value of the '<em><b>Independent Parameters</b></em>' reference list.
	 * The list contents are of type {@link org.sopeco.model.configuration.environment.ParameterDefinition}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Independent Parameters</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Independent Parameters</em>' reference list.
	 * @see org.sopeco.model.configuration.analysis.AnalysisPackage#getAnalysisConfiguration_IndependentParameters()
	 * @model
	 * @generated
	 */
	EList<ParameterDefinition> getIndependentParameters();
} // AnalysisConfiguration

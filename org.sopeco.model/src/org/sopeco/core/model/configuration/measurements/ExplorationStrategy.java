/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.core.model.configuration.measurements;

import org.eclipse.emf.common.util.EList;
import org.sopeco.core.model.configuration.analysis.AnalysisConfiguration;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Exploration Strategy</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.sopeco.core.model.configuration.measurements.ExplorationStrategy#getAnalysisConfigurations <em>Analysis Configurations</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.sopeco.core.model.configuration.measurements.MeasurementsPackage#getExplorationStrategy()
 * @model
 * @generated
 */
public interface ExplorationStrategy extends ExtensibleElement {

	/**
	 * Returns the value of the '<em><b>Analysis Configurations</b></em>' containment reference list.
	 * The list contents are of type {@link org.sopeco.core.model.configuration.analysis.AnalysisConfiguration}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Analysis Configurations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Analysis Configurations</em>' containment reference list.
	 * @see org.sopeco.core.model.configuration.measurements.MeasurementsPackage#getExplorationStrategy_AnalysisConfigurations()
	 * @model containment="true"
	 * @generated
	 */
	EList<AnalysisConfiguration> getAnalysisConfigurations();
} // ExplorationStrategy

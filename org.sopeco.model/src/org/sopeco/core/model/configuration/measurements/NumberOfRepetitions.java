/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.core.model.configuration.measurements;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Number Of Repetitions</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.sopeco.core.model.configuration.measurements.NumberOfRepetitions#getNumberOfRepetitions <em>Number Of Repetitions</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.sopeco.core.model.configuration.measurements.MeasurementsPackage#getNumberOfRepetitions()
 * @model
 * @generated
 */
public interface NumberOfRepetitions extends ExperimentTerminationCondition {
	/**
	 * Returns the value of the '<em><b>Number Of Repetitions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Number Of Repetitions</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Number Of Repetitions</em>' attribute.
	 * @see #setNumberOfRepetitions(long)
	 * @see org.sopeco.core.model.configuration.measurements.MeasurementsPackage#getNumberOfRepetitions_NumberOfRepetitions()
	 * @model
	 * @generated
	 */
	long getNumberOfRepetitions();

	/**
	 * Sets the value of the '{@link org.sopeco.core.model.configuration.measurements.NumberOfRepetitions#getNumberOfRepetitions <em>Number Of Repetitions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Number Of Repetitions</em>' attribute.
	 * @see #getNumberOfRepetitions()
	 * @generated
	 */
	void setNumberOfRepetitions(long value);

} // NumberOfRepetitions

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.core.model.configuration.measurements;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Time Out</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.sopeco.core.model.configuration.measurements.TimeOut#getMaxDuration <em>Max Duration</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.sopeco.core.model.configuration.measurements.MeasurementsPackage#getTimeOut()
 * @model
 * @generated
 */
public interface TimeOut extends ExperimentTerminationCondition {
	/**
	 * Returns the value of the '<em><b>Max Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Duration</em>' attribute.
	 * @see #setMaxDuration(long)
	 * @see org.sopeco.core.model.configuration.measurements.MeasurementsPackage#getTimeOut_MaxDuration()
	 * @model
	 * @generated
	 */
	long getMaxDuration();

	/**
	 * Sets the value of the '{@link org.sopeco.core.model.configuration.measurements.TimeOut#getMaxDuration <em>Max Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Duration</em>' attribute.
	 * @see #getMaxDuration()
	 * @generated
	 */
	void setMaxDuration(long value);

} // TimeOut

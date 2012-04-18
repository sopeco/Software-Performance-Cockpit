/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.measurements;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.sopeco.model.configuration.measurements.MeasurementsFactory
 * @model kind="package"
 * @generated
 */
public interface MeasurementsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "measurements";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://sopeco.org/configuration/measurements";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "measurements";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MeasurementsPackage eINSTANCE = org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.sopeco.model.configuration.measurements.impl.MeasurementSpecificationImpl <em>Measurement Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.model.configuration.measurements.impl.MeasurementSpecificationImpl
	 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getMeasurementSpecification()
	 * @generated
	 */
	int MEASUREMENT_SPECIFICATION = 0;

	/**
	 * The feature id for the '<em><b>Experiment Series Definitions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEASUREMENT_SPECIFICATION__EXPERIMENT_SERIES_DEFINITIONS = 0;

	/**
	 * The feature id for the '<em><b>Initialization Assignemts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEASUREMENT_SPECIFICATION__INITIALIZATION_ASSIGNEMTS = 1;

	/**
	 * The number of structural features of the '<em>Measurement Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEASUREMENT_SPECIFICATION_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.sopeco.model.configuration.measurements.impl.ExperimentSeriesDefinitionImpl <em>Experiment Series Definition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.model.configuration.measurements.impl.ExperimentSeriesDefinitionImpl
	 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getExperimentSeriesDefinition()
	 * @generated
	 */
	int EXPERIMENT_SERIES_DEFINITION = 1;

	/**
	 * The feature id for the '<em><b>Exploration Strategy</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT_SERIES_DEFINITION__EXPLORATION_STRATEGY = 0;

	/**
	 * The feature id for the '<em><b>Experiment Assignments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_ASSIGNMENTS = 1;

	/**
	 * The feature id for the '<em><b>Experiment Termination Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_TERMINATION_CONDITION = 2;

	/**
	 * The feature id for the '<em><b>Preperation Assignments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT_SERIES_DEFINITION__PREPERATION_ASSIGNMENTS = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT_SERIES_DEFINITION__NAME = 4;

	/**
	 * The number of structural features of the '<em>Experiment Series Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT_SERIES_DEFINITION_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.sopeco.model.configuration.measurements.impl.ParameterValueAssignmentImpl <em>Parameter Value Assignment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.model.configuration.measurements.impl.ParameterValueAssignmentImpl
	 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getParameterValueAssignment()
	 * @generated
	 */
	int PARAMETER_VALUE_ASSIGNMENT = 2;

	/**
	 * The feature id for the '<em><b>Parameter</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_VALUE_ASSIGNMENT__PARAMETER = 0;

	/**
	 * The number of structural features of the '<em>Parameter Value Assignment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_VALUE_ASSIGNMENT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.sopeco.model.configuration.measurements.impl.ExtensibleElementImpl <em>Extensible Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.model.configuration.measurements.impl.ExtensibleElementImpl
	 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getExtensibleElement()
	 * @generated
	 */
	int EXTENSIBLE_ELEMENT = 6;

	/**
	 * The feature id for the '<em><b>Configuration</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTENSIBLE_ELEMENT__CONFIGURATION = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTENSIBLE_ELEMENT__NAME = 1;

	/**
	 * The number of structural features of the '<em>Extensible Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTENSIBLE_ELEMENT_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.sopeco.model.configuration.measurements.impl.ExplorationStrategyImpl <em>Exploration Strategy</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.model.configuration.measurements.impl.ExplorationStrategyImpl
	 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getExplorationStrategy()
	 * @generated
	 */
	int EXPLORATION_STRATEGY = 3;

	/**
	 * The feature id for the '<em><b>Configuration</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPLORATION_STRATEGY__CONFIGURATION = EXTENSIBLE_ELEMENT__CONFIGURATION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPLORATION_STRATEGY__NAME = EXTENSIBLE_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Analysis Configurations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPLORATION_STRATEGY__ANALYSIS_CONFIGURATIONS = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Exploration Strategy</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPLORATION_STRATEGY_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.sopeco.model.configuration.measurements.impl.ExperimentTerminationConditionImpl <em>Experiment Termination Condition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.model.configuration.measurements.impl.ExperimentTerminationConditionImpl
	 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getExperimentTerminationCondition()
	 * @generated
	 */
	int EXPERIMENT_TERMINATION_CONDITION = 4;

	/**
	 * The number of structural features of the '<em>Experiment Termination Condition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT_TERMINATION_CONDITION_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.sopeco.model.configuration.measurements.impl.ConfigurationNodeImpl <em>Configuration Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.model.configuration.measurements.impl.ConfigurationNodeImpl
	 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getConfigurationNode()
	 * @generated
	 */
	int CONFIGURATION_NODE = 5;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_NODE__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_NODE__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Configuration Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_NODE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.sopeco.model.configuration.measurements.impl.NumberOfRepetitionsImpl <em>Number Of Repetitions</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.model.configuration.measurements.impl.NumberOfRepetitionsImpl
	 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getNumberOfRepetitions()
	 * @generated
	 */
	int NUMBER_OF_REPETITIONS = 7;

	/**
	 * The feature id for the '<em><b>Number Of Repetitions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NUMBER_OF_REPETITIONS__NUMBER_OF_REPETITIONS = EXPERIMENT_TERMINATION_CONDITION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Number Of Repetitions</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NUMBER_OF_REPETITIONS_FEATURE_COUNT = EXPERIMENT_TERMINATION_CONDITION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.sopeco.model.configuration.measurements.impl.TimeOutImpl <em>Time Out</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.model.configuration.measurements.impl.TimeOutImpl
	 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getTimeOut()
	 * @generated
	 */
	int TIME_OUT = 8;

	/**
	 * The feature id for the '<em><b>Max Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_OUT__MAX_DURATION = EXPERIMENT_TERMINATION_CONDITION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Time Out</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_OUT_FEATURE_COUNT = EXPERIMENT_TERMINATION_CONDITION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.sopeco.model.configuration.measurements.impl.ConstantValueAssignmentImpl <em>Constant Value Assignment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.model.configuration.measurements.impl.ConstantValueAssignmentImpl
	 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getConstantValueAssignment()
	 * @generated
	 */
	int CONSTANT_VALUE_ASSIGNMENT = 9;

	/**
	 * The feature id for the '<em><b>Parameter</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTANT_VALUE_ASSIGNMENT__PARAMETER = PARAMETER_VALUE_ASSIGNMENT__PARAMETER;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTANT_VALUE_ASSIGNMENT__VALUE = PARAMETER_VALUE_ASSIGNMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Constant Value Assignment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTANT_VALUE_ASSIGNMENT_FEATURE_COUNT = PARAMETER_VALUE_ASSIGNMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.sopeco.model.configuration.measurements.impl.DynamicValueAssignmentImpl <em>Dynamic Value Assignment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.sopeco.model.configuration.measurements.impl.DynamicValueAssignmentImpl
	 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getDynamicValueAssignment()
	 * @generated
	 */
	int DYNAMIC_VALUE_ASSIGNMENT = 10;

	/**
	 * The feature id for the '<em><b>Configuration</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_VALUE_ASSIGNMENT__CONFIGURATION = EXTENSIBLE_ELEMENT__CONFIGURATION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_VALUE_ASSIGNMENT__NAME = EXTENSIBLE_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Parameter</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_VALUE_ASSIGNMENT__PARAMETER = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Dynamic Value Assignment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_VALUE_ASSIGNMENT_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;


	/**
	 * Returns the meta object for class '{@link org.sopeco.model.configuration.measurements.MeasurementSpecification <em>Measurement Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Measurement Specification</em>'.
	 * @see org.sopeco.model.configuration.measurements.MeasurementSpecification
	 * @generated
	 */
	EClass getMeasurementSpecification();

	/**
	 * Returns the meta object for the containment reference list '{@link org.sopeco.model.configuration.measurements.MeasurementSpecification#getExperimentSeriesDefinitions <em>Experiment Series Definitions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Experiment Series Definitions</em>'.
	 * @see org.sopeco.model.configuration.measurements.MeasurementSpecification#getExperimentSeriesDefinitions()
	 * @see #getMeasurementSpecification()
	 * @generated
	 */
	EReference getMeasurementSpecification_ExperimentSeriesDefinitions();

	/**
	 * Returns the meta object for the containment reference list '{@link org.sopeco.model.configuration.measurements.MeasurementSpecification#getInitializationAssignemts <em>Initialization Assignemts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Initialization Assignemts</em>'.
	 * @see org.sopeco.model.configuration.measurements.MeasurementSpecification#getInitializationAssignemts()
	 * @see #getMeasurementSpecification()
	 * @generated
	 */
	EReference getMeasurementSpecification_InitializationAssignemts();

	/**
	 * Returns the meta object for class '{@link org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition <em>Experiment Series Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Experiment Series Definition</em>'.
	 * @see org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition
	 * @generated
	 */
	EClass getExperimentSeriesDefinition();

	/**
	 * Returns the meta object for the containment reference '{@link org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getExplorationStrategy <em>Exploration Strategy</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Exploration Strategy</em>'.
	 * @see org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getExplorationStrategy()
	 * @see #getExperimentSeriesDefinition()
	 * @generated
	 */
	EReference getExperimentSeriesDefinition_ExplorationStrategy();

	/**
	 * Returns the meta object for the containment reference list '{@link org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getExperimentAssignments <em>Experiment Assignments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Experiment Assignments</em>'.
	 * @see org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getExperimentAssignments()
	 * @see #getExperimentSeriesDefinition()
	 * @generated
	 */
	EReference getExperimentSeriesDefinition_ExperimentAssignments();

	/**
	 * Returns the meta object for the containment reference '{@link org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getExperimentTerminationCondition <em>Experiment Termination Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Experiment Termination Condition</em>'.
	 * @see org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getExperimentTerminationCondition()
	 * @see #getExperimentSeriesDefinition()
	 * @generated
	 */
	EReference getExperimentSeriesDefinition_ExperimentTerminationCondition();

	/**
	 * Returns the meta object for the containment reference list '{@link org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getPreperationAssignments <em>Preperation Assignments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Preperation Assignments</em>'.
	 * @see org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getPreperationAssignments()
	 * @see #getExperimentSeriesDefinition()
	 * @generated
	 */
	EReference getExperimentSeriesDefinition_PreperationAssignments();

	/**
	 * Returns the meta object for the attribute '{@link org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition#getName()
	 * @see #getExperimentSeriesDefinition()
	 * @generated
	 */
	EAttribute getExperimentSeriesDefinition_Name();

	/**
	 * Returns the meta object for class '{@link org.sopeco.model.configuration.measurements.ParameterValueAssignment <em>Parameter Value Assignment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameter Value Assignment</em>'.
	 * @see org.sopeco.model.configuration.measurements.ParameterValueAssignment
	 * @generated
	 */
	EClass getParameterValueAssignment();

	/**
	 * Returns the meta object for the reference '{@link org.sopeco.model.configuration.measurements.ParameterValueAssignment#getParameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Parameter</em>'.
	 * @see org.sopeco.model.configuration.measurements.ParameterValueAssignment#getParameter()
	 * @see #getParameterValueAssignment()
	 * @generated
	 */
	EReference getParameterValueAssignment_Parameter();

	/**
	 * Returns the meta object for class '{@link org.sopeco.model.configuration.measurements.ExplorationStrategy <em>Exploration Strategy</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Exploration Strategy</em>'.
	 * @see org.sopeco.model.configuration.measurements.ExplorationStrategy
	 * @generated
	 */
	EClass getExplorationStrategy();

	/**
	 * Returns the meta object for the containment reference list '{@link org.sopeco.model.configuration.measurements.ExplorationStrategy#getAnalysisConfigurations <em>Analysis Configurations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Analysis Configurations</em>'.
	 * @see org.sopeco.model.configuration.measurements.ExplorationStrategy#getAnalysisConfigurations()
	 * @see #getExplorationStrategy()
	 * @generated
	 */
	EReference getExplorationStrategy_AnalysisConfigurations();

	/**
	 * Returns the meta object for class '{@link org.sopeco.model.configuration.measurements.ExperimentTerminationCondition <em>Experiment Termination Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Experiment Termination Condition</em>'.
	 * @see org.sopeco.model.configuration.measurements.ExperimentTerminationCondition
	 * @generated
	 */
	EClass getExperimentTerminationCondition();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Configuration Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Configuration Node</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueDataType="org.eclipse.emf.ecore.EString"
	 * @generated
	 */
	EClass getConfigurationNode();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getConfigurationNode()
	 * @generated
	 */
	EAttribute getConfigurationNode_Key();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getConfigurationNode()
	 * @generated
	 */
	EAttribute getConfigurationNode_Value();

	/**
	 * Returns the meta object for class '{@link org.sopeco.model.configuration.measurements.ExtensibleElement <em>Extensible Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Extensible Element</em>'.
	 * @see org.sopeco.model.configuration.measurements.ExtensibleElement
	 * @generated
	 */
	EClass getExtensibleElement();

	/**
	 * Returns the meta object for the map '{@link org.sopeco.model.configuration.measurements.ExtensibleElement#getConfiguration <em>Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Configuration</em>'.
	 * @see org.sopeco.model.configuration.measurements.ExtensibleElement#getConfiguration()
	 * @see #getExtensibleElement()
	 * @generated
	 */
	EReference getExtensibleElement_Configuration();

	/**
	 * Returns the meta object for the attribute '{@link org.sopeco.model.configuration.measurements.ExtensibleElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.sopeco.model.configuration.measurements.ExtensibleElement#getName()
	 * @see #getExtensibleElement()
	 * @generated
	 */
	EAttribute getExtensibleElement_Name();

	/**
	 * Returns the meta object for class '{@link org.sopeco.model.configuration.measurements.NumberOfRepetitions <em>Number Of Repetitions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Number Of Repetitions</em>'.
	 * @see org.sopeco.model.configuration.measurements.NumberOfRepetitions
	 * @generated
	 */
	EClass getNumberOfRepetitions();

	/**
	 * Returns the meta object for the attribute '{@link org.sopeco.model.configuration.measurements.NumberOfRepetitions#getNumberOfRepetitions <em>Number Of Repetitions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number Of Repetitions</em>'.
	 * @see org.sopeco.model.configuration.measurements.NumberOfRepetitions#getNumberOfRepetitions()
	 * @see #getNumberOfRepetitions()
	 * @generated
	 */
	EAttribute getNumberOfRepetitions_NumberOfRepetitions();

	/**
	 * Returns the meta object for class '{@link org.sopeco.model.configuration.measurements.TimeOut <em>Time Out</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Time Out</em>'.
	 * @see org.sopeco.model.configuration.measurements.TimeOut
	 * @generated
	 */
	EClass getTimeOut();

	/**
	 * Returns the meta object for the attribute '{@link org.sopeco.model.configuration.measurements.TimeOut#getMaxDuration <em>Max Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Duration</em>'.
	 * @see org.sopeco.model.configuration.measurements.TimeOut#getMaxDuration()
	 * @see #getTimeOut()
	 * @generated
	 */
	EAttribute getTimeOut_MaxDuration();

	/**
	 * Returns the meta object for class '{@link org.sopeco.model.configuration.measurements.ConstantValueAssignment <em>Constant Value Assignment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Constant Value Assignment</em>'.
	 * @see org.sopeco.model.configuration.measurements.ConstantValueAssignment
	 * @generated
	 */
	EClass getConstantValueAssignment();

	/**
	 * Returns the meta object for the attribute '{@link org.sopeco.model.configuration.measurements.ConstantValueAssignment#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.sopeco.model.configuration.measurements.ConstantValueAssignment#getValue()
	 * @see #getConstantValueAssignment()
	 * @generated
	 */
	EAttribute getConstantValueAssignment_Value();

	/**
	 * Returns the meta object for class '{@link org.sopeco.model.configuration.measurements.DynamicValueAssignment <em>Dynamic Value Assignment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dynamic Value Assignment</em>'.
	 * @see org.sopeco.model.configuration.measurements.DynamicValueAssignment
	 * @generated
	 */
	EClass getDynamicValueAssignment();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MeasurementsFactory getMeasurementsFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.sopeco.model.configuration.measurements.impl.MeasurementSpecificationImpl <em>Measurement Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.model.configuration.measurements.impl.MeasurementSpecificationImpl
		 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getMeasurementSpecification()
		 * @generated
		 */
		EClass MEASUREMENT_SPECIFICATION = eINSTANCE.getMeasurementSpecification();

		/**
		 * The meta object literal for the '<em><b>Experiment Series Definitions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MEASUREMENT_SPECIFICATION__EXPERIMENT_SERIES_DEFINITIONS = eINSTANCE.getMeasurementSpecification_ExperimentSeriesDefinitions();

		/**
		 * The meta object literal for the '<em><b>Initialization Assignemts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MEASUREMENT_SPECIFICATION__INITIALIZATION_ASSIGNEMTS = eINSTANCE.getMeasurementSpecification_InitializationAssignemts();

		/**
		 * The meta object literal for the '{@link org.sopeco.model.configuration.measurements.impl.ExperimentSeriesDefinitionImpl <em>Experiment Series Definition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.model.configuration.measurements.impl.ExperimentSeriesDefinitionImpl
		 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getExperimentSeriesDefinition()
		 * @generated
		 */
		EClass EXPERIMENT_SERIES_DEFINITION = eINSTANCE.getExperimentSeriesDefinition();

		/**
		 * The meta object literal for the '<em><b>Exploration Strategy</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXPERIMENT_SERIES_DEFINITION__EXPLORATION_STRATEGY = eINSTANCE.getExperimentSeriesDefinition_ExplorationStrategy();

		/**
		 * The meta object literal for the '<em><b>Experiment Assignments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_ASSIGNMENTS = eINSTANCE.getExperimentSeriesDefinition_ExperimentAssignments();

		/**
		 * The meta object literal for the '<em><b>Experiment Termination Condition</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_TERMINATION_CONDITION = eINSTANCE.getExperimentSeriesDefinition_ExperimentTerminationCondition();

		/**
		 * The meta object literal for the '<em><b>Preperation Assignments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXPERIMENT_SERIES_DEFINITION__PREPERATION_ASSIGNMENTS = eINSTANCE.getExperimentSeriesDefinition_PreperationAssignments();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPERIMENT_SERIES_DEFINITION__NAME = eINSTANCE.getExperimentSeriesDefinition_Name();

		/**
		 * The meta object literal for the '{@link org.sopeco.model.configuration.measurements.impl.ParameterValueAssignmentImpl <em>Parameter Value Assignment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.model.configuration.measurements.impl.ParameterValueAssignmentImpl
		 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getParameterValueAssignment()
		 * @generated
		 */
		EClass PARAMETER_VALUE_ASSIGNMENT = eINSTANCE.getParameterValueAssignment();

		/**
		 * The meta object literal for the '<em><b>Parameter</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETER_VALUE_ASSIGNMENT__PARAMETER = eINSTANCE.getParameterValueAssignment_Parameter();

		/**
		 * The meta object literal for the '{@link org.sopeco.model.configuration.measurements.impl.ExplorationStrategyImpl <em>Exploration Strategy</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.model.configuration.measurements.impl.ExplorationStrategyImpl
		 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getExplorationStrategy()
		 * @generated
		 */
		EClass EXPLORATION_STRATEGY = eINSTANCE.getExplorationStrategy();

		/**
		 * The meta object literal for the '<em><b>Analysis Configurations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXPLORATION_STRATEGY__ANALYSIS_CONFIGURATIONS = eINSTANCE.getExplorationStrategy_AnalysisConfigurations();

		/**
		 * The meta object literal for the '{@link org.sopeco.model.configuration.measurements.impl.ExperimentTerminationConditionImpl <em>Experiment Termination Condition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.model.configuration.measurements.impl.ExperimentTerminationConditionImpl
		 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getExperimentTerminationCondition()
		 * @generated
		 */
		EClass EXPERIMENT_TERMINATION_CONDITION = eINSTANCE.getExperimentTerminationCondition();

		/**
		 * The meta object literal for the '{@link org.sopeco.model.configuration.measurements.impl.ConfigurationNodeImpl <em>Configuration Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.model.configuration.measurements.impl.ConfigurationNodeImpl
		 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getConfigurationNode()
		 * @generated
		 */
		EClass CONFIGURATION_NODE = eINSTANCE.getConfigurationNode();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONFIGURATION_NODE__KEY = eINSTANCE.getConfigurationNode_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONFIGURATION_NODE__VALUE = eINSTANCE.getConfigurationNode_Value();

		/**
		 * The meta object literal for the '{@link org.sopeco.model.configuration.measurements.impl.ExtensibleElementImpl <em>Extensible Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.model.configuration.measurements.impl.ExtensibleElementImpl
		 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getExtensibleElement()
		 * @generated
		 */
		EClass EXTENSIBLE_ELEMENT = eINSTANCE.getExtensibleElement();

		/**
		 * The meta object literal for the '<em><b>Configuration</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXTENSIBLE_ELEMENT__CONFIGURATION = eINSTANCE.getExtensibleElement_Configuration();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXTENSIBLE_ELEMENT__NAME = eINSTANCE.getExtensibleElement_Name();

		/**
		 * The meta object literal for the '{@link org.sopeco.model.configuration.measurements.impl.NumberOfRepetitionsImpl <em>Number Of Repetitions</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.model.configuration.measurements.impl.NumberOfRepetitionsImpl
		 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getNumberOfRepetitions()
		 * @generated
		 */
		EClass NUMBER_OF_REPETITIONS = eINSTANCE.getNumberOfRepetitions();

		/**
		 * The meta object literal for the '<em><b>Number Of Repetitions</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NUMBER_OF_REPETITIONS__NUMBER_OF_REPETITIONS = eINSTANCE.getNumberOfRepetitions_NumberOfRepetitions();

		/**
		 * The meta object literal for the '{@link org.sopeco.model.configuration.measurements.impl.TimeOutImpl <em>Time Out</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.model.configuration.measurements.impl.TimeOutImpl
		 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getTimeOut()
		 * @generated
		 */
		EClass TIME_OUT = eINSTANCE.getTimeOut();

		/**
		 * The meta object literal for the '<em><b>Max Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TIME_OUT__MAX_DURATION = eINSTANCE.getTimeOut_MaxDuration();

		/**
		 * The meta object literal for the '{@link org.sopeco.model.configuration.measurements.impl.ConstantValueAssignmentImpl <em>Constant Value Assignment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.model.configuration.measurements.impl.ConstantValueAssignmentImpl
		 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getConstantValueAssignment()
		 * @generated
		 */
		EClass CONSTANT_VALUE_ASSIGNMENT = eINSTANCE.getConstantValueAssignment();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONSTANT_VALUE_ASSIGNMENT__VALUE = eINSTANCE.getConstantValueAssignment_Value();

		/**
		 * The meta object literal for the '{@link org.sopeco.model.configuration.measurements.impl.DynamicValueAssignmentImpl <em>Dynamic Value Assignment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.sopeco.model.configuration.measurements.impl.DynamicValueAssignmentImpl
		 * @see org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl#getDynamicValueAssignment()
		 * @generated
		 */
		EClass DYNAMIC_VALUE_ASSIGNMENT = eINSTANCE.getDynamicValueAssignment();

	}

} //MeasurementsPackage

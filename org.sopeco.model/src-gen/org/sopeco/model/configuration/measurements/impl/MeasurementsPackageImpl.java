/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.measurements.impl;

import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.sopeco.model.configuration.ConfigurationPackage;

import org.sopeco.model.configuration.analysis.AnalysisPackage;

import org.sopeco.model.configuration.analysis.impl.AnalysisPackageImpl;

import org.sopeco.model.configuration.common.CommonPackage;

import org.sopeco.model.configuration.common.impl.CommonPackageImpl;

import org.sopeco.model.configuration.environment.EnvironmentPackage;

import org.sopeco.model.configuration.environment.impl.EnvironmentPackageImpl;

import org.sopeco.model.configuration.impl.ConfigurationPackageImpl;

import org.sopeco.model.configuration.measurements.ConstantValueAssignment;
import org.sopeco.model.configuration.measurements.DynamicValueAssignment;
import org.sopeco.model.configuration.measurements.ExperimentSeriesDefinition;
import org.sopeco.model.configuration.measurements.ExperimentTerminationCondition;
import org.sopeco.model.configuration.measurements.ExplorationStrategy;
import org.sopeco.model.configuration.measurements.ExtensibleElement;
import org.sopeco.model.configuration.measurements.MeasurementSpecification;
import org.sopeco.model.configuration.measurements.MeasurementsFactory;
import org.sopeco.model.configuration.measurements.MeasurementsPackage;
import org.sopeco.model.configuration.measurements.NumberOfRepetitions;
import org.sopeco.model.configuration.measurements.ParameterValueAssignment;
import org.sopeco.model.configuration.measurements.TimeOut;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MeasurementsPackageImpl extends EPackageImpl implements MeasurementsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass measurementSpecificationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass experimentSeriesDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parameterValueAssignmentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass explorationStrategyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass experimentTerminationConditionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass configurationNodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass extensibleElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass numberOfRepetitionsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass timeOutEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass constantValueAssignmentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dynamicValueAssignmentEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.sopeco.model.configuration.measurements.MeasurementsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MeasurementsPackageImpl() {
		super(eNS_URI, MeasurementsFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link MeasurementsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static MeasurementsPackage init() {
		if (isInited) return (MeasurementsPackage)EPackage.Registry.INSTANCE.getEPackage(MeasurementsPackage.eNS_URI);

		// Obtain or create and register package
		MeasurementsPackageImpl theMeasurementsPackage = (MeasurementsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof MeasurementsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new MeasurementsPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		ConfigurationPackageImpl theConfigurationPackage = (ConfigurationPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ConfigurationPackage.eNS_URI) instanceof ConfigurationPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ConfigurationPackage.eNS_URI) : ConfigurationPackage.eINSTANCE);
		EnvironmentPackageImpl theEnvironmentPackage = (EnvironmentPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(EnvironmentPackage.eNS_URI) instanceof EnvironmentPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(EnvironmentPackage.eNS_URI) : EnvironmentPackage.eINSTANCE);
		AnalysisPackageImpl theAnalysisPackage = (AnalysisPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(AnalysisPackage.eNS_URI) instanceof AnalysisPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(AnalysisPackage.eNS_URI) : AnalysisPackage.eINSTANCE);
		CommonPackageImpl theCommonPackage = (CommonPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI) instanceof CommonPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI) : CommonPackage.eINSTANCE);

		// Create package meta-data objects
		theMeasurementsPackage.createPackageContents();
		theConfigurationPackage.createPackageContents();
		theEnvironmentPackage.createPackageContents();
		theAnalysisPackage.createPackageContents();
		theCommonPackage.createPackageContents();

		// Initialize created meta-data
		theMeasurementsPackage.initializePackageContents();
		theConfigurationPackage.initializePackageContents();
		theEnvironmentPackage.initializePackageContents();
		theAnalysisPackage.initializePackageContents();
		theCommonPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theMeasurementsPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(MeasurementsPackage.eNS_URI, theMeasurementsPackage);
		return theMeasurementsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMeasurementSpecification() {
		return measurementSpecificationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMeasurementSpecification_ExperimentSeriesDefinitions() {
		return (EReference)measurementSpecificationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMeasurementSpecification_InitializationAssignemts() {
		return (EReference)measurementSpecificationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMeasurementSpecification_Name() {
		return (EAttribute)measurementSpecificationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExperimentSeriesDefinition() {
		return experimentSeriesDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExperimentSeriesDefinition_ExplorationStrategy() {
		return (EReference)experimentSeriesDefinitionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExperimentSeriesDefinition_ExperimentAssignments() {
		return (EReference)experimentSeriesDefinitionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExperimentSeriesDefinition_ExperimentTerminationCondition() {
		return (EReference)experimentSeriesDefinitionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExperimentSeriesDefinition_PreperationAssignments() {
		return (EReference)experimentSeriesDefinitionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExperimentSeriesDefinition_Name() {
		return (EAttribute)experimentSeriesDefinitionEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParameterValueAssignment() {
		return parameterValueAssignmentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameterValueAssignment_Parameter() {
		return (EReference)parameterValueAssignmentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExplorationStrategy() {
		return explorationStrategyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExplorationStrategy_AnalysisConfigurations() {
		return (EReference)explorationStrategyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExperimentTerminationCondition() {
		return experimentTerminationConditionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConfigurationNode() {
		return configurationNodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConfigurationNode_Key() {
		return (EAttribute)configurationNodeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConfigurationNode_Value() {
		return (EAttribute)configurationNodeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExtensibleElement() {
		return extensibleElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExtensibleElement_Configuration() {
		return (EReference)extensibleElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExtensibleElement_Name() {
		return (EAttribute)extensibleElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNumberOfRepetitions() {
		return numberOfRepetitionsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNumberOfRepetitions_NumberOfRepetitions() {
		return (EAttribute)numberOfRepetitionsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTimeOut() {
		return timeOutEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTimeOut_MaxDuration() {
		return (EAttribute)timeOutEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConstantValueAssignment() {
		return constantValueAssignmentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConstantValueAssignment_Value() {
		return (EAttribute)constantValueAssignmentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDynamicValueAssignment() {
		return dynamicValueAssignmentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MeasurementsFactory getMeasurementsFactory() {
		return (MeasurementsFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		measurementSpecificationEClass = createEClass(MEASUREMENT_SPECIFICATION);
		createEReference(measurementSpecificationEClass, MEASUREMENT_SPECIFICATION__EXPERIMENT_SERIES_DEFINITIONS);
		createEReference(measurementSpecificationEClass, MEASUREMENT_SPECIFICATION__INITIALIZATION_ASSIGNEMTS);
		createEAttribute(measurementSpecificationEClass, MEASUREMENT_SPECIFICATION__NAME);

		experimentSeriesDefinitionEClass = createEClass(EXPERIMENT_SERIES_DEFINITION);
		createEReference(experimentSeriesDefinitionEClass, EXPERIMENT_SERIES_DEFINITION__EXPLORATION_STRATEGY);
		createEReference(experimentSeriesDefinitionEClass, EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_ASSIGNMENTS);
		createEReference(experimentSeriesDefinitionEClass, EXPERIMENT_SERIES_DEFINITION__EXPERIMENT_TERMINATION_CONDITION);
		createEReference(experimentSeriesDefinitionEClass, EXPERIMENT_SERIES_DEFINITION__PREPERATION_ASSIGNMENTS);
		createEAttribute(experimentSeriesDefinitionEClass, EXPERIMENT_SERIES_DEFINITION__NAME);

		parameterValueAssignmentEClass = createEClass(PARAMETER_VALUE_ASSIGNMENT);
		createEReference(parameterValueAssignmentEClass, PARAMETER_VALUE_ASSIGNMENT__PARAMETER);

		explorationStrategyEClass = createEClass(EXPLORATION_STRATEGY);
		createEReference(explorationStrategyEClass, EXPLORATION_STRATEGY__ANALYSIS_CONFIGURATIONS);

		experimentTerminationConditionEClass = createEClass(EXPERIMENT_TERMINATION_CONDITION);

		configurationNodeEClass = createEClass(CONFIGURATION_NODE);
		createEAttribute(configurationNodeEClass, CONFIGURATION_NODE__KEY);
		createEAttribute(configurationNodeEClass, CONFIGURATION_NODE__VALUE);

		extensibleElementEClass = createEClass(EXTENSIBLE_ELEMENT);
		createEReference(extensibleElementEClass, EXTENSIBLE_ELEMENT__CONFIGURATION);
		createEAttribute(extensibleElementEClass, EXTENSIBLE_ELEMENT__NAME);

		numberOfRepetitionsEClass = createEClass(NUMBER_OF_REPETITIONS);
		createEAttribute(numberOfRepetitionsEClass, NUMBER_OF_REPETITIONS__NUMBER_OF_REPETITIONS);

		timeOutEClass = createEClass(TIME_OUT);
		createEAttribute(timeOutEClass, TIME_OUT__MAX_DURATION);

		constantValueAssignmentEClass = createEClass(CONSTANT_VALUE_ASSIGNMENT);
		createEAttribute(constantValueAssignmentEClass, CONSTANT_VALUE_ASSIGNMENT__VALUE);

		dynamicValueAssignmentEClass = createEClass(DYNAMIC_VALUE_ASSIGNMENT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		CommonPackage theCommonPackage = (CommonPackage)EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI);
		EnvironmentPackage theEnvironmentPackage = (EnvironmentPackage)EPackage.Registry.INSTANCE.getEPackage(EnvironmentPackage.eNS_URI);
		AnalysisPackage theAnalysisPackage = (AnalysisPackage)EPackage.Registry.INSTANCE.getEPackage(AnalysisPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		experimentSeriesDefinitionEClass.getESuperTypes().add(theCommonPackage.getSerializable());
		parameterValueAssignmentEClass.getESuperTypes().add(theCommonPackage.getSerializable());
		explorationStrategyEClass.getESuperTypes().add(this.getExtensibleElement());
		experimentTerminationConditionEClass.getESuperTypes().add(theCommonPackage.getSerializable());
		configurationNodeEClass.getESuperTypes().add(theCommonPackage.getSerializable());
		extensibleElementEClass.getESuperTypes().add(theCommonPackage.getSerializable());
		numberOfRepetitionsEClass.getESuperTypes().add(this.getExperimentTerminationCondition());
		timeOutEClass.getESuperTypes().add(this.getExperimentTerminationCondition());
		constantValueAssignmentEClass.getESuperTypes().add(this.getParameterValueAssignment());
		dynamicValueAssignmentEClass.getESuperTypes().add(this.getExtensibleElement());
		dynamicValueAssignmentEClass.getESuperTypes().add(this.getParameterValueAssignment());

		// Initialize classes and features; add operations and parameters
		initEClass(measurementSpecificationEClass, MeasurementSpecification.class, "MeasurementSpecification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMeasurementSpecification_ExperimentSeriesDefinitions(), this.getExperimentSeriesDefinition(), null, "experimentSeriesDefinitions", null, 1, -1, MeasurementSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMeasurementSpecification_InitializationAssignemts(), this.getConstantValueAssignment(), null, "initializationAssignemts", null, 0, -1, MeasurementSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMeasurementSpecification_Name(), ecorePackage.getEString(), "name", null, 1, 1, MeasurementSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(experimentSeriesDefinitionEClass, ExperimentSeriesDefinition.class, "ExperimentSeriesDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getExperimentSeriesDefinition_ExplorationStrategy(), this.getExplorationStrategy(), null, "explorationStrategy", null, 1, 1, ExperimentSeriesDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExperimentSeriesDefinition_ExperimentAssignments(), this.getParameterValueAssignment(), null, "experimentAssignments", null, 1, -1, ExperimentSeriesDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExperimentSeriesDefinition_ExperimentTerminationCondition(), this.getExperimentTerminationCondition(), null, "experimentTerminationCondition", null, 1, 1, ExperimentSeriesDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExperimentSeriesDefinition_PreperationAssignments(), this.getConstantValueAssignment(), null, "preperationAssignments", null, 0, -1, ExperimentSeriesDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExperimentSeriesDefinition_Name(), ecorePackage.getEString(), "name", null, 1, 1, ExperimentSeriesDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parameterValueAssignmentEClass, ParameterValueAssignment.class, "ParameterValueAssignment", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getParameterValueAssignment_Parameter(), theEnvironmentPackage.getParameterDefinition(), null, "parameter", null, 1, 1, ParameterValueAssignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(explorationStrategyEClass, ExplorationStrategy.class, "ExplorationStrategy", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getExplorationStrategy_AnalysisConfigurations(), theAnalysisPackage.getAnalysisConfiguration(), null, "analysisConfigurations", null, 0, -1, ExplorationStrategy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(experimentTerminationConditionEClass, ExperimentTerminationCondition.class, "ExperimentTerminationCondition", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(configurationNodeEClass, Map.Entry.class, "ConfigurationNode", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConfigurationNode_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConfigurationNode_Value(), ecorePackage.getEString(), "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(extensibleElementEClass, ExtensibleElement.class, "ExtensibleElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getExtensibleElement_Configuration(), this.getConfigurationNode(), null, "configuration", null, 0, -1, ExtensibleElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExtensibleElement_Name(), ecorePackage.getEString(), "name", null, 0, 1, ExtensibleElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(numberOfRepetitionsEClass, NumberOfRepetitions.class, "NumberOfRepetitions", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNumberOfRepetitions_NumberOfRepetitions(), ecorePackage.getELong(), "numberOfRepetitions", null, 0, 1, NumberOfRepetitions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(timeOutEClass, TimeOut.class, "TimeOut", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTimeOut_MaxDuration(), ecorePackage.getELong(), "maxDuration", null, 0, 1, TimeOut.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(constantValueAssignmentEClass, ConstantValueAssignment.class, "ConstantValueAssignment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConstantValueAssignment_Value(), ecorePackage.getEString(), "value", null, 0, 1, ConstantValueAssignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dynamicValueAssignmentEClass, DynamicValueAssignment.class, "DynamicValueAssignment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
	}

} //MeasurementsPackageImpl

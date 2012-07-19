/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sopeco.model.configuration.environment.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.sopeco.model.configuration.ConfigurationPackage;

import org.sopeco.model.configuration.analysis.AnalysisPackage;

import org.sopeco.model.configuration.analysis.impl.AnalysisPackageImpl;

import org.sopeco.model.configuration.common.CommonPackage;

import org.sopeco.model.configuration.common.impl.CommonPackageImpl;

import org.sopeco.model.configuration.environment.EnvironmentFactory;
import org.sopeco.model.configuration.environment.EnvironmentPackage;
import org.sopeco.model.configuration.environment.MeasurementEnvironmentDefinition;
import org.sopeco.model.configuration.environment.ParameterDefinition;
import org.sopeco.model.configuration.environment.ParameterNamespace;
import org.sopeco.model.configuration.environment.ParameterRole;

import org.sopeco.model.configuration.impl.ConfigurationPackageImpl;

import org.sopeco.model.configuration.measurements.MeasurementsPackage;

import org.sopeco.model.configuration.measurements.impl.MeasurementsPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EnvironmentPackageImpl extends EPackageImpl implements EnvironmentPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass measurementEnvironmentDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parameterNamespaceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parameterDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum parameterRoleEEnum = null;

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
	 * @see org.sopeco.model.configuration.environment.EnvironmentPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private EnvironmentPackageImpl() {
		super(eNS_URI, EnvironmentFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link EnvironmentPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static EnvironmentPackage init() {
		if (isInited) return (EnvironmentPackage)EPackage.Registry.INSTANCE.getEPackage(EnvironmentPackage.eNS_URI);

		// Obtain or create and register package
		EnvironmentPackageImpl theEnvironmentPackage = (EnvironmentPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EnvironmentPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EnvironmentPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		ConfigurationPackageImpl theConfigurationPackage = (ConfigurationPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ConfigurationPackage.eNS_URI) instanceof ConfigurationPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ConfigurationPackage.eNS_URI) : ConfigurationPackage.eINSTANCE);
		MeasurementsPackageImpl theMeasurementsPackage = (MeasurementsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(MeasurementsPackage.eNS_URI) instanceof MeasurementsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(MeasurementsPackage.eNS_URI) : MeasurementsPackage.eINSTANCE);
		AnalysisPackageImpl theAnalysisPackage = (AnalysisPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(AnalysisPackage.eNS_URI) instanceof AnalysisPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(AnalysisPackage.eNS_URI) : AnalysisPackage.eINSTANCE);
		CommonPackageImpl theCommonPackage = (CommonPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI) instanceof CommonPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI) : CommonPackage.eINSTANCE);

		// Create package meta-data objects
		theEnvironmentPackage.createPackageContents();
		theConfigurationPackage.createPackageContents();
		theMeasurementsPackage.createPackageContents();
		theAnalysisPackage.createPackageContents();
		theCommonPackage.createPackageContents();

		// Initialize created meta-data
		theEnvironmentPackage.initializePackageContents();
		theConfigurationPackage.initializePackageContents();
		theMeasurementsPackage.initializePackageContents();
		theAnalysisPackage.initializePackageContents();
		theCommonPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theEnvironmentPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(EnvironmentPackage.eNS_URI, theEnvironmentPackage);
		return theEnvironmentPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMeasurementEnvironmentDefinition() {
		return measurementEnvironmentDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMeasurementEnvironmentDefinition_Root() {
		return (EReference)measurementEnvironmentDefinitionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParameterNamespace() {
		return parameterNamespaceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameterNamespace_Children() {
		return (EReference)parameterNamespaceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameterNamespace_Name() {
		return (EAttribute)parameterNamespaceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameterNamespace_Parameters() {
		return (EReference)parameterNamespaceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParameterDefinition() {
		return parameterDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameterDefinition_Name() {
		return (EAttribute)parameterDefinitionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameterDefinition_Type() {
		return (EAttribute)parameterDefinitionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameterDefinition_Role() {
		return (EAttribute)parameterDefinitionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameterDefinition_FullName() {
		return (EAttribute)parameterDefinitionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getParameterRole() {
		return parameterRoleEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentFactory getEnvironmentFactory() {
		return (EnvironmentFactory)getEFactoryInstance();
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
		measurementEnvironmentDefinitionEClass = createEClass(MEASUREMENT_ENVIRONMENT_DEFINITION);
		createEReference(measurementEnvironmentDefinitionEClass, MEASUREMENT_ENVIRONMENT_DEFINITION__ROOT);

		parameterNamespaceEClass = createEClass(PARAMETER_NAMESPACE);
		createEReference(parameterNamespaceEClass, PARAMETER_NAMESPACE__CHILDREN);
		createEAttribute(parameterNamespaceEClass, PARAMETER_NAMESPACE__NAME);
		createEReference(parameterNamespaceEClass, PARAMETER_NAMESPACE__PARAMETERS);

		parameterDefinitionEClass = createEClass(PARAMETER_DEFINITION);
		createEAttribute(parameterDefinitionEClass, PARAMETER_DEFINITION__NAME);
		createEAttribute(parameterDefinitionEClass, PARAMETER_DEFINITION__TYPE);
		createEAttribute(parameterDefinitionEClass, PARAMETER_DEFINITION__ROLE);
		createEAttribute(parameterDefinitionEClass, PARAMETER_DEFINITION__FULL_NAME);

		// Create enums
		parameterRoleEEnum = createEEnum(PARAMETER_ROLE);
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

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		measurementEnvironmentDefinitionEClass.getESuperTypes().add(theCommonPackage.getSerializable());
		parameterNamespaceEClass.getESuperTypes().add(theCommonPackage.getSerializable());
		parameterDefinitionEClass.getESuperTypes().add(theCommonPackage.getSerializable());

		// Initialize classes and features; add operations and parameters
		initEClass(measurementEnvironmentDefinitionEClass, MeasurementEnvironmentDefinition.class, "MeasurementEnvironmentDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMeasurementEnvironmentDefinition_Root(), this.getParameterNamespace(), null, "root", null, 1, 1, MeasurementEnvironmentDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parameterNamespaceEClass, ParameterNamespace.class, "ParameterNamespace", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getParameterNamespace_Children(), this.getParameterNamespace(), null, "children", null, 0, -1, ParameterNamespace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameterNamespace_Name(), ecorePackage.getEString(), "name", "", 0, 1, ParameterNamespace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getParameterNamespace_Parameters(), this.getParameterDefinition(), null, "parameters", null, 0, -1, ParameterNamespace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parameterDefinitionEClass, ParameterDefinition.class, "ParameterDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getParameterDefinition_Name(), ecorePackage.getEString(), "name", null, 1, 1, ParameterDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameterDefinition_Type(), ecorePackage.getEString(), "type", null, 1, 1, ParameterDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameterDefinition_Role(), this.getParameterRole(), "role", null, 1, 1, ParameterDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameterDefinition_FullName(), ecorePackage.getEString(), "fullName", null, 0, 1, ParameterDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(parameterDefinitionEClass, ecorePackage.getEString(), "getFullName", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "namespaceDelimiter", 0, 1, IS_UNIQUE, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(parameterRoleEEnum, ParameterRole.class, "ParameterRole");
		addEEnumLiteral(parameterRoleEEnum, ParameterRole.INPUT);
		addEEnumLiteral(parameterRoleEEnum, ParameterRole.OBSERVATION);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";		
		addAnnotation
		  (getParameterNamespace_Parameters(), 
		   source, 
		   new String[] {
			 "namespace", ""
		   });	
	}

} //EnvironmentPackageImpl

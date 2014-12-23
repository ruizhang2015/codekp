/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.detectorConfiger.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import edu.pku.cn.xml.detectorConfiger.CategoryType;
import edu.pku.cn.xml.detectorConfiger.DectectorFactoryType;
import edu.pku.cn.xml.detectorConfiger.DetectorConfigerFactory;
import edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage;
import edu.pku.cn.xml.detectorConfiger.DetectorType;
import edu.pku.cn.xml.detectorConfiger.DocumentRoot;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class DetectorConfigerPackageImpl extends EPackageImpl implements DetectorConfigerPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass categoryTypeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass dectectorFactoryTypeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass detectorTypeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass documentRootEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the
	 * package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory
	 * method {@link #init init()}, which also performs initialization of the
	 * package, or returns the registered package, if one already exists. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DetectorConfigerPackageImpl() {
		super(eNS_URI, DetectorConfigerFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model,
	 * and for any others upon which it depends. Simple dependencies are
	 * satisfied by calling this method on all dependent packages before doing
	 * anything else. This method drives initialization for interdependent
	 * packages directly, in parallel with this package, itself.
	 * <p>
	 * Of this package and its interdependencies, all packages which have not
	 * yet been registered by their URI values are first created and registered.
	 * The packages are then initialized in two steps: meta-model objects for
	 * all of the packages are created before any are initialized, since one
	 * package's meta-model objects may refer to those of another.
	 * <p>
	 * Invocation of this method will not affect any packages that have already
	 * been initialized. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static DetectorConfigerPackage init() {
		if (isInited)
			return (DetectorConfigerPackage) EPackage.Registry.INSTANCE.getEPackage(DetectorConfigerPackage.eNS_URI);

		// Obtain or create and register package
		DetectorConfigerPackageImpl theDetectorConfigerPackage = (DetectorConfigerPackageImpl) (EPackage.Registry.INSTANCE
				.getEPackage(eNS_URI) instanceof DetectorConfigerPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(eNS_URI) : new DetectorConfigerPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theDetectorConfigerPackage.createPackageContents();

		// Initialize created meta-data
		theDetectorConfigerPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDetectorConfigerPackage.freeze();

		return theDetectorConfigerPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getCategoryType() {
		return categoryTypeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getCategoryType_Detector() {
		return (EReference) categoryTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getCategoryType_Name() {
		return (EAttribute) categoryTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getCategoryType_PackageName() {
		return (EAttribute) categoryTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getCategoryType_Priority() {
		return (EAttribute) categoryTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getDectectorFactoryType() {
		return dectectorFactoryTypeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getDectectorFactoryType_Category() {
		return (EReference) dectectorFactoryTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getDetectorType() {
		return detectorTypeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getDetectorType_ClassName() {
		return (EAttribute) detectorTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getDetectorType_Disabled() {
		return (EAttribute) detectorTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getDetectorType_Hidden() {
		return (EAttribute) detectorTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getDetectorType_Reports() {
		return (EAttribute) detectorTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getDetectorType_Speed() {
		return (EAttribute) detectorTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getDocumentRoot() {
		return documentRootEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getDocumentRoot_Mixed() {
		return (EAttribute) documentRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getDocumentRoot_XMLNSPrefixMap() {
		return (EReference) documentRootEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getDocumentRoot_XSISchemaLocation() {
		return (EReference) documentRootEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getDocumentRoot_Category() {
		return (EReference) documentRootEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getDocumentRoot_DectectorFactory() {
		return (EReference) documentRootEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getDocumentRoot_Detector() {
		return (EReference) documentRootEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DetectorConfigerFactory getDetectorConfigerFactory() {
		return (DetectorConfigerFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is guarded to
	 * have no affect on any invocation but its first. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		categoryTypeEClass = createEClass(CATEGORY_TYPE);
		createEReference(categoryTypeEClass, CATEGORY_TYPE__DETECTOR);
		createEAttribute(categoryTypeEClass, CATEGORY_TYPE__NAME);
		createEAttribute(categoryTypeEClass, CATEGORY_TYPE__PACKAGE_NAME);
		createEAttribute(categoryTypeEClass, CATEGORY_TYPE__PRIORITY);

		dectectorFactoryTypeEClass = createEClass(DECTECTOR_FACTORY_TYPE);
		createEReference(dectectorFactoryTypeEClass, DECTECTOR_FACTORY_TYPE__CATEGORY);

		detectorTypeEClass = createEClass(DETECTOR_TYPE);
		createEAttribute(detectorTypeEClass, DETECTOR_TYPE__CLASS_NAME);
		createEAttribute(detectorTypeEClass, DETECTOR_TYPE__DISABLED);
		createEAttribute(detectorTypeEClass, DETECTOR_TYPE__HIDDEN);
		createEAttribute(detectorTypeEClass, DETECTOR_TYPE__REPORTS);
		createEAttribute(detectorTypeEClass, DETECTOR_TYPE__SPEED);

		documentRootEClass = createEClass(DOCUMENT_ROOT);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__CATEGORY);
		createEReference(documentRootEClass, DOCUMENT_ROOT__DECTECTOR_FACTORY);
		createEReference(documentRootEClass, DOCUMENT_ROOT__DETECTOR);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This
	 * method is guarded to have no affect on any invocation but its first. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		XMLTypePackage theXMLTypePackage = (XMLTypePackage) EPackage.Registry.INSTANCE
				.getEPackage(XMLTypePackage.eNS_URI);

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(categoryTypeEClass, CategoryType.class, "CategoryType", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCategoryType_Detector(), this.getDetectorType(), null, "detector", null, 1, -1,
				CategoryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCategoryType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, CategoryType.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCategoryType_PackageName(), theXMLTypePackage.getString(), "packageName", null, 0, 1,
				CategoryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getCategoryType_Priority(), theXMLTypePackage.getString(), "priority", null, 0, 1,
				CategoryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(dectectorFactoryTypeEClass, DectectorFactoryType.class, "DectectorFactoryType", !IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDectectorFactoryType_Category(), this.getCategoryType(), null, "category", null, 1, -1,
				DectectorFactoryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(detectorTypeEClass, DetectorType.class, "DetectorType", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDetectorType_ClassName(), theXMLTypePackage.getString(), "className", null, 0, 1,
				DetectorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getDetectorType_Disabled(), theXMLTypePackage.getString(), "disabled", null, 0, 1,
				DetectorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getDetectorType_Hidden(), theXMLTypePackage.getString(), "hidden", null, 0, 1,
				DetectorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getDetectorType_Reports(), theXMLTypePackage.getString(), "reports", null, 0, 1,
				DetectorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getDetectorType_Speed(), theXMLTypePackage.getString(), "speed", null, 0, 1, DetectorType.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null,
				"xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null,
				"xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Category(), this.getCategoryType(), null, "category", null, 0, -2, null,
				IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
				IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_DectectorFactory(), this.getDectectorFactoryType(), null, "dectectorFactory",
				null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Detector(), this.getDetectorType(), null, "detector", null, 0, -2, null,
				IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
				IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for
	 * <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";
		addAnnotation(this, source, new String[] { "qualified", "false" });
		addAnnotation(categoryTypeEClass, source, new String[] { "name", "Category_._type", "kind", "elementOnly" });
		addAnnotation(getCategoryType_Detector(), source, new String[] { "kind", "element", "name", "Detector",
				"namespace", "##targetNamespace" });
		addAnnotation(getCategoryType_Name(), source, new String[] { "kind", "attribute", "name", "name", "namespace",
				"##targetNamespace" });
		addAnnotation(getCategoryType_PackageName(), source, new String[] { "kind", "attribute", "name", "packageName",
				"namespace", "##targetNamespace" });
		addAnnotation(getCategoryType_Priority(), source, new String[] { "kind", "attribute", "name", "priority",
				"namespace", "##targetNamespace" });
		addAnnotation(dectectorFactoryTypeEClass, source, new String[] { "name", "DectectorFactory_._type", "kind",
				"elementOnly" });
		addAnnotation(getDectectorFactoryType_Category(), source, new String[] { "kind", "element", "name", "Category",
				"namespace", "##targetNamespace" });
		addAnnotation(detectorTypeEClass, source, new String[] { "name", "Detector_._type", "kind", "empty" });
		addAnnotation(getDetectorType_ClassName(), source, new String[] { "kind", "attribute", "name", "className",
				"namespace", "##targetNamespace" });
		addAnnotation(getDetectorType_Disabled(), source, new String[] { "kind", "attribute", "name", "disabled",
				"namespace", "##targetNamespace" });
		addAnnotation(getDetectorType_Hidden(), source, new String[] { "kind", "attribute", "name", "hidden",
				"namespace", "##targetNamespace" });
		addAnnotation(getDetectorType_Reports(), source, new String[] { "kind", "attribute", "name", "reports",
				"namespace", "##targetNamespace" });
		addAnnotation(getDetectorType_Speed(), source, new String[] { "kind", "attribute", "name", "speed",
				"namespace", "##targetNamespace" });
		addAnnotation(documentRootEClass, source, new String[] { "name", "", "kind", "mixed" });
		addAnnotation(getDocumentRoot_Mixed(), source, new String[] { "kind", "elementWildcard", "name", ":mixed" });
		addAnnotation(getDocumentRoot_XMLNSPrefixMap(), source, new String[] { "kind", "attribute", "name",
				"xmlns:prefix" });
		addAnnotation(getDocumentRoot_XSISchemaLocation(), source, new String[] { "kind", "attribute", "name",
				"xsi:schemaLocation" });
		addAnnotation(getDocumentRoot_Category(), source, new String[] { "kind", "element", "name", "Category",
				"namespace", "##targetNamespace" });
		addAnnotation(getDocumentRoot_DectectorFactory(), source, new String[] { "kind", "element", "name",
				"DectectorFactory", "namespace", "##targetNamespace" });
		addAnnotation(getDocumentRoot_Detector(), source, new String[] { "kind", "element", "name", "Detector",
				"namespace", "##targetNamespace" });
	}

} // DetectorConfigerPackageImpl

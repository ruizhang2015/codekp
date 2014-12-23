/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.JavaLib.impl;


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import edu.pku.cn.xml.JavaLib.ClassType;
import edu.pku.cn.xml.JavaLib.DocumentRoot;
import edu.pku.cn.xml.JavaLib.FieldType;
import edu.pku.cn.xml.JavaLib.ImplementType;
import edu.pku.cn.xml.JavaLib.JavaConfigType;
import edu.pku.cn.xml.JavaLib.JavaLibFactory;
import edu.pku.cn.xml.JavaLib.JavaLibPackage;
import edu.pku.cn.xml.JavaLib.JavaLibType;
import edu.pku.cn.xml.JavaLib.MethodType;
import edu.pku.cn.xml.JavaLib.PackageType;
import edu.pku.cn.xml.JavaLib.ReturnTypeType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class JavaLibPackageImpl extends EPackageImpl implements JavaLibPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass classTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass documentRootEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass fieldTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass implementTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass javaConfigTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass javaLibTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass methodTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass packageTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass returnTypeTypeEClass = null;

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
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private JavaLibPackageImpl()
  {
    super(eNS_URI, JavaLibFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this
   * model, and for any others upon which it depends.  Simple
   * dependencies are satisfied by calling this method on all
   * dependent packages before doing anything else.  This method drives
   * initialization for interdependent packages directly, in parallel
   * with this package, itself.
   * <p>Of this package and its interdependencies, all packages which
   * have not yet been registered by their URI values are first created
   * and registered.  The packages are then initialized in two steps:
   * meta-model objects for all of the packages are created before any
   * are initialized, since one package's meta-model objects may refer to
   * those of another.
   * <p>Invocation of this method will not affect any packages that have
   * already been initialized.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static JavaLibPackage init()
  {
    if (isInited) return (JavaLibPackage)EPackage.Registry.INSTANCE.getEPackage(JavaLibPackage.eNS_URI);

    // Obtain or create and register package
    JavaLibPackageImpl theJavaLibPackage = (JavaLibPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof JavaLibPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new JavaLibPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    XMLTypePackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theJavaLibPackage.createPackageContents();

    // Initialize created meta-data
    theJavaLibPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theJavaLibPackage.freeze();

    return theJavaLibPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getClassType()
  {
    return classTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getClassType_Implement()
  {
    return (EReference)classTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getClassType_Field()
  {
    return (EReference)classTypeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getClassType_Method()
  {
    return (EReference)classTypeEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getClassType_Extends()
  {
    return (EAttribute)classTypeEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getClassType_IsInterface()
  {
    return (EAttribute)classTypeEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getClassType_Name()
  {
    return (EAttribute)classTypeEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDocumentRoot()
  {
    return documentRootEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDocumentRoot_Mixed()
  {
    return (EAttribute)documentRootEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDocumentRoot_XMLNSPrefixMap()
  {
    return (EReference)documentRootEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDocumentRoot_XSISchemaLocation()
  {
    return (EReference)documentRootEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDocumentRoot_Class()
  {
    return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDocumentRoot_Field()
  {
    return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDocumentRoot_Implement()
  {
    return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDocumentRoot_JavaConfig()
  {
    return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDocumentRoot_JavaLib()
  {
    return (EReference)documentRootEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDocumentRoot_Method()
  {
    return (EReference)documentRootEClass.getEStructuralFeatures().get(8);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDocumentRoot_Package()
  {
    return (EReference)documentRootEClass.getEStructuralFeatures().get(9);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDocumentRoot_ReturnType()
  {
    return (EReference)documentRootEClass.getEStructuralFeatures().get(10);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getFieldType()
  {
    return fieldTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getFieldType_Access()
  {
    return (EAttribute)fieldTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getFieldType_Desc()
  {
    return (EAttribute)fieldTypeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getFieldType_Name()
  {
    return (EAttribute)fieldTypeEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getFieldType_Signature()
  {
    return (EAttribute)fieldTypeEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getImplementType()
  {
    return implementTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getImplementType_ClassName()
  {
    return (EAttribute)implementTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getJavaConfigType()
  {
    return javaConfigTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getJavaConfigType_JavaLib()
  {
    return (EReference)javaConfigTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getJavaLibType()
  {
    return javaLibTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getJavaLibType_Package()
  {
    return (EReference)javaLibTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getJavaLibType_Name()
  {
    return (EAttribute)javaLibTypeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getJavaLibType_Version()
  {
    return (EAttribute)javaLibTypeEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMethodType()
  {
    return methodTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMethodType_ReturnType()
  {
    return (EReference)methodTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMethodType_Access()
  {
    return (EAttribute)methodTypeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMethodType_Desc()
  {
    return (EAttribute)methodTypeEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMethodType_Name()
  {
    return (EAttribute)methodTypeEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMethodType_Signature()
  {
    return (EAttribute)methodTypeEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getPackageType()
  {
    return packageTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPackageType_Class()
  {
    return (EReference)packageTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPackageType_Package()
  {
    return (EReference)packageTypeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPackageType_Name()
  {
    return (EAttribute)packageTypeEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getReturnTypeType()
  {
    return returnTypeTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getReturnTypeType_Name()
  {
    return (EAttribute)returnTypeTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaLibFactory getJavaLibFactory()
  {
    return (JavaLibFactory)getEFactoryInstance();
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
  public void createPackageContents()
  {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    classTypeEClass = createEClass(CLASS_TYPE);
    createEReference(classTypeEClass, CLASS_TYPE__IMPLEMENT);
    createEReference(classTypeEClass, CLASS_TYPE__FIELD);
    createEReference(classTypeEClass, CLASS_TYPE__METHOD);
    createEAttribute(classTypeEClass, CLASS_TYPE__EXTENDS);
    createEAttribute(classTypeEClass, CLASS_TYPE__IS_INTERFACE);
    createEAttribute(classTypeEClass, CLASS_TYPE__NAME);

    documentRootEClass = createEClass(DOCUMENT_ROOT);
    createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
    createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
    createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
    createEReference(documentRootEClass, DOCUMENT_ROOT__CLASS);
    createEReference(documentRootEClass, DOCUMENT_ROOT__FIELD);
    createEReference(documentRootEClass, DOCUMENT_ROOT__IMPLEMENT);
    createEReference(documentRootEClass, DOCUMENT_ROOT__JAVA_CONFIG);
    createEReference(documentRootEClass, DOCUMENT_ROOT__JAVA_LIB);
    createEReference(documentRootEClass, DOCUMENT_ROOT__METHOD);
    createEReference(documentRootEClass, DOCUMENT_ROOT__PACKAGE);
    createEReference(documentRootEClass, DOCUMENT_ROOT__RETURN_TYPE);

    fieldTypeEClass = createEClass(FIELD_TYPE);
    createEAttribute(fieldTypeEClass, FIELD_TYPE__ACCESS);
    createEAttribute(fieldTypeEClass, FIELD_TYPE__DESC);
    createEAttribute(fieldTypeEClass, FIELD_TYPE__NAME);
    createEAttribute(fieldTypeEClass, FIELD_TYPE__SIGNATURE);

    implementTypeEClass = createEClass(IMPLEMENT_TYPE);
    createEAttribute(implementTypeEClass, IMPLEMENT_TYPE__CLASS_NAME);

    javaConfigTypeEClass = createEClass(JAVA_CONFIG_TYPE);
    createEReference(javaConfigTypeEClass, JAVA_CONFIG_TYPE__JAVA_LIB);

    javaLibTypeEClass = createEClass(JAVA_LIB_TYPE);
    createEReference(javaLibTypeEClass, JAVA_LIB_TYPE__PACKAGE);
    createEAttribute(javaLibTypeEClass, JAVA_LIB_TYPE__NAME);
    createEAttribute(javaLibTypeEClass, JAVA_LIB_TYPE__VERSION);

    methodTypeEClass = createEClass(METHOD_TYPE);
    createEReference(methodTypeEClass, METHOD_TYPE__RETURN_TYPE);
    createEAttribute(methodTypeEClass, METHOD_TYPE__ACCESS);
    createEAttribute(methodTypeEClass, METHOD_TYPE__DESC);
    createEAttribute(methodTypeEClass, METHOD_TYPE__NAME);
    createEAttribute(methodTypeEClass, METHOD_TYPE__SIGNATURE);

    packageTypeEClass = createEClass(PACKAGE_TYPE);
    createEReference(packageTypeEClass, PACKAGE_TYPE__CLASS);
    createEReference(packageTypeEClass, PACKAGE_TYPE__PACKAGE);
    createEAttribute(packageTypeEClass, PACKAGE_TYPE__NAME);

    returnTypeTypeEClass = createEClass(RETURN_TYPE_TYPE);
    createEAttribute(returnTypeTypeEClass, RETURN_TYPE_TYPE__NAME);
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
  public void initializePackageContents()
  {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Obtain other dependent packages
    XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

    // Add supertypes to classes

    // Initialize classes and features; add operations and parameters
    initEClass(classTypeEClass, ClassType.class, "ClassType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getClassType_Implement(), this.getImplementType(), null, "implement", null, 0, -1, ClassType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getClassType_Field(), this.getFieldType(), null, "field", null, 0, -1, ClassType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getClassType_Method(), this.getMethodType(), null, "method", null, 0, -1, ClassType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getClassType_Extends(), theXMLTypePackage.getString(), "extends", null, 0, 1, ClassType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getClassType_IsInterface(), theXMLTypePackage.getBoolean(), "isInterface", null, 0, 1, ClassType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getClassType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, ClassType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDocumentRoot_Class(), this.getClassType(), null, "class", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEReference(getDocumentRoot_Field(), this.getFieldType(), null, "field", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEReference(getDocumentRoot_Implement(), this.getImplementType(), null, "implement", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEReference(getDocumentRoot_JavaConfig(), this.getJavaConfigType(), null, "javaConfig", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEReference(getDocumentRoot_JavaLib(), this.getJavaLibType(), null, "javaLib", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEReference(getDocumentRoot_Method(), this.getMethodType(), null, "method", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEReference(getDocumentRoot_Package(), this.getPackageType(), null, "package", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEReference(getDocumentRoot_ReturnType(), this.getReturnTypeType(), null, "returnType", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

    initEClass(fieldTypeEClass, FieldType.class, "FieldType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getFieldType_Access(), theXMLTypePackage.getString(), "access", null, 0, 1, FieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getFieldType_Desc(), theXMLTypePackage.getString(), "desc", null, 0, 1, FieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getFieldType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, FieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getFieldType_Signature(), theXMLTypePackage.getString(), "signature", null, 0, 1, FieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(implementTypeEClass, ImplementType.class, "ImplementType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getImplementType_ClassName(), theXMLTypePackage.getString(), "className", null, 0, 1, ImplementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(javaConfigTypeEClass, JavaConfigType.class, "JavaConfigType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getJavaConfigType_JavaLib(), this.getJavaLibType(), null, "javaLib", null, 1, -1, JavaConfigType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(javaLibTypeEClass, JavaLibType.class, "JavaLibType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getJavaLibType_Package(), this.getPackageType(), null, "package", null, 1, -1, JavaLibType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getJavaLibType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, JavaLibType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getJavaLibType_Version(), theXMLTypePackage.getString(), "version", null, 0, 1, JavaLibType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(methodTypeEClass, MethodType.class, "MethodType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getMethodType_ReturnType(), this.getReturnTypeType(), null, "returnType", null, 0, -1, MethodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMethodType_Access(), theXMLTypePackage.getString(), "access", null, 0, 1, MethodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMethodType_Desc(), theXMLTypePackage.getString(), "desc", null, 0, 1, MethodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMethodType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, MethodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMethodType_Signature(), theXMLTypePackage.getString(), "signature", null, 0, 1, MethodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(packageTypeEClass, PackageType.class, "PackageType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getPackageType_Class(), this.getClassType(), null, "class", null, 0, -1, PackageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getPackageType_Package(), this.getPackageType(), null, "package", null, 0, -1, PackageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getPackageType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, PackageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(returnTypeTypeEClass, ReturnTypeType.class, "ReturnTypeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getReturnTypeType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, ReturnTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource(eNS_URI);

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
  protected void createExtendedMetaDataAnnotations()
  {
    String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";		
    addAnnotation
      (this, 
       source, 
       new String[] 
       {
       "qualified", "false"
       });		
    addAnnotation
      (classTypeEClass, 
       source, 
       new String[] 
       {
       "name", "Class_._type",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getClassType_Implement(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Implement",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getClassType_Field(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Field",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getClassType_Method(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Method",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getClassType_Extends(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "extends",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getClassType_IsInterface(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "isInterface",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getClassType_Name(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "name",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (documentRootEClass, 
       source, 
       new String[] 
       {
       "name", "",
       "kind", "mixed"
       });		
    addAnnotation
      (getDocumentRoot_Mixed(), 
       source, 
       new String[] 
       {
       "kind", "elementWildcard",
       "name", ":mixed"
       });		
    addAnnotation
      (getDocumentRoot_XMLNSPrefixMap(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "xmlns:prefix"
       });		
    addAnnotation
      (getDocumentRoot_XSISchemaLocation(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "xsi:schemaLocation"
       });		
    addAnnotation
      (getDocumentRoot_Class(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Class",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getDocumentRoot_Field(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Field",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getDocumentRoot_Implement(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Implement",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getDocumentRoot_JavaConfig(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "JavaConfig",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getDocumentRoot_JavaLib(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "JavaLib",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getDocumentRoot_Method(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Method",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getDocumentRoot_Package(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Package",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getDocumentRoot_ReturnType(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "ReturnType",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (fieldTypeEClass, 
       source, 
       new String[] 
       {
       "name", "Field_._type",
       "kind", "empty"
       });		
    addAnnotation
      (getFieldType_Access(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "access",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getFieldType_Desc(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "desc",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getFieldType_Name(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "name",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getFieldType_Signature(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "signature",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (implementTypeEClass, 
       source, 
       new String[] 
       {
       "name", "Implement_._type",
       "kind", "empty"
       });		
    addAnnotation
      (getImplementType_ClassName(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "className",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (javaConfigTypeEClass, 
       source, 
       new String[] 
       {
       "name", "JavaConfig_._type",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getJavaConfigType_JavaLib(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "JavaLib",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (javaLibTypeEClass, 
       source, 
       new String[] 
       {
       "name", "JavaLib_._type",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getJavaLibType_Package(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Package",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getJavaLibType_Name(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "name",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getJavaLibType_Version(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "version",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (methodTypeEClass, 
       source, 
       new String[] 
       {
       "name", "Method_._type",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getMethodType_ReturnType(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "ReturnType",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getMethodType_Access(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "access",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getMethodType_Desc(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "desc",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getMethodType_Name(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "name",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getMethodType_Signature(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "signature",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (packageTypeEClass, 
       source, 
       new String[] 
       {
       "name", "Package_._type",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getPackageType_Class(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Class",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getPackageType_Package(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Package",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (getPackageType_Name(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "name",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (returnTypeTypeEClass, 
       source, 
       new String[] 
       {
       "name", "ReturnType_._type",
       "kind", "empty"
       });		
    addAnnotation
      (getReturnTypeType_Name(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "name",
       "namespace", "##targetNamespace"
       });
  }

} //JavaLibPackageImpl

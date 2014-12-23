/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.JavaLib;

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
 * @see edu.pku.cn.xml.JavaLib.JavaLibFactory
 * @model kind="package"
 *        extendedMetaData="qualified='false'"
 * @generated
 */
public interface JavaLibPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "JavaLib";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "file:/D:/Program%20Files/IBM/workspace/aa/JavaLib.xsd";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "JavaLib";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  JavaLibPackage eINSTANCE = edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl.init();

  /**
   * The meta object id for the '{@link edu.pku.cn.xml.JavaLib.impl.ClassTypeImpl <em>Class Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see edu.pku.cn.xml.JavaLib.impl.ClassTypeImpl
   * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getClassType()
   * @generated
   */
  int CLASS_TYPE = 0;

  /**
   * The feature id for the '<em><b>Implement</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLASS_TYPE__IMPLEMENT = 0;

  /**
   * The feature id for the '<em><b>Field</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLASS_TYPE__FIELD = 1;

  /**
   * The feature id for the '<em><b>Method</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLASS_TYPE__METHOD = 2;

  /**
   * The feature id for the '<em><b>Extends</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLASS_TYPE__EXTENDS = 3;

  /**
   * The feature id for the '<em><b>Is Interface</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLASS_TYPE__IS_INTERFACE = 4;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLASS_TYPE__NAME = 5;

  /**
   * The number of structural features of the '<em>Class Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLASS_TYPE_FEATURE_COUNT = 6;

  /**
   * The meta object id for the '{@link edu.pku.cn.xml.JavaLib.impl.DocumentRootImpl <em>Document Root</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see edu.pku.cn.xml.JavaLib.impl.DocumentRootImpl
   * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getDocumentRoot()
   * @generated
   */
  int DOCUMENT_ROOT = 1;

  /**
   * The feature id for the '<em><b>Mixed</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__MIXED = 0;

  /**
   * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

  /**
   * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

  /**
   * The feature id for the '<em><b>Class</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__CLASS = 3;

  /**
   * The feature id for the '<em><b>Field</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__FIELD = 4;

  /**
   * The feature id for the '<em><b>Implement</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__IMPLEMENT = 5;

  /**
   * The feature id for the '<em><b>Java Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__JAVA_CONFIG = 6;

  /**
   * The feature id for the '<em><b>Java Lib</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__JAVA_LIB = 7;

  /**
   * The feature id for the '<em><b>Method</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__METHOD = 8;

  /**
   * The feature id for the '<em><b>Package</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__PACKAGE = 9;

  /**
   * The feature id for the '<em><b>Return Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__RETURN_TYPE = 10;

  /**
   * The number of structural features of the '<em>Document Root</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT_FEATURE_COUNT = 11;

  /**
   * The meta object id for the '{@link edu.pku.cn.xml.JavaLib.impl.FieldTypeImpl <em>Field Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see edu.pku.cn.xml.JavaLib.impl.FieldTypeImpl
   * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getFieldType()
   * @generated
   */
  int FIELD_TYPE = 2;

  /**
   * The feature id for the '<em><b>Access</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FIELD_TYPE__ACCESS = 0;

  /**
   * The feature id for the '<em><b>Desc</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FIELD_TYPE__DESC = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FIELD_TYPE__NAME = 2;

  /**
   * The feature id for the '<em><b>Signature</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FIELD_TYPE__SIGNATURE = 3;

  /**
   * The number of structural features of the '<em>Field Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FIELD_TYPE_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link edu.pku.cn.xml.JavaLib.impl.ImplementTypeImpl <em>Implement Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see edu.pku.cn.xml.JavaLib.impl.ImplementTypeImpl
   * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getImplementType()
   * @generated
   */
  int IMPLEMENT_TYPE = 3;

  /**
   * The feature id for the '<em><b>Class Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPLEMENT_TYPE__CLASS_NAME = 0;

  /**
   * The number of structural features of the '<em>Implement Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPLEMENT_TYPE_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link edu.pku.cn.xml.JavaLib.impl.JavaConfigTypeImpl <em>Java Config Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see edu.pku.cn.xml.JavaLib.impl.JavaConfigTypeImpl
   * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getJavaConfigType()
   * @generated
   */
  int JAVA_CONFIG_TYPE = 4;

  /**
   * The feature id for the '<em><b>Java Lib</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JAVA_CONFIG_TYPE__JAVA_LIB = 0;

  /**
   * The number of structural features of the '<em>Java Config Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JAVA_CONFIG_TYPE_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link edu.pku.cn.xml.JavaLib.impl.JavaLibTypeImpl <em>Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see edu.pku.cn.xml.JavaLib.impl.JavaLibTypeImpl
   * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getJavaLibType()
   * @generated
   */
  int JAVA_LIB_TYPE = 5;

  /**
   * The feature id for the '<em><b>Package</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JAVA_LIB_TYPE__PACKAGE = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JAVA_LIB_TYPE__NAME = 1;

  /**
   * The feature id for the '<em><b>Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JAVA_LIB_TYPE__VERSION = 2;

  /**
   * The number of structural features of the '<em>Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JAVA_LIB_TYPE_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link edu.pku.cn.xml.JavaLib.impl.MethodTypeImpl <em>Method Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see edu.pku.cn.xml.JavaLib.impl.MethodTypeImpl
   * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getMethodType()
   * @generated
   */
  int METHOD_TYPE = 6;

  /**
   * The feature id for the '<em><b>Return Type</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHOD_TYPE__RETURN_TYPE = 0;

  /**
   * The feature id for the '<em><b>Access</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHOD_TYPE__ACCESS = 1;

  /**
   * The feature id for the '<em><b>Desc</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHOD_TYPE__DESC = 2;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHOD_TYPE__NAME = 3;

  /**
   * The feature id for the '<em><b>Signature</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHOD_TYPE__SIGNATURE = 4;

  /**
   * The number of structural features of the '<em>Method Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHOD_TYPE_FEATURE_COUNT = 5;

  /**
   * The meta object id for the '{@link edu.pku.cn.xml.JavaLib.impl.PackageTypeImpl <em>Package Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see edu.pku.cn.xml.JavaLib.impl.PackageTypeImpl
   * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getPackageType()
   * @generated
   */
  int PACKAGE_TYPE = 7;

  /**
   * The feature id for the '<em><b>Class</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PACKAGE_TYPE__CLASS = 0;

  /**
   * The feature id for the '<em><b>Package</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PACKAGE_TYPE__PACKAGE = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PACKAGE_TYPE__NAME = 2;

  /**
   * The number of structural features of the '<em>Package Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PACKAGE_TYPE_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link edu.pku.cn.xml.JavaLib.impl.ReturnTypeTypeImpl <em>Return Type Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see edu.pku.cn.xml.JavaLib.impl.ReturnTypeTypeImpl
   * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getReturnTypeType()
   * @generated
   */
  int RETURN_TYPE_TYPE = 8;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RETURN_TYPE_TYPE__NAME = 0;

  /**
   * The number of structural features of the '<em>Return Type Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RETURN_TYPE_TYPE_FEATURE_COUNT = 1;


  /**
   * Returns the meta object for class '{@link edu.pku.cn.xml.JavaLib.ClassType <em>Class Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Class Type</em>'.
   * @see edu.pku.cn.xml.JavaLib.ClassType
   * @generated
   */
  EClass getClassType();

  /**
   * Returns the meta object for the containment reference list '{@link edu.pku.cn.xml.JavaLib.ClassType#getImplement <em>Implement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Implement</em>'.
   * @see edu.pku.cn.xml.JavaLib.ClassType#getImplement()
   * @see #getClassType()
   * @generated
   */
  EReference getClassType_Implement();

  /**
   * Returns the meta object for the containment reference list '{@link edu.pku.cn.xml.JavaLib.ClassType#getField <em>Field</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Field</em>'.
   * @see edu.pku.cn.xml.JavaLib.ClassType#getField()
   * @see #getClassType()
   * @generated
   */
  EReference getClassType_Field();

  /**
   * Returns the meta object for the containment reference list '{@link edu.pku.cn.xml.JavaLib.ClassType#getMethod <em>Method</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Method</em>'.
   * @see edu.pku.cn.xml.JavaLib.ClassType#getMethod()
   * @see #getClassType()
   * @generated
   */
  EReference getClassType_Method();

  /**
   * Returns the meta object for the attribute '{@link edu.pku.cn.xml.JavaLib.ClassType#getExtends <em>Extends</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Extends</em>'.
   * @see edu.pku.cn.xml.JavaLib.ClassType#getExtends()
   * @see #getClassType()
   * @generated
   */
  EAttribute getClassType_Extends();

  /**
   * Returns the meta object for the attribute '{@link edu.pku.cn.xml.JavaLib.ClassType#isIsInterface <em>Is Interface</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Is Interface</em>'.
   * @see edu.pku.cn.xml.JavaLib.ClassType#isIsInterface()
   * @see #getClassType()
   * @generated
   */
  EAttribute getClassType_IsInterface();

  /**
   * Returns the meta object for the attribute '{@link edu.pku.cn.xml.JavaLib.ClassType#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see edu.pku.cn.xml.JavaLib.ClassType#getName()
   * @see #getClassType()
   * @generated
   */
  EAttribute getClassType_Name();

  /**
   * Returns the meta object for class '{@link edu.pku.cn.xml.JavaLib.DocumentRoot <em>Document Root</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Document Root</em>'.
   * @see edu.pku.cn.xml.JavaLib.DocumentRoot
   * @generated
   */
  EClass getDocumentRoot();

  /**
   * Returns the meta object for the attribute list '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getMixed <em>Mixed</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Mixed</em>'.
   * @see edu.pku.cn.xml.JavaLib.DocumentRoot#getMixed()
   * @see #getDocumentRoot()
   * @generated
   */
  EAttribute getDocumentRoot_Mixed();

  /**
   * Returns the meta object for the map '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
   * @see edu.pku.cn.xml.JavaLib.DocumentRoot#getXMLNSPrefixMap()
   * @see #getDocumentRoot()
   * @generated
   */
  EReference getDocumentRoot_XMLNSPrefixMap();

  /**
   * Returns the meta object for the map '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the map '<em>XSI Schema Location</em>'.
   * @see edu.pku.cn.xml.JavaLib.DocumentRoot#getXSISchemaLocation()
   * @see #getDocumentRoot()
   * @generated
   */
  EReference getDocumentRoot_XSISchemaLocation();

  /**
   * Returns the meta object for the containment reference '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getClass_ <em>Class</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Class</em>'.
   * @see edu.pku.cn.xml.JavaLib.DocumentRoot#getClass_()
   * @see #getDocumentRoot()
   * @generated
   */
  EReference getDocumentRoot_Class();

  /**
   * Returns the meta object for the containment reference '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getField <em>Field</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Field</em>'.
   * @see edu.pku.cn.xml.JavaLib.DocumentRoot#getField()
   * @see #getDocumentRoot()
   * @generated
   */
  EReference getDocumentRoot_Field();

  /**
   * Returns the meta object for the containment reference '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getImplement <em>Implement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Implement</em>'.
   * @see edu.pku.cn.xml.JavaLib.DocumentRoot#getImplement()
   * @see #getDocumentRoot()
   * @generated
   */
  EReference getDocumentRoot_Implement();

  /**
   * Returns the meta object for the containment reference '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getJavaConfig <em>Java Config</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Java Config</em>'.
   * @see edu.pku.cn.xml.JavaLib.DocumentRoot#getJavaConfig()
   * @see #getDocumentRoot()
   * @generated
   */
  EReference getDocumentRoot_JavaConfig();

  /**
   * Returns the meta object for the containment reference '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getJavaLib <em>Java Lib</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Java Lib</em>'.
   * @see edu.pku.cn.xml.JavaLib.DocumentRoot#getJavaLib()
   * @see #getDocumentRoot()
   * @generated
   */
  EReference getDocumentRoot_JavaLib();

  /**
   * Returns the meta object for the containment reference '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getMethod <em>Method</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Method</em>'.
   * @see edu.pku.cn.xml.JavaLib.DocumentRoot#getMethod()
   * @see #getDocumentRoot()
   * @generated
   */
  EReference getDocumentRoot_Method();

  /**
   * Returns the meta object for the containment reference '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getPackage <em>Package</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Package</em>'.
   * @see edu.pku.cn.xml.JavaLib.DocumentRoot#getPackage()
   * @see #getDocumentRoot()
   * @generated
   */
  EReference getDocumentRoot_Package();

  /**
   * Returns the meta object for the containment reference '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getReturnType <em>Return Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Return Type</em>'.
   * @see edu.pku.cn.xml.JavaLib.DocumentRoot#getReturnType()
   * @see #getDocumentRoot()
   * @generated
   */
  EReference getDocumentRoot_ReturnType();

  /**
   * Returns the meta object for class '{@link edu.pku.cn.xml.JavaLib.FieldType <em>Field Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Field Type</em>'.
   * @see edu.pku.cn.xml.JavaLib.FieldType
   * @generated
   */
  EClass getFieldType();

  /**
   * Returns the meta object for the attribute '{@link edu.pku.cn.xml.JavaLib.FieldType#getAccess <em>Access</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Access</em>'.
   * @see edu.pku.cn.xml.JavaLib.FieldType#getAccess()
   * @see #getFieldType()
   * @generated
   */
  EAttribute getFieldType_Access();

  /**
   * Returns the meta object for the attribute '{@link edu.pku.cn.xml.JavaLib.FieldType#getDesc <em>Desc</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Desc</em>'.
   * @see edu.pku.cn.xml.JavaLib.FieldType#getDesc()
   * @see #getFieldType()
   * @generated
   */
  EAttribute getFieldType_Desc();

  /**
   * Returns the meta object for the attribute '{@link edu.pku.cn.xml.JavaLib.FieldType#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see edu.pku.cn.xml.JavaLib.FieldType#getName()
   * @see #getFieldType()
   * @generated
   */
  EAttribute getFieldType_Name();

  /**
   * Returns the meta object for the attribute '{@link edu.pku.cn.xml.JavaLib.FieldType#getSignature <em>Signature</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Signature</em>'.
   * @see edu.pku.cn.xml.JavaLib.FieldType#getSignature()
   * @see #getFieldType()
   * @generated
   */
  EAttribute getFieldType_Signature();

  /**
   * Returns the meta object for class '{@link edu.pku.cn.xml.JavaLib.ImplementType <em>Implement Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Implement Type</em>'.
   * @see edu.pku.cn.xml.JavaLib.ImplementType
   * @generated
   */
  EClass getImplementType();

  /**
   * Returns the meta object for the attribute '{@link edu.pku.cn.xml.JavaLib.ImplementType#getClassName <em>Class Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Class Name</em>'.
   * @see edu.pku.cn.xml.JavaLib.ImplementType#getClassName()
   * @see #getImplementType()
   * @generated
   */
  EAttribute getImplementType_ClassName();

  /**
   * Returns the meta object for class '{@link edu.pku.cn.xml.JavaLib.JavaConfigType <em>Java Config Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Java Config Type</em>'.
   * @see edu.pku.cn.xml.JavaLib.JavaConfigType
   * @generated
   */
  EClass getJavaConfigType();

  /**
   * Returns the meta object for the containment reference list '{@link edu.pku.cn.xml.JavaLib.JavaConfigType#getJavaLib <em>Java Lib</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Java Lib</em>'.
   * @see edu.pku.cn.xml.JavaLib.JavaConfigType#getJavaLib()
   * @see #getJavaConfigType()
   * @generated
   */
  EReference getJavaConfigType_JavaLib();

  /**
   * Returns the meta object for class '{@link edu.pku.cn.xml.JavaLib.JavaLibType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Type</em>'.
   * @see edu.pku.cn.xml.JavaLib.JavaLibType
   * @generated
   */
  EClass getJavaLibType();

  /**
   * Returns the meta object for the containment reference list '{@link edu.pku.cn.xml.JavaLib.JavaLibType#getPackage <em>Package</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Package</em>'.
   * @see edu.pku.cn.xml.JavaLib.JavaLibType#getPackage()
   * @see #getJavaLibType()
   * @generated
   */
  EReference getJavaLibType_Package();

  /**
   * Returns the meta object for the attribute '{@link edu.pku.cn.xml.JavaLib.JavaLibType#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see edu.pku.cn.xml.JavaLib.JavaLibType#getName()
   * @see #getJavaLibType()
   * @generated
   */
  EAttribute getJavaLibType_Name();

  /**
   * Returns the meta object for the attribute '{@link edu.pku.cn.xml.JavaLib.JavaLibType#getVersion <em>Version</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Version</em>'.
   * @see edu.pku.cn.xml.JavaLib.JavaLibType#getVersion()
   * @see #getJavaLibType()
   * @generated
   */
  EAttribute getJavaLibType_Version();

  /**
   * Returns the meta object for class '{@link edu.pku.cn.xml.JavaLib.MethodType <em>Method Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Method Type</em>'.
   * @see edu.pku.cn.xml.JavaLib.MethodType
   * @generated
   */
  EClass getMethodType();

  /**
   * Returns the meta object for the containment reference list '{@link edu.pku.cn.xml.JavaLib.MethodType#getReturnType <em>Return Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Return Type</em>'.
   * @see edu.pku.cn.xml.JavaLib.MethodType#getReturnType()
   * @see #getMethodType()
   * @generated
   */
  EReference getMethodType_ReturnType();

  /**
   * Returns the meta object for the attribute '{@link edu.pku.cn.xml.JavaLib.MethodType#getAccess <em>Access</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Access</em>'.
   * @see edu.pku.cn.xml.JavaLib.MethodType#getAccess()
   * @see #getMethodType()
   * @generated
   */
  EAttribute getMethodType_Access();

  /**
   * Returns the meta object for the attribute '{@link edu.pku.cn.xml.JavaLib.MethodType#getDesc <em>Desc</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Desc</em>'.
   * @see edu.pku.cn.xml.JavaLib.MethodType#getDesc()
   * @see #getMethodType()
   * @generated
   */
  EAttribute getMethodType_Desc();

  /**
   * Returns the meta object for the attribute '{@link edu.pku.cn.xml.JavaLib.MethodType#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see edu.pku.cn.xml.JavaLib.MethodType#getName()
   * @see #getMethodType()
   * @generated
   */
  EAttribute getMethodType_Name();

  /**
   * Returns the meta object for the attribute '{@link edu.pku.cn.xml.JavaLib.MethodType#getSignature <em>Signature</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Signature</em>'.
   * @see edu.pku.cn.xml.JavaLib.MethodType#getSignature()
   * @see #getMethodType()
   * @generated
   */
  EAttribute getMethodType_Signature();

  /**
   * Returns the meta object for class '{@link edu.pku.cn.xml.JavaLib.PackageType <em>Package Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Package Type</em>'.
   * @see edu.pku.cn.xml.JavaLib.PackageType
   * @generated
   */
  EClass getPackageType();

  /**
   * Returns the meta object for the containment reference list '{@link edu.pku.cn.xml.JavaLib.PackageType#getClass_ <em>Class</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Class</em>'.
   * @see edu.pku.cn.xml.JavaLib.PackageType#getClass_()
   * @see #getPackageType()
   * @generated
   */
  EReference getPackageType_Class();

  /**
   * Returns the meta object for the containment reference list '{@link edu.pku.cn.xml.JavaLib.PackageType#getPackage <em>Package</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Package</em>'.
   * @see edu.pku.cn.xml.JavaLib.PackageType#getPackage()
   * @see #getPackageType()
   * @generated
   */
  EReference getPackageType_Package();

  /**
   * Returns the meta object for the attribute '{@link edu.pku.cn.xml.JavaLib.PackageType#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see edu.pku.cn.xml.JavaLib.PackageType#getName()
   * @see #getPackageType()
   * @generated
   */
  EAttribute getPackageType_Name();

  /**
   * Returns the meta object for class '{@link edu.pku.cn.xml.JavaLib.ReturnTypeType <em>Return Type Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Return Type Type</em>'.
   * @see edu.pku.cn.xml.JavaLib.ReturnTypeType
   * @generated
   */
  EClass getReturnTypeType();

  /**
   * Returns the meta object for the attribute '{@link edu.pku.cn.xml.JavaLib.ReturnTypeType#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see edu.pku.cn.xml.JavaLib.ReturnTypeType#getName()
   * @see #getReturnTypeType()
   * @generated
   */
  EAttribute getReturnTypeType_Name();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  JavaLibFactory getJavaLibFactory();

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
  interface Literals
  {
    /**
     * The meta object literal for the '{@link edu.pku.cn.xml.JavaLib.impl.ClassTypeImpl <em>Class Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see edu.pku.cn.xml.JavaLib.impl.ClassTypeImpl
     * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getClassType()
     * @generated
     */
    EClass CLASS_TYPE = eINSTANCE.getClassType();

    /**
     * The meta object literal for the '<em><b>Implement</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CLASS_TYPE__IMPLEMENT = eINSTANCE.getClassType_Implement();

    /**
     * The meta object literal for the '<em><b>Field</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CLASS_TYPE__FIELD = eINSTANCE.getClassType_Field();

    /**
     * The meta object literal for the '<em><b>Method</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CLASS_TYPE__METHOD = eINSTANCE.getClassType_Method();

    /**
     * The meta object literal for the '<em><b>Extends</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CLASS_TYPE__EXTENDS = eINSTANCE.getClassType_Extends();

    /**
     * The meta object literal for the '<em><b>Is Interface</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CLASS_TYPE__IS_INTERFACE = eINSTANCE.getClassType_IsInterface();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CLASS_TYPE__NAME = eINSTANCE.getClassType_Name();

    /**
     * The meta object literal for the '{@link edu.pku.cn.xml.JavaLib.impl.DocumentRootImpl <em>Document Root</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see edu.pku.cn.xml.JavaLib.impl.DocumentRootImpl
     * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getDocumentRoot()
     * @generated
     */
    EClass DOCUMENT_ROOT = eINSTANCE.getDocumentRoot();

    /**
     * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DOCUMENT_ROOT__MIXED = eINSTANCE.getDocumentRoot_Mixed();

    /**
     * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getDocumentRoot_XMLNSPrefixMap();

    /**
     * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getDocumentRoot_XSISchemaLocation();

    /**
     * The meta object literal for the '<em><b>Class</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DOCUMENT_ROOT__CLASS = eINSTANCE.getDocumentRoot_Class();

    /**
     * The meta object literal for the '<em><b>Field</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DOCUMENT_ROOT__FIELD = eINSTANCE.getDocumentRoot_Field();

    /**
     * The meta object literal for the '<em><b>Implement</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DOCUMENT_ROOT__IMPLEMENT = eINSTANCE.getDocumentRoot_Implement();

    /**
     * The meta object literal for the '<em><b>Java Config</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DOCUMENT_ROOT__JAVA_CONFIG = eINSTANCE.getDocumentRoot_JavaConfig();

    /**
     * The meta object literal for the '<em><b>Java Lib</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DOCUMENT_ROOT__JAVA_LIB = eINSTANCE.getDocumentRoot_JavaLib();

    /**
     * The meta object literal for the '<em><b>Method</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DOCUMENT_ROOT__METHOD = eINSTANCE.getDocumentRoot_Method();

    /**
     * The meta object literal for the '<em><b>Package</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DOCUMENT_ROOT__PACKAGE = eINSTANCE.getDocumentRoot_Package();

    /**
     * The meta object literal for the '<em><b>Return Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DOCUMENT_ROOT__RETURN_TYPE = eINSTANCE.getDocumentRoot_ReturnType();

    /**
     * The meta object literal for the '{@link edu.pku.cn.xml.JavaLib.impl.FieldTypeImpl <em>Field Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see edu.pku.cn.xml.JavaLib.impl.FieldTypeImpl
     * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getFieldType()
     * @generated
     */
    EClass FIELD_TYPE = eINSTANCE.getFieldType();

    /**
     * The meta object literal for the '<em><b>Access</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FIELD_TYPE__ACCESS = eINSTANCE.getFieldType_Access();

    /**
     * The meta object literal for the '<em><b>Desc</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FIELD_TYPE__DESC = eINSTANCE.getFieldType_Desc();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FIELD_TYPE__NAME = eINSTANCE.getFieldType_Name();

    /**
     * The meta object literal for the '<em><b>Signature</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FIELD_TYPE__SIGNATURE = eINSTANCE.getFieldType_Signature();

    /**
     * The meta object literal for the '{@link edu.pku.cn.xml.JavaLib.impl.ImplementTypeImpl <em>Implement Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see edu.pku.cn.xml.JavaLib.impl.ImplementTypeImpl
     * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getImplementType()
     * @generated
     */
    EClass IMPLEMENT_TYPE = eINSTANCE.getImplementType();

    /**
     * The meta object literal for the '<em><b>Class Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IMPLEMENT_TYPE__CLASS_NAME = eINSTANCE.getImplementType_ClassName();

    /**
     * The meta object literal for the '{@link edu.pku.cn.xml.JavaLib.impl.JavaConfigTypeImpl <em>Java Config Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see edu.pku.cn.xml.JavaLib.impl.JavaConfigTypeImpl
     * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getJavaConfigType()
     * @generated
     */
    EClass JAVA_CONFIG_TYPE = eINSTANCE.getJavaConfigType();

    /**
     * The meta object literal for the '<em><b>Java Lib</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference JAVA_CONFIG_TYPE__JAVA_LIB = eINSTANCE.getJavaConfigType_JavaLib();

    /**
     * The meta object literal for the '{@link edu.pku.cn.xml.JavaLib.impl.JavaLibTypeImpl <em>Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see edu.pku.cn.xml.JavaLib.impl.JavaLibTypeImpl
     * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getJavaLibType()
     * @generated
     */
    EClass JAVA_LIB_TYPE = eINSTANCE.getJavaLibType();

    /**
     * The meta object literal for the '<em><b>Package</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference JAVA_LIB_TYPE__PACKAGE = eINSTANCE.getJavaLibType_Package();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute JAVA_LIB_TYPE__NAME = eINSTANCE.getJavaLibType_Name();

    /**
     * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute JAVA_LIB_TYPE__VERSION = eINSTANCE.getJavaLibType_Version();

    /**
     * The meta object literal for the '{@link edu.pku.cn.xml.JavaLib.impl.MethodTypeImpl <em>Method Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see edu.pku.cn.xml.JavaLib.impl.MethodTypeImpl
     * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getMethodType()
     * @generated
     */
    EClass METHOD_TYPE = eINSTANCE.getMethodType();

    /**
     * The meta object literal for the '<em><b>Return Type</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference METHOD_TYPE__RETURN_TYPE = eINSTANCE.getMethodType_ReturnType();

    /**
     * The meta object literal for the '<em><b>Access</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute METHOD_TYPE__ACCESS = eINSTANCE.getMethodType_Access();

    /**
     * The meta object literal for the '<em><b>Desc</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute METHOD_TYPE__DESC = eINSTANCE.getMethodType_Desc();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute METHOD_TYPE__NAME = eINSTANCE.getMethodType_Name();

    /**
     * The meta object literal for the '<em><b>Signature</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute METHOD_TYPE__SIGNATURE = eINSTANCE.getMethodType_Signature();

    /**
     * The meta object literal for the '{@link edu.pku.cn.xml.JavaLib.impl.PackageTypeImpl <em>Package Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see edu.pku.cn.xml.JavaLib.impl.PackageTypeImpl
     * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getPackageType()
     * @generated
     */
    EClass PACKAGE_TYPE = eINSTANCE.getPackageType();

    /**
     * The meta object literal for the '<em><b>Class</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PACKAGE_TYPE__CLASS = eINSTANCE.getPackageType_Class();

    /**
     * The meta object literal for the '<em><b>Package</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PACKAGE_TYPE__PACKAGE = eINSTANCE.getPackageType_Package();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PACKAGE_TYPE__NAME = eINSTANCE.getPackageType_Name();

    /**
     * The meta object literal for the '{@link edu.pku.cn.xml.JavaLib.impl.ReturnTypeTypeImpl <em>Return Type Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see edu.pku.cn.xml.JavaLib.impl.ReturnTypeTypeImpl
     * @see edu.pku.cn.xml.JavaLib.impl.JavaLibPackageImpl#getReturnTypeType()
     * @generated
     */
    EClass RETURN_TYPE_TYPE = eINSTANCE.getReturnTypeType();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RETURN_TYPE_TYPE__NAME = eINSTANCE.getReturnTypeType_Name();

  }

} //JavaLibPackage

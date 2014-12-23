/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.JavaLib;

import commonj.sdo.Sequence;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getClass_ <em>Class</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getField <em>Field</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getImplement <em>Implement</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getJavaConfig <em>Java Config</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getJavaLib <em>Java Lib</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getMethod <em>Method</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getPackage <em>Package</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getReturnType <em>Return Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject
{
  /**
   * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Mixed</em>' attribute list.
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getDocumentRoot_Mixed()
   * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
   *        extendedMetaData="kind='elementWildcard' name=':mixed'"
   * @generated
   */
  Sequence getMixed();

  /**
   * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map.
   * The key is of type {@link java.lang.String},
   * and the value is of type {@link java.lang.String},
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>XMLNS Prefix Map</em>' map isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>XMLNS Prefix Map</em>' map.
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getDocumentRoot_XMLNSPrefixMap()
   * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
   *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
   * @generated
   */
  Map getXMLNSPrefixMap();

  /**
   * Returns the value of the '<em><b>XSI Schema Location</b></em>' map.
   * The key is of type {@link java.lang.String},
   * and the value is of type {@link java.lang.String},
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>XSI Schema Location</em>' map isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>XSI Schema Location</em>' map.
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getDocumentRoot_XSISchemaLocation()
   * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
   *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
   * @generated
   */
  Map getXSISchemaLocation();

  /**
   * Returns the value of the '<em><b>Class</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Class</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Class</em>' containment reference.
   * @see #setClass(ClassType)
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getDocumentRoot_Class()
   * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
   *        extendedMetaData="kind='element' name='Class' namespace='##targetNamespace'"
   * @generated
   */
  ClassType getClass_();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getClass_ <em>Class</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Class</em>' containment reference.
   * @see #getClass_()
   * @generated
   */
  void setClass(ClassType value);

  /**
   * Returns the value of the '<em><b>Field</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Field</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Field</em>' containment reference.
   * @see #setField(FieldType)
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getDocumentRoot_Field()
   * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
   *        extendedMetaData="kind='element' name='Field' namespace='##targetNamespace'"
   * @generated
   */
  FieldType getField();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getField <em>Field</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Field</em>' containment reference.
   * @see #getField()
   * @generated
   */
  void setField(FieldType value);

  /**
   * Returns the value of the '<em><b>Implement</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Implement</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Implement</em>' containment reference.
   * @see #setImplement(ImplementType)
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getDocumentRoot_Implement()
   * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
   *        extendedMetaData="kind='element' name='Implement' namespace='##targetNamespace'"
   * @generated
   */
  ImplementType getImplement();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getImplement <em>Implement</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Implement</em>' containment reference.
   * @see #getImplement()
   * @generated
   */
  void setImplement(ImplementType value);

  /**
   * Returns the value of the '<em><b>Java Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Java Config</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Java Config</em>' containment reference.
   * @see #setJavaConfig(JavaConfigType)
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getDocumentRoot_JavaConfig()
   * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
   *        extendedMetaData="kind='element' name='JavaConfig' namespace='##targetNamespace'"
   * @generated
   */
  JavaConfigType getJavaConfig();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getJavaConfig <em>Java Config</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Java Config</em>' containment reference.
   * @see #getJavaConfig()
   * @generated
   */
  void setJavaConfig(JavaConfigType value);

  /**
   * Returns the value of the '<em><b>Java Lib</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Java Lib</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Java Lib</em>' containment reference.
   * @see #setJavaLib(JavaLibType)
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getDocumentRoot_JavaLib()
   * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
   *        extendedMetaData="kind='element' name='JavaLib' namespace='##targetNamespace'"
   * @generated
   */
  JavaLibType getJavaLib();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getJavaLib <em>Java Lib</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Java Lib</em>' containment reference.
   * @see #getJavaLib()
   * @generated
   */
  void setJavaLib(JavaLibType value);

  /**
   * Returns the value of the '<em><b>Method</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Method</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Method</em>' containment reference.
   * @see #setMethod(MethodType)
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getDocumentRoot_Method()
   * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
   *        extendedMetaData="kind='element' name='Method' namespace='##targetNamespace'"
   * @generated
   */
  MethodType getMethod();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getMethod <em>Method</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Method</em>' containment reference.
   * @see #getMethod()
   * @generated
   */
  void setMethod(MethodType value);

  /**
   * Returns the value of the '<em><b>Package</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Package</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Package</em>' containment reference.
   * @see #setPackage(PackageType)
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getDocumentRoot_Package()
   * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
   *        extendedMetaData="kind='element' name='Package' namespace='##targetNamespace'"
   * @generated
   */
  PackageType getPackage();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getPackage <em>Package</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Package</em>' containment reference.
   * @see #getPackage()
   * @generated
   */
  void setPackage(PackageType value);

  /**
   * Returns the value of the '<em><b>Return Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Return Type</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Return Type</em>' containment reference.
   * @see #setReturnType(ReturnTypeType)
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getDocumentRoot_ReturnType()
   * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
   *        extendedMetaData="kind='element' name='ReturnType' namespace='##targetNamespace'"
   * @generated
   */
  ReturnTypeType getReturnType();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.DocumentRoot#getReturnType <em>Return Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Return Type</em>' containment reference.
   * @see #getReturnType()
   * @generated
   */
  void setReturnType(ReturnTypeType value);

} // DocumentRoot
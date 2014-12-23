/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.JavaLib;

import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Method Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link edu.pku.cn.xml.JavaLib.MethodType#getReturnType <em>Return Type</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.MethodType#getAccess <em>Access</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.MethodType#getDesc <em>Desc</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.MethodType#getName <em>Name</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.MethodType#getSignature <em>Signature</em>}</li>
 * </ul>
 * </p>
 *
 * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getMethodType()
 * @model extendedMetaData="name='Method_._type' kind='elementOnly'"
 * @generated
 */
public interface MethodType
{
  /**
   * Returns the value of the '<em><b>Return Type</b></em>' containment reference list.
   * The list contents are of type {@link edu.pku.cn.xml.JavaLib.ReturnTypeType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Return Type</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Return Type</em>' containment reference list.
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getMethodType_ReturnType()
   * @model type="JavaLib.ReturnTypeType" containment="true"
   *        extendedMetaData="kind='element' name='ReturnType' namespace='##targetNamespace'"
   * @generated
   */
  List getReturnType();

  /**
   * Returns the value of the '<em><b>Access</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Access</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Access</em>' attribute.
   * @see #setAccess(String)
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getMethodType_Access()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='access' namespace='##targetNamespace'"
   * @generated
   */
  String getAccess();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.MethodType#getAccess <em>Access</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Access</em>' attribute.
   * @see #getAccess()
   * @generated
   */
  void setAccess(String value);

  /**
   * Returns the value of the '<em><b>Desc</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Desc</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Desc</em>' attribute.
   * @see #setDesc(String)
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getMethodType_Desc()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='desc' namespace='##targetNamespace'"
   * @generated
   */
  String getDesc();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.MethodType#getDesc <em>Desc</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Desc</em>' attribute.
   * @see #getDesc()
   * @generated
   */
  void setDesc(String value);

  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getMethodType_Name()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='name' namespace='##targetNamespace'"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.MethodType#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Signature</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Signature</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Signature</em>' attribute.
   * @see #setSignature(String)
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getMethodType_Signature()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='signature' namespace='##targetNamespace'"
   * @generated
   */
  String getSignature();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.MethodType#getSignature <em>Signature</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Signature</em>' attribute.
   * @see #getSignature()
   * @generated
   */
  void setSignature(String value);

} // MethodType
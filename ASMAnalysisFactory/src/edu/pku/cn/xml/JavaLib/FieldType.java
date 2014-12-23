/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.JavaLib;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Field Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link edu.pku.cn.xml.JavaLib.FieldType#getAccess <em>Access</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.FieldType#getDesc <em>Desc</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.FieldType#getName <em>Name</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.FieldType#getSignature <em>Signature</em>}</li>
 * </ul>
 * </p>
 *
 * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getFieldType()
 * @model extendedMetaData="name='Field_._type' kind='empty'"
 * @generated
 */
public interface FieldType
{
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
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getFieldType_Access()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='access' namespace='##targetNamespace'"
   * @generated
   */
  String getAccess();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.FieldType#getAccess <em>Access</em>}' attribute.
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
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getFieldType_Desc()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='desc' namespace='##targetNamespace'"
   * @generated
   */
  String getDesc();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.FieldType#getDesc <em>Desc</em>}' attribute.
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
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getFieldType_Name()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='name' namespace='##targetNamespace'"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.FieldType#getName <em>Name</em>}' attribute.
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
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getFieldType_Signature()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='signature' namespace='##targetNamespace'"
   * @generated
   */
  String getSignature();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.FieldType#getSignature <em>Signature</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Signature</em>' attribute.
   * @see #getSignature()
   * @generated
   */
  void setSignature(String value);

} // FieldType
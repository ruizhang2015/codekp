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
 * A representation of the model object '<em><b>Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link edu.pku.cn.xml.JavaLib.JavaLibType#getPackage <em>Package</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.JavaLibType#getName <em>Name</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.JavaLibType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getJavaLibType()
 * @model extendedMetaData="name='JavaLib_._type' kind='elementOnly'"
 * @generated
 */
public interface JavaLibType
{
  /**
   * Returns the value of the '<em><b>Package</b></em>' containment reference list.
   * The list contents are of type {@link edu.pku.cn.xml.JavaLib.PackageType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Package</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Package</em>' containment reference list.
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getJavaLibType_Package()
   * @model type="JavaLib.PackageType" containment="true" required="true"
   *        extendedMetaData="kind='element' name='Package' namespace='##targetNamespace'"
   * @generated
   */
  List getPackage();

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
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getJavaLibType_Name()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='name' namespace='##targetNamespace'"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.JavaLibType#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Version</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Version</em>' attribute.
   * @see #setVersion(String)
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getJavaLibType_Version()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='version' namespace='##targetNamespace'"
   * @generated
   */
  String getVersion();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.JavaLibType#getVersion <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Version</em>' attribute.
   * @see #getVersion()
   * @generated
   */
  void setVersion(String value);

} // JavaLibType
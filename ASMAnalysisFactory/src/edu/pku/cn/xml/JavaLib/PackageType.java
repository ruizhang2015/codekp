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
 * A representation of the model object '<em><b>Package Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link edu.pku.cn.xml.JavaLib.PackageType#getClass_ <em>Class</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.PackageType#getPackage <em>Package</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.PackageType#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getPackageType()
 * @model extendedMetaData="name='Package_._type' kind='elementOnly'"
 * @generated
 */
public interface PackageType
{
  /**
   * Returns the value of the '<em><b>Class</b></em>' containment reference list.
   * The list contents are of type {@link edu.pku.cn.xml.JavaLib.ClassType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Class</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Class</em>' containment reference list.
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getPackageType_Class()
   * @model type="JavaLib.ClassType" containment="true"
   *        extendedMetaData="kind='element' name='Class' namespace='##targetNamespace'"
   * @generated
   */
  List getClass_();

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
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getPackageType_Package()
   * @model type="JavaLib.PackageType" containment="true"
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
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getPackageType_Name()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='name' namespace='##targetNamespace'"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.PackageType#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

} // PackageType
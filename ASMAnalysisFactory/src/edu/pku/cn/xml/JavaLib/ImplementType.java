/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.JavaLib;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Implement Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link edu.pku.cn.xml.JavaLib.ImplementType#getClassName <em>Class Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getImplementType()
 * @model extendedMetaData="name='Implement_._type' kind='empty'"
 * @generated
 */
public interface ImplementType
{
  /**
   * Returns the value of the '<em><b>Class Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Class Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Class Name</em>' attribute.
   * @see #setClassName(String)
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getImplementType_ClassName()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='className' namespace='##targetNamespace'"
   * @generated
   */
  String getClassName();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.ImplementType#getClassName <em>Class Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Class Name</em>' attribute.
   * @see #getClassName()
   * @generated
   */
  void setClassName(String value);

} // ImplementType
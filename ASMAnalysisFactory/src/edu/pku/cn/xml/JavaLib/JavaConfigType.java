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
 * A representation of the model object '<em><b>Java Config Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link edu.pku.cn.xml.JavaLib.JavaConfigType#getJavaLib <em>Java Lib</em>}</li>
 * </ul>
 * </p>
 *
 * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getJavaConfigType()
 * @model extendedMetaData="name='JavaConfig_._type' kind='elementOnly'"
 * @generated
 */
public interface JavaConfigType
{
  /**
   * Returns the value of the '<em><b>Java Lib</b></em>' containment reference list.
   * The list contents are of type {@link edu.pku.cn.xml.JavaLib.JavaLibType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Java Lib</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Java Lib</em>' containment reference list.
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getJavaConfigType_JavaLib()
   * @model type="JavaLib.JavaLibType" containment="true" required="true"
   *        extendedMetaData="kind='element' name='JavaLib' namespace='##targetNamespace'"
   * @generated
   */
  List getJavaLib();

} // JavaConfigType
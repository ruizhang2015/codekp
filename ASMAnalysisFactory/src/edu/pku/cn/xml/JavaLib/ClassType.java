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
 * A representation of the model object '<em><b>Class Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link edu.pku.cn.xml.JavaLib.ClassType#getImplement <em>Implement</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.ClassType#getField <em>Field</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.ClassType#getMethod <em>Method</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.ClassType#getExtends <em>Extends</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.ClassType#isIsInterface <em>Is Interface</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.ClassType#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getClassType()
 * @model extendedMetaData="name='Class_._type' kind='elementOnly'"
 * @generated
 */
public interface ClassType
{
  /**
   * Returns the value of the '<em><b>Implement</b></em>' containment reference list.
   * The list contents are of type {@link edu.pku.cn.xml.JavaLib.ImplementType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Implement</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Implement</em>' containment reference list.
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getClassType_Implement()
   * @model type="JavaLib.ImplementType" containment="true"
   *        extendedMetaData="kind='element' name='Implement' namespace='##targetNamespace'"
   * @generated
   */
  List getImplement();

  /**
   * Returns the value of the '<em><b>Field</b></em>' containment reference list.
   * The list contents are of type {@link edu.pku.cn.xml.JavaLib.FieldType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Field</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Field</em>' containment reference list.
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getClassType_Field()
   * @model type="JavaLib.FieldType" containment="true"
   *        extendedMetaData="kind='element' name='Field' namespace='##targetNamespace'"
   * @generated
   */
  List getField();

  /**
   * Returns the value of the '<em><b>Method</b></em>' containment reference list.
   * The list contents are of type {@link edu.pku.cn.xml.JavaLib.MethodType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Method</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Method</em>' containment reference list.
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getClassType_Method()
   * @model type="JavaLib.MethodType" containment="true"
   *        extendedMetaData="kind='element' name='Method' namespace='##targetNamespace'"
   * @generated
   */
  List getMethod();

  /**
   * Returns the value of the '<em><b>Extends</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Extends</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Extends</em>' attribute.
   * @see #setExtends(String)
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getClassType_Extends()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='extends' namespace='##targetNamespace'"
   * @generated
   */
  String getExtends();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.ClassType#getExtends <em>Extends</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Extends</em>' attribute.
   * @see #getExtends()
   * @generated
   */
  void setExtends(String value);

  /**
   * Returns the value of the '<em><b>Is Interface</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Is Interface</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Is Interface</em>' attribute.
   * @see #isSetIsInterface()
   * @see #unsetIsInterface()
   * @see #setIsInterface(boolean)
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getClassType_IsInterface()
   * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
   *        extendedMetaData="kind='attribute' name='isInterface' namespace='##targetNamespace'"
   * @generated
   */
  boolean isIsInterface();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.ClassType#isIsInterface <em>Is Interface</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Is Interface</em>' attribute.
   * @see #isSetIsInterface()
   * @see #unsetIsInterface()
   * @see #isIsInterface()
   * @generated
   */
  void setIsInterface(boolean value);

  /**
   * Unsets the value of the '{@link edu.pku.cn.xml.JavaLib.ClassType#isIsInterface <em>Is Interface</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSetIsInterface()
   * @see #isIsInterface()
   * @see #setIsInterface(boolean)
   * @generated
   */
  void unsetIsInterface();

  /**
   * Returns whether the value of the '{@link edu.pku.cn.xml.JavaLib.ClassType#isIsInterface <em>Is Interface</em>}' attribute is set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return whether the value of the '<em>Is Interface</em>' attribute is set.
   * @see #unsetIsInterface()
   * @see #isIsInterface()
   * @see #setIsInterface(boolean)
   * @generated
   */
  boolean isSetIsInterface();

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
   * @see edu.pku.cn.xml.JavaLib.JavaLibPackage#getClassType_Name()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='name' namespace='##targetNamespace'"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link edu.pku.cn.xml.JavaLib.ClassType#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

} // ClassType
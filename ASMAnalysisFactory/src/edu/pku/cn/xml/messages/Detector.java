/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.messages;

import java.util.Map;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Detector Type</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link edu.pku.cn.xml.messages.Detector#getDetails <em>Details</em>}</li>
 * <li>{@link edu.pku.cn.xml.messages.Detector#getBugPattern <em>Bug Pattern
 * </em>}</li>
 * <li>{@link edu.pku.cn.xml.messages.Detector#getClassName <em>Class Name</em>}
 * </li>
 * </ul>
 * </p>
 * 
 * @see edu.pku.cn.xml.messages.MessagesPackage#getDetectorType()
 * @model extendedMetaData="name='Detector_._type' kind='elementOnly'"
 * @generated
 */
public interface Detector {
	/**
	 * Returns the value of the '<em><b>Details</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Details</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Details</em>' attribute.
	 * @see #setDetails(String)
	 * @see edu.pku.cn.xml.messages.MessagesPackage#getDetectorType_Details()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true" extendedMetaData=
	 *        "kind='element' name='Details' namespace='##targetNamespace'"
	 * @generated
	 */
	String getDetails();

	/**
	 * Sets the value of the '
	 * {@link edu.pku.cn.xml.messages.Detector#getDetails <em>Details</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Details</em>' attribute.
	 * @see #getDetails()
	 * @generated
	 */
	void setDetails(String value);

	/**
	 * Returns the value of the '<em><b>Bug Pattern</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link edu.pku.cn.xml.messages.BugPattern}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bug Pattern</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Bug Pattern</em>' containment reference
	 *         list.
	 * @see edu.pku.cn.xml.messages.MessagesPackage#getDetectorType_BugPattern()
	 * @model type="Messages.BugPatternType" containment="true" required="true"
	 *        extendedMetaData
	 *        ="kind='element' name='BugPattern' namespace='##targetNamespace'"
	 * @generated
	 */
	Map<String, BugPattern> getBugPattern();

	BugPattern getBugPattern(String name);

	/**
	 * Returns the value of the '<em><b>Class Name</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Class Name</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Class Name</em>' attribute.
	 * @see #setClassName(String)
	 * @see edu.pku.cn.xml.messages.MessagesPackage#getDetectorType_ClassName()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData=
	 *        "kind='attribute' name='className' namespace='##targetNamespace'"
	 * @generated
	 */
	String getClassName();

	/**
	 * Sets the value of the '
	 * {@link edu.pku.cn.xml.messages.Detector#getClassName <em>Class Name</em>}
	 * ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Class Name</em>' attribute.
	 * @see #getClassName()
	 * @generated
	 */
	void setClassName(String value);

} // DetectorType
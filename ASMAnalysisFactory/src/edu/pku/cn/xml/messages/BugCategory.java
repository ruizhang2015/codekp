/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.messages;

import java.util.List;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Bug Category Type</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link edu.pku.cn.xml.messages.BugCategory#getDescription <em>Description
 * </em>}</li>
 * <li>{@link edu.pku.cn.xml.messages.BugCategory#getDetails <em>Details</em>}</li>
 * <li>{@link edu.pku.cn.xml.messages.BugCategory#getDetector <em>Detector</em>}
 * </li>
 * <li>{@link edu.pku.cn.xml.messages.BugCategory#getAbbreviation <em>
 * Abbreviation</em>}</li>
 * <li>{@link edu.pku.cn.xml.messages.BugCategory#getCategory <em>Category</em>}
 * </li>
 * </ul>
 * </p>
 * 
 * @see edu.pku.cn.xml.messages.MessagesPackage#getBugCategoryType()
 * @model extendedMetaData="name='BugCategory_._type' kind='elementOnly'"
 * @generated
 */
public interface BugCategory {
	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see edu.pku.cn.xml.messages.MessagesPackage#getBugCategoryType_Description()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true" extendedMetaData=
	 *        "kind='element' name='Description' namespace='##targetNamespace'"
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '
	 * {@link edu.pku.cn.xml.messages.BugCategory#getDescription
	 * <em>Description</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

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
	 * @see edu.pku.cn.xml.messages.MessagesPackage#getBugCategoryType_Details()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true" extendedMetaData=
	 *        "kind='element' name='Details' namespace='##targetNamespace'"
	 * @generated
	 */
	String getDetails();

	/**
	 * Sets the value of the '
	 * {@link edu.pku.cn.xml.messages.BugCategory#getDetails <em>Details</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Details</em>' attribute.
	 * @see #getDetails()
	 * @generated
	 */
	void setDetails(String value);

	/**
	 * Returns the value of the '<em><b>Detector</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link edu.pku.cn.xml.messages.Detector}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Detector</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Detector</em>' containment reference list.
	 * @see edu.pku.cn.xml.messages.MessagesPackage#getBugCategoryType_Detector()
	 * @model type="Messages.DetectorType" containment="true" required="true"
	 *        extendedMetaData
	 *        ="kind='element' name='Detector' namespace='##targetNamespace'"
	 * @generated
	 */
	List<Detector> getDetector();

	/**
	 * Returns the value of the '<em><b>Abbreviation</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Abbreviation</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Abbreviation</em>' attribute.
	 * @see #setAbbreviation(String)
	 * @see edu.pku.cn.xml.messages.MessagesPackage#getBugCategoryType_Abbreviation()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData=
	 *        "kind='attribute' name='abbreviation' namespace='##targetNamespace'"
	 * @generated
	 */
	String getAbbreviation();

	/**
	 * Sets the value of the '
	 * {@link edu.pku.cn.xml.messages.BugCategory#getAbbreviation
	 * <em>Abbreviation</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Abbreviation</em>' attribute.
	 * @see #getAbbreviation()
	 * @generated
	 */
	void setAbbreviation(String value);

	/**
	 * Returns the value of the '<em><b>Category</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Category</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Category</em>' attribute.
	 * @see #setCategory(String)
	 * @see edu.pku.cn.xml.messages.MessagesPackage#getBugCategoryType_Category()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData
	 *        ="kind='attribute' name='category' namespace='##targetNamespace'"
	 * @generated
	 */
	String getCategory();

	/**
	 * Sets the value of the '
	 * {@link edu.pku.cn.xml.messages.BugCategory#getCategory <em>Category</em>}
	 * ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Category</em>' attribute.
	 * @see #getCategory()
	 * @generated
	 */
	void setCategory(String value);

} // BugCategoryType
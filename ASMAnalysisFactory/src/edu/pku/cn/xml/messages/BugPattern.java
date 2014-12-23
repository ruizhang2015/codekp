/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.messages;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Bug Pattern Type</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link edu.pku.cn.xml.messages.BugPattern#getShortDescription <em>Short
 * Description</em>}</li>
 * <li>{@link edu.pku.cn.xml.messages.BugPattern#getLongDescription <em>Long
 * Description</em>}</li>
 * <li>{@link edu.pku.cn.xml.messages.BugPattern#getDetails <em>Details</em>}</li>
 * <li>{@link edu.pku.cn.xml.messages.BugPattern#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 * 
 * @see edu.pku.cn.xml.messages.MessagesPackage#getBugPatternType()
 * @model extendedMetaData="name='BugPattern_._type' kind='elementOnly'"
 * @generated
 */
public interface BugPattern {
	/**
	 * Returns the value of the '<em><b>Short Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Short Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Short Description</em>' attribute.
	 * @see #setShortDescription(String)
	 * @see edu.pku.cn.xml.messages.MessagesPackage#getBugPatternType_ShortDescription()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true" extendedMetaData=
	 *        "kind='element' name='ShortDescription' namespace='##targetNamespace'"
	 * @generated
	 */
	String getShortDescription();

	/**
	 * Sets the value of the '
	 * {@link edu.pku.cn.xml.messages.BugPattern#getShortDescription
	 * <em>Short Description</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Short Description</em>' attribute.
	 * @see #getShortDescription()
	 * @generated
	 */
	void setShortDescription(String value);

	/**
	 * Returns the value of the '<em><b>Long Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Long Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Long Description</em>' attribute.
	 * @see #setLongDescription(String)
	 * @see edu.pku.cn.xml.messages.MessagesPackage#getBugPatternType_LongDescription()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true" extendedMetaData=
	 *        "kind='element' name='LongDescription' namespace='##targetNamespace'"
	 * @generated
	 */
	String getLongDescription();

	/**
	 * Sets the value of the '
	 * {@link edu.pku.cn.xml.messages.BugPattern#getLongDescription
	 * <em>Long Description</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Long Description</em>' attribute.
	 * @see #getLongDescription()
	 * @generated
	 */
	void setLongDescription(String value);

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
	 * @see edu.pku.cn.xml.messages.MessagesPackage#getBugPatternType_Details()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true" extendedMetaData=
	 *        "kind='element' name='Details' namespace='##targetNamespace'"
	 * @generated
	 */
	String getDetails();

	/**
	 * Sets the value of the '
	 * {@link edu.pku.cn.xml.messages.BugPattern#getDetails <em>Details</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Details</em>' attribute.
	 * @see #getDetails()
	 * @generated
	 */
	void setDetails(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see edu.pku.cn.xml.messages.MessagesPackage#getBugPatternType_Name()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData
	 *        ="kind='attribute' name='name' namespace='##targetNamespace'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link edu.pku.cn.xml.messages.BugPattern#getName
	 * <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // BugPatternType
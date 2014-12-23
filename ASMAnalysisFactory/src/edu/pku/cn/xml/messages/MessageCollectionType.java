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
 * <em><b>Message Collection Type</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link edu.pku.cn.xml.messages.MessageCollectionType#getBugCategory <em>
 * Bug Category</em>}</li>
 * </ul>
 * </p>
 * 
 * @see edu.pku.cn.xml.messages.MessagesPackage#getMessageCollectionType()
 * @model extendedMetaData="name='MessageCollection_._type' kind='elementOnly'"
 * @generated
 */
public interface MessageCollectionType {
	/**
	 * Returns the value of the '<em><b>Bug Category</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link edu.pku.cn.xml.messages.BugCategory}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bug Category</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Bug Category</em>' containment reference
	 *         list.
	 * @see edu.pku.cn.xml.messages.MessagesPackage#getMessageCollectionType_BugCategory()
	 * @model type="Messages.BugCategoryType" containment="true" required="true"
	 *        extendedMetaData=
	 *        "kind='element' name='BugCategory' namespace='##targetNamespace'"
	 * @generated
	 */
	List<BugCategory> getBugCategory();

} // MessageCollectionType
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.messages;

import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see edu.pku.cn.xml.messages.MessagesPackage
 * @generated
 */
public interface MessagesFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	MessagesFactory eINSTANCE = edu.pku.cn.xml.messages.impl.MessagesFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Bug Category Type</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Bug Category Type</em>'.
	 * @generated
	 */
	BugCategory createBugCategory(Element element);

	/**
	 * Returns a new object of class '<em>Bug Pattern Type</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Bug Pattern Type</em>'.
	 * @generated
	 */
	BugPattern createBugPattern(Element element);

	/**
	 * Returns a new object of class '<em>Detector Type</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Detector Type</em>'.
	 * @generated
	 */
	Detector createDetector(Element element);

	/**
	 * Returns a new object of class '<em>Message Collection Type</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Message Collection Type</em>'.
	 * @generated
	 */
	MessageCollectionType createMessageCollectionType();

} // MessagesFactory

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.messages.impl;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.pku.cn.xml.messages.*;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class MessagesFactoryImpl implements MessagesFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public static MessagesFactory init() {
		try {
			MessagesFactory theMessagesFactory = (MessagesFactory) EPackage.Registry.INSTANCE
					.getEFactory("file:/C:/Documents%20and%20Settings/Liuxizhiyi/IBM/rationalsdp7.0/workspace/DetectorConfig/messages.xsd");
			if (theMessagesFactory != null) {
				return theMessagesFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MessagesFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public MessagesFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public BugCategory createBugCategory(Element element) {
		BugCategoryImpl bugCategoryType = new BugCategoryImpl();
		bugCategoryType.setAbbreviation(element.getAttribute("abbreviation"));
		bugCategoryType.setCategory(element.getAttribute("category"));
		NodeList nodes = element.getElementsByTagName("Description");
		bugCategoryType.setDescription(nodes.item(0).getTextContent());
		nodes = element.getElementsByTagName("Details");
		bugCategoryType.setDetails(nodes.item(0).getTextContent());
		nodes = element.getElementsByTagName("Detector");
		List<Detector> detectors = bugCategoryType.getDetector();
		for (int i = 0; i < nodes.getLength(); i++) {
			detectors.add(createDetector((Element) nodes.item(i)));
		}
		return bugCategoryType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public BugPattern createBugPattern(Element element) {
		BugPatternImpl bugPatternType = new BugPatternImpl();
		bugPatternType.setName(element.getAttribute("name"));
		NodeList nodes = element.getElementsByTagName("ShortDescription");
		bugPatternType.setShortDescription(nodes.item(0).getTextContent());
		nodes = element.getElementsByTagName("LongDescription");
		bugPatternType.setLongDescription(nodes.item(0).getTextContent());
		nodes = element.getElementsByTagName("Details");
		bugPatternType.setDetails(nodes.item(0).getTextContent());
		return bugPatternType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Detector createDetector(Element element) {
		DetectorImpl detectorType = new DetectorImpl();
		detectorType.setClassName(element.getAttribute("className"));
		NodeList nodes = element.getElementsByTagName("Details");
		detectorType.setDetails(nodes.item(0).getTextContent());
		Map<String, BugPattern> patterns = detectorType.getBugPattern();
		nodes = element.getElementsByTagName("BugPattern");
		for (int i = 0; i < nodes.getLength(); i++) {
			BugPattern pattern = createBugPattern((Element) nodes.item(i));
			patterns.put(pattern.getName(), pattern);
		}

		return detectorType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MessageCollectionType createMessageCollectionType() {
		MessageCollectionTypeImpl messageCollectionType = new MessageCollectionTypeImpl();
		return messageCollectionType;
	}

} // MessagesFactoryImpl

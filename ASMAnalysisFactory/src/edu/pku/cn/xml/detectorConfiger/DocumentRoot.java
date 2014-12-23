/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.detectorConfiger;

import commonj.sdo.Sequence;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Document Root</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.DocumentRoot#getMixed <em>Mixed
 * </em>}</li>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.DocumentRoot#getXMLNSPrefixMap
 * <em>XMLNS Prefix Map</em>}</li>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.DocumentRoot#getXSISchemaLocation
 * <em>XSI Schema Location</em>}</li>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.DocumentRoot#getCategory <em>
 * Category</em>}</li>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.DocumentRoot#getDectectorFactory
 * <em>Dectector Factory</em>}</li>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.DocumentRoot#getDetector <em>
 * Detector</em>}</li>
 * </ul>
 * </p>
 * 
 * @see edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Mixed</b></em>' attribute list. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mixed</em>' attribute list isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Mixed</em>' attribute list.
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage#getDocumentRoot_Mixed()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry"
	 *        many="true"
	 *        extendedMetaData="kind='elementWildcard' name=':mixed'"
	 * @generated
	 */
	Sequence getMixed();

	/**
	 * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map. The key
	 * is of type {@link java.lang.String}, and the value is of type
	 * {@link java.lang.String}, <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>XMLNS Prefix Map</em>' map isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>XMLNS Prefix Map</em>' map.
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage#getDocumentRoot_XMLNSPrefixMap()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry"
	 *        keyType="java.lang.String" valueType="java.lang.String"
	 *        transient="true"
	 *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
	 * @generated
	 */
	Map getXMLNSPrefixMap();

	/**
	 * Returns the value of the '<em><b>XSI Schema Location</b></em>' map. The
	 * key is of type {@link java.lang.String}, and the value is of type
	 * {@link java.lang.String}, <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>XSI Schema Location</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>XSI Schema Location</em>' map.
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage#getDocumentRoot_XSISchemaLocation()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry"
	 *        keyType="java.lang.String" valueType="java.lang.String"
	 *        transient="true"
	 *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
	 * @generated
	 */
	Map getXSISchemaLocation();

	/**
	 * Returns the value of the '<em><b>Category</b></em>' containment
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Category</em>' containment reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Category</em>' containment reference.
	 * @see #setCategory(CategoryType)
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage#getDocumentRoot_Category()
	 * @model containment="true" upper="-2" transient="true" volatile="true"
	 *        derived="true" extendedMetaData=
	 *        "kind='element' name='Category' namespace='##targetNamespace'"
	 * @generated
	 */
	CategoryType getCategory();

	/**
	 * Sets the value of the '
	 * {@link edu.pku.cn.xml.detectorConfiger.DocumentRoot#getCategory
	 * <em>Category</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Category</em>' containment
	 *            reference.
	 * @see #getCategory()
	 * @generated
	 */
	void setCategory(CategoryType value);

	/**
	 * Returns the value of the '<em><b>Dectector Factory</b></em>' containment
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dectector Factory</em>' containment reference
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Dectector Factory</em>' containment
	 *         reference.
	 * @see #setDectectorFactory(DectectorFactoryType)
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage#getDocumentRoot_DectectorFactory()
	 * @model containment="true" upper="-2" transient="true" volatile="true"
	 *        derived="true" extendedMetaData=
	 *        "kind='element' name='DectectorFactory' namespace='##targetNamespace'"
	 * @generated
	 */
	DectectorFactoryType getDectectorFactory();

	/**
	 * Sets the value of the '
	 * {@link edu.pku.cn.xml.detectorConfiger.DocumentRoot#getDectectorFactory
	 * <em>Dectector Factory</em>}' containment reference. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Dectector Factory</em>' containment
	 *            reference.
	 * @see #getDectectorFactory()
	 * @generated
	 */
	void setDectectorFactory(DectectorFactoryType value);

	/**
	 * Returns the value of the '<em><b>Detector</b></em>' containment
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Detector</em>' containment reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Detector</em>' containment reference.
	 * @see #setDetector(DetectorType)
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage#getDocumentRoot_Detector()
	 * @model containment="true" upper="-2" transient="true" volatile="true"
	 *        derived="true" extendedMetaData=
	 *        "kind='element' name='Detector' namespace='##targetNamespace'"
	 * @generated
	 */
	DetectorType getDetector();

	/**
	 * Sets the value of the '
	 * {@link edu.pku.cn.xml.detectorConfiger.DocumentRoot#getDetector
	 * <em>Detector</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Detector</em>' containment
	 *            reference.
	 * @see #getDetector()
	 * @generated
	 */
	void setDetector(DetectorType value);

} // DocumentRoot
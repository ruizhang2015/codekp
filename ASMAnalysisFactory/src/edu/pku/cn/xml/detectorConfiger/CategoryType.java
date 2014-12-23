/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.detectorConfiger;

import java.util.List;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Category Type</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.CategoryType#getDetector <em>
 * Detector</em>}</li>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.CategoryType#getName <em>Name
 * </em>}</li>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.CategoryType#getPackageName <em>
 * Package Name</em>}</li>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.CategoryType#getPriority <em>
 * Priority</em>}</li>
 * </ul>
 * </p>
 * 
 * @see edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage#getCategoryType()
 * @model extendedMetaData="name='Category_._type' kind='elementOnly'"
 * @generated
 */
public interface CategoryType {
	/**
	 * Returns the value of the '<em><b>Detector</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link edu.pku.cn.xml.detectorConfiger.DetectorType}. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Detector</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Detector</em>' containment reference list.
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage#getCategoryType_Detector()
	 * @model type="DetectorConfiger.DetectorType" containment="true"
	 *        required="true" extendedMetaData=
	 *        "kind='element' name='Detector' namespace='##targetNamespace'"
	 * @generated
	 */
	List getDetector();

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
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage#getCategoryType_Name()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData
	 *        ="kind='attribute' name='name' namespace='##targetNamespace'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '
	 * {@link edu.pku.cn.xml.detectorConfiger.CategoryType#getName
	 * <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Package Name</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Package Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Package Name</em>' attribute.
	 * @see #setPackageName(String)
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage#getCategoryType_PackageName()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData=
	 *        "kind='attribute' name='packageName' namespace='##targetNamespace'"
	 * @generated
	 */
	String getPackageName();

	/**
	 * Sets the value of the '
	 * {@link edu.pku.cn.xml.detectorConfiger.CategoryType#getPackageName
	 * <em>Package Name</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Package Name</em>' attribute.
	 * @see #getPackageName()
	 * @generated
	 */
	void setPackageName(String value);

	/**
	 * Returns the value of the '<em><b>Priority</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Priority</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Priority</em>' attribute.
	 * @see #setPriority(String)
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage#getCategoryType_Priority()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData
	 *        ="kind='attribute' name='priority' namespace='##targetNamespace'"
	 * @generated
	 */
	String getPriority();

	/**
	 * Sets the value of the '
	 * {@link edu.pku.cn.xml.detectorConfiger.CategoryType#getPriority
	 * <em>Priority</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>Priority</em>' attribute.
	 * @see #getPriority()
	 * @generated
	 */
	void setPriority(String value);

} // CategoryType
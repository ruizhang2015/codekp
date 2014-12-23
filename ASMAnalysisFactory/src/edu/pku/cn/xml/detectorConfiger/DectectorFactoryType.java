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
 * <em><b>Dectector Factory Type</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.DectectorFactoryType#getCategory
 * <em>Category</em>}</li>
 * </ul>
 * </p>
 * 
 * @see edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage#getDectectorFactoryType()
 * @model extendedMetaData="name='DectectorFactory_._type' kind='elementOnly'"
 * @generated
 */
public interface DectectorFactoryType {
	/**
	 * Returns the value of the '<em><b>Category</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link edu.pku.cn.xml.detectorConfiger.CategoryType}. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Category</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Category</em>' containment reference list.
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage#getDectectorFactoryType_Category()
	 * @model type="DetectorConfiger.CategoryType" containment="true"
	 *        required="true" extendedMetaData=
	 *        "kind='element' name='Category' namespace='##targetNamespace'"
	 * @generated
	 */
	List getCategory();

} // DectectorFactoryType
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.detectorConfiger;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage
 * @generated
 */
public interface DetectorConfigerFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	DetectorConfigerFactory eINSTANCE = edu.pku.cn.xml.detectorConfiger.impl.DetectorConfigerFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Category Type</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Category Type</em>'.
	 * @generated
	 */
	CategoryType createCategoryType();

	/**
	 * Returns a new object of class '<em>Dectector Factory Type</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Dectector Factory Type</em>'.
	 * @generated
	 */
	DectectorFactoryType createDectectorFactoryType();

	/**
	 * Returns a new object of class '<em>Detector Type</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Detector Type</em>'.
	 * @generated
	 */
	DetectorType createDetectorType();

	/**
	 * Returns a new object of class '<em>Document Root</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Document Root</em>'.
	 * @generated
	 */
	DocumentRoot createDocumentRoot();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	DetectorConfigerPackage getDetectorConfigerPackage();

} // DetectorConfigerFactory

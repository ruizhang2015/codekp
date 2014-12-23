/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.detectorConfiger.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import edu.pku.cn.xml.detectorConfiger.*;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class DetectorConfigerFactoryImpl extends EFactoryImpl implements DetectorConfigerFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public static DetectorConfigerFactory init() {
		try {
			DetectorConfigerFactory theDetectorConfigerFactory = (DetectorConfigerFactory) EPackage.Registry.INSTANCE
					.getEFactory("file:/C:/Documents%20and%20Settings/Liuxizhiyi/IBM/rationalsdp7.0/workspace/DetectorConfig/DetectorConfiger.xsd");
			if (theDetectorConfigerFactory != null) {
				return theDetectorConfigerFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DetectorConfigerFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public DetectorConfigerFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case DetectorConfigerPackage.CATEGORY_TYPE:
			return (EObject) createCategoryType();
		case DetectorConfigerPackage.DECTECTOR_FACTORY_TYPE:
			return (EObject) createDectectorFactoryType();
		case DetectorConfigerPackage.DETECTOR_TYPE:
			return (EObject) createDetectorType();
		case DetectorConfigerPackage.DOCUMENT_ROOT:
			return (EObject) createDocumentRoot();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public CategoryType createCategoryType() {
		CategoryTypeImpl categoryType = new CategoryTypeImpl();
		return categoryType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DectectorFactoryType createDectectorFactoryType() {
		DectectorFactoryTypeImpl dectectorFactoryType = new DectectorFactoryTypeImpl();
		return dectectorFactoryType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DetectorType createDetectorType() {
		DetectorTypeImpl detectorType = new DetectorTypeImpl();
		return detectorType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DocumentRoot createDocumentRoot() {
		DocumentRootImpl documentRoot = new DocumentRootImpl();
		return documentRoot;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DetectorConfigerPackage getDetectorConfigerPackage() {
		return (DetectorConfigerPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	public static DetectorConfigerPackage getPackage() {
		return DetectorConfigerPackage.eINSTANCE;
	}

} // DetectorConfigerFactoryImpl

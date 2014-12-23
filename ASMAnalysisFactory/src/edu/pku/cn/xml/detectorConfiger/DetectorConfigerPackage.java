/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.detectorConfiger;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains
 * accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see edu.pku.cn.xml.detectorConfiger.DetectorConfigerFactory
 * @model kind="package" extendedMetaData="qualified='false'"
 * @generated
 */
public interface DetectorConfigerPackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "DetectorConfiger";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "file:/C:/Documents%20and%20Settings/Liuxizhiyi/IBM/rationalsdp7.0/workspace/DetectorConfig/DetectorConfiger.xsd";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "DetectorConfiger";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	DetectorConfigerPackage eINSTANCE = edu.pku.cn.xml.detectorConfiger.impl.DetectorConfigerPackageImpl.init();

	/**
	 * The meta object id for the '
	 * {@link edu.pku.cn.xml.detectorConfiger.impl.CategoryTypeImpl
	 * <em>Category Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see edu.pku.cn.xml.detectorConfiger.impl.CategoryTypeImpl
	 * @see edu.pku.cn.xml.detectorConfiger.impl.DetectorConfigerPackageImpl#getCategoryType()
	 * @generated
	 */
	int CATEGORY_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Detector</b></em>' containment reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORY_TYPE__DETECTOR = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORY_TYPE__NAME = 1;

	/**
	 * The feature id for the '<em><b>Package Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORY_TYPE__PACKAGE_NAME = 2;

	/**
	 * The feature id for the '<em><b>Priority</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORY_TYPE__PRIORITY = 3;

	/**
	 * The number of structural features of the '<em>Category Type</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORY_TYPE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '
	 * {@link edu.pku.cn.xml.detectorConfiger.impl.DectectorFactoryTypeImpl
	 * <em>Dectector Factory Type</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see edu.pku.cn.xml.detectorConfiger.impl.DectectorFactoryTypeImpl
	 * @see edu.pku.cn.xml.detectorConfiger.impl.DetectorConfigerPackageImpl#getDectectorFactoryType()
	 * @generated
	 */
	int DECTECTOR_FACTORY_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Category</b></em>' containment reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DECTECTOR_FACTORY_TYPE__CATEGORY = 0;

	/**
	 * The number of structural features of the '<em>Dectector Factory Type</em>
	 * ' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DECTECTOR_FACTORY_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '
	 * {@link edu.pku.cn.xml.detectorConfiger.impl.DetectorTypeImpl
	 * <em>Detector Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see edu.pku.cn.xml.detectorConfiger.impl.DetectorTypeImpl
	 * @see edu.pku.cn.xml.detectorConfiger.impl.DetectorConfigerPackageImpl#getDetectorType()
	 * @generated
	 */
	int DETECTOR_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Class Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DETECTOR_TYPE__CLASS_NAME = 0;

	/**
	 * The feature id for the '<em><b>Disabled</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DETECTOR_TYPE__DISABLED = 1;

	/**
	 * The feature id for the '<em><b>Hidden</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DETECTOR_TYPE__HIDDEN = 2;

	/**
	 * The feature id for the '<em><b>Reports</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DETECTOR_TYPE__REPORTS = 3;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DETECTOR_TYPE__SPEED = 4;

	/**
	 * The number of structural features of the '<em>Detector Type</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DETECTOR_TYPE_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '
	 * {@link edu.pku.cn.xml.detectorConfiger.impl.DocumentRootImpl
	 * <em>Document Root</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see edu.pku.cn.xml.detectorConfiger.impl.DocumentRootImpl
	 * @see edu.pku.cn.xml.detectorConfiger.impl.DetectorConfigerPackageImpl#getDocumentRoot()
	 * @generated
	 */
	int DOCUMENT_ROOT = 3;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Category</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__CATEGORY = 3;

	/**
	 * The feature id for the '<em><b>Dectector Factory</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__DECTECTOR_FACTORY = 4;

	/**
	 * The feature id for the '<em><b>Detector</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__DETECTOR = 5;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 6;

	/**
	 * Returns the meta object for class '
	 * {@link edu.pku.cn.xml.detectorConfiger.CategoryType
	 * <em>Category Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Category Type</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.CategoryType
	 * @generated
	 */
	EClass getCategoryType();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link edu.pku.cn.xml.detectorConfiger.CategoryType#getDetector
	 * <em>Detector</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Detector</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.CategoryType#getDetector()
	 * @see #getCategoryType()
	 * @generated
	 */
	EReference getCategoryType_Detector();

	/**
	 * Returns the meta object for the attribute '
	 * {@link edu.pku.cn.xml.detectorConfiger.CategoryType#getName
	 * <em>Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.CategoryType#getName()
	 * @see #getCategoryType()
	 * @generated
	 */
	EAttribute getCategoryType_Name();

	/**
	 * Returns the meta object for the attribute '
	 * {@link edu.pku.cn.xml.detectorConfiger.CategoryType#getPackageName
	 * <em>Package Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Package Name</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.CategoryType#getPackageName()
	 * @see #getCategoryType()
	 * @generated
	 */
	EAttribute getCategoryType_PackageName();

	/**
	 * Returns the meta object for the attribute '
	 * {@link edu.pku.cn.xml.detectorConfiger.CategoryType#getPriority
	 * <em>Priority</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Priority</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.CategoryType#getPriority()
	 * @see #getCategoryType()
	 * @generated
	 */
	EAttribute getCategoryType_Priority();

	/**
	 * Returns the meta object for class '
	 * {@link edu.pku.cn.xml.detectorConfiger.DectectorFactoryType
	 * <em>Dectector Factory Type</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Dectector Factory Type</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.DectectorFactoryType
	 * @generated
	 */
	EClass getDectectorFactoryType();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link edu.pku.cn.xml.detectorConfiger.DectectorFactoryType#getCategory
	 * <em>Category</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Category</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.DectectorFactoryType#getCategory()
	 * @see #getDectectorFactoryType()
	 * @generated
	 */
	EReference getDectectorFactoryType_Category();

	/**
	 * Returns the meta object for class '
	 * {@link edu.pku.cn.xml.detectorConfiger.DetectorType
	 * <em>Detector Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Detector Type</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorType
	 * @generated
	 */
	EClass getDetectorType();

	/**
	 * Returns the meta object for the attribute '
	 * {@link edu.pku.cn.xml.detectorConfiger.DetectorType#getClassName
	 * <em>Class Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Class Name</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorType#getClassName()
	 * @see #getDetectorType()
	 * @generated
	 */
	EAttribute getDetectorType_ClassName();

	/**
	 * Returns the meta object for the attribute '
	 * {@link edu.pku.cn.xml.detectorConfiger.DetectorType#getDisabled
	 * <em>Disabled</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Disabled</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorType#getDisabled()
	 * @see #getDetectorType()
	 * @generated
	 */
	EAttribute getDetectorType_Disabled();

	/**
	 * Returns the meta object for the attribute '
	 * {@link edu.pku.cn.xml.detectorConfiger.DetectorType#getHidden
	 * <em>Hidden</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Hidden</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorType#getHidden()
	 * @see #getDetectorType()
	 * @generated
	 */
	EAttribute getDetectorType_Hidden();

	/**
	 * Returns the meta object for the attribute '
	 * {@link edu.pku.cn.xml.detectorConfiger.DetectorType#getReports
	 * <em>Reports</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Reports</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorType#getReports()
	 * @see #getDetectorType()
	 * @generated
	 */
	EAttribute getDetectorType_Reports();

	/**
	 * Returns the meta object for the attribute '
	 * {@link edu.pku.cn.xml.detectorConfiger.DetectorType#getSpeed
	 * <em>Speed</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Speed</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.DetectorType#getSpeed()
	 * @see #getDetectorType()
	 * @generated
	 */
	EAttribute getDetectorType_Speed();

	/**
	 * Returns the meta object for class '
	 * {@link edu.pku.cn.xml.detectorConfiger.DocumentRoot
	 * <em>Document Root</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.DocumentRoot
	 * @generated
	 */
	EClass getDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '
	 * {@link edu.pku.cn.xml.detectorConfiger.DocumentRoot#getMixed
	 * <em>Mixed</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.DocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '
	 * {@link edu.pku.cn.xml.detectorConfiger.DocumentRoot#getXMLNSPrefixMap
	 * <em>XMLNS Prefix Map</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.DocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '
	 * {@link edu.pku.cn.xml.detectorConfiger.DocumentRoot#getXSISchemaLocation
	 * <em>XSI Schema Location</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.DocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link edu.pku.cn.xml.detectorConfiger.DocumentRoot#getCategory
	 * <em>Category</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Category</em>
	 *         '.
	 * @see edu.pku.cn.xml.detectorConfiger.DocumentRoot#getCategory()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Category();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link edu.pku.cn.xml.detectorConfiger.DocumentRoot#getDectectorFactory
	 * <em>Dectector Factory</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the containment reference '
	 *         <em>Dectector Factory</em>'.
	 * @see edu.pku.cn.xml.detectorConfiger.DocumentRoot#getDectectorFactory()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_DectectorFactory();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link edu.pku.cn.xml.detectorConfiger.DocumentRoot#getDetector
	 * <em>Detector</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Detector</em>
	 *         '.
	 * @see edu.pku.cn.xml.detectorConfiger.DocumentRoot#getDetector()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Detector();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DetectorConfigerFactory getDetectorConfigerFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that
	 * represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '
		 * {@link edu.pku.cn.xml.detectorConfiger.impl.CategoryTypeImpl
		 * <em>Category Type</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see edu.pku.cn.xml.detectorConfiger.impl.CategoryTypeImpl
		 * @see edu.pku.cn.xml.detectorConfiger.impl.DetectorConfigerPackageImpl#getCategoryType()
		 * @generated
		 */
		EClass CATEGORY_TYPE = eINSTANCE.getCategoryType();

		/**
		 * The meta object literal for the '<em><b>Detector</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CATEGORY_TYPE__DETECTOR = eINSTANCE.getCategoryType_Detector();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute CATEGORY_TYPE__NAME = eINSTANCE.getCategoryType_Name();

		/**
		 * The meta object literal for the '<em><b>Package Name</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute CATEGORY_TYPE__PACKAGE_NAME = eINSTANCE.getCategoryType_PackageName();

		/**
		 * The meta object literal for the '<em><b>Priority</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute CATEGORY_TYPE__PRIORITY = eINSTANCE.getCategoryType_Priority();

		/**
		 * The meta object literal for the '
		 * {@link edu.pku.cn.xml.detectorConfiger.impl.DectectorFactoryTypeImpl
		 * <em>Dectector Factory Type</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see edu.pku.cn.xml.detectorConfiger.impl.DectectorFactoryTypeImpl
		 * @see edu.pku.cn.xml.detectorConfiger.impl.DetectorConfigerPackageImpl#getDectectorFactoryType()
		 * @generated
		 */
		EClass DECTECTOR_FACTORY_TYPE = eINSTANCE.getDectectorFactoryType();

		/**
		 * The meta object literal for the '<em><b>Category</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DECTECTOR_FACTORY_TYPE__CATEGORY = eINSTANCE.getDectectorFactoryType_Category();

		/**
		 * The meta object literal for the '
		 * {@link edu.pku.cn.xml.detectorConfiger.impl.DetectorTypeImpl
		 * <em>Detector Type</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see edu.pku.cn.xml.detectorConfiger.impl.DetectorTypeImpl
		 * @see edu.pku.cn.xml.detectorConfiger.impl.DetectorConfigerPackageImpl#getDetectorType()
		 * @generated
		 */
		EClass DETECTOR_TYPE = eINSTANCE.getDetectorType();

		/**
		 * The meta object literal for the '<em><b>Class Name</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DETECTOR_TYPE__CLASS_NAME = eINSTANCE.getDetectorType_ClassName();

		/**
		 * The meta object literal for the '<em><b>Disabled</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DETECTOR_TYPE__DISABLED = eINSTANCE.getDetectorType_Disabled();

		/**
		 * The meta object literal for the '<em><b>Hidden</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DETECTOR_TYPE__HIDDEN = eINSTANCE.getDetectorType_Hidden();

		/**
		 * The meta object literal for the '<em><b>Reports</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DETECTOR_TYPE__REPORTS = eINSTANCE.getDetectorType_Reports();

		/**
		 * The meta object literal for the '<em><b>Speed</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DETECTOR_TYPE__SPEED = eINSTANCE.getDetectorType_Speed();

		/**
		 * The meta object literal for the '
		 * {@link edu.pku.cn.xml.detectorConfiger.impl.DocumentRootImpl
		 * <em>Document Root</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see edu.pku.cn.xml.detectorConfiger.impl.DocumentRootImpl
		 * @see edu.pku.cn.xml.detectorConfiger.impl.DetectorConfigerPackageImpl#getDocumentRoot()
		 * @generated
		 */
		EClass DOCUMENT_ROOT = eINSTANCE.getDocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute
		 * list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DOCUMENT_ROOT__MIXED = eINSTANCE.getDocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>'
		 * map feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getDocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>'
		 * map feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getDocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Category</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DOCUMENT_ROOT__CATEGORY = eINSTANCE.getDocumentRoot_Category();

		/**
		 * The meta object literal for the '<em><b>Dectector Factory</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DOCUMENT_ROOT__DECTECTOR_FACTORY = eINSTANCE.getDocumentRoot_DectectorFactory();

		/**
		 * The meta object literal for the '<em><b>Detector</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DOCUMENT_ROOT__DETECTOR = eINSTANCE.getDocumentRoot_Detector();

	}

} // DetectorConfigerPackage

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.detectorConfiger.impl;

import commonj.sdo.Sequence;

import java.util.Map;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;

import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;

import org.eclipse.emf.ecore.sdo.util.BasicESequence;
import org.eclipse.emf.ecore.sdo.util.ESequence;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import edu.pku.cn.xml.detectorConfiger.CategoryType;
import edu.pku.cn.xml.detectorConfiger.DectectorFactoryType;
import edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage;
import edu.pku.cn.xml.detectorConfiger.DetectorType;
import edu.pku.cn.xml.detectorConfiger.DocumentRoot;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Document Root</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.impl.DocumentRootImpl#getMixed
 * <em>Mixed</em>}</li>
 * <li>
 * {@link edu.pku.cn.xml.detectorConfiger.impl.DocumentRootImpl#getXMLNSPrefixMap
 * <em>XMLNS Prefix Map</em>}</li>
 * <li>
 * {@link edu.pku.cn.xml.detectorConfiger.impl.DocumentRootImpl#getXSISchemaLocation
 * <em>XSI Schema Location</em>}</li>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.impl.DocumentRootImpl#getCategory
 * <em>Category</em>}</li>
 * <li>
 * {@link edu.pku.cn.xml.detectorConfiger.impl.DocumentRootImpl#getDectectorFactory
 * <em>Dectector Factory</em>}</li>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.impl.DocumentRootImpl#getDetector
 * <em>Detector</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class DocumentRootImpl extends EDataObjectImpl implements DocumentRoot {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMixed()
	 * @generated
	 * @ordered
	 */
	protected ESequence mixed = null;

	/**
	 * The cached value of the '{@link #getXMLNSPrefixMap()
	 * <em>XMLNS Prefix Map</em>}' map. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getXMLNSPrefixMap()
	 * @generated
	 * @ordered
	 */
	protected EMap xMLNSPrefixMap = null;

	/**
	 * The cached value of the '{@link #getXSISchemaLocation()
	 * <em>XSI Schema Location</em>}' map. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getXSISchemaLocation()
	 * @generated
	 * @ordered
	 */
	protected EMap xSISchemaLocation = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DocumentRootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EClass eStaticClass() {
		return DetectorConfigerPackage.Literals.DOCUMENT_ROOT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Sequence getMixed() {
		if (mixed == null) {
			mixed = new BasicESequence(new BasicFeatureMap(this, DetectorConfigerPackage.DOCUMENT_ROOT__MIXED));
		}
		return mixed;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Map getXMLNSPrefixMap() {
		if (xMLNSPrefixMap == null) {
			xMLNSPrefixMap = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY,
					EStringToStringMapEntryImpl.class, this, DetectorConfigerPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		}
		return xMLNSPrefixMap.map();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Map getXSISchemaLocation() {
		if (xSISchemaLocation == null) {
			xSISchemaLocation = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY,
					EStringToStringMapEntryImpl.class, this, DetectorConfigerPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		}
		return xSISchemaLocation.map();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public CategoryType getCategory() {
		return (CategoryType) ((FeatureMap.Internal.Wrapper) getMixed()).featureMap().get(
				DetectorConfigerPackage.Literals.DOCUMENT_ROOT__CATEGORY, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetCategory(CategoryType newCategory, NotificationChain msgs) {
		return ((FeatureMap.Internal) ((FeatureMap.Internal.Wrapper) getMixed()).featureMap()).basicAdd(
				DetectorConfigerPackage.Literals.DOCUMENT_ROOT__CATEGORY, newCategory, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setCategory(CategoryType newCategory) {
		((FeatureMap.Internal) ((FeatureMap.Internal.Wrapper) getMixed()).featureMap()).set(
				DetectorConfigerPackage.Literals.DOCUMENT_ROOT__CATEGORY, newCategory);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DectectorFactoryType getDectectorFactory() {
		return (DectectorFactoryType) ((FeatureMap.Internal.Wrapper) getMixed()).featureMap().get(
				DetectorConfigerPackage.Literals.DOCUMENT_ROOT__DECTECTOR_FACTORY, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetDectectorFactory(DectectorFactoryType newDectectorFactory, NotificationChain msgs) {
		return ((FeatureMap.Internal) ((FeatureMap.Internal.Wrapper) getMixed()).featureMap()).basicAdd(
				DetectorConfigerPackage.Literals.DOCUMENT_ROOT__DECTECTOR_FACTORY, newDectectorFactory, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDectectorFactory(DectectorFactoryType newDectectorFactory) {
		((FeatureMap.Internal) ((FeatureMap.Internal.Wrapper) getMixed()).featureMap()).set(
				DetectorConfigerPackage.Literals.DOCUMENT_ROOT__DECTECTOR_FACTORY, newDectectorFactory);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DetectorType getDetector() {
		return (DetectorType) ((FeatureMap.Internal.Wrapper) getMixed()).featureMap().get(
				DetectorConfigerPackage.Literals.DOCUMENT_ROOT__DETECTOR, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetDetector(DetectorType newDetector, NotificationChain msgs) {
		return ((FeatureMap.Internal) ((FeatureMap.Internal.Wrapper) getMixed()).featureMap()).basicAdd(
				DetectorConfigerPackage.Literals.DOCUMENT_ROOT__DETECTOR, newDetector, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDetector(DetectorType newDetector) {
		((FeatureMap.Internal) ((FeatureMap.Internal.Wrapper) getMixed()).featureMap()).set(
				DetectorConfigerPackage.Literals.DOCUMENT_ROOT__DETECTOR, newDetector);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case DetectorConfigerPackage.DOCUMENT_ROOT__MIXED:
			return ((InternalEList) ((FeatureMap.Internal.Wrapper) getMixed()).featureMap())
					.basicRemove(otherEnd, msgs);
		case DetectorConfigerPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
			return ((InternalEList) ((EMap.InternalMapView) getXMLNSPrefixMap()).eMap()).basicRemove(otherEnd, msgs);
		case DetectorConfigerPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
			return ((InternalEList) ((EMap.InternalMapView) getXSISchemaLocation()).eMap()).basicRemove(otherEnd, msgs);
		case DetectorConfigerPackage.DOCUMENT_ROOT__CATEGORY:
			return basicSetCategory(null, msgs);
		case DetectorConfigerPackage.DOCUMENT_ROOT__DECTECTOR_FACTORY:
			return basicSetDectectorFactory(null, msgs);
		case DetectorConfigerPackage.DOCUMENT_ROOT__DETECTOR:
			return basicSetDetector(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case DetectorConfigerPackage.DOCUMENT_ROOT__MIXED:
			if (coreType)
				return ((FeatureMap.Internal.Wrapper) getMixed()).featureMap();
			return getMixed();
		case DetectorConfigerPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
			if (coreType)
				return ((EMap.InternalMapView) getXMLNSPrefixMap()).eMap();
			else
				return getXMLNSPrefixMap();
		case DetectorConfigerPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
			if (coreType)
				return ((EMap.InternalMapView) getXSISchemaLocation()).eMap();
			else
				return getXSISchemaLocation();
		case DetectorConfigerPackage.DOCUMENT_ROOT__CATEGORY:
			return getCategory();
		case DetectorConfigerPackage.DOCUMENT_ROOT__DECTECTOR_FACTORY:
			return getDectectorFactory();
		case DetectorConfigerPackage.DOCUMENT_ROOT__DETECTOR:
			return getDetector();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case DetectorConfigerPackage.DOCUMENT_ROOT__MIXED:
			((FeatureMap.Internal) ((FeatureMap.Internal.Wrapper) getMixed()).featureMap()).set(newValue);
			return;
		case DetectorConfigerPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
			((EStructuralFeature.Setting) ((EMap.InternalMapView) getXMLNSPrefixMap()).eMap()).set(newValue);
			return;
		case DetectorConfigerPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
			((EStructuralFeature.Setting) ((EMap.InternalMapView) getXSISchemaLocation()).eMap()).set(newValue);
			return;
		case DetectorConfigerPackage.DOCUMENT_ROOT__CATEGORY:
			setCategory((CategoryType) newValue);
			return;
		case DetectorConfigerPackage.DOCUMENT_ROOT__DECTECTOR_FACTORY:
			setDectectorFactory((DectectorFactoryType) newValue);
			return;
		case DetectorConfigerPackage.DOCUMENT_ROOT__DETECTOR:
			setDetector((DetectorType) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
		case DetectorConfigerPackage.DOCUMENT_ROOT__MIXED:
			((FeatureMap.Internal.Wrapper) getMixed()).featureMap().clear();
			return;
		case DetectorConfigerPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
			getXMLNSPrefixMap().clear();
			return;
		case DetectorConfigerPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
			getXSISchemaLocation().clear();
			return;
		case DetectorConfigerPackage.DOCUMENT_ROOT__CATEGORY:
			setCategory((CategoryType) null);
			return;
		case DetectorConfigerPackage.DOCUMENT_ROOT__DECTECTOR_FACTORY:
			setDectectorFactory((DectectorFactoryType) null);
			return;
		case DetectorConfigerPackage.DOCUMENT_ROOT__DETECTOR:
			setDetector((DetectorType) null);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case DetectorConfigerPackage.DOCUMENT_ROOT__MIXED:
			return mixed != null && !mixed.featureMap().isEmpty();
		case DetectorConfigerPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
			return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
		case DetectorConfigerPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
			return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
		case DetectorConfigerPackage.DOCUMENT_ROOT__CATEGORY:
			return getCategory() != null;
		case DetectorConfigerPackage.DOCUMENT_ROOT__DECTECTOR_FACTORY:
			return getDectectorFactory() != null;
		case DetectorConfigerPackage.DOCUMENT_ROOT__DETECTOR:
			return getDetector() != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (mixed: ");
		result.append(mixed);
		result.append(')');
		return result.toString();
	}

} // DocumentRootImpl
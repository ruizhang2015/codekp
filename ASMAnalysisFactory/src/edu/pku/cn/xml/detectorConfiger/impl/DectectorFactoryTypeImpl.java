/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.detectorConfiger.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import edu.pku.cn.xml.detectorConfiger.CategoryType;
import edu.pku.cn.xml.detectorConfiger.DectectorFactoryType;
import edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Dectector Factory Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link edu.pku.cn.xml.detectorConfiger.impl.DectectorFactoryTypeImpl#getCategory
 * <em>Category</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class DectectorFactoryTypeImpl extends EDataObjectImpl implements DectectorFactoryType {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The cached value of the '{@link #getCategory() <em>Category</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getCategory()
	 * @generated
	 * @ordered
	 */
	protected EList category = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DectectorFactoryTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EClass eStaticClass() {
		return DetectorConfigerPackage.Literals.DECTECTOR_FACTORY_TYPE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public List getCategory() {
		if (category == null) {
			category = new EObjectContainmentEList(CategoryType.class, this,
					DetectorConfigerPackage.DECTECTOR_FACTORY_TYPE__CATEGORY);
		}
		return category;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case DetectorConfigerPackage.DECTECTOR_FACTORY_TYPE__CATEGORY:
			return ((InternalEList) getCategory()).basicRemove(otherEnd, msgs);
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
		case DetectorConfigerPackage.DECTECTOR_FACTORY_TYPE__CATEGORY:
			return getCategory();
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
		case DetectorConfigerPackage.DECTECTOR_FACTORY_TYPE__CATEGORY:
			getCategory().clear();
			getCategory().addAll((Collection) newValue);
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
		case DetectorConfigerPackage.DECTECTOR_FACTORY_TYPE__CATEGORY:
			getCategory().clear();
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
		case DetectorConfigerPackage.DECTECTOR_FACTORY_TYPE__CATEGORY:
			return category != null && !category.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // DectectorFactoryTypeImpl
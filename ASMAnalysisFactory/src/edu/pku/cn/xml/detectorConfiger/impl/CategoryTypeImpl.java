/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.detectorConfiger.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import edu.pku.cn.xml.detectorConfiger.CategoryType;
import edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage;
import edu.pku.cn.xml.detectorConfiger.DetectorType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Category Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.impl.CategoryTypeImpl#getDetector
 * <em>Detector</em>}</li>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.impl.CategoryTypeImpl#getName <em>
 * Name</em>}</li>
 * <li>
 * {@link edu.pku.cn.xml.detectorConfiger.impl.CategoryTypeImpl#getPackageName
 * <em>Package Name</em>}</li>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.impl.CategoryTypeImpl#getPriority
 * <em>Priority</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class CategoryTypeImpl extends EDataObjectImpl implements CategoryType {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The cached value of the '{@link #getDetector() <em>Detector</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDetector()
	 * @generated
	 * @ordered
	 */
	protected EList detector = null;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getPackageName() <em>Package Name</em>}
	 * ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPackageName()
	 * @generated
	 * @ordered
	 */
	protected static final String PACKAGE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPackageName() <em>Package Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPackageName()
	 * @generated
	 * @ordered
	 */
	protected String packageName = PACKAGE_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getPriority() <em>Priority</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPriority()
	 * @generated
	 * @ordered
	 */
	protected static final String PRIORITY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPriority() <em>Priority</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPriority()
	 * @generated
	 * @ordered
	 */
	protected String priority = PRIORITY_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected CategoryTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EClass eStaticClass() {
		return DetectorConfigerPackage.Literals.CATEGORY_TYPE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public List getDetector() {
		if (detector == null) {
			detector = new EObjectContainmentEList(DetectorType.class, this,
					DetectorConfigerPackage.CATEGORY_TYPE__DETECTOR);
		}
		return detector;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DetectorConfigerPackage.CATEGORY_TYPE__NAME, oldName,
					name));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setPackageName(String newPackageName) {
		String oldPackageName = packageName;
		packageName = newPackageName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DetectorConfigerPackage.CATEGORY_TYPE__PACKAGE_NAME,
					oldPackageName, packageName));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setPriority(String newPriority) {
		String oldPriority = priority;
		priority = newPriority;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DetectorConfigerPackage.CATEGORY_TYPE__PRIORITY,
					oldPriority, priority));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case DetectorConfigerPackage.CATEGORY_TYPE__DETECTOR:
			return ((InternalEList) getDetector()).basicRemove(otherEnd, msgs);
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
		case DetectorConfigerPackage.CATEGORY_TYPE__DETECTOR:
			return getDetector();
		case DetectorConfigerPackage.CATEGORY_TYPE__NAME:
			return getName();
		case DetectorConfigerPackage.CATEGORY_TYPE__PACKAGE_NAME:
			return getPackageName();
		case DetectorConfigerPackage.CATEGORY_TYPE__PRIORITY:
			return getPriority();
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
		case DetectorConfigerPackage.CATEGORY_TYPE__DETECTOR:
			getDetector().clear();
			getDetector().addAll((Collection) newValue);
			return;
		case DetectorConfigerPackage.CATEGORY_TYPE__NAME:
			setName((String) newValue);
			return;
		case DetectorConfigerPackage.CATEGORY_TYPE__PACKAGE_NAME:
			setPackageName((String) newValue);
			return;
		case DetectorConfigerPackage.CATEGORY_TYPE__PRIORITY:
			setPriority((String) newValue);
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
		case DetectorConfigerPackage.CATEGORY_TYPE__DETECTOR:
			getDetector().clear();
			return;
		case DetectorConfigerPackage.CATEGORY_TYPE__NAME:
			setName(NAME_EDEFAULT);
			return;
		case DetectorConfigerPackage.CATEGORY_TYPE__PACKAGE_NAME:
			setPackageName(PACKAGE_NAME_EDEFAULT);
			return;
		case DetectorConfigerPackage.CATEGORY_TYPE__PRIORITY:
			setPriority(PRIORITY_EDEFAULT);
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
		case DetectorConfigerPackage.CATEGORY_TYPE__DETECTOR:
			return detector != null && !detector.isEmpty();
		case DetectorConfigerPackage.CATEGORY_TYPE__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case DetectorConfigerPackage.CATEGORY_TYPE__PACKAGE_NAME:
			return PACKAGE_NAME_EDEFAULT == null ? packageName != null : !PACKAGE_NAME_EDEFAULT.equals(packageName);
		case DetectorConfigerPackage.CATEGORY_TYPE__PRIORITY:
			return PRIORITY_EDEFAULT == null ? priority != null : !PRIORITY_EDEFAULT.equals(priority);
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
		result.append(" (name: ");
		result.append(name);
		result.append(", packageName: ");
		result.append(packageName);
		result.append(", priority: ");
		result.append(priority);
		result.append(')');
		return result.toString();
	}

} // CategoryTypeImpl
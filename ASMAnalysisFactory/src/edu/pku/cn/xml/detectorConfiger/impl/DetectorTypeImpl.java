/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.detectorConfiger.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;

import edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage;
import edu.pku.cn.xml.detectorConfiger.DetectorType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Detector Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link edu.pku.cn.xml.detectorConfiger.impl.DetectorTypeImpl#getClassName
 * <em>Class Name</em>}</li>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.impl.DetectorTypeImpl#getDisabled
 * <em>Disabled</em>}</li>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.impl.DetectorTypeImpl#getHidden
 * <em>Hidden</em>}</li>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.impl.DetectorTypeImpl#getReports
 * <em>Reports</em>}</li>
 * <li>{@link edu.pku.cn.xml.detectorConfiger.impl.DetectorTypeImpl#getSpeed
 * <em>Speed</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class DetectorTypeImpl extends EDataObjectImpl implements DetectorType {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The default value of the '{@link #getClassName() <em>Class Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getClassName()
	 * @generated
	 * @ordered
	 */
	protected static final String CLASS_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getClassName() <em>Class Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getClassName()
	 * @generated
	 * @ordered
	 */
	protected String className = CLASS_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDisabled() <em>Disabled</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDisabled()
	 * @generated
	 * @ordered
	 */
	protected static final String DISABLED_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDisabled() <em>Disabled</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDisabled()
	 * @generated
	 * @ordered
	 */
	protected String disabled = DISABLED_EDEFAULT;

	/**
	 * The default value of the '{@link #getHidden() <em>Hidden</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getHidden()
	 * @generated
	 * @ordered
	 */
	protected static final String HIDDEN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHidden() <em>Hidden</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getHidden()
	 * @generated
	 * @ordered
	 */
	protected String hidden = HIDDEN_EDEFAULT;

	/**
	 * The default value of the '{@link #getReports() <em>Reports</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getReports()
	 * @generated
	 * @ordered
	 */
	protected static final String REPORTS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getReports() <em>Reports</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getReports()
	 * @generated
	 * @ordered
	 */
	protected String reports = REPORTS_EDEFAULT;

	/**
	 * The default value of the '{@link #getSpeed() <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSpeed()
	 * @generated
	 * @ordered
	 */
	protected static final String SPEED_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSpeed() <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSpeed()
	 * @generated
	 * @ordered
	 */
	protected String speed = SPEED_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DetectorTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EClass eStaticClass() {
		return DetectorConfigerPackage.Literals.DETECTOR_TYPE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setClassName(String newClassName) {
		String oldClassName = className;
		className = newClassName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DetectorConfigerPackage.DETECTOR_TYPE__CLASS_NAME,
					oldClassName, className));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getDisabled() {
		return disabled;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDisabled(String newDisabled) {
		String oldDisabled = disabled;
		disabled = newDisabled;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DetectorConfigerPackage.DETECTOR_TYPE__DISABLED,
					oldDisabled, disabled));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getHidden() {
		return hidden;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setHidden(String newHidden) {
		String oldHidden = hidden;
		hidden = newHidden;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DetectorConfigerPackage.DETECTOR_TYPE__HIDDEN,
					oldHidden, hidden));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getReports() {
		return reports;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setReports(String newReports) {
		String oldReports = reports;
		reports = newReports;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DetectorConfigerPackage.DETECTOR_TYPE__REPORTS,
					oldReports, reports));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getSpeed() {
		return speed;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSpeed(String newSpeed) {
		String oldSpeed = speed;
		speed = newSpeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DetectorConfigerPackage.DETECTOR_TYPE__SPEED,
					oldSpeed, speed));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case DetectorConfigerPackage.DETECTOR_TYPE__CLASS_NAME:
			return getClassName();
		case DetectorConfigerPackage.DETECTOR_TYPE__DISABLED:
			return getDisabled();
		case DetectorConfigerPackage.DETECTOR_TYPE__HIDDEN:
			return getHidden();
		case DetectorConfigerPackage.DETECTOR_TYPE__REPORTS:
			return getReports();
		case DetectorConfigerPackage.DETECTOR_TYPE__SPEED:
			return getSpeed();
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
		case DetectorConfigerPackage.DETECTOR_TYPE__CLASS_NAME:
			setClassName((String) newValue);
			return;
		case DetectorConfigerPackage.DETECTOR_TYPE__DISABLED:
			setDisabled((String) newValue);
			return;
		case DetectorConfigerPackage.DETECTOR_TYPE__HIDDEN:
			setHidden((String) newValue);
			return;
		case DetectorConfigerPackage.DETECTOR_TYPE__REPORTS:
			setReports((String) newValue);
			return;
		case DetectorConfigerPackage.DETECTOR_TYPE__SPEED:
			setSpeed((String) newValue);
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
		case DetectorConfigerPackage.DETECTOR_TYPE__CLASS_NAME:
			setClassName(CLASS_NAME_EDEFAULT);
			return;
		case DetectorConfigerPackage.DETECTOR_TYPE__DISABLED:
			setDisabled(DISABLED_EDEFAULT);
			return;
		case DetectorConfigerPackage.DETECTOR_TYPE__HIDDEN:
			setHidden(HIDDEN_EDEFAULT);
			return;
		case DetectorConfigerPackage.DETECTOR_TYPE__REPORTS:
			setReports(REPORTS_EDEFAULT);
			return;
		case DetectorConfigerPackage.DETECTOR_TYPE__SPEED:
			setSpeed(SPEED_EDEFAULT);
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
		case DetectorConfigerPackage.DETECTOR_TYPE__CLASS_NAME:
			return CLASS_NAME_EDEFAULT == null ? className != null : !CLASS_NAME_EDEFAULT.equals(className);
		case DetectorConfigerPackage.DETECTOR_TYPE__DISABLED:
			return DISABLED_EDEFAULT == null ? disabled != null : !DISABLED_EDEFAULT.equals(disabled);
		case DetectorConfigerPackage.DETECTOR_TYPE__HIDDEN:
			return HIDDEN_EDEFAULT == null ? hidden != null : !HIDDEN_EDEFAULT.equals(hidden);
		case DetectorConfigerPackage.DETECTOR_TYPE__REPORTS:
			return REPORTS_EDEFAULT == null ? reports != null : !REPORTS_EDEFAULT.equals(reports);
		case DetectorConfigerPackage.DETECTOR_TYPE__SPEED:
			return SPEED_EDEFAULT == null ? speed != null : !SPEED_EDEFAULT.equals(speed);
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
		result.append(" (className: ");
		result.append(className);
		result.append(", disabled: ");
		result.append(disabled);
		result.append(", hidden: ");
		result.append(hidden);
		result.append(", reports: ");
		result.append(reports);
		result.append(", speed: ");
		result.append(speed);
		result.append(')');
		return result.toString();
	}

} // DetectorTypeImpl
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.messages.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;

import edu.pku.cn.xml.messages.BugPattern;
import edu.pku.cn.xml.messages.Detector;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Detector Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link edu.pku.cn.xml.messages.impl.DetectorImpl#getDetails <em>Details
 * </em>}</li>
 * <li>{@link edu.pku.cn.xml.messages.impl.DetectorImpl#getBugPattern <em>Bug
 * Pattern</em>}</li>
 * <li>{@link edu.pku.cn.xml.messages.impl.DetectorImpl#getClassName <em>Class
 * Name</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class DetectorImpl extends EDataObjectImpl implements Detector {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The default value of the '{@link #getDetails() <em>Details</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDetails()
	 * @generated
	 * @ordered
	 */
	protected static final String DETAILS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDetails() <em>Details</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDetails()
	 * @generated
	 * @ordered
	 */
	protected String details = DETAILS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getBugPattern() <em>Bug Pattern</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getBugPattern()
	 * @generated
	 * @ordered
	 */
	// protected List<BugPattern> bugPattern = null;
	protected Map<String, BugPattern> bugPattern = null;

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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DetectorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDetails(String newDetails) {
		details = newDetails;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Map<String, BugPattern> getBugPattern() {
		if (bugPattern == null) {
			bugPattern = new HashMap<String, BugPattern>();
		}
		return bugPattern;
	}

	public BugPattern getBugPattern(String name) {
		return bugPattern.get(name);
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
		className = newClassName;
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
		result.append(" (details: ");
		result.append(details);
		result.append(", className: ");
		result.append(className);
		result.append(')');
		return result.toString();
	}

} // DetectorTypeImpl
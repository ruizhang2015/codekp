/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.JavaLib.impl;


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

import edu.pku.cn.xml.JavaLib.JavaLibPackage;
import edu.pku.cn.xml.JavaLib.MethodType;
import edu.pku.cn.xml.JavaLib.ReturnTypeType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Method Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.MethodTypeImpl#getReturnType <em>Return Type</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.MethodTypeImpl#getAccess <em>Access</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.MethodTypeImpl#getDesc <em>Desc</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.MethodTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.MethodTypeImpl#getSignature <em>Signature</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MethodTypeImpl extends EDataObjectImpl implements MethodType
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final long serialVersionUID = 1L;

  /**
   * The cached value of the '{@link #getReturnType() <em>Return Type</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getReturnType()
   * @generated
   * @ordered
   */
  protected EList returnType = null;

  /**
   * The default value of the '{@link #getAccess() <em>Access</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAccess()
   * @generated
   * @ordered
   */
  protected static final String ACCESS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getAccess() <em>Access</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAccess()
   * @generated
   * @ordered
   */
  protected String access = ACCESS_EDEFAULT;

  /**
   * The default value of the '{@link #getDesc() <em>Desc</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDesc()
   * @generated
   * @ordered
   */
  protected static final String DESC_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDesc() <em>Desc</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDesc()
   * @generated
   * @ordered
   */
  protected String desc = DESC_EDEFAULT;

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The default value of the '{@link #getSignature() <em>Signature</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSignature()
   * @generated
   * @ordered
   */
  protected static final String SIGNATURE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSignature() <em>Signature</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSignature()
   * @generated
   * @ordered
   */
  protected String signature = SIGNATURE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MethodTypeImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EClass eStaticClass()
  {
    return JavaLibPackage.Literals.METHOD_TYPE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getReturnType()
  {
    if (returnType == null)
    {
      returnType = new EObjectContainmentEList(ReturnTypeType.class, this, JavaLibPackage.METHOD_TYPE__RETURN_TYPE);
    }
    return returnType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getAccess()
  {
    return access;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAccess(String newAccess)
  {
    String oldAccess = access;
    access = newAccess;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaLibPackage.METHOD_TYPE__ACCESS, oldAccess, access));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDesc()
  {
    return desc;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDesc(String newDesc)
  {
    String oldDesc = desc;
    desc = newDesc;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaLibPackage.METHOD_TYPE__DESC, oldDesc, desc));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaLibPackage.METHOD_TYPE__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getSignature()
  {
    return signature;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSignature(String newSignature)
  {
    String oldSignature = signature;
    signature = newSignature;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaLibPackage.METHOD_TYPE__SIGNATURE, oldSignature, signature));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case JavaLibPackage.METHOD_TYPE__RETURN_TYPE:
        return ((InternalEList)getReturnType()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case JavaLibPackage.METHOD_TYPE__RETURN_TYPE:
        return getReturnType();
      case JavaLibPackage.METHOD_TYPE__ACCESS:
        return getAccess();
      case JavaLibPackage.METHOD_TYPE__DESC:
        return getDesc();
      case JavaLibPackage.METHOD_TYPE__NAME:
        return getName();
      case JavaLibPackage.METHOD_TYPE__SIGNATURE:
        return getSignature();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case JavaLibPackage.METHOD_TYPE__RETURN_TYPE:
        getReturnType().clear();
        getReturnType().addAll((Collection)newValue);
        return;
      case JavaLibPackage.METHOD_TYPE__ACCESS:
        setAccess((String)newValue);
        return;
      case JavaLibPackage.METHOD_TYPE__DESC:
        setDesc((String)newValue);
        return;
      case JavaLibPackage.METHOD_TYPE__NAME:
        setName((String)newValue);
        return;
      case JavaLibPackage.METHOD_TYPE__SIGNATURE:
        setSignature((String)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case JavaLibPackage.METHOD_TYPE__RETURN_TYPE:
        getReturnType().clear();
        return;
      case JavaLibPackage.METHOD_TYPE__ACCESS:
        setAccess(ACCESS_EDEFAULT);
        return;
      case JavaLibPackage.METHOD_TYPE__DESC:
        setDesc(DESC_EDEFAULT);
        return;
      case JavaLibPackage.METHOD_TYPE__NAME:
        setName(NAME_EDEFAULT);
        return;
      case JavaLibPackage.METHOD_TYPE__SIGNATURE:
        setSignature(SIGNATURE_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case JavaLibPackage.METHOD_TYPE__RETURN_TYPE:
        return returnType != null && !returnType.isEmpty();
      case JavaLibPackage.METHOD_TYPE__ACCESS:
        return ACCESS_EDEFAULT == null ? access != null : !ACCESS_EDEFAULT.equals(access);
      case JavaLibPackage.METHOD_TYPE__DESC:
        return DESC_EDEFAULT == null ? desc != null : !DESC_EDEFAULT.equals(desc);
      case JavaLibPackage.METHOD_TYPE__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case JavaLibPackage.METHOD_TYPE__SIGNATURE:
        return SIGNATURE_EDEFAULT == null ? signature != null : !SIGNATURE_EDEFAULT.equals(signature);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (access: ");
    result.append(access);
    result.append(", desc: ");
    result.append(desc);
    result.append(", name: ");
    result.append(name);
    result.append(", signature: ");
    result.append(signature);
    result.append(')');
    return result.toString();
  }

} //MethodTypeImpl
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

import edu.pku.cn.xml.JavaLib.ClassType;
import edu.pku.cn.xml.JavaLib.JavaLibPackage;
import edu.pku.cn.xml.JavaLib.PackageType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Package Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.PackageTypeImpl#getClass_ <em>Class</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.PackageTypeImpl#getPackage <em>Package</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.PackageTypeImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PackageTypeImpl extends EDataObjectImpl implements PackageType
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final long serialVersionUID = 1L;

  /**
   * The cached value of the '{@link #getClass_() <em>Class</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getClass_()
   * @generated
   * @ordered
   */
  protected EList class_ = null;

  /**
   * The cached value of the '{@link #getPackage() <em>Package</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPackage()
   * @generated
   * @ordered
   */
  protected EList package_ = null;

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
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected PackageTypeImpl()
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
    return JavaLibPackage.Literals.PACKAGE_TYPE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getClass_()
  {
    if (class_ == null)
    {
      class_ = new EObjectContainmentEList(ClassType.class, this, JavaLibPackage.PACKAGE_TYPE__CLASS);
    }
    return class_;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getPackage()
  {
    if (package_ == null)
    {
      package_ = new EObjectContainmentEList(PackageType.class, this, JavaLibPackage.PACKAGE_TYPE__PACKAGE);
    }
    return package_;
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
      eNotify(new ENotificationImpl(this, Notification.SET, JavaLibPackage.PACKAGE_TYPE__NAME, oldName, name));
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
      case JavaLibPackage.PACKAGE_TYPE__CLASS:
        return ((InternalEList)getClass_()).basicRemove(otherEnd, msgs);
      case JavaLibPackage.PACKAGE_TYPE__PACKAGE:
        return ((InternalEList)getPackage()).basicRemove(otherEnd, msgs);
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
      case JavaLibPackage.PACKAGE_TYPE__CLASS:
        return getClass_();
      case JavaLibPackage.PACKAGE_TYPE__PACKAGE:
        return getPackage();
      case JavaLibPackage.PACKAGE_TYPE__NAME:
        return getName();
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
      case JavaLibPackage.PACKAGE_TYPE__CLASS:
        getClass_().clear();
        getClass_().addAll((Collection)newValue);
        return;
      case JavaLibPackage.PACKAGE_TYPE__PACKAGE:
        getPackage().clear();
        getPackage().addAll((Collection)newValue);
        return;
      case JavaLibPackage.PACKAGE_TYPE__NAME:
        setName((String)newValue);
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
      case JavaLibPackage.PACKAGE_TYPE__CLASS:
        getClass_().clear();
        return;
      case JavaLibPackage.PACKAGE_TYPE__PACKAGE:
        getPackage().clear();
        return;
      case JavaLibPackage.PACKAGE_TYPE__NAME:
        setName(NAME_EDEFAULT);
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
      case JavaLibPackage.PACKAGE_TYPE__CLASS:
        return class_ != null && !class_.isEmpty();
      case JavaLibPackage.PACKAGE_TYPE__PACKAGE:
        return package_ != null && !package_.isEmpty();
      case JavaLibPackage.PACKAGE_TYPE__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
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
    result.append(" (name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

} //PackageTypeImpl
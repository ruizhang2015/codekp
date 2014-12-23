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
import edu.pku.cn.xml.JavaLib.FieldType;
import edu.pku.cn.xml.JavaLib.ImplementType;
import edu.pku.cn.xml.JavaLib.JavaLibPackage;
import edu.pku.cn.xml.JavaLib.MethodType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Class Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.ClassTypeImpl#getImplement <em>Implement</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.ClassTypeImpl#getField <em>Field</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.ClassTypeImpl#getMethod <em>Method</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.ClassTypeImpl#getExtends <em>Extends</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.ClassTypeImpl#isIsInterface <em>Is Interface</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.ClassTypeImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ClassTypeImpl extends EDataObjectImpl implements ClassType
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final long serialVersionUID = 1L;

  /**
   * The cached value of the '{@link #getImplement() <em>Implement</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getImplement()
   * @generated
   * @ordered
   */
  protected EList implement = null;

  /**
   * The cached value of the '{@link #getField() <em>Field</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getField()
   * @generated
   * @ordered
   */
  protected EList field = null;

  /**
   * The cached value of the '{@link #getMethod() <em>Method</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMethod()
   * @generated
   * @ordered
   */
  protected EList method = null;

  /**
   * The default value of the '{@link #getExtends() <em>Extends</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExtends()
   * @generated
   * @ordered
   */
  protected static final String EXTENDS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getExtends() <em>Extends</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExtends()
   * @generated
   * @ordered
   */
  protected String extends_ = EXTENDS_EDEFAULT;

  /**
   * The default value of the '{@link #isIsInterface() <em>Is Interface</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsInterface()
   * @generated
   * @ordered
   */
  protected static final boolean IS_INTERFACE_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isIsInterface() <em>Is Interface</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsInterface()
   * @generated
   * @ordered
   */
  protected boolean isInterface = IS_INTERFACE_EDEFAULT;

  /**
   * This is true if the Is Interface attribute has been set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  protected boolean isInterfaceESet = false;

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
  protected ClassTypeImpl()
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
    return JavaLibPackage.Literals.CLASS_TYPE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getImplement()
  {
    if (implement == null)
    {
      implement = new EObjectContainmentEList(ImplementType.class, this, JavaLibPackage.CLASS_TYPE__IMPLEMENT);
    }
    return implement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getField()
  {
    if (field == null)
    {
      field = new EObjectContainmentEList(FieldType.class, this, JavaLibPackage.CLASS_TYPE__FIELD);
    }
    return field;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getMethod()
  {
    if (method == null)
    {
      method = new EObjectContainmentEList(MethodType.class, this, JavaLibPackage.CLASS_TYPE__METHOD);
    }
    return method;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getExtends()
  {
    return extends_;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setExtends(String newExtends)
  {
    String oldExtends = extends_;
    extends_ = newExtends;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaLibPackage.CLASS_TYPE__EXTENDS, oldExtends, extends_));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIsInterface()
  {
    return isInterface;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIsInterface(boolean newIsInterface)
  {
    boolean oldIsInterface = isInterface;
    isInterface = newIsInterface;
    boolean oldIsInterfaceESet = isInterfaceESet;
    isInterfaceESet = true;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaLibPackage.CLASS_TYPE__IS_INTERFACE, oldIsInterface, isInterface, !oldIsInterfaceESet));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void unsetIsInterface()
  {
    boolean oldIsInterface = isInterface;
    boolean oldIsInterfaceESet = isInterfaceESet;
    isInterface = IS_INTERFACE_EDEFAULT;
    isInterfaceESet = false;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.UNSET, JavaLibPackage.CLASS_TYPE__IS_INTERFACE, oldIsInterface, IS_INTERFACE_EDEFAULT, oldIsInterfaceESet));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isSetIsInterface()
  {
    return isInterfaceESet;
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
      eNotify(new ENotificationImpl(this, Notification.SET, JavaLibPackage.CLASS_TYPE__NAME, oldName, name));
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
      case JavaLibPackage.CLASS_TYPE__IMPLEMENT:
        return ((InternalEList)getImplement()).basicRemove(otherEnd, msgs);
      case JavaLibPackage.CLASS_TYPE__FIELD:
        return ((InternalEList)getField()).basicRemove(otherEnd, msgs);
      case JavaLibPackage.CLASS_TYPE__METHOD:
        return ((InternalEList)getMethod()).basicRemove(otherEnd, msgs);
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
      case JavaLibPackage.CLASS_TYPE__IMPLEMENT:
        return getImplement();
      case JavaLibPackage.CLASS_TYPE__FIELD:
        return getField();
      case JavaLibPackage.CLASS_TYPE__METHOD:
        return getMethod();
      case JavaLibPackage.CLASS_TYPE__EXTENDS:
        return getExtends();
      case JavaLibPackage.CLASS_TYPE__IS_INTERFACE:
        return isIsInterface() ? Boolean.TRUE : Boolean.FALSE;
      case JavaLibPackage.CLASS_TYPE__NAME:
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
      case JavaLibPackage.CLASS_TYPE__IMPLEMENT:
        getImplement().clear();
        getImplement().addAll((Collection)newValue);
        return;
      case JavaLibPackage.CLASS_TYPE__FIELD:
        getField().clear();
        getField().addAll((Collection)newValue);
        return;
      case JavaLibPackage.CLASS_TYPE__METHOD:
        getMethod().clear();
        getMethod().addAll((Collection)newValue);
        return;
      case JavaLibPackage.CLASS_TYPE__EXTENDS:
        setExtends((String)newValue);
        return;
      case JavaLibPackage.CLASS_TYPE__IS_INTERFACE:
        setIsInterface(((Boolean)newValue).booleanValue());
        return;
      case JavaLibPackage.CLASS_TYPE__NAME:
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
      case JavaLibPackage.CLASS_TYPE__IMPLEMENT:
        getImplement().clear();
        return;
      case JavaLibPackage.CLASS_TYPE__FIELD:
        getField().clear();
        return;
      case JavaLibPackage.CLASS_TYPE__METHOD:
        getMethod().clear();
        return;
      case JavaLibPackage.CLASS_TYPE__EXTENDS:
        setExtends(EXTENDS_EDEFAULT);
        return;
      case JavaLibPackage.CLASS_TYPE__IS_INTERFACE:
        unsetIsInterface();
        return;
      case JavaLibPackage.CLASS_TYPE__NAME:
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
      case JavaLibPackage.CLASS_TYPE__IMPLEMENT:
        return implement != null && !implement.isEmpty();
      case JavaLibPackage.CLASS_TYPE__FIELD:
        return field != null && !field.isEmpty();
      case JavaLibPackage.CLASS_TYPE__METHOD:
        return method != null && !method.isEmpty();
      case JavaLibPackage.CLASS_TYPE__EXTENDS:
        return EXTENDS_EDEFAULT == null ? extends_ != null : !EXTENDS_EDEFAULT.equals(extends_);
      case JavaLibPackage.CLASS_TYPE__IS_INTERFACE:
        return isSetIsInterface();
      case JavaLibPackage.CLASS_TYPE__NAME:
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
    result.append(" (extends: ");
    result.append(extends_);
    result.append(", isInterface: ");
    if (isInterfaceESet) result.append(isInterface); else result.append("<unset>");
    result.append(", name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

} //ClassTypeImpl
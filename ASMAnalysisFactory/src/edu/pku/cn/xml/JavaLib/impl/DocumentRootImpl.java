/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.JavaLib.impl;


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

import edu.pku.cn.xml.JavaLib.ClassType;
import edu.pku.cn.xml.JavaLib.DocumentRoot;
import edu.pku.cn.xml.JavaLib.FieldType;
import edu.pku.cn.xml.JavaLib.ImplementType;
import edu.pku.cn.xml.JavaLib.JavaConfigType;
import edu.pku.cn.xml.JavaLib.JavaLibPackage;
import edu.pku.cn.xml.JavaLib.JavaLibType;
import edu.pku.cn.xml.JavaLib.MethodType;
import edu.pku.cn.xml.JavaLib.PackageType;
import edu.pku.cn.xml.JavaLib.ReturnTypeType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.DocumentRootImpl#getClass_ <em>Class</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.DocumentRootImpl#getField <em>Field</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.DocumentRootImpl#getImplement <em>Implement</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.DocumentRootImpl#getJavaConfig <em>Java Config</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.DocumentRootImpl#getJavaLib <em>Java Lib</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.DocumentRootImpl#getMethod <em>Method</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.DocumentRootImpl#getPackage <em>Package</em>}</li>
 *   <li>{@link edu.pku.cn.xml.JavaLib.impl.DocumentRootImpl#getReturnType <em>Return Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DocumentRootImpl extends EDataObjectImpl implements DocumentRoot
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final long serialVersionUID = 1L;

  /**
   * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMixed()
   * @generated
   * @ordered
   */
  protected ESequence mixed = null;

  /**
   * The cached value of the '{@link #getXMLNSPrefixMap() <em>XMLNS Prefix Map</em>}' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getXMLNSPrefixMap()
   * @generated
   * @ordered
   */
  protected EMap xMLNSPrefixMap = null;

  /**
   * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getXSISchemaLocation()
   * @generated
   * @ordered
   */
  protected EMap xSISchemaLocation = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DocumentRootImpl()
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
    return JavaLibPackage.Literals.DOCUMENT_ROOT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Sequence getMixed()
  {
    if (mixed == null)
    {
      mixed = new BasicESequence(new BasicFeatureMap(this, JavaLibPackage.DOCUMENT_ROOT__MIXED));
    }
    return mixed;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Map getXMLNSPrefixMap()
  {
    if (xMLNSPrefixMap == null)
    {
      xMLNSPrefixMap = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, JavaLibPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
    }
    return xMLNSPrefixMap.map();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Map getXSISchemaLocation()
  {
    if (xSISchemaLocation == null)
    {
      xSISchemaLocation = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, JavaLibPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
    }
    return xSISchemaLocation.map();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ClassType getClass_()
  {
    return (ClassType)((FeatureMap.Internal.Wrapper)getMixed()).featureMap().get(JavaLibPackage.Literals.DOCUMENT_ROOT__CLASS, true);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetClass(ClassType newClass, NotificationChain msgs)
  {
    return ((FeatureMap.Internal)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).basicAdd(JavaLibPackage.Literals.DOCUMENT_ROOT__CLASS, newClass, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setClass(ClassType newClass)
  {
    ((FeatureMap.Internal)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).set(JavaLibPackage.Literals.DOCUMENT_ROOT__CLASS, newClass);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FieldType getField()
  {
    return (FieldType)((FeatureMap.Internal.Wrapper)getMixed()).featureMap().get(JavaLibPackage.Literals.DOCUMENT_ROOT__FIELD, true);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetField(FieldType newField, NotificationChain msgs)
  {
    return ((FeatureMap.Internal)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).basicAdd(JavaLibPackage.Literals.DOCUMENT_ROOT__FIELD, newField, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setField(FieldType newField)
  {
    ((FeatureMap.Internal)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).set(JavaLibPackage.Literals.DOCUMENT_ROOT__FIELD, newField);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ImplementType getImplement()
  {
    return (ImplementType)((FeatureMap.Internal.Wrapper)getMixed()).featureMap().get(JavaLibPackage.Literals.DOCUMENT_ROOT__IMPLEMENT, true);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetImplement(ImplementType newImplement, NotificationChain msgs)
  {
    return ((FeatureMap.Internal)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).basicAdd(JavaLibPackage.Literals.DOCUMENT_ROOT__IMPLEMENT, newImplement, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setImplement(ImplementType newImplement)
  {
    ((FeatureMap.Internal)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).set(JavaLibPackage.Literals.DOCUMENT_ROOT__IMPLEMENT, newImplement);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaConfigType getJavaConfig()
  {
    return (JavaConfigType)((FeatureMap.Internal.Wrapper)getMixed()).featureMap().get(JavaLibPackage.Literals.DOCUMENT_ROOT__JAVA_CONFIG, true);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetJavaConfig(JavaConfigType newJavaConfig, NotificationChain msgs)
  {
    return ((FeatureMap.Internal)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).basicAdd(JavaLibPackage.Literals.DOCUMENT_ROOT__JAVA_CONFIG, newJavaConfig, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setJavaConfig(JavaConfigType newJavaConfig)
  {
    ((FeatureMap.Internal)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).set(JavaLibPackage.Literals.DOCUMENT_ROOT__JAVA_CONFIG, newJavaConfig);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaLibType getJavaLib()
  {
    return (JavaLibType)((FeatureMap.Internal.Wrapper)getMixed()).featureMap().get(JavaLibPackage.Literals.DOCUMENT_ROOT__JAVA_LIB, true);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetJavaLib(JavaLibType newJavaLib, NotificationChain msgs)
  {
    return ((FeatureMap.Internal)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).basicAdd(JavaLibPackage.Literals.DOCUMENT_ROOT__JAVA_LIB, newJavaLib, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setJavaLib(JavaLibType newJavaLib)
  {
    ((FeatureMap.Internal)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).set(JavaLibPackage.Literals.DOCUMENT_ROOT__JAVA_LIB, newJavaLib);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MethodType getMethod()
  {
    return (MethodType)((FeatureMap.Internal.Wrapper)getMixed()).featureMap().get(JavaLibPackage.Literals.DOCUMENT_ROOT__METHOD, true);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMethod(MethodType newMethod, NotificationChain msgs)
  {
    return ((FeatureMap.Internal)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).basicAdd(JavaLibPackage.Literals.DOCUMENT_ROOT__METHOD, newMethod, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMethod(MethodType newMethod)
  {
    ((FeatureMap.Internal)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).set(JavaLibPackage.Literals.DOCUMENT_ROOT__METHOD, newMethod);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PackageType getPackage()
  {
    return (PackageType)((FeatureMap.Internal.Wrapper)getMixed()).featureMap().get(JavaLibPackage.Literals.DOCUMENT_ROOT__PACKAGE, true);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetPackage(PackageType newPackage, NotificationChain msgs)
  {
    return ((FeatureMap.Internal)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).basicAdd(JavaLibPackage.Literals.DOCUMENT_ROOT__PACKAGE, newPackage, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPackage(PackageType newPackage)
  {
    ((FeatureMap.Internal)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).set(JavaLibPackage.Literals.DOCUMENT_ROOT__PACKAGE, newPackage);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ReturnTypeType getReturnType()
  {
    return (ReturnTypeType)((FeatureMap.Internal.Wrapper)getMixed()).featureMap().get(JavaLibPackage.Literals.DOCUMENT_ROOT__RETURN_TYPE, true);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetReturnType(ReturnTypeType newReturnType, NotificationChain msgs)
  {
    return ((FeatureMap.Internal)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).basicAdd(JavaLibPackage.Literals.DOCUMENT_ROOT__RETURN_TYPE, newReturnType, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setReturnType(ReturnTypeType newReturnType)
  {
    ((FeatureMap.Internal)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).set(JavaLibPackage.Literals.DOCUMENT_ROOT__RETURN_TYPE, newReturnType);
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
      case JavaLibPackage.DOCUMENT_ROOT__MIXED:
        return ((InternalEList)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).basicRemove(otherEnd, msgs);
      case JavaLibPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
        return ((InternalEList)((EMap.InternalMapView)getXMLNSPrefixMap()).eMap()).basicRemove(otherEnd, msgs);
      case JavaLibPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
        return ((InternalEList)((EMap.InternalMapView)getXSISchemaLocation()).eMap()).basicRemove(otherEnd, msgs);
      case JavaLibPackage.DOCUMENT_ROOT__CLASS:
        return basicSetClass(null, msgs);
      case JavaLibPackage.DOCUMENT_ROOT__FIELD:
        return basicSetField(null, msgs);
      case JavaLibPackage.DOCUMENT_ROOT__IMPLEMENT:
        return basicSetImplement(null, msgs);
      case JavaLibPackage.DOCUMENT_ROOT__JAVA_CONFIG:
        return basicSetJavaConfig(null, msgs);
      case JavaLibPackage.DOCUMENT_ROOT__JAVA_LIB:
        return basicSetJavaLib(null, msgs);
      case JavaLibPackage.DOCUMENT_ROOT__METHOD:
        return basicSetMethod(null, msgs);
      case JavaLibPackage.DOCUMENT_ROOT__PACKAGE:
        return basicSetPackage(null, msgs);
      case JavaLibPackage.DOCUMENT_ROOT__RETURN_TYPE:
        return basicSetReturnType(null, msgs);
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
      case JavaLibPackage.DOCUMENT_ROOT__MIXED:
        if (coreType) return ((FeatureMap.Internal.Wrapper)getMixed()).featureMap();
        return getMixed();
      case JavaLibPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
        if (coreType) return ((EMap.InternalMapView)getXMLNSPrefixMap()).eMap();
        else return getXMLNSPrefixMap();
      case JavaLibPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
        if (coreType) return ((EMap.InternalMapView)getXSISchemaLocation()).eMap();
        else return getXSISchemaLocation();
      case JavaLibPackage.DOCUMENT_ROOT__CLASS:
        return getClass_();
      case JavaLibPackage.DOCUMENT_ROOT__FIELD:
        return getField();
      case JavaLibPackage.DOCUMENT_ROOT__IMPLEMENT:
        return getImplement();
      case JavaLibPackage.DOCUMENT_ROOT__JAVA_CONFIG:
        return getJavaConfig();
      case JavaLibPackage.DOCUMENT_ROOT__JAVA_LIB:
        return getJavaLib();
      case JavaLibPackage.DOCUMENT_ROOT__METHOD:
        return getMethod();
      case JavaLibPackage.DOCUMENT_ROOT__PACKAGE:
        return getPackage();
      case JavaLibPackage.DOCUMENT_ROOT__RETURN_TYPE:
        return getReturnType();
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
      case JavaLibPackage.DOCUMENT_ROOT__MIXED:
        ((FeatureMap.Internal)((FeatureMap.Internal.Wrapper)getMixed()).featureMap()).set(newValue);
        return;
      case JavaLibPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
        ((EStructuralFeature.Setting)((EMap.InternalMapView)getXMLNSPrefixMap()).eMap()).set(newValue);
        return;
      case JavaLibPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
        ((EStructuralFeature.Setting)((EMap.InternalMapView)getXSISchemaLocation()).eMap()).set(newValue);
        return;
      case JavaLibPackage.DOCUMENT_ROOT__CLASS:
        setClass((ClassType)newValue);
        return;
      case JavaLibPackage.DOCUMENT_ROOT__FIELD:
        setField((FieldType)newValue);
        return;
      case JavaLibPackage.DOCUMENT_ROOT__IMPLEMENT:
        setImplement((ImplementType)newValue);
        return;
      case JavaLibPackage.DOCUMENT_ROOT__JAVA_CONFIG:
        setJavaConfig((JavaConfigType)newValue);
        return;
      case JavaLibPackage.DOCUMENT_ROOT__JAVA_LIB:
        setJavaLib((JavaLibType)newValue);
        return;
      case JavaLibPackage.DOCUMENT_ROOT__METHOD:
        setMethod((MethodType)newValue);
        return;
      case JavaLibPackage.DOCUMENT_ROOT__PACKAGE:
        setPackage((PackageType)newValue);
        return;
      case JavaLibPackage.DOCUMENT_ROOT__RETURN_TYPE:
        setReturnType((ReturnTypeType)newValue);
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
      case JavaLibPackage.DOCUMENT_ROOT__MIXED:
        ((FeatureMap.Internal.Wrapper)getMixed()).featureMap().clear();
        return;
      case JavaLibPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
        getXMLNSPrefixMap().clear();
        return;
      case JavaLibPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
        getXSISchemaLocation().clear();
        return;
      case JavaLibPackage.DOCUMENT_ROOT__CLASS:
        setClass((ClassType)null);
        return;
      case JavaLibPackage.DOCUMENT_ROOT__FIELD:
        setField((FieldType)null);
        return;
      case JavaLibPackage.DOCUMENT_ROOT__IMPLEMENT:
        setImplement((ImplementType)null);
        return;
      case JavaLibPackage.DOCUMENT_ROOT__JAVA_CONFIG:
        setJavaConfig((JavaConfigType)null);
        return;
      case JavaLibPackage.DOCUMENT_ROOT__JAVA_LIB:
        setJavaLib((JavaLibType)null);
        return;
      case JavaLibPackage.DOCUMENT_ROOT__METHOD:
        setMethod((MethodType)null);
        return;
      case JavaLibPackage.DOCUMENT_ROOT__PACKAGE:
        setPackage((PackageType)null);
        return;
      case JavaLibPackage.DOCUMENT_ROOT__RETURN_TYPE:
        setReturnType((ReturnTypeType)null);
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
      case JavaLibPackage.DOCUMENT_ROOT__MIXED:
        return mixed != null && !mixed.featureMap().isEmpty();
      case JavaLibPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
        return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
      case JavaLibPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
        return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
      case JavaLibPackage.DOCUMENT_ROOT__CLASS:
        return getClass_() != null;
      case JavaLibPackage.DOCUMENT_ROOT__FIELD:
        return getField() != null;
      case JavaLibPackage.DOCUMENT_ROOT__IMPLEMENT:
        return getImplement() != null;
      case JavaLibPackage.DOCUMENT_ROOT__JAVA_CONFIG:
        return getJavaConfig() != null;
      case JavaLibPackage.DOCUMENT_ROOT__JAVA_LIB:
        return getJavaLib() != null;
      case JavaLibPackage.DOCUMENT_ROOT__METHOD:
        return getMethod() != null;
      case JavaLibPackage.DOCUMENT_ROOT__PACKAGE:
        return getPackage() != null;
      case JavaLibPackage.DOCUMENT_ROOT__RETURN_TYPE:
        return getReturnType() != null;
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
    result.append(" (mixed: ");
    result.append(mixed);
    result.append(')');
    return result.toString();
  }

} //DocumentRootImpl
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.JavaLib.impl;


import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import edu.pku.cn.xml.JavaLib.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class JavaLibFactoryImpl extends EFactoryImpl implements JavaLibFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static JavaLibFactory init()
  {
    try
    {
      JavaLibFactory theJavaLibFactory = (JavaLibFactory)EPackage.Registry.INSTANCE.getEFactory("file:/D:/Program%20Files/IBM/workspace/aa/JavaLib.xsd"); 
      if (theJavaLibFactory != null)
      {
        return theJavaLibFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new JavaLibFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaLibFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case JavaLibPackage.CLASS_TYPE: return (EObject)createClassType();
      case JavaLibPackage.DOCUMENT_ROOT: return (EObject)createDocumentRoot();
      case JavaLibPackage.FIELD_TYPE: return (EObject)createFieldType();
      case JavaLibPackage.IMPLEMENT_TYPE: return (EObject)createImplementType();
      case JavaLibPackage.JAVA_CONFIG_TYPE: return (EObject)createJavaConfigType();
      case JavaLibPackage.JAVA_LIB_TYPE: return (EObject)createJavaLibType();
      case JavaLibPackage.METHOD_TYPE: return (EObject)createMethodType();
      case JavaLibPackage.PACKAGE_TYPE: return (EObject)createPackageType();
      case JavaLibPackage.RETURN_TYPE_TYPE: return (EObject)createReturnTypeType();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ClassType createClassType()
  {
    ClassTypeImpl classType = new ClassTypeImpl();
    return classType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DocumentRoot createDocumentRoot()
  {
    DocumentRootImpl documentRoot = new DocumentRootImpl();
    return documentRoot;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FieldType createFieldType()
  {
    FieldTypeImpl fieldType = new FieldTypeImpl();
    return fieldType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ImplementType createImplementType()
  {
    ImplementTypeImpl implementType = new ImplementTypeImpl();
    return implementType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaConfigType createJavaConfigType()
  {
    JavaConfigTypeImpl javaConfigType = new JavaConfigTypeImpl();
    return javaConfigType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaLibType createJavaLibType()
  {
    JavaLibTypeImpl javaLibType = new JavaLibTypeImpl();
    return javaLibType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MethodType createMethodType()
  {
    MethodTypeImpl methodType = new MethodTypeImpl();
    return methodType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PackageType createPackageType()
  {
    PackageTypeImpl packageType = new PackageTypeImpl();
    return packageType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ReturnTypeType createReturnTypeType()
  {
    ReturnTypeTypeImpl returnTypeType = new ReturnTypeTypeImpl();
    return returnTypeType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaLibPackage getJavaLibPackage()
  {
    return (JavaLibPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  public static JavaLibPackage getPackage()
  {
    return JavaLibPackage.eINSTANCE;
  }

} //JavaLibFactoryImpl

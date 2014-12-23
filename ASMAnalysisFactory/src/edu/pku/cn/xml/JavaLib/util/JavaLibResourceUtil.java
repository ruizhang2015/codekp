/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.JavaLib.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.EObject;

import edu.pku.cn.xml.JavaLib.DocumentRoot;
import edu.pku.cn.xml.JavaLib.JavaLibFactory;
import edu.pku.cn.xml.JavaLib.JavaLibPackage;


/**
 * The utility class for loading and storing SDO instances as XML files.
 * @generated
 */
public class JavaLibResourceUtil 
{
  /**
   * The single instance of this class.
   * @generated
   */
  private static JavaLibResourceUtil instance;

  /**
   * Return the single instance of this class.
   * @generated
   */  
  public static JavaLibResourceUtil getInstance()
  {
  	if (instance == null)
  	{	
  	  instance = new JavaLibResourceUtil();
  	}
  	return instance;
  }
  
  /**
   * The default constructor.
   * @generated
   */  
  public JavaLibResourceUtil() 
  {
    initialize();
  }

  /**
   * @generated
   */
  private void initialize()
  {
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xml", new JavaLibResourceFactoryImpl());
    JavaLibPackage pkg = JavaLibPackage.eINSTANCE;   
    JavaLibFactory factory = JavaLibFactory.eINSTANCE;
  }

  /**
   * Load an existing XML file.
   * @param filename the absolute path name of the XML file to be loaded.
   * @exception IOException failed loading from an XML file.
   * @return DocumentRoot
   * @generated
   */  
  public DocumentRoot load(String filename) throws IOException
  {
    JavaLibResourceImpl resource = (JavaLibResourceImpl)(new JavaLibResourceFactoryImpl()).createResource(URI.createURI(filename));
    resource.load(null);
    DocumentRoot documentRoot = (DocumentRoot)resource.getContents().get(0);
    return documentRoot;
  }

  /**
   * Load an existing XML file.
   * @param istream the InputStream to load the XML content from.
   * @exception IOException failed loading from an XML file.
   * @return DocumentRoot
   * @generated
   */   
  public DocumentRoot load(InputStream istream) throws IOException
  {
    JavaLibResourceImpl resource = (JavaLibResourceImpl)(new JavaLibResourceFactoryImpl()).createResource(URI.createURI("*.xml"));
    resource.load(istream,null);
    DocumentRoot documentRoot = (DocumentRoot)resource.getContents().get(0);
    return documentRoot;
  }
  
  /**
   * Save as an XML file.
   * @param documentRoot the document root of the SDO instances.
   * @param filename the absolute path name of the XML file to be created.
   * @exception IOException failed storing to an XML file.
   * @generated
   */
  public void save(DocumentRoot documentRoot, String filename) throws IOException
  {
  	JavaLibResourceImpl resource = getJavaLibResourceImpl(documentRoot);
    resource.setURI(URI.createURI(filename));
    if (!resource.getContents().contains(documentRoot))
    { 	
      resource.getContents().add(documentRoot);
    }  
    resource.setEncoding("UTF-8");
    resource.save(null);
  } 
 
  /**
   * Save as an XML output stream.
   * @param documentRoot the document root of the SDO instances.
   * @param ostream the OutputStream where the XML content is to be written.
   * @exception IOException failed storing to an XML file.
   * @generated
   */ 
  public void save(DocumentRoot documentRoot, OutputStream ostream) throws IOException
  {
  	JavaLibResourceImpl resource = getJavaLibResourceImpl(documentRoot);
    if (!resource.getContents().contains(documentRoot))
    { 	
      resource.getContents().add(documentRoot);
    }  
    resource.setEncoding("UTF-8");
    resource.save(ostream,null);
  } 
  
  /**
   * Return a resource associated with documentRoot.
   * @param documentRoot the document root of the SDO instances.
   * @return JavaLibResourceImpl
   * @generated
   */   
  private JavaLibResourceImpl getJavaLibResourceImpl(DocumentRoot documentRoot)
  {
  	JavaLibResourceImpl resource = (JavaLibResourceImpl)((EObject)documentRoot).eResource();
    if (resource == null)
      resource = (JavaLibResourceImpl)(new JavaLibResourceFactoryImpl()).createResource(URI.createURI("*.xml"));
    return resource;    
  }

}

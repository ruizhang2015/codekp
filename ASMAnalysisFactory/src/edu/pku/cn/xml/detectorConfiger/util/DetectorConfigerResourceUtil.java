/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package edu.pku.cn.xml.detectorConfiger.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.EObject;

import edu.pku.cn.xml.detectorConfiger.DetectorConfigerFactory;
import edu.pku.cn.xml.detectorConfiger.DetectorConfigerPackage;
import edu.pku.cn.xml.detectorConfiger.DocumentRoot;

/**
 * The utility class for loading and storing SDO instances as XML files.
 * 
 * @generated
 */
public class DetectorConfigerResourceUtil {
	/**
	 * The single instance of this class.
	 * 
	 * @generated
	 */
	private static DetectorConfigerResourceUtil instance;

	/**
	 * Return the single instance of this class.
	 * 
	 * @generated
	 */
	public static DetectorConfigerResourceUtil getInstance() {
		if (instance == null) {
			instance = new DetectorConfigerResourceUtil();
		}
		return instance;
	}

	/**
	 * The default constructor.
	 * 
	 * @generated
	 */
	public DetectorConfigerResourceUtil() {
		initialize();
	}

	/**
	 * @generated
	 */
	private void initialize() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xml",
				new DetectorConfigerResourceFactoryImpl());
		DetectorConfigerPackage pkg = DetectorConfigerPackage.eINSTANCE;
		DetectorConfigerFactory factory = DetectorConfigerFactory.eINSTANCE;
	}

	/**
	 * Load an existing XML file.
	 * 
	 * @param filename
	 *            the absolute path name of the XML file to be loaded.
	 * @exception IOException
	 *                failed loading from an XML file.
	 * @return DocumentRoot
	 * @generated
	 */
	public DocumentRoot load(String filename) throws IOException {
		DetectorConfigerResourceImpl resource = (DetectorConfigerResourceImpl) (new DetectorConfigerResourceFactoryImpl())
				.createResource(URI.createURI(filename));
		resource.load(null);
		DocumentRoot documentRoot = (DocumentRoot) resource.getContents().get(0);
		return documentRoot;
	}

	/**
	 * Load an existing XML file.
	 * 
	 * @param istream
	 *            the InputStream to load the XML content from.
	 * @exception IOException
	 *                failed loading from an XML file.
	 * @return DocumentRoot
	 * @generated
	 */
	public DocumentRoot load(InputStream istream) throws IOException {
		DetectorConfigerResourceImpl resource = (DetectorConfigerResourceImpl) (new DetectorConfigerResourceFactoryImpl())
				.createResource(URI.createURI("*.xml"));
		resource.load(istream, null);
		DocumentRoot documentRoot = (DocumentRoot) resource.getContents().get(0);
		return documentRoot;
	}

	/**
	 * Save as an XML file.
	 * 
	 * @param documentRoot
	 *            the document root of the SDO instances.
	 * @param filename
	 *            the absolute path name of the XML file to be created.
	 * @exception IOException
	 *                failed storing to an XML file.
	 * @generated
	 */
	public void save(DocumentRoot documentRoot, String filename) throws IOException {
		DetectorConfigerResourceImpl resource = getDetectorConfigerResourceImpl(documentRoot);
		resource.setURI(URI.createURI(filename));
		if (!resource.getContents().contains(documentRoot)) {
			resource.getContents().add(documentRoot);
		}
		resource.setEncoding("UTF-8");
		resource.save(null);
	}

	/**
	 * Save as an XML output stream.
	 * 
	 * @param documentRoot
	 *            the document root of the SDO instances.
	 * @param ostream
	 *            the OutputStream where the XML content is to be written.
	 * @exception IOException
	 *                failed storing to an XML file.
	 * @generated
	 */
	public void save(DocumentRoot documentRoot, OutputStream ostream) throws IOException {
		DetectorConfigerResourceImpl resource = getDetectorConfigerResourceImpl(documentRoot);
		if (!resource.getContents().contains(documentRoot)) {
			resource.getContents().add(documentRoot);
		}
		resource.setEncoding("UTF-8");
		resource.save(ostream, null);
	}

	/**
	 * Return a resource associated with documentRoot.
	 * 
	 * @param documentRoot
	 *            the document root of the SDO instances.
	 * @return DetectorConfigerResourceImpl
	 * @generated
	 */
	private DetectorConfigerResourceImpl getDetectorConfigerResourceImpl(DocumentRoot documentRoot) {
		DetectorConfigerResourceImpl resource = (DetectorConfigerResourceImpl) ((EObject) documentRoot).eResource();
		if (resource == null)
			resource = (DetectorConfigerResourceImpl) (new DetectorConfigerResourceFactoryImpl()).createResource(URI
					.createURI("*.xml"));
		return resource;
	}

}

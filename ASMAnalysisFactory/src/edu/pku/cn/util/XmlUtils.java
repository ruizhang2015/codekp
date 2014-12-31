package edu.pku.cn.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author hongliang.dinghl DOM生成与解析XML文档
 */
public class XmlUtils {
	private Document document;

	public XmlUtils() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.document = builder.newDocument();
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		}
	}

	public void createXml(String fileName) {
		Element root = this.document.createElement("employees");
		this.document.appendChild(root);
		Element employee = this.document.createElement("employee");
		Element name = this.document.createElement("name");
		name.appendChild(this.document.createTextNode("丁宏亮"));
		employee.appendChild(name);
		Element sex = this.document.createElement("sex");
		sex.appendChild(this.document.createTextNode("m"));
		employee.appendChild(sex);
		Element age = this.document.createElement("age");
		age.appendChild(this.document.createTextNode("30"));
		employee.appendChild(age);
		root.appendChild(employee);
		TransformerFactory tf = TransformerFactory.newInstance();
		try {
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
			System.out.println("生成XML文件成功!");
		} catch (TransformerConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (TransformerException e) {
			System.out.println(e.getMessage());
		}
	}

	public String[] parserXml(String in) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(in);
			//InputStream instream = new ByteArrayInputStream(in.getBytes());  
			//Document document = db.parse(instream);
			NodeList employees = document.getChildNodes();
			String res1 = null;
			String res2 = null;
			for (int i = 0; i < employees.getLength(); i++) {
				Node employee = employees.item(i);
				NodeList employeeInfo = employee.getChildNodes();
				for (int j = 0; j < employeeInfo.getLength(); j++) {
					Node node = employeeInfo.item(j);
					NodeList employeeMeta = node.getChildNodes();
					for (int k = 0; k < employeeMeta.getLength(); k++) {
						Node bugInfo = employeeMeta.item(k);
						
						if (bugInfo.getNodeName()=="short_desc"){
							//System.out.println(bugInfo.getNodeName() + ":" + bugInfo.getTextContent());
							res1 = bugInfo.getTextContent();
						}
						else if (bugInfo.getNodeName()=="long_desc"){
							
							NodeList longdesc = bugInfo.getChildNodes();
							for (int p = 0; p < longdesc.getLength(); p++) {
								Node lnode = longdesc.item(p);
								//if (lnode.getNodeName().equals("comment_count") && !lnode.getTextContent().equals("0")) break;
								if (lnode.getNodeName().equals("thetext")){
									//System.out.println(lnode.getNodeName() + "::" + lnode.getTextContent());
									res2 = lnode.getTextContent();
									if (res1 != null)
										return new String[]{res1, res2};
								}
							}
						}
					}
				}
			}
			System.out.println("Null XML res");
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (SAXException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public static void main(String[] args) {
		XmlUtils xu = new XmlUtils();
		// xu.createXml("testcc.xml");
		System.out.println(xu.parserXml("test.xml")[1]);

	}

}
package com.togetherseatech.whaleshark.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import android.util.Log;

public class XmlUtil {
  
	public static Node getSubNode(Element root, String tagName) {
		NodeList list = root.getElementsByTagName(tagName);

	    for (int loop = 0; loop < list.getLength(); loop++) {
	      Node node = list.item(loop);
	      if (node != null)
	        return node;
	    }
	    return null;
	}

	public static Node getSubNode(Element root, String tagName, String subTagName) {
	    NodeList list = root.getElementsByTagName(tagName);
	
	    for (int loop = 0; loop < list.getLength(); loop++) {
	      Node node = list.item(loop);
	
	      if (node == null) {
	        continue;
	      }
	      Node child = getSubNode(node, subTagName);
	
	      if (child != null)
	        return child;
	    }
	    return null;
	}

	public static Node getSubNode(Node node, String tagName) {
	    if (node == null) {
	      return null;
	    }
	    NodeList children = node.getChildNodes();
	
	    for (int innerLoop = 0; innerLoop < children.getLength(); innerLoop++) {
	      Node child = children.item(innerLoop);
	      if ((child == null) || (child.getNodeName() == null) || 
	        (!child.getNodeName().equals(tagName))) continue;
	      Node grandChild = child.getFirstChild();
	      if ((grandChild != null) && (grandChild.getNodeValue() != null)) {
	        return grandChild;
	      }
	    }
	    return null;
	}

	public static String getTagAttribute(Element root, String tagName, String attribute) {
	    String returnString = "";
	    NodeList list = root.getElementsByTagName(tagName);
	    for (int loop = 0; loop < list.getLength(); loop++) {
	      Node node = list.item(loop);
	      if (node != null) {
	    	  return ((Element)node).getAttribute(attribute);
	      }
	    }
	    return returnString;
	}
	
	public static String getSubTagAttribute(Element root, String tagName, String subTagName, String attribute) {
	    String returnString = "";
	    NodeList list = root.getElementsByTagName(tagName);
	    for (int loop = 0; loop < list.getLength(); loop++) {
	      Node node = list.item(loop);
	      if (node != null) {
	        NodeList children = node.getChildNodes();
	        for (int innerLoop = 0; innerLoop < children.getLength(); innerLoop++) {
	          Node child = children.item(innerLoop);
	          if ((child != null) && (child.getNodeName() != null) && (child.getNodeName().equals(subTagName)) && 
	            ((child instanceof Element))) {
	            return ((Element)child).getAttribute(attribute);
	          }
	        }
	      }
	    }
	    return returnString;
	}

	public static String getSubTagValue(Element root, String tagName, String subTagName) {
	    Node rnode = getSubNode(root, tagName, subTagName);
	    if (rnode == null)
	      return "";
	    return rnode.getNodeValue();
	}

	public static String getSubTagValue(Node node, String subTagName) {
	    Node rnode = getSubNode(node, subTagName);
	    if (rnode == null)
	      return "";
	    return rnode.getNodeValue();
	}

	public static String getTagTextContent(Element root, String tagName) {
	    String returnString = "";
	    NodeList list = root.getElementsByTagName(tagName);
	    for (int loop = 0; loop < list.getLength(); loop++) {
	      Node node = list.item(loop);
	      if (node != null) {
	    	  Node child = node.getFirstChild();
	    	  if ((child != null) && (child.getTextContent() != null))
	          return child.getTextContent();
	      }
	    }
	    return returnString;
	}
	
	public static String getTagTextContent(Element root, String tagName, int position) {
	    String returnString = "";
	    NodeList list = root.getElementsByTagName(tagName);
	    Node node = list.item(position);
	    if (node != null) {
	    	Node child = node.getFirstChild();
	    	if ((child != null) && (child.getTextContent() != null))
	    		return child.getTextContent();
	    }
	    return returnString;
	}
	
	public static String getTagValue(Element root, String tagName) {
	    String returnString = "";
	    NodeList list = root.getElementsByTagName(tagName);
	    for (int loop = 0; loop < list.getLength(); loop++) {
	      Node node = list.item(loop);
	      if (node != null) {
	        Node child = node.getFirstChild();
	        if ((child != null) && (child.getNodeValue() != null))
	          return child.getNodeValue();
	      }
	    }
	    return returnString;
	}

	public static int getTagCnt(Element root, String tagName) {
	    NodeList list = root.getElementsByTagName(tagName);
	    return list.getLength();
	}


	public static Element loadDocument(String data) throws Exception {
	    try {
	    	InputSource xmlInp = new InputSource();
	    	xmlInp.setCharacterStream(new StringReader(data));
	    	Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlInp);
	    	Element root = doc.getDocumentElement();
	    	root.normalize();
	    	return root;
	    } catch (SAXParseException spe) {
	    	throw spe;
	    } catch (SAXException se) {
	    	throw se;
	    } catch (MalformedURLException mux) {
	    	throw mux;
	    } catch (IOException ioe) {
	    	throw ioe; 
	    } catch (Exception e) {
	    	throw e;
	    }
	}

	public static String string2Xml(ArrayList<Map<String,Object>> obj)
			throws TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException, DOMException, UnsupportedEncodingException {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Node root = document.createElement("seatech");
		document.appendChild(root);

		/*Element Return = document.createElement("return");
		Return.setAttribute("status", status);
		root.appendChild(Return);*/

		if(obj != null){
			for (int i = 0; i < obj.size(); i++) {
				Element Row = document.createElement("ROW");
				root.appendChild(Row);
				Map<String,Object> item = obj.get(i);
				Set key = item.keySet();
				for (Iterator iterator = key.iterator(); iterator.hasNext();) {
					String keyName = (String) iterator.next();
//	    			String valueName = (String) item.get(keyName);
					String valueName = String.valueOf(item.get(keyName)) ;
//					System.out.println("string2Xml >>>>>>>>>> "+keyName +" = " +valueName);
					Element name = document.createElement(keyName.toUpperCase());
//					name.setAttribute();
//					Log.e("XmlIUtil","keyName : " + keyName.toUpperCase());
					if(!"RELATIVE_REGULATION".equals(keyName.toUpperCase())) {
						name.appendChild(document.createTextNode(CommonUtil.getEncode(valueName)));
					}else {
						name.appendChild(document.createTextNode(valueName));
					}
					Row.appendChild(name);
				}
			}
		}

		// Document 저장
		DOMSource xmlDOM = new DOMSource(document);

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		transformer.transform(xmlDOM, result);

		return sw.toString();
	}
}

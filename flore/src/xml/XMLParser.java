package xml;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import javax.xml.xpath.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.File;
import java.net.URI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


/**This class provides a clean interface to the SAX Xpath engine.
 *
 *@author <a href="mailto:alcmene.gs@gmail.com">Matti Schneider-Ghibaudo</a>
 *@version 0.1
 */

public class XMLParser {
	
	Document XML;
	XPath xpath;
	
	public XMLParser(String path) {
		this(new File(path));
	}
	
	/**Loads the given XML file.
	 *@param	file	the XML file to parse.
	 *@throws	IllegalArgumentException	if the given file isn't parsable by this version of the parser.
	 */	
	public XMLParser(File file) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			XML = builder.parse(file);
			xpath = XPathFactory.newInstance().newXPath();
		} catch (javax.xml.parsers.ParserConfigurationException e) {
			throw new RuntimeException("Erreur au chargement d'un fichier de description XML !\n" + e);
		} catch (org.xml.sax.SAXException e) {
			throw new RuntimeException("Erreur au chargement d'un fichier de description XML !\n" + e);
		} catch (java.io.IOException e) {
			throw new RuntimeException("Erreur au chargement d'un fichier de description XML !\n" + e);
		}
	}
	
	/**Returns the URI of the loaded XML file.
	 */
	public URI getDocumentURI() {
		try {
			return new URI(XML.getDocumentURI());
		} catch (java.net.URISyntaxException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	/**Returns the result of the evaluation of an Xpath query as a String.
	 */
	public String get(String query) {
		try {
			return xpath.evaluate(query, XML);
		} catch (javax.xml.xpath.XPathExpressionException e) {
			throw new RuntimeException("Erreur (" + e + ") à l'analyse d'un fichier de description XML !\nRequête Xpath : " + query);
		}
	
	}
	
	/**Returns the result of the evaluation of an Xpath query as a Double.
	 */
	public Double getDouble(String query) {
		try {
			return (Double) xpath.evaluate(query, XML, XPathConstants.NUMBER);
		} catch (javax.xml.xpath.XPathExpressionException e) {
			throw new RuntimeException("Erreur (" + e + ") à l'analyse d'un fichier de description XML !\nRequête Xpath : " + query);
		}
	}
	
	/**Returns the result of the evaluation of an Xpath query as a list of Nodes.
	 *Every entry of the list will be one of the nodes described in the query parameter.
	 */	
	public List<Node> getNodes(String query) {
		NodeList nodes = null;
		try {
			nodes = ((NodeList) xpath.evaluate(query, XML, XPathConstants.NODESET));
		} catch (javax.xml.xpath.XPathExpressionException e) {
			throw new RuntimeException("Erreur (" + e + ") à l'analyse d'un fichier de description XML !\nRequête Xpath : " + query);
		}
		
		List<Node> values = new ArrayList<Node>(nodes.getLength());
		for (int i = 0; i < nodes.getLength(); i++)
			values.add(nodes.item(i));
		
		return values;
	}
	
	/**Returns the result of the evaluation of an Xpath query as a list of NodeNames.
	 *Every entry of the list will be one of the tag of the nodes described in the query parameter.
	 */	
	public List<String> getTags(String query) {
		List<Node> nodes = getNodes(query);
		List<String> result = new ArrayList<String>(nodes.size());
		for (Node node : nodes)
			result.add(node.getNodeName());
		return result;
	}
	
	/**Returns the result of the evaluation of an Xpath query as a list of Strings.
	 *Every entry of the list will be the value of one of the children of the node described in the query parameter.
	 *@param	query	an Xpath expression targeting one node whose children will be used as values in the list (as opposed to the children themselves)
	 */
	public List<String> getValues(String query) {
		List<Node> nodes = getNodes(query + "/*");
		List<String> values = new ArrayList<String>(nodes.size());
		for (Node node : nodes) {
			if (node.getNodeType() == Node.ELEMENT_NODE) //otherwise we load everything, including comments and pure text
				values.add(node.getTextContent());
		}
		return values;
	}
	
	/**Returns the result of the evaluation of an Xpath query as a map of Strings to lists of Strings.
	 *An entry in the map will have its key be the tag of a node and its value be a list of all values inside it.
	 *@param	query	an Xpath expression targeting one node whose children will be used as keys for the map (as opposed to the children themselves)
	 *@see	getValues
	 *@throws	RuntimeException	if one of the nodes from the query doesn't have children.
	 */
	public Map<String, List<String>> getListsMap(String query) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		List<Node> keys = getNodes(query + "/*");
		
		for (Node key : keys) {
			if (! key.hasChildNodes())
				throw new RuntimeException("The node \"" + key.getNodeName() + "\" has no children !");
			
			result.put(key.getNodeName(), getValues(query + "/" + key.getNodeName()));
		}
		return result;
	}
	
	/**Returns the result of the evaluation of an Xpath query as a map of Strings to lists of Nodes.
	 *An entry in the map will have its key be the tag of a node and its value be a list of all nodes inside it.
	 *@param	query	an Xpath expression targeting one node whose children will be used as keys for the map (as opposed to the children themselves)
	 *@see	getValues
	 *@throws	RuntimeException	if one of the nodes from the query doesn't have children.
	 */
	public Map<String, List<Node>> getNodesListsMap(String query) {
		Map<String, List<Node>> result = new HashMap<String, List<Node>>();
		List<Node> keys = getNodes(query + "/*");
		
		for (Node key : keys) {
			if (! key.hasChildNodes())
				throw new RuntimeException("The node \"" + key.getNodeName() + "\" has no children !");
			
			result.put(key.getNodeName(), getNodes(query + "/" + key.getNodeName() + "/*"));
		}
		return result;
	}
}

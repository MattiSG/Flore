package element;

import java.util.Map;
import java.util.List;

import javax.xml.xpath.*;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import java.io.File;

import java.awt.image.BufferedImage;

abstract public class Element {
	/**Version check. If the version of the parser is not compatible with the version the file asks for, the file won't be parsed.*/
	public final static double PARSER_VERSION = 1;
	
	protected String ID;
    protected String name;
	protected String description;
	protected Map<String, List<BufferedImage>> assets;

	private final static String EXPR_VERSION = "//id/../@version";
	private final static String EXPR_ID = "//id";
	private final static String EXPR_NAME = "//name[@lang='fr']";
	private final static String EXPR_ASSETS = "//assets";
	private final static String EXPR_DESCRIPTION = "//description";
	
	/**@name	Getters*/
	//@{
	public String ID() {
		return ID;
	}
	
	public String name() {
		return name;
	}
	
	public String description() {
		return description;
	}
	
	public List<BufferedImage> seedImages() {
		return null;
	}
	
	public List<BufferedImage> shaftImages() {
		return null;
	}
	
	public List<BufferedImage> leavesImages() {
		return null;
	}
	
	public List<BufferedImage> flowersImages() {
		return null;
	}
	//@}
	
	
	/**@name	XML parsing*/
	//@{
	/**Tells whether the given file format version is parsable or not.*/
	public boolean checkVersion(double version) {
		return version == PARSER_VERSION;
	}
	
	/**Tries to initialize a new Element from an XML description file.
	 *@param	file	the XML file to parse.
	 *@throws	IllegalArgumentException	if the given file isn't parsable by this version of the parser.
	 */
    protected void loadFromXML(String file) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document XML = builder.parse(new File(file));
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			if (! checkVersion((Double) xpath.evaluate(EXPR_VERSION, XML, XPathConstants.NUMBER)))
				throw new IllegalArgumentException("File version is newer than parser ! Please update this software.");
			
			ID = xpath.evaluate(EXPR_ID, XML);
			name = xpath.evaluate(EXPR_NAME, XML);
			description = xpath.evaluate(EXPR_DESCRIPTION, XML);
		} catch (javax.xml.parsers.ParserConfigurationException e) {
			System.err.println("Erreur au chargement d'un fichier d'élément (insecte ou plante) !\n" + e);
			e.printStackTrace();
		} catch (java.io.IOException e) {
			System.err.println("Erreur à la lecture d'un fichier d'élément (insecte ou plante) !\n" + e);
			e.printStackTrace();
		} catch (org.xml.sax.SAXException e) {
			System.err.println("Erreur au chargement d'un fichier d'élément (insecte ou plante) !\n" + e);
			e.printStackTrace();
		} catch (javax.xml.xpath.XPathExpressionException e) {
			System.err.println("Erreur à l'analyse d'un fichier d'élément (insecte ou plante) !\n" + e);
			e.printStackTrace();
		}
	}
	
	private void loadAssets() {
		
	}
	//@}
}

package element;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import javax.xml.xpath.*;
import javax.xml.parsers.*;
import org.w3c.dom.*; 
import java.io.File;

import javax.imageio.ImageIO;
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
	private final static String EXPR_ASSETS = "//assets[@type='standard']";
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
	//@}
	
	
	/**@name	XML parsing*/
	//@{
	/**Tells whether the given file format version is parsable or not.*/
	public boolean checkVersion(double version) {
		return version == PARSER_VERSION;
	}
	
	/**The names of all assets nodes to load.
	 *Designed to be overriden for specific needs by the inheriting classes.
	 *@see	loadAssets
	 */
	protected abstract String[] getAssetsNames();
	
	
	/**Loads an Element file from its ID.
	 *Just a gateway to loadFromXML(), hiding the file hierarchy to clients.
	 *@param	ID	the ID of the element to load.
	 *@throws	IllegalArgumentException	if the given file isn't parsable or doesn't exist.
	 */
	protected void load(String ID) {
		loadFromXML("ressources/elements/" + ID + "/description.xml");
	}
	
	/**Tries to initialize a new Element from an XML description file.
	 *@param	file	the XML file to parse.
	 *@throws	IllegalArgumentException	if the given file isn't parsable by this version of the parser.
	 */
    private void loadFromXML(String file) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document XML = builder.parse(new File(file));
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			if (! checkVersion((Double) xpath.evaluate(EXPR_VERSION, XML, XPathConstants.NUMBER)))
				throw new IllegalArgumentException("File version is newer than parser ! Please update this software.");
			
			ID = xpath.evaluate(EXPR_ID, XML);
			name = xpath.evaluate(EXPR_NAME, XML);
			description = xpath.evaluate(EXPR_DESCRIPTION, XML);
			
			assets = new HashMap<String, List<BufferedImage>>();
			
			loadAssets(xpath, XML);
			
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
	
	/**Generic method to load assets from the assetsNames attribute, to be defined in inheriting classes.
	 *While they share a lot of common attributes, the inner assets organisation is completely different, hence the need to redefine some details in the inheriting classes.
	 *This method populates the assets Map.
	 *@param	xpath	the XPath to be used to parse the given XML document.
	 *@param	XML		the XML document to be parsed.
	 *@see		getAssetsNames
	 */
	protected void loadAssets(XPath xpath, Document XML) {
		for (String nodeName : getAssetsNames()) {
			NodeList imagesPaths = null;
			String query = EXPR_ASSETS + "/" + nodeName + "/img";
			try {
				imagesPaths = ((NodeList) xpath.evaluate(query, XML, XPathConstants.NODESET));
			} catch (javax.xml.xpath.XPathExpressionException e) {
				System.err.println("Erreur (" + e + ") à l'analyse d'un fichier d'élément (insecte ou plante) !\nRequête Xpath : " + query);
				e.printStackTrace();
			}
			
			List<BufferedImage> images = new ArrayList<BufferedImage>(imagesPaths.getLength());
			for (int i = 0; i < imagesPaths.getLength(); i++) {
				String file = "ressources/elements/" + ID + "/" + imagesPaths.item(i).getTextContent();
				try {
					images.add(ImageIO.read(new File(file)));
				} catch(java.io.IOException e) {
					System.err.println("Erreur (" + e + ") au chargement d'une image !\nFichier à charger : " + file);
					e.printStackTrace();
				}
			}
			assets.put(nodeName, images);
		}
	}
	//@}
}

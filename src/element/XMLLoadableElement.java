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
import java.net.URI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**Generic class to handle XML parsing for every distinct element of the game.
 *For example, insects, plants and so on are stored through XML files, and inherit this class to get free XML parsing and assets loading.
 *@author <a href="mailto:alcmene.gs@gmail.com">Matti Schneider-Ghibaudo</a>
 *@version 0.2
 */
abstract public class XMLLoadableElement {
	/**Version check. If the version of the parser is not compatible with the version the file asks for, the file won't be parsed.*/
	protected final double PARSER_VERSION = 0.2;
	
	/**@name	Element description*/
	//@{
	private String ID;
    private String name;
	private String description;
	private Map<String, List<BufferedImage>> assets;
	//@}

	/**@name	Xpath expressions
	 *These expressions are used to parse the XML files.
	 *They represent the common data shared by all game elements represented by XML files.
	 */
	//@{
	private final static String EXPR_VERSION = "//id/../@version";
	private final static String EXPR_ID = "//id";
	private final static String EXPR_NAME = "//name[@lang='fr']";
	private final static String EXPR_ASSETS = "//assets[@type='standard']";
	private final static String EXPR_DESCRIPTION = "//description";
	//@}
	
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
	
	/**The names of all assets nodes to load.
	 *Designed to be overriden for specific needs by the inheriting classes.
	 *@see	loadAssets
	 */
	public abstract String[] getAssetsNames();
	
	/**The key has to be one from the String array returned by getAssetsNames.
	 *@see	getAssetsNames
	 */
	public List<BufferedImage> getAssets(String key) {
		return assets.get(key);
	}
	//@}
	
	
	/**@name	XML parsing*/
	//@{
	/**Tells whether the given file format version is parsable or not.*/
	public boolean checkVersion(double version) {
		if (version > PARSER_VERSION) {
			System.err.println("The given file's version (" + version + ") is newer than this parser (" + PARSER_VERSION + ").\nI'll try to read it, but be aware that you may get errors ! You should update this software.");
			return true;
		}
		return version == PARSER_VERSION;
	}	
	
	/**Loads an Element file from its ID.
	 *Just a gateway to loadFromXML(), hiding the file hierarchy to clients.
	 *@param	ID	the ID of the element to load.
	 *@throws	IllegalArgumentException	if the given file isn't parsable or doesn't exist.
	 */
	protected void load(String ID) {
		loadFromXML("../ressources/elements/" + ID + "/description.xml");
		if (! ID.equals(ID()))
			throw new RuntimeException("Folder ID (" + ID + ") and parsed inner ID (" + ID() + ") do not match !");
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
			throw new RuntimeException("Erreur au chargement d'un fichier d'élément (insecte ou plante) !\n" + e);
		} catch (java.io.IOException e) {
			throw new RuntimeException("Erreur au chargement d'un fichier d'élément (insecte ou plante) !\n" + e);
		} catch (org.xml.sax.SAXException e) {
			throw new RuntimeException("Erreur au chargement d'un fichier d'élément (insecte ou plante) !\n" + e);
		} catch (javax.xml.xpath.XPathExpressionException e) {
			throw new RuntimeException("Erreur au chargement d'un fichier d'élément (insecte ou plante) !\n" + e);
		}
	}
	
	/**Generic method to load assets from the assetsNames attribute, to be defined in inheriting classes.
	 *While they share a lot of common attributes, the inner assets organisation is completely different, hence the need to redefine some details in the inheriting classes.
	 *This method populates the assets Map.
	 *@param	xpath	the XPath to be used to parse the given XML document.
	 *@param	XML		the XML document to be parsed.
	 *@throws	RuntimeException	if the parsing of the XML file, or the loading of an image, threw an exception.
	 *@see		getAssetsNames
	 */
	private void loadAssets(XPath xpath, Document XML) {
		for (String nodeName : getAssetsNames()) {
			NodeList imagesPaths = null;
			String query = EXPR_ASSETS + "/" + nodeName + "/img";
			try {
				imagesPaths = ((NodeList) xpath.evaluate(query, XML, XPathConstants.NODESET));
			} catch (javax.xml.xpath.XPathExpressionException e) {
				throw new RuntimeException("Erreur (" + e + ") à l'analyse d'un fichier d'élément (insecte ou plante) !\nRequête Xpath : " + query);
			}
			
			if (imagesPaths.getLength() == 0)
				throw new RuntimeException("Erreur à l'initialisation de l'élément " + ID() + " (\"" + name() + "\") : aucune image n'a pu être chargée !\nRequête effectuée : " + query);
			
			List<BufferedImage> images = new ArrayList<BufferedImage>(imagesPaths.getLength());
			for (int i = 0; i < imagesPaths.getLength(); i++) {
				URI imageURI = null;
				try {
					imageURI = new URI(XML.getDocumentURI());
					imageURI = imageURI.normalize().resolve(imagesPaths.item(i).getTextContent());
					images.add(ImageIO.read(new File(imageURI)));
				} catch(java.io.IOException e) {
					throw new RuntimeException("Erreur (" + e + ") au chargement d'une image !\nFichier à charger : " + imageURI);
				} catch (java.net.URISyntaxException e) {
					throw new RuntimeException("Erreur au chargement d'un fichier d'élément (insecte ou plante) !\n" + e);
				}
			}
			assets.put(nodeName, images);
		}
	}
	//@}
}
